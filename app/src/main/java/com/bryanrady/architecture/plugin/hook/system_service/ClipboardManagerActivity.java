package com.bryanrady.architecture.plugin.hook.system_service;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bryanrady.architecture.BaseActivity;
import com.bryanrady.architecture.R;

import butterknife.BindView;

/**
 * Created by Administrator on 2019/5/31.
 */

public class ClipboardManagerActivity extends BaseActivity {

    @BindView(R.id.tv_clipboard)
    TextView tv_clipboard;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.tv_toolbar_center_title)
    TextView tvToolbarTitle;

    @Override
    public int bindLayout() {
        return R.layout.activity_plugin_hook_clipboard;
    }

    @Override
    public void initView(View view) {
        initToolbar();
    }

    private void initToolbar() {
        tvToolbarTitle.setText("拦截ClipboardManager剪切板系统服务");
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
    }

    @Override
    public void doBusiness(Context context) {
        //我们在获取系统服务的时候 进行拦截
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        //设置剪贴板的内容
        clipboardManager.setPrimaryClip(ClipData.newPlainText("data","I am raw content"));
        tv_clipboard.append("I am raw content\n");
        //获取剪贴板的内容
        ClipData primaryClip = clipboardManager.getPrimaryClip();
        String primaryClipText = primaryClip.getItemAt(0).getText().toString();
        Log.d("wangqingbin","primaryClipText == "+ primaryClipText);

        tv_clipboard.append(primaryClipText);
    }


}
