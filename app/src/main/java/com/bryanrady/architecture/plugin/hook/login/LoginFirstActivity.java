package com.bryanrady.architecture.plugin.hook.login;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bryanrady.architecture.BaseActivity;
import com.bryanrady.architecture.R;

import butterknife.BindView;

/**
 * Created by 48608 on 2018/1/12.
 */

public class LoginFirstActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ll_toolbar_back)
    LinearLayout llBack;

    @BindView(R.id.tv_toolbar_center_title)
    TextView tvToolbarTitle;

    @BindView(R.id.tv_toolbar_right_title)
    TextView tvRightTitle;

    @Override
    public int bindLayout() {
        return R.layout.activity_plugin_hook_login_first;
    }

    @Override
    public void initView(View view) {
        initToolbar();
    }

    private void initToolbar() {
        tvToolbarTitle.setText("第一个Activity");
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
    }

    @Override
    public void doBusiness(Context context) {

    }
}
