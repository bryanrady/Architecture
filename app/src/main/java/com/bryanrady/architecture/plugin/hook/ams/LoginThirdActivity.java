package com.bryanrady.architecture.plugin.hook.ams;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bryanrady.architecture.BaseActivity;
import com.bryanrady.architecture.R;

import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;


/**
 * Created by Administrator on 2018/2/26 0026.
 */

public class LoginThirdActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.tv_toolbar_center_title)
    TextView tvToolbarTitle;

    @Override
    public int bindLayout() {
        return R.layout.activity_plugin_hook_login_third;
    }

    @Override
    public void initView(View view) {
        initToolbar();
    }

    private void initToolbar() {
        tvToolbarTitle.setText("第三个Activity");
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
    }

    @Override
    public void doBusiness(Context context) {

    }
}
