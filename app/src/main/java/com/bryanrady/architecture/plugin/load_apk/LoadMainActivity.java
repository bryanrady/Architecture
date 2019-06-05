package com.bryanrady.architecture.plugin.load_apk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bryanrady.architecture.BaseActivity;
import com.bryanrady.architecture.R;

import butterknife.BindView;

/**
 * Created by Administrator on 2019/5/29.
 */

public class LoadMainActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.tv_toolbar_center_title)
    TextView tvToolbarTitle;

    @Override
    public int bindLayout() {
        return R.layout.acitivity_plugin_hook_login_main;
    }

    @Override
    public void initView(View view) {
        initToolbar();

        Button jump1 = findViewById(R.id.jump1);
        jump1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump1(v);
            }
        });
    }

    private void initToolbar() {
        tvToolbarTitle.setText("加载apk实现集中式登录");
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
    }

    @Override
    public void doBusiness(Context context) {

    }

    public void jump1(View view) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.bryanrady.plugin_load_apk",
                "com.bryanrady.plugin_load_apk.LoadFirstActivity"));
        startActivity(intent);
    }
    public void jump2(View view) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.bryanrady.plugin_load_apk",
                "com.bryanrady.plugin_load_apk.LoadSecondActivity"));
        startActivity(intent);
    }

    public void jump3(View view) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.bryanrady.plugin_load_apk",
                "com.bryanrady.plugin_load_apk.LoadThirdActivity"));
        startActivity(intent);
    }

    public void logout(View view) {
        SharedPreferences share = this.getSharedPreferences("wangqingbin", MODE_PRIVATE);//实例化
        SharedPreferences.Editor editor = share.edit(); //使处于可编辑状态
        editor.putBoolean("login",false);   //设置保存的数据
        Toast.makeText(this, "退出登录成功",Toast.LENGTH_SHORT).show();
        editor.commit();    //提交数据保存
    }
}
