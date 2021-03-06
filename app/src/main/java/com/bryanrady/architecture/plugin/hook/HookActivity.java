package com.bryanrady.architecture.plugin.hook;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bryanrady.architecture.BaseActivity;
import com.bryanrady.architecture.R;
import com.bryanrady.architecture.plugin.hook.ams.LoginMainActivity;
import com.bryanrady.architecture.plugin.hook.dex_elements.DexElementsActivity;
import com.bryanrady.architecture.plugin.hook.loadedapk.LoadedApkActivity;
import com.bryanrady.architecture.plugin.hook.system_service.ClipboardManagerActivity;

import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2019/5/29.
 */

public class HookActivity extends BaseActivity{

    @BindView(R.id.btn_plugin_hook_login)
    Button btn_plugin_hook_login;

    @BindView(R.id.btn_plugin_hook_clipboard_service)
    Button btn_plugin_hook_clipboard_service;

    @BindView(R.id.btn_plugin_hook_dex_elements)
    Button btn_plugin_hook_dex_elements;

    @BindView(R.id.btn_plugin_hook_loadedApk)
    Button btn_plugin_hook_loadedApk;

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
        return R.layout.activity_plugin_hook;
    }

    @Override
    public void initView(View view) {
        initToolbar();
    }

    private void initToolbar() {
        llBack.setVisibility(View.VISIBLE);
        tvToolbarTitle.setText("Hook钩子函数应用");
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
    }

    @Override
    public void doBusiness(Context context) {

    }

    @OnClick({R.id.ll_toolbar_back,R.id.btn_plugin_hook_login,R.id.btn_plugin_hook_clipboard_service,
            R.id.btn_plugin_hook_dex_elements,R.id.btn_plugin_hook_loadedApk})
    void onClick(View view){
        switch (view.getId()){
            case R.id.ll_toolbar_back:
                defaultFinish();
                break;
            case R.id.btn_plugin_hook_login:
                startActivity(LoginMainActivity.class);
                break;
            case R.id.btn_plugin_hook_clipboard_service:
                startActivity(ClipboardManagerActivity.class);
                break;
            case R.id.btn_plugin_hook_dex_elements:
                startActivity(DexElementsActivity.class);
                break;
            case R.id.btn_plugin_hook_loadedApk:
                startActivity(LoadedApkActivity.class);
                break;
        }
    }

}
