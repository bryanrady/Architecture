package com.bryanrady.taopiaopiao;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.bryanrady.pluginstandard.PluginInterfaceActivity;

/**
 * Created by Administrator on 2019/5/27.
 */

public class TppBaseActivity extends Activity implements PluginInterfaceActivity {

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
    public void setContentView(View view) {
        if (mThat != null){
            mThat.setContentView(view);
        }else{
            super.setContentView(view);
        }
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
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (mThat != null){
            mThat.setContentView(view, params);
        }else{
            super.setContentView(view, params);
        }
    }

    @NonNull
    @Override
    public LayoutInflater getLayoutInflater() {
        if (mThat != null){
            mThat.getLayoutInflater();
        }
        return super.getLayoutInflater();
    }

    @Override
    public <T extends View> T findViewById(int id) {
        if (mThat != null){
            mThat.findViewById(id);
        }
        return super.findViewById(id);
    }

    @Override
    public void startActivity(Intent intent) {
        if (mThat != null){
            //        ProxyActivity --->className
            Intent m = new Intent();
            m.putExtra("activityName", intent.getComponent().getClassName());
            Log.d("wangqingbin","activityName intent.getComponent().getClassName()=="+intent.getComponent().getClassName());
            mThat.startActivity(m);
        }else{
            super.startActivity(intent);
        }

    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (mThat != null){
            mThat.startActivityForResult(intent, requestCode);
        }else{
            super.startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public ComponentName startService(Intent service) {
        if (mThat != null){
            Intent m = new Intent();
            m.putExtra("serviceName", service.getComponent().getClassName());
            Log.d("wangqingbin","serviceName service.getComponent().getClassName()=="+service.getComponent().getClassName());
            return mThat.startService(m);
        }
        return super.startService(service);
    }

    @Override
    public Intent getIntent() {
        if (mThat != null){
            return mThat.getIntent();
        }
        return super.getIntent();
    }

    @Override
    public ClassLoader getClassLoader() {
        if (mThat != null){
            return mThat.getClassLoader();
        }
        return super.getClassLoader();
    }

    @Override
    public Window getWindow() {
        if (mThat != null){
            return mThat.getWindow();
        }
        return super.getWindow();
    }

    @Override
    public WindowManager getWindowManager() {
        if (mThat != null){
            return mThat.getWindowManager();
        }
        return super.getWindowManager();
    }
}
