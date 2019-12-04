package com.bryanrady.architecture.plugin.insert;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.bryanrady.pluginstandard.PluginInterfaceService;
import java.lang.reflect.Constructor;

/**
 * 这个服务是真正注册了的服务
 * Created by Administrator on 2018/3/28.
 */

public class ProxyService extends Service {

    private String mServiceName;
    private PluginInterfaceService mPluginInterfaceService;

    @Override
    public IBinder onBind(Intent intent) {
        init(intent);
        return null;
    }

    private void init(Intent intent) {
        mServiceName = intent.getStringExtra("serviceName");
        try {
            Class loadClass= PluginManager.getInstance().getDexClassLoader().loadClass(mServiceName);

            Constructor<?> localConstructor = loadClass.getConstructor(new Class[] {});
            Object instance = localConstructor.newInstance(new Object[] {});

            mPluginInterfaceService = (PluginInterfaceService) instance;
            mPluginInterfaceService.attach(this);

            Bundle bundle = new Bundle();
            bundle.putInt("form", 1);
            mPluginInterfaceService.onCreate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mPluginInterfaceService == null) {
            init(intent);
        }
        return mPluginInterfaceService.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mPluginInterfaceService.onDestroy();
        super.onDestroy();

    }

    @Override
    public void onLowMemory() {
        mPluginInterfaceService.onLowMemory();
        super.onLowMemory();
    }


    @Override
    public boolean onUnbind(Intent intent) {
        mPluginInterfaceService.onUnbind(intent);
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        mPluginInterfaceService.onRebind(intent);
        super.onRebind(intent);
    }

}
