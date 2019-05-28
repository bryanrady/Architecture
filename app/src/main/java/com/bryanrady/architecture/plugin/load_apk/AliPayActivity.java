package com.bryanrady.architecture.plugin.load_apk;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

    @Override
    public int bindLayout() {
        return R.layout.activity_load_plugin;
    }

    @Override
    public void initView(View view) {
        initToolbar();
    }

    private void initToolbar() {
        tvToolbarTitle.setText("插件化应用");
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
    }

    @Override
    public void doBusiness(Context context) {

    }

    public void load(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                copyFileToPrivateDir(AliPayActivity.this);
            }
        }).start();
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
                Log.d("wangqingbin","PluginManager.getInstance().getPackageInfo().activities[0].name=="+PluginManager.getInstance().getPackageInfo().activities[0].name);
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

    public void click(View view) {
        Log.d("wangqingbin","PluginManager.getInstance().getPackageInfo().activities[0].name=="+PluginManager.getInstance().getPackageInfo().activities[0].name);
        Intent intent = new Intent(this, ProxyActivity.class);
        intent.putExtra("activityName", PluginManager.getInstance().getPackageInfo().activities[0].name);
        startActivity(intent);

//        Intent aIntent = new Intent(this, ProxyActivity.class);
//        aIntent.putExtra("activityName", PluginManager.getInstance().getPackageInfo().activities[0].name);
//        startActivity(aIntent);
//
//        Intent sIntent = new Intent(this, ProxyService.class);
//        sIntent.putExtra("serviceName", PluginManager.getInstance().getPackageInfo().services[0].name);
//        startService(sIntent);
    }
}
