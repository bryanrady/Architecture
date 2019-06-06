package com.bryanrady.architecture.plugin.hook;

import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bryanrady.architecture.FrameApplication;
import com.bryanrady.architecture.plugin.hook.ams.LoginFirstActivity;
import com.bryanrady.architecture.plugin.hook.ams.LoginLoginActivity;
import com.bryanrady.architecture.plugin.hook.ams.LoginProxyActivity;
import com.bryanrady.architecture.plugin.hook.ams.LoginSecondActivity;
import com.bryanrady.architecture.plugin.hook.ams.LoginThirdActivity;
import com.bryanrady.architecture.plugin.hook.loadedapk.FileUtil;
import com.bryanrady.architecture.plugin.hook.loadedapk.LoadedPluginClassLoader;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;


/**
 * Created by Administrator on 2019/5/29.
 */

public class HookUtil {


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
    public void hookIActivityManager(Context context) {
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
            Object realIActivityManager = mInstance;

            //使用动态代理   第二参数  是即将返回的对象 这里就是实现了系统中IActivityManager的所有接口
            IActivityManagerHandler iActivityManagerHandler = new IActivityManagerHandler(context, realIActivityManager);

            //寻找 IActivityManager 系统对象  这样动态代理对象就会实现IActivityManager中的所有方法
            Class iActivityManagerClass = Class.forName("android.app.IActivityManager");
            Object proxyIActivityManager = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                    new Class[]{iActivityManagerClass},iActivityManagerHandler);

            //将系统的IActivityManager 替换成 自己通过动态代理实现的对象  proxyIActivityManager

            mInstanceField.set(gDefault,proxyIActivityManager);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //动态代理方式
    class IActivityManagerHandler implements InvocationHandler{
        private Context mContext;
        private Object mIActivityManager;

