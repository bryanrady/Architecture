package com.bryanrady.architecture.plugin.load_apk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bryanrady.pluginstandard.PluginInterfaceBroadcastReceiver;

import java.lang.reflect.Constructor;

/**
 * 这个广播是真正注册的广播
 * Created by Administrator on 2019/5/28.
 */

public class ProxyBroadcastReceiver extends BroadcastReceiver {

    private String mReceiverName;
    private PluginInterfaceBroadcastReceiver mPluginInterfaceBroadcastReceiver;

    //这个context是ProxyActivity 因为要在里面进行注册广播
    public ProxyBroadcastReceiver(String className,Context context) {
        this.mReceiverName = className;
        try {
            Class loadClass = PluginManager.getInstance().getDexClassLoader().loadClass(className);
            Constructor<?> localConstructor = loadClass.getConstructor(new Class[]{});
            Object instance = localConstructor.newInstance(new Object[]{});
            mPluginInterfaceBroadcastReceiver = (PluginInterfaceBroadcastReceiver) instance;
            mPluginInterfaceBroadcastReceiver.attach(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 进行转发
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        mPluginInterfaceBroadcastReceiver.onReceive(context,intent);
    }
}
