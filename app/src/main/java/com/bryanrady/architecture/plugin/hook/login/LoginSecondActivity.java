package com.bryanrady.architecture.plugin.hook.login;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bryanrady.architecture.BaseActivity;
import com.bryanrady.architecture.R;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/2/26 0026.
 */

public class LoginSecondActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.tv_toolbar_center_title)
    TextView tvToolbarTitle;

    @Override
    public int bindLayout() {
        return R.layout.activity_plugin_hook_login_second;
    }

    @Override
    public void initView(View view) {
        initToolbar();
    }

    private void initToolbar() {
        tvToolbarTitle.setText("第二个Activity");
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
    }

    @Override
    public void doBusiness(Context context) {

    }
}
