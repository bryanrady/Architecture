package com.bryanrady.pluginstandard;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2019/5/28.
 */

public interface PluginInterfaceBroadcastReceiver {

    public void attach(Context context);

    public void onReceive(Context context, Intent intent);

}
