package com.bryanrady.architecture.plugin.hook.ams;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bryanrady.architecture.BaseActivity;
import com.bryanrady.architecture.R;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/2/26 0026.
 */

public class LoginLoginActivity extends BaseActivity {

    @BindView(R.id.name)
    EditText name;

    @BindView(R.id.password)
    EditText password;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ll_toolbar_back)
    LinearLayout llBack;

    @BindView(R.id.tv_toolbar_center_title)
    TextView tvToolbarTitle;

    @BindView(R.id.tv_toolbar_right_title)
    TextView tvRightTitle;

    private String mClassName;
    private SharedPreferences mPreferences;

    @Override
    public int bindLayout() {
        return R.layout.activity_plugin_hook_login_login;
    }

    @Override
    public void initView(View view) {
        initToolbar();
    }

    private void initToolbar() {
        tvToolbarTitle.setText("登录界面");
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
    }

    @Override
    public void doBusiness(Context context) {
        mPreferences = context.getSharedPreferences("wangqingbin", MODE_PRIVATE);
        mClassName = getIntent().getStringExtra("extraIntent");
        if (mClassName != null) {
            ((TextView)findViewById(R.id.text)).setText(" 跳转界面："+mClassName);
        }
    }

    public void login(View view) {
        if ((name.getText() == null || password.getText() == null)) {
            Toast.makeText(this, "请填写用户名 或密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("wangqingbin".equals(name.getText().toString()) && "123456".equals(password.getText().toString())) {
            SharedPreferences share = this.getSharedPreferences("wangqingbin", MODE_PRIVATE);//实例化
            SharedPreferences.Editor editor = share.edit(); //使处于可编辑状态
            editor.putString("name", name.getText().toString());
            editor.putString("password", password.getText().toString());
            editor.putBoolean("login",true);   //设置保存的数据
            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
            editor.commit();    //提交数据保存

            if (mClassName != null) {
                ComponentName componentName = new ComponentName(this, mClassName);
                Intent intent = new Intent();
                intent.setComponent(componentName);
                startActivity(intent);
                finish();
            }
        }else{
            SharedPreferences.Editor editor = mPreferences.edit(); //使处于可编辑状态
            editor.putBoolean("login",false);   //设置保存的数据
            Toast.makeText(this, "登录失败", Toast.LENGTH_SHORT).show();
            editor.commit();    //提交数据保存
        }
    }
}
