package com.bryanrady.lib_permission.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.bryanrady.lib_permission.R;

import java.util.ArrayList;
import java.util.List;

import androidx.core.app.ActivityCompat;

/**
 * 一个透明的Activity 专门用来做权限申请
 * Created by wangqingbin on 2019/2/26.
 */

public class PermissionActivity extends Activity {

    private static final String PARAM_PERMISSIONS = "param_permissions";
    private static final String PARAM_REQUEST_CODE = "param_request_code";

    private String[] mPermissions;
    private int mRequestCode;
    private static IPermission mIPermission;

    public static void requestPermission(Context context, String[] permissions, int requestCode, IPermission iPermission){
        mIPermission = iPermission;

        Intent intent = new Intent(context, PermissionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Bundle bundle = new Bundle();
        bundle.putStringArray(PARAM_PERMISSIONS, permissions);
        bundle.putInt(PARAM_REQUEST_CODE, requestCode);
        intent.putExtras(bundle);

        context.startActivity(intent);

        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(0, 0);
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_layout);

        this.mPermissions = getIntent().getStringArrayExtra(PARAM_PERMISSIONS);
        this.mRequestCode = getIntent().getIntExtra(PARAM_REQUEST_CODE, -1);

        if (mPermissions == null || mRequestCode < 0 || mIPermission == null) {
            this.finish();
            return;
        }

        //检查是否已授权
        if (PermissionUtils.hasPermission(this, mPermissions)) {
            mIPermission.granted();
            finish();
            return;
        }

        //请求权限
        ActivityCompat.requestPermissions(this, this.mPermissions, this.mRequestCode);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //请求权限成功
        if (PermissionUtils.verifyPermission(this, grantResults)) {
            mIPermission.granted();
            finish();
            return;
        }

        //用户点击了不再显示
        if (!PermissionUtils.shouldShowRequestPermissionRationale(this, permissions)) {
            if (permissions.length != grantResults.length) {
                return;
            }
            List<String> deinedList = new ArrayList<>();
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    deinedList.add(permissions[i]);
                }
            }
            mIPermission.denied();
            finish();
            return;
        }

        //用户取消
        mIPermission.canceled();
        finish();

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,0);
    }
}
