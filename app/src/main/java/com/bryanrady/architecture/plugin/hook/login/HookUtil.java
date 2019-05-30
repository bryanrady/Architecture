package com.bryanrady.architecture.plugin.hook.login;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.bryanrady.architecture.plugin.PluginActivity;
import com.bryanrady.architecture.plugin.load_apk.ProxyActivity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


/**
 * Created by Administrator on 2019/5/29.
 */

public class HookUtil {

    private Context mContext;

    public void init(Context context){
        mContext = context;
        hookStartActivity();
        hookMHHandleMessage();
    }

    /**
     * startActivity() --> Activity.startActivity() --> Activity.startActivityForResult() --> Instrumentation -->execStartActivity
     *  -->  ActivityManagerNative.getDefault().startActivity
     *
     *  ActivityManagerNative.getDefault() -->static public IActivityManager getDefault() 这只是个静态方法 我们要一直找到静态变量才行 继续往下找
     *
     *  --》gDefault.get() --》 private static final Singleton<IActivityManager> gDefault = new Singleton<IActivityManager>()
     *
     *  而这个 gDefault 恰好就是一个静态变量 可以进行hook
     *
     * public abstract class Singleton<T> {
     *
     *      private T mInstance;
     *
     *      protected abstract T create();
     *
     *      public final T get() {
     *          synchronized (this) {
     *              if (mInstance == null) {
     *                  mInstance = create();
     *              }
     *              return mInstance;
     *          }
     *      }
     * }
     *
     *  从这里我们可以看出  gDefault 实际上是返回了 mInstance变量 所以我们要对 mInstance 进行替换
     *
     */

