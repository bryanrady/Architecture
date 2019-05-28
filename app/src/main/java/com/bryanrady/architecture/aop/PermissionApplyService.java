package com.bryanrady.architecture.aop;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.bryanrady.lib_permission.annotation.NeedPermission;
import com.bryanrady.lib_permission.annotation.PermissionCanceled;
import com.bryanrady.lib_permission.annotation.PermissionDenied;


/**
 * Created by Administrator on 2019/2/26.
 */

public class PermissionApplyService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        requestCallPhone();

        return super.onStartCommand(intent, flags, startId);
    }

    @NeedPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    private void requestCallPhone() {
        Toast.makeText(getApplicationContext(),"Service中请求(读)权限成功!",Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied()
    private void denied() {
        Toast.makeText(getApplicationContext(),"Service中请求(读)权限被拒绝!",Toast.LENGTH_SHORT).show();
    }

    @PermissionCanceled()
    private void canceled() {
        Toast.makeText(getApplicationContext(),"Service中请求(读)权限被取消!",Toast.LENGTH_SHORT).show();
    }
}