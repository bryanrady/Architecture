package com.bryanrady.architecture.plugin.binder;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bryanrady.architecture.BaseActivity;
import com.bryanrady.architecture.MyAppAIDL;
import com.bryanrady.architecture.R;

import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;

/**
 * Created by Administrator on 2019/5/31.
 */

public class BinderClientActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.tv_toolbar_center_title)
    TextView tvToolbarTitle;

    @Override
    public int bindLayout() {
        return R.layout.activity_plugin_binder_client;
    }

    @Override
    public void initView(View view) {
        initToolbar();
    }

    private void initToolbar() {
        tvToolbarTitle.setText("Binder进程间通信");
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
    }

    @Override
    public void doBusiness(Context context) {

    }


    private boolean mIsConnect = false;

    public void remoteService(View view) {
        if (!mIsConnect){
            Intent intent = new Intent();
            intent.setAction("com.bryanrady.architecture.MyAppService");
            intent.setPackage("com.bryanrady.remoteprocess");   //远程服务的包名

            //目的   binderServise  ----->  IBinder iBinder
            bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        }else{
            try {
                if(myAppAIDL != null){
                    myAppAIDL.setName("张三");

                    String remoteName = myAppAIDL.getName();    //这是从远程得来的名字

                    Toast.makeText(BinderClientActivity.this, "remoteName == " + remoteName, Toast.LENGTH_SHORT).show();
                }

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private MyAppAIDL myAppAIDL;

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            Log.d("wangqingbin","onServiceConnected()............... 连接上了远程服务");

            Toast.makeText(BinderClientActivity.this, "远程服务已连接", Toast.LENGTH_SHORT).show();

            //通过获取远程服务的代理对象 通过 MyAppAIDL.Stub 然后进行 asInterface 就可以得到本地进程可用的代理对象 myAppAIDL
            myAppAIDL = MyAppAIDL.Stub.asInterface(service);
            try {
                if(myAppAIDL != null){
                    mIsConnect = true;

                    myAppAIDL.setName("张三");

                    String remoteName = myAppAIDL.getName();    //这是从远程得来的名字

                    Toast.makeText(BinderClientActivity.this, "remoteName == " + remoteName, Toast.LENGTH_SHORT).show();
                }

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        /**
         * 类ServiceConnection中的onServiceDisconnected()方法在正常情况下是不被调用的，
         * 它的调用时机是当Service服务被异外销毁时，例如内存的资源不足时.
         * @param name
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIsConnect = false;
            Log.d("wangqingbin","onServiceDisconnected()............... 与远程服务断开连接");

            Toast.makeText(BinderClientActivity.this, "远程服务已断开", Toast.LENGTH_SHORT).show();
        }
    };

    private void unBindService(){
        if(mServiceConnection != null){
            unbindService(mServiceConnection);
            mIsConnect = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unBindService();
    }

}
