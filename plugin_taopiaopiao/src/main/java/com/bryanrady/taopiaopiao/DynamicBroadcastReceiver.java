package com.bryanrady.taopiaopiao;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.bryanrady.pluginstandard.PluginInterfaceBroadcastReceiver;

/**
 * 动态注册 广播接收者
 * Created by Administrator on 2019/5/28.
 */

public class DynamicBroadcastReceiver extends BroadcastReceiver implements PluginInterfaceBroadcastReceiver {

    public static final String DYNAMIC_ACTION = "com.bryanrady.taopiaopiao.DYNAMIC_ACTION";

    @Override
    public void attach(Context context) {
        Toast.makeText(context, "-----绑定上下文成功---->", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(DYNAMIC_ACTION.equals(action)){
            Toast.makeText(context, "-----插件收到动态注册的广播--->", Toast.LENGTH_SHORT).show();
        }
    }
}