        public IActivityManagerHandler(Context context,Object iActivityManager){
            this.mContext = context;
            this.mIActivityManager = iActivityManager;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
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
                if(originalIntent != null && originalIntent.getComponent() != null){
                    if(onlyForActivity(originalIntent)){
                        //目的  ---在载入activity的时候将intent还原为最开始的意图
                        Intent proxyIntent = new Intent();
                        ComponentName componentName = new ComponentName(mContext, LoginProxyActivity.class);
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
    public void hookMHHandleMessage(Context context) {
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
            mCallbackField.set(mH,new InterfaceMHCallBack(context,mH));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //设置接口方式
    class InterfaceMHCallBack implements Handler.Callback{

        private Context mContext;
        private Handler mH;

        public InterfaceMHCallBack(Context context,Handler handler){
            this.mContext = context;
            this.mH = handler;
        }

        @Override
        public boolean handleMessage(Message msg) {
            //LAUNCH_ACTIVITY ==100 即将要加载一个activity了
            if (msg.what == 100){
                //加工 --完  一定丢给系统  secondActivity  -hook->proxyActivity---hook->    secondeActivtiy
                try {
                    // obj实际上是 ActivityClientRecord
                    Object obj = msg.obj;
                    Field intentField = obj.getClass().getDeclaredField("intent");
                    intentField.setAccessible(true);

                    //这个proxyIntent是包装之后的intent 所以我们要进行还原
                    Intent proxyIntent = (Intent) intentField.get(obj);
                    if(proxyIntent != null){
                        Intent originalIntent = proxyIntent.getParcelableExtra("originalIntent");
                        if(originalIntent != null && originalIntent.getComponent() != null){
                            if(onlyForActivity(originalIntent)){
                                //为了绕开AMS检查
                                handleLaunchActivity(mContext,obj,proxyIntent,originalIntent);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //继续执行系统的处理消息的方法
            mH.handleMessage(msg);
            return true;
        }

        private void handleLaunchActivity(Context context,Object obj,Intent proxyIntent,Intent originalIntent) {
            //然后就在这里做集中式登录  还原 把原有的意图  放到proxyIntent
            SharedPreferences share = context.getSharedPreferences("wangqingbin", Context.MODE_PRIVATE);
            if (share.getBoolean("login",false)) {
                proxyIntent.setComponent(originalIntent.getComponent());
            }else {
                ComponentName componentName = new ComponentName(context, LoginLoginActivity.class);
                proxyIntent.putExtra("extraIntent", originalIntent.getComponent().getClassName());
                proxyIntent.setComponent(componentName);
            }

            if(onlyForActivity2(originalIntent)){
                try {
                    Field activityInfoField = obj.getClass().getDeclaredField("activityInfo");
                    activityInfoField.setAccessible(true);
                    ActivityInfo activityInfo= (ActivityInfo) activityInfoField.get(obj);
                    if(originalIntent.getPackage() == null){
                        activityInfo.applicationInfo.packageName = originalIntent.getComponent().getPackageName();
                    }else{
                        activityInfo.applicationInfo.packageName = originalIntent.getPackage();
                    }
//                activityInfo.applicationInfo.packageName = originalIntent.getPackage() == null
//                        ? originalIntent.getComponent().getPackageName()
//                        : originalIntent.getPackage();

                    hookIPackageManager();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void hookIPackageManager() {
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread");
            Object sCurrentActivityThread = currentActivityThreadMethod.invoke(null);

            // 获取ActivityThread里面原始的 sPackageManager
            Field sPackageManagerField = activityThreadClass.getDeclaredField("sPackageManager");
            sPackageManagerField.setAccessible(true);
            Object sPackageManager = sPackageManagerField.get(sCurrentActivityThread);

            Class iPackageManagerClass = Class.forName("android.content.pm.IPackageManager");

            // 准备好代理对象, 用来替换原始的对象
            IPackageManagerHandler iPackageManagerHandler = new IPackageManagerHandler(sPackageManager);
            Object proxyIPackageManager = Proxy.newProxyInstance(
                    iPackageManagerClass.getClassLoader(),
                    new Class[]{iPackageManagerClass},
                    iPackageManagerHandler);

            sPackageManagerField.set(sCurrentActivityThread,proxyIPackageManager);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class IPackageManagerHandler implements InvocationHandler{

        Object mPackageManager;

        public IPackageManagerHandler(Object iPackageManager){
            mPackageManager = iPackageManager;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            //这里要进行拦截 如果不拦截的话 返回的还是宿主的包名
            if (method.getName().equals("getPackageInfo")) {
                Log.d("wangqingbin","-----------------getPackageInfo()--------------------------");
            //    return new PackageInfo();
                return null;
            }
            return method.invoke(mPackageManager,args);
        }
    }

    /**
     * 只针对这些Activity
     * @param originalIntent
     * @return
     */
    private boolean onlyForActivity(Intent originalIntent){
        String className = originalIntent.getComponent().getClassName();
        if(LoginLoginActivity.class.getName().equals(className)
                || LoginFirstActivity.class.getName().equals(className)
                || LoginSecondActivity.class.getName().equals(className)
                || LoginThirdActivity.class.getName().equals(className)
                ||"com.bryanrady.dexelements.DexFirstActivity".equals(className)
                ||"com.bryanrady.dexelements.DexSecondActivity".equals(className)
                ||"com.bryanrady.dexelements.DexThirdActivity".equals(className)
                ||"com.bryanrady.loadedapk.LoadedFirstActivity".equals(className)
                ||"com.bryanrady.loadedapk.LoadedSecondActivity".equals(className)
                ||"com.bryanrady.loadedapk.LoadedThirdActivity".equals(className)
                )
        {
            return true;
        }
        return false;
    }

    private boolean onlyForActivity2(Intent originalIntent){
        String className = originalIntent.getComponent().getClassName();
        if("com.bryanrady.loadedapk.LoadedFirstActivity".equals(className)
                ||"com.bryanrady.loadedapk.LoadedSecondActivity".equals(className)
                ||"com.bryanrady.loadedapk.LoadedThirdActivity".equals(className)
                )
        {
            return true;
        }
        return false;
    }

    /**
     * 将插件apk的LoadedApk添加到map集合中
     * @param context
     * @param apkPath
     */
    public void putPluginLoadedApkToArrayMap(Context context,String apkPath){
        try {
            //1.得到系统的ActivityThread对象
            //通过ActivityThread的内部方法currentActivityThread（）返回
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread");
            Object sCurrentActivityThread = currentActivityThreadMethod.invoke(null);

            //    final ArrayMap<String, WeakReference<LoadedApk>> mPackages = new ArrayMap<String, WeakReference<LoadedApk>>();
            //2.得到ActivityThread里面的map集合
            Field mPackagesField = activityThreadClass.getDeclaredField("mPackages");
            mPackagesField.setAccessible(true);
            ArrayMap mPackages = (ArrayMap) mPackagesField.get(sCurrentActivityThread);

            //3.通过插件apk解析后生成对应的插件LoadedApk
            // LAUNCH_ACTIVITY
            //final ActivityClientRecord r = (ActivityClientRecord) msg.obj;
            //r.packageInfo = getPackageInfoNoCheck(r.activityInfo.applicationInfo, r.compatInfo);
            //r.packageInfo 这个就是一个LoadedApk对象

            //CompatibilityInfo是做兼容的一个类 我们只要获取它里面默认的即可
            Class compatibilityInfoClass = Class.forName("android.content.res.CompatibilityInfo");
            Field default_compatibility_info_Field = compatibilityInfoClass.getDeclaredField("DEFAULT_COMPATIBILITY_INFO");
            Object default_compatibility_info = default_compatibility_info_Field.get(null);

            Method getPackageInfoNoCheckMethod = activityThreadClass.getDeclaredMethod("getPackageInfoNoCheck", ApplicationInfo.class, compatibilityInfoClass);
            //获取ApplicationInfo
            ApplicationInfo applicationInfo = parseApplicationInfo(apkPath);
            String packageName = applicationInfo.packageName;
            Object loadedApk = getPackageInfoNoCheckMethod.invoke(sCurrentActivityThread, applicationInfo, default_compatibility_info);

            String odexPath = FileUtil.getPluginOptDexDir(applicationInfo.packageName).getPath();
            String libPath = FileUtil.getPluginLibDir(applicationInfo.packageName).getPath();

            ClassLoader pluginClassLoader = new LoadedPluginClassLoader(apkPath, odexPath, libPath, context.getClassLoader());
            //找到LoadedApk中的mClassLoader 然后进行替换
            Field mClassLoaderField = loadedApk.getClass().getDeclaredField("mClassLoader");
            mClassLoaderField.setAccessible(true);
            mClassLoaderField.set(loadedApk, pluginClassLoader);

            WeakReference<Object> refLoadedApk = new WeakReference<>(loadedApk);
            //key：包名， value：LoadedApk
            mPackages.put(packageName,refLoadedApk);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据apk解析出ApplicationInfo
     * @param apkPath
     * @return
     */
    private ApplicationInfo parseApplicationInfo(String apkPath){
        try {
            Class packageParserClass = Class.forName("android.content.pm.PackageParser");
            Object packageParser = packageParserClass.newInstance();
            Method parsePackageMethod = packageParserClass.getDeclaredMethod("parsePackage", File.class, int.class);
            //通过PackageParser的parsePackage()方法获取到Package对象 PackageParser.Package
            Object packageObj = parsePackageMethod.invoke(packageParser, new File(apkPath), PackageManager.GET_ACTIVITIES);

            Class packageUserStateClass = Class.forName("android.content.pm.PackageUserState");
            Object packageUserState = packageUserStateClass.newInstance();

            Method generateApplicationInfoMethod = packageParserClass.getDeclaredMethod("generateApplicationInfo",
                    packageObj.getClass(),
                    int.class,
                    packageUserStateClass);
            ApplicationInfo applicationInfo = (ApplicationInfo) generateApplicationInfoMethod.
                    invoke(packageParser, packageObj, 0, packageUserState);
            applicationInfo.sourceDir = apkPath;
            applicationInfo.publicSourceDir = apkPath;

            return applicationInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }























    /**
     * 我们想在点击的时候做点事情 点进去 View.serOnClickListener() --> getListenerInfo().mOnClickListener = l;
     * 发现mOnClickListener是ListenerInfo的一个成员变量，但是不是静态的，那我们继续找ListenerInfo，发现它是View
     * 里面的一个内部类，并且这个对象还是一个单例的，所以我们就可以通过getListenerInfo()方法找到这个系统对象
     * 
     */
    public void hookOnClickListener(Context context,View view){
        try {
            //第一步：拿到系统中View的 ListenerInfo 对象
            Method getListenerInfoMethod = View.class.getDeclaredMethod("getListenerInfo");
            getListenerInfoMethod.setAccessible(true);
            Object mListenerInfo = getListenerInfoMethod.invoke(view);

            //第二步: 拿到系统中的 OnClickListener 对象
            Class listenerInfoClass = Class.forName("android.view.View$ListenerInfo");
        //    Class listenerInfoClass = mListenerInfo.getClass();
            Field mOnClickListenerField = listenerInfoClass.getDeclaredField("mOnClickListener");
            View.OnClickListener mOnClickListener = (View.OnClickListener) mOnClickListenerField.get(mListenerInfo);

            //第三步:  用自定义的 OnClickListener 替换原始的 OnClickListener
            mOnClickListenerField.set(mListenerInfo,new InterfaceHookOnClickListener(context,mOnClickListener));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //设置接口方式
    class InterfaceHookOnClickListener implements View.OnClickListener{
        private Context mContext;
        //这个是系统的OnClickListener
        private View.OnClickListener mOnClickListener;

        public InterfaceHookOnClickListener(Context context,View.OnClickListener listener){
            this.mContext = context;
            this.mOnClickListener = listener;
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(mContext, "hook click", Toast.LENGTH_SHORT).show();
            Log.d("wangqingbin","Before Click   do what you want to to.");

            //继续执行系统的点击方法
            if (mOnClickListener != null) {
                mOnClickListener.onClick(v);
            }
            Log.d("wangqingbin","After Click   do what you want to to.");
        }
    }


    /**
     * context.getSystemService(Context.CLIPBOARD_SERVICE) --》ContextImpl.getSystemService() --> SystemServiceRegistry.getSystemService(this, name);
     *
     *  --> SYSTEM_SERVICE_FETCHERS.get(name); --> SystemServiceRegistry.registerService --> private static final HashMap<String, ServiceFetcher<?>> SYSTEM_SERVICE_FETCHERS =new HashMap<String, ServiceFetcher<?>>();
     *
     *  --> return new ClipboardManager(ctx.getOuterContext(),ctx.mMainThread.getHandler()); -->这里得到了ClipboardManager对象，而这个对象是包装IClipboard远端服务的对象
     *
     *  setPrimaryClip --》 getService().setPrimaryClip --》IClipboard getService()--》 IBinder b = ServiceManager.getService("clipboard");
     *
     *  --》sService = IClipboard.Stub.asInterface(b); 这里就得到了本地可用的远程服务
     *
     *  我们Hook系统的剪切板服务功能，拦截其方法，上面也说道了，既然要Hook服务，首先得找到Hook点，
     *  通过开始对Android中系统服务的调用流程分析知道，其实这些服务都是一些保存在ServiceManager中的远端IBinder对象，这其实是一个Hook点：
     *  其实ServiceManager中每次在获取服务的时候，其实是先从一个缓存池中查找，如果有就直接返回了：
     *  而这个缓存池正好是全局的static类型，所以就可以很好的使用反射机制获取到他了，然后进行操作了。
     *
     *  接下来，我们就需要构造一个剪切板的服务IBinder对象了，然后在把这个对象放到上面得到的池子中即可。那么按照上面的动态代理的流程，
     *  第一、原始对象必须实现一个接口，这里也正好符合这个规则，每个远程服务其实是实现了IBinder接口的。
     *  第二、其次是要有原始对象，这个也可以，通过上面的缓存池即可获取
     *
     */
    public void hookClipSystemService(Context context){
        try {
            Class serviceManagerClass = Class.forName("android.os.ServiceManager");
            Method getServiceMethod = serviceManagerClass.getDeclaredMethod("getService",String.class);

            //获取系统的clipboard的IBinder对象
            IBinder clipboardBinder = (IBinder) getServiceMethod.invoke(null, Context.CLIPBOARD_SERVICE);

            //获取自定义的clipboard的IBinder对象
            IClipboardHookBinderInvocationHandler proxyClipboardManagerHandler = new IClipboardHookBinderInvocationHandler(clipboardBinder);
            IBinder proxyClipboardBinder = (IBinder) Proxy.newProxyInstance(
                    context.getClassLoader(),
                    new Class[]{IBinder.class},
                    proxyClipboardManagerHandler);

            Field sCacheField = serviceManagerClass.getDeclaredField("sCache");
            sCacheField.setAccessible(true);
            HashMap<String, IBinder> sCache = (HashMap<String, IBinder>) sCacheField.get(null);
            sCache.put(Context.CLIPBOARD_SERVICE,proxyClipboardBinder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class IClipboardHookBinderInvocationHandler implements InvocationHandler{

        private IBinder mRawBinder;
        private Class mStubClass;
        private Class mIClipboardClass;

        public IClipboardHookBinderInvocationHandler(IBinder rawIBinder){
            mRawBinder = rawIBinder;
            try {
                mStubClass = Class.forName("android.content.IClipboard$Stub");
                mIClipboardClass = Class.forName("android.content.IClipboard");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            Log.d("wangqingbin","method --》"+method.getName());

            if("queryLocalInterface".equals(method.getName())){
                Log.d("wangqingbin","hook -- queryLocalInterface");

                return Proxy.newProxyInstance(
                        mRawBinder.getClass().getClassLoader(),
                        new Class[]{mIClipboardClass},
                        new HookBinderInvocationHandler(mRawBinder,mStubClass));
            }
            return method.invoke(mRawBinder,args);
        }

    }


    class HookBinderInvocationHandler implements InvocationHandler{

        private Object mIClipboard;

        public HookBinderInvocationHandler(IBinder rawIBinder, Class stubClass){
            try {
                Method asInterfaceMethod = stubClass.getDeclaredMethod("asInterface", IBinder.class);
                //将远程服务IBinder对象 转化成本地可用的代理对象
                mIClipboard = asInterfaceMethod.invoke(null,rawIBinder);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            //拦截这个方法 修改为自己的内容
            if("getPrimaryClip".equals(method.getName())){
                return ClipData.newPlainText(null,"I am from hook text");
            }
            //欺骗系统 让系统认为剪切板一直有内容
            if("hasPrimaryClip".equals(method.getName())){
                return true;
            }
            return method.invoke(mIClipboard,args);
        }
    }

}

