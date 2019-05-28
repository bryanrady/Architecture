package com.bryanrady.pluginstandard;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

/**
 * 将支付宝的Activity里面的东西传递到淘票票插件中
 * Created by Administrator on 2019/5/27.
 */

public interface PluginInterfaceActivity {

    public void attach(Activity proxyActivity);

    public void onCreate(Bundle savedInstanceState);
    public void onStart();
    public void onResume();
    public void onRestart();
    public void onPause();
    public void onStop();
    public void onDestory();

    public void onSaveInstanceState(Bundle outState);
    public boolean onTouchEvent(MotionEvent event);
    public void onBackPressed();

}
