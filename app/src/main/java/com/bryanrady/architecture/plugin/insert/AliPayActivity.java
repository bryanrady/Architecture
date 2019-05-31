package com.bryanrady.architecture.plugin.insert;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bryanrady.architecture.BaseActivity;
import com.bryanrady.architecture.R;
import com.bryanrady.lib_permission.annotation.NeedPermission;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;

/**
 * 模拟支付宝 打开 淘票票 应用
 *  1.1插件化开发的好处
 *      宿主和插件分开编译
 *      并发开发
 *      动态更新插件
 *      按需下载模块
 *      方法数或变量数 爆棚
 *
 *  1.2  DynamicLoadApk 提供了 3 种开发方式，让开发者在无需理解其工作原理的情况下快速的集成插件化功能。
 *          宿主程序与插件完全独立
 *          宿主程序开放部分接口供插件与之通信
 *          宿主程序耦合插件的部分业务逻辑
 *          三种开发模式都可以在 demo 中看到。
 *
 *  1.3 核心概念
 *          (1) 宿主：主 App，可以加载插件，也称 Host。
 *          (2) 插件：插件 App，被宿主加载的 App，也称 Plugin，可以是跟普通 App 一样的 Apk 文件。
 *          (3) 组件：指 Android 中的Activity、Service、BroadcastReceiver、ContentProvider，目前 DL 支持Activity、
 *          Service以及动态的BroadcastReceiver。
 *          (4) 插件组件：插件中的组件。
 *          (5) 代理组件：在宿主的 Manifest 中注册，启动插件组件时首先被启动的组件。
 *              目前包括 DLProxyActivity(代理 Activity)、DLProxyFragmentActivity(代理 FragmentActivity)、
 *              DLProxyService(代理 Service)。
 *
 * Created by Administrator on 2019/5/27.
 */

public class AliPayActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ll_toolbar_back)
    LinearLayout llBack;

    @BindView(R.id.tv_toolbar_center_title)
    TextView tvToolbarTitle;

    @BindView(R.id.tv_toolbar_right_title)
    TextView tvRightTitle;

    public static final String MAIN_TO_PLUGIN_ACTION = "com.bryanrady.architecture.plugin.load_apk.MAIN_TO_PLUGIN_ACTION";
    public static final String PLUGIN_TO_MAIN_ACTION = "com.bryanrady.taopiaopiao.PLUGIN_TO_MAIN_ACTION";

    @Override
    public int bindLayout() {
        return R.layout.activity_plugin_load;
    }

    @Override
    public void initView(View view) {
        initToolbar();
    }

    private void initToolbar() {
        tvToolbarTitle.setText("插桩技术加载插件apk");
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
    }

    @Override
    public void doBusiness(Context context) {
        registerReceiver(mReceiver, new IntentFilter(PLUGIN_TO_MAIN_ACTION));
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(PLUGIN_TO_MAIN_ACTION.equals(intent.getAction())){
                Toast.makeText(context, " 我是宿主，收到你的回复,握手完成!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void load(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                copyFileToPrivateDir(AliPayActivity.this);
            }
        }).start();
    }

    public void sendBroadCast(View view){
        sendBroadcast(new Intent(MAIN_TO_PLUGIN_ACTION));
        Toast.makeText(getApplicationContext(), " 我是宿主, 插件插件 ,收到请回答!!", Toast.LENGTH_SHORT).show();
    }

    /**
     * 将外置卡中的插件apk复制到私有目录中 然后加载插件
     * @param context
     */

    @NeedPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    private void copyFileToPrivateDir(Context context){
        String fileName = "taopiaopiao.apk";

        File filesDir = context.getDir("plugin", Context.MODE_PRIVATE);
        String filePath = new File(filesDir, fileName).getAbsolutePath();
        File file = new File(filePath);
        if(file.exists()){
            file.delete();
        }

        InputStream is = null;
        FileOutputStream fos = null;
        try {
            is = new FileInputStream(new File(Environment.getExternalStorageDirectory()
                    +"/plugin",fileName).getAbsolutePath());
            fos = new FileOutputStream(filePath);

            int len;
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer)) != -1){
                fos.write(buffer,0, len);
            }

            boolean loadIsSuccess = PluginManager.getInstance().loadPluginApk(context);
            if(loadIsSuccess){
                Intent intent = new Intent(this, ProxyActivity.class);
                intent.putExtra("activityName", PluginManager.getInstance().getPackageInfo().activities[0].name);
                startActivity(intent);
            }else{
                Toast.makeText(this,"加载插件出了问题",Toast.LENGTH_SHORT).show();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mReceiver != null){
            unregisterReceiver(mReceiver);
        }
    }
}
