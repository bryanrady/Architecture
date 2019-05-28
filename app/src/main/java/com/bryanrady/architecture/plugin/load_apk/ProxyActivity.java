package com.bryanrady.architecture.plugin.load_apk;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bryanrady.pluginstandard.PluginInterfaceActivity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 这个占位Activity是用来加载淘票票插件内容的页面 因为淘票票的Activity没有被注册在支付宝上面
 * Created by Administrator on 2019/5/27.
 */

public class ProxyActivity extends Activity {

    /**
     * 需要加载淘票票的Activity  类名
     */
    private String mActivityName;

    private PluginInterfaceActivity mPluginInterfaceActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityName = getIntent().getStringExtra("activityName");
        Log.d("wangqingbin","mActivityName == "+ mActivityName);
        //Class.forName(mActivityName); 这里不能通过反射去获得淘票票Activity的全类名 因为没有被安装到手机上
        try {
            Class activityClass = getClassLoader().loadClass(mActivityName);
            Constructor activityClassConstructor = activityClass.getConstructor(new Class[]{});
            Object newInstance = activityClassConstructor.newInstance(new Object[]{});

            mPluginInterfaceActivity = (PluginInterfaceActivity) newInstance;

            mPluginInterfaceActivity.attach(this);
            Bundle bundle = new Bundle();
            bundle.putString("name","张三");
            bundle.putInt("age",25);
            mPluginInterfaceActivity.onCreate(bundle);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startActivity(Intent intent) {
        String activityName = intent.getStringExtra("activityName");
        Intent m = new Intent(this, ProxyActivity.class);
        m.putExtra("activityName", activityName);
        super.startActivity(m);
    }

    @Override
    public ComponentName startService(Intent service) {
        String serviceName = service.getStringExtra("serviceName");
        Intent m = new Intent(this, ProxyService.class);
        m.putExtra("serviceName", serviceName);
        return super.startService(m);
    }

    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        IntentFilter newInterFilter = new IntentFilter();
        //取action的过程
        for (int i=0;i<filter.countActions();i++) {
            newInterFilter.addAction(filter.getAction(i));
        }
        return super.registerReceiver(new ProxyBroadcastReceiver(receiver.getClass().getName(),this),newInterFilter);
    }

    @Override
    public ClassLoader getClassLoader() {
        return PluginManager.getInstance().getDexClassLoader();
    }

    @Override
    public Resources getResources() {
        return PluginManager.getInstance().getResources();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPluginInterfaceActivity.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPluginInterfaceActivity.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mPluginInterfaceActivity.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPluginInterfaceActivity.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPluginInterfaceActivity.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPluginInterfaceActivity.onDestory();
    }
}
