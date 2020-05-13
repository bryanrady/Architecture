package com.bryanrady.taopiaopiao;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bryanrady.pluginstandard.PluginInterfaceActivity;

/**
 * Created by Administrator on 2019/5/27.
 */

public class BaseActivity extends Activity implements PluginInterfaceActivity {

    protected Activity mThat;

    @Override
    public void attach(Activity proxyActivity) {
        this.mThat = proxyActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestory() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void setContentView(int layoutResID) {
        if (mThat != null){
            mThat.setContentView(layoutResID);
        }else{
            super.setContentView(layoutResID);
        }
    }

    @Override
    public <T extends View> T findViewById(int id) {
        if (mThat != null){
            return mThat.findViewById(id);
        }
        return super.findViewById(id);
    }

    @Override
    public Intent getIntent() {
        if (mThat != null){
            return mThat.getIntent();
        }
        return super.getIntent();
    }

    @Override
    public void startActivity(Intent intent) {
        if (mThat != null){
            //        ProxyActivity --->className
            Intent m = new Intent();
            m.putExtra("activityName", intent.getComponent().getClassName());
            mThat.startActivity(m);
        }else{
            super.startActivity(intent);
        }

    }

    @Override
    public ComponentName startService(Intent service) {
        if (mThat != null){
            Intent m = new Intent();
            m.putExtra("serviceName", service.getComponent().getClassName());
            return mThat.startService(m);
        }
        return super.startService(service);
    }

    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        if (mThat != null){
            return mThat.registerReceiver(receiver, filter);
        }
        return super.registerReceiver(receiver, filter);
    }

    @Override
    public void sendBroadcast(Intent intent) {
        if (mThat != null){
            mThat.sendBroadcast(intent);
        }else{
            super.sendBroadcast(intent);
        }
    }

}