    /**
     * 这一步相当于伪装的过程 把真实的intent进行伪装
     */
    private void hookStartActivity() {
        try {
            Class activityManagerNativeClass = Class.forName("android.app.ActivityManagerNative");
            Field gDefaultField = activityManagerNativeClass.getDeclaredField("gDefault");
            gDefaultField.setAccessible(true);  //gDefault字段是个private
            //因为是静态变量  所以获取的到的是系统值  这个gDefaultObj对象实际上是一个IActivityManager  hook   伪hook 如果不是静态变量get(null)会出现空指针异常
            Object gDefault = gDefaultField.get(null);

            Class singleTonClass = Class.forName("android.util.Singleton");
            Field mInstanceField = singleTonClass.getDeclaredField("mInstance");
            mInstanceField.setAccessible(true);
            //这个mInstanceObj对象实际上是一个IActivityManager
            Object mInstance = mInstanceField.get(gDefault);
            Object iActivityManager = mInstance;

            //使用动态代理   第二参数  是即将返回的对象 这里就是实现了系统中IActivityManager的所有接口
            IActivityManagerInvocationHandler invocationHandler = new IActivityManagerInvocationHandler(mContext,iActivityManager);

            //寻找 IActivityManager 系统对象  这样动态代理对象就会实现IActivityManager中的所有方法
            Class iActivityManagerClass = Class.forName("android.app.IActivityManager");
            Object proxyIActivityManager = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                    new Class[]{iActivityManagerClass},invocationHandler);

            //将系统的IActivityManager 替换成 自己通过动态代理实现的对象  proxyIActivityManager

            mInstanceField.set(gDefault,proxyIActivityManager);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class IActivityManagerInvocationHandler implements InvocationHandler{
        private Context mContext;
        private Object mIActivityManager;

        public IActivityManagerInvocationHandler(Context context,Object iActivityManager){
            this.mContext = context;
            this.mIActivityManager = iActivityManager;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            Log.d("wangqingbin","invoke  " + method.getName());

            //拦截所有的startActivity方法
            if ("startActivity".equals(method.getName())) {
                Log.d("wangqingbin","-----------------startActivity--------------------------");
                //寻找传进来的intent 这个intent是最初的intent
                Intent originalIntent = null;
                int index = 0;
                for (int i = 0;i< args.length; i++) {
                    Object arg = args[i];
                    if (arg instanceof Intent) {
                        originalIntent = (Intent) args[i];
                        index = i;
                    }
                }
                if(originalIntent != null){
                    if(onlyForActivity(originalIntent)){
                        //目的  ---在载入activity的时候将intent还原为最开始的意图
                        Intent proxyIntent = new Intent();
                        ComponentName componentName = new ComponentName(mContext, HookLoginProxyActivity.class);
                        proxyIntent.setComponent(componentName);
                        //最原始的真实的意图
                        proxyIntent.putExtra("originalIntent", originalIntent);
                        args[index] = proxyIntent;
                    }
                }
            }
            return method.invoke(mIActivityManager, args);
        }
    }


    /**
     *  handleLaunchActivity(r, null, "LAUNCH_ACTIVITY");
     *  由Activity启动流程得知 mH H extends Handler 当mH收到LAUNCH_ACTIVITY  100的消息时 就会加载activity
     *  所以我们要在加载之前 也就是 mH 收到100消息的时候 进行还原 所以mH.handlerMessage就是hook点
     *
     *  这时候我们看      final H mH = new H(); mH不是一个静态变量 那我们找H的父类ActivityThread，看ActivityThread有没有
     *  ActivityThread 的变量  private static volatile ActivityThread sCurrentActivityThread;  结果发现有并且还是静态的
     *  
     */

    /**
     * 这一步相当于还原过程   把系统的执行放到我们的代码外面
     */
    private void hookMHHandleMessage() {
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Field sCurrentActivityThreadField = activityThreadClass.getDeclaredField("sCurrentActivityThread");
            sCurrentActivityThreadField.setAccessible(true);
            Object sCurrentActivityThread = sCurrentActivityThreadField.get(null);

            Field mHField = activityThreadClass.getDeclaredField("mH");
            mHField.setAccessible(true);
            Handler mH = (Handler) mHField.get(sCurrentActivityThread);

            Field mCallbackField = Handler.class.getDeclaredField("mCallback");
            mCallbackField.setAccessible(true);
            //替换CallBack
            mCallbackField.set(mH,new MHCallBack(mH));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    class MHCallBack implements Handler.Callback{

        private Handler mH;

        public MHCallBack(Handler handler){
            this.mH = handler;
        }

        @Override
        public boolean handleMessage(Message msg) {
            //LAUNCH_ACTIVITY ==100 即将要加载一个activity了
            if (msg.what == 100){
                //加工 --完  一定丢给系统  secondActivity  -hook->proxyActivity---hook->    secondeActivtiy
                try {
                    Object obj = msg.obj;
                    Field intentField = obj.getClass().getDeclaredField("intent");
                    intentField.setAccessible(true);

                    //这个proxyIntent是包装之后的intent 所以我们要进行还原
                    Intent proxyIntent = (Intent) intentField.get(obj);
                    if(proxyIntent != null){
                        Intent originalIntent = proxyIntent.getParcelableExtra("originalIntent");
                        if(originalIntent != null){
                            if(onlyForActivity(originalIntent)){
                                handleLaunchActivity(proxyIntent,originalIntent);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //做了真正的跳转
            mH.handleMessage(msg);
            return true;
        }

        private void handleLaunchActivity(Intent proxyIntent,Intent originalIntent) {
            //然后就在这里做集中式登录  还原 把原有的意图  放到proxyIntent
            SharedPreferences share = mContext.getSharedPreferences("wangqingbin", Context.MODE_PRIVATE);
            if (share.getBoolean("login",false)) {
                proxyIntent.setComponent(originalIntent.getComponent());
            }else {
                ComponentName componentName = new ComponentName(mContext,HookLoginLoginActivity.class);
                proxyIntent.putExtra("extraIntent", originalIntent.getComponent().getClassName());
                proxyIntent.setComponent(componentName);
            }
        }
    }

    /**
     * 只针对这些Activity
     * @param originalIntent
     * @return
     */
    private boolean onlyForActivity(Intent originalIntent){
        String className = originalIntent.getComponent().getClassName();
        if(HookLoginLoginActivity.class.getName().equals(className)
                || HookLoginFirstActivity.class.getName().equals(className)
                || HookLoginSecondActivity.class.getName().equals(className)
                || HookLoginThirdActivity.class.getName().equals(className)){
            return true;
        }
        return false;
    }
}
