package com.bryanrady.architecture.plugin;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bryanrady.architecture.BaseActivity;
import com.bryanrady.architecture.R;
import com.bryanrady.architecture.plugin.binder.BinderClientActivity;
import com.bryanrady.architecture.plugin.hook.HookActivity;
import com.bryanrady.architecture.plugin.insert.AliPayInsertActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * https://www.jianshu.com/p/b30de498c3cc
 * Created by wangqingbin on 2019/1/9.
 */

public class PluginActivity extends BaseActivity {

    @BindView(R.id.btn_plugin_insert)
    Button btn_plugin_insert;

    @BindView(R.id.btn_plugin_hook)
    Button btn_plugin_hook;

    @BindView(R.id.btn_plugin_binder)
    Button btn_plugin_binder;

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
        return R.layout.activity_plugin;
    }

    @Override
    public void initView(View view) {
        initToolbar();
    }

    private void initToolbar() {
        llBack.setVisibility(View.VISIBLE);
        tvToolbarTitle.setText("插件化应用");
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
    }

    @Override
    public void doBusiness(Context context) {

    }

    @OnClick({R.id.ll_toolbar_back,R.id.btn_plugin_insert,R.id.btn_plugin_hook,R.id.btn_plugin_binder})
    void onClick(View view){
        switch (view.getId()){
            case R.id.ll_toolbar_back:
                defaultFinish();
                break;
            case R.id.btn_plugin_insert:
                startActivity(AliPayInsertActivity.class);
                break;
            case R.id.btn_plugin_hook:
                startActivity(HookActivity.class);
                break;
            case R.id.btn_plugin_binder:
                startActivity(BinderClientActivity.class);
                break;
        }
    }

}
