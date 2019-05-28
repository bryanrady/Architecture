package com.bryanrady.taopiaopiao;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.bryanrady.pluginstandard.PluginInterfaceBroadcastReceiver;

/**
 * Created by baby on 2018/3/30.
 */

public class StaticBroadcastReceiver extends BroadcastReceiver{

    public static final String MAIN_TO_PLUGIN_ACTION = "com.bryanrady.architecture.plugin.load_apk.MAIN_TO_PLUGIN_ACTION";
    public static final String PLUGIN_TO_MAIN_ACTION = "com.bryanrady.taopiaopiao.PLUGIN_TO_MAIN_ACTION";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(MAIN_TO_PLUGIN_ACTION.equals(action)){
            Toast.makeText(context, "我是插件, 收到宿主的消息!", Toast.LENGTH_SHORT).show();
            context.sendBroadcast(new Intent(PLUGIN_TO_MAIN_ACTION));
        }
    }

}
