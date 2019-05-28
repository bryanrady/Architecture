package com.bryanrady.architecture;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;


import com.bryanrady.architecture.utils.AppManager;
import com.bryanrady.architecture.utils.LogUtil;
import com.bryanrady.architecture.utils.SystemBarTintManager;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2019/1/10.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);//通知栏所需颜色
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置所有的Activity竖屏显示

        AppManager.getAppManager().addActivity(this);
        View view = LayoutInflater.from(this).inflate(bindLayout(),null);
        setContentView(view);

        ButterKnife.bind(this);

        initView(view);
        doBusiness(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);// 结束Activity&从堆栈中移除
    }
    public abstract int bindLayout();
    public abstract void initView(View view);
    public abstract void doBusiness(Context context);

    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 通过Class跳转界面
     * @param cls
     */
    protected void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 含有Bundle通过Class跳转界面
     * @param cls
     * @param bundle
     */
    protected void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }else{
            LogUtil.e(TAG, "there is no activity can handle this intent: "+intent.getAction().toString());
        }
//        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);//Activity的进出效果
    }

    /**
     * 通过Action跳转界面
     * @param action
     */
    protected void startActivity(String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }else{
            LogUtil.e(TAG, "there is no activity can handle this intent: "+intent.getAction().toString());
        }
    }

    /***
     * 含有Date通过Action跳转界面
     * @param action
     * @param data
     */
    protected void startActivity(String action,Uri data) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.setData(data);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }else{
            LogUtil.e(TAG, "there is no activity can handle this intent: "+intent.getAction().toString());
        }
    }

    /**
     * 含有Bundle通过Action跳转界面
     * @param action
     * @param bundle
     */
    protected void startActivity(String action, Bundle bundle) {
        Intent intent = new Intent();
        intent.setAction(action);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }else{
            LogUtil.e(TAG, "there is no activity can handle this intent: "+intent.getAction().toString());
        }
//        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
    /**
     * 获取字符串资源
     * @param id
     * @return
     */
    public String getResStr(int id){
        return this.getResources().getString(id);
    }

    /**
     * 短暂显示Toast提示(来自res)
     * @param resId
     */
    protected void showShortToast(int resId) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast提示(来自res)
     * @param resId
     */
    protected void showLongToast(int resId) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_LONG).show();
    }

    /**
     * 显示短提示内容
     * @param text
     */
    protected void showShortToast(String text) {
        if (text != null && !text.equals("")) {
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 显示长提示内容
     */
    protected void showLongToast(String text) {
        if (text != null && !text.equals("")) {
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 默认退出
     */
    protected void defaultFinish() {
        super.finish();
    }

}
