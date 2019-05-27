package com.bryanrady.architecture.aop;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bryanrady.architecture.BaseActivity;
import com.bryanrady.architecture.R;
import com.bryanrady.architecture.aop.annotation.NeedPermission;
import com.bryanrady.architecture.aop.annotation.PermissionCanceled;
import com.bryanrady.architecture.aop.annotation.PermissionDenied;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2019/2/26.
 */

public class DynamicPermissionActivity extends BaseActivity {

    @BindView(R.id.btn_permission_apply_single)
    Button btn_permission_apply_single;

    @BindView(R.id.btn_permission_apply_multiple)
    Button btn_permission_apply_multiple;

    @BindView(R.id.btn_permission_apply_location)
    Button btn_permission_apply_location;

    @BindView(R.id.btn_permission_apply_service)
    Button btn_permission_apply_service;

    @BindView(R.id.btn_permission_apply_fragment)
    Button btn_permission_apply_fragment;

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
        return R.layout.activity_dynamic_permission;
    }

    @Override
    public void initView(View view) {
        initToolbar();
    }

    private void initToolbar() {
        llBack.setVisibility(View.VISIBLE);
        tvToolbarTitle.setText("Aop动态权限申请");
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
    }

    @Override
    public void doBusiness(Context context) {

    }

    @OnClick({R.id.ll_toolbar_back,R.id.btn_permission_apply_single,R.id.btn_permission_apply_multiple
            , R.id.btn_permission_apply_location,R.id.btn_permission_apply_service,R.id.btn_permission_apply_fragment})
    void onClick(View view){
        switch (view.getId()){
            case R.id.ll_toolbar_back:
                defaultFinish();
                break;
            case R.id.btn_permission_apply_single:
                requestSinglePermission();
                break;
            case R.id.btn_permission_apply_multiple:
                requestMultiplePermission();
                break;
            case R.id.btn_permission_apply_location:
                requestLocationPermission();
                break;
            case R.id.btn_permission_apply_service:
                Intent intent = new Intent(this,PermissionApplyService.class);
                startService(intent);
                break;
            case R.id.btn_permission_apply_fragment:
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.frame_layout, new PermissionApplyFragment());
                transaction.commit();
                break;
        }
    }

    @NeedPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private void requestSinglePermission() {
        showShortToast("请求(写)的单个权限成功!");
    }

    @NeedPermission({Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE})
    private void requestMultiplePermission() {
        showShortToast("请求(录音和相机)多个权限成功!");
    }

    @PermissionCanceled()
    private void canceled() {
        showShortToast("请求的权限被取消!");
    }

    @PermissionDenied()
    private void denied() {
        showShortToast("请求的权限被拒绝!");
    }

    @NeedPermission(value = {Manifest.permission.ACCESS_FINE_LOCATION},requestCode = 200)
    private void requestLocationPermission() {
        showShortToast("请求定位权限成功!");
    }

    @PermissionCanceled(requestCode = 200)
    private void canceledLocation(){
        showShortToast("请求定位权限被取消!");
    }

    @PermissionDenied(requestCode = 200)
    private void deniedLocation(){
        showShortToast("请求定位权限被拒绝!");
    }

}