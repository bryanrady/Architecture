package com.bryanrady.architecture.plugin.hook.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bryanrady.architecture.BaseActivity;
import com.bryanrady.architecture.R;

import butterknife.BindView;

/**
 * Created by Administrator on 2019/5/29.
 */

public class HookLoginMainActivity extends BaseActivity {

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
    }

    private void initToolbar() {
        tvToolbarTitle.setText("Hook钩子函数应用——登录架构");
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
    }

    @Override
    public void doBusiness(Context context) {

    }

    public void jump1(View view) {
        Intent intent = new Intent(this,HookLoginFirstActivity.class);
//        系统里面做了手脚   --》newIntent   msg--->obj-->intent
        startActivity(intent);
    }
    public void jump2(View view) {
        Intent intent = new Intent(this, HookLoginSecondActivity.class);
        startActivity(intent);
    }
    public void jump3(View view) {
        Intent intent = new Intent(this,HookLoginThirdActivity.class);
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
