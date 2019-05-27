package com.bryanrady.architecture.aop;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.bryanrady.architecture.R;
import com.bryanrady.architecture.aop.annotation.NeedPermission;
import com.bryanrady.architecture.aop.annotation.PermissionCanceled;
import com.bryanrady.architecture.aop.annotation.PermissionDenied;

/**
 * Created by Administrator on 2019/2/26.
 */

public class PermissionApplyFragment extends Fragment {

    private static final String TAG = "MyFragment";
    private Button button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_permission, container, false);
        button = view.findViewById(R.id.btn_permission_fragment_apply);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestReadPhone();
            }
        });
    }

    @NeedPermission(Manifest.permission.CALL_PHONE)
    private void requestReadPhone() {
        Toast.makeText(getContext(), "在Fragment中请求(打电话)权限通过!", Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied()
    private void denied() {
        Toast.makeText(getContext(), "在Fragment中请求(打电话)权限被拒绝!", Toast.LENGTH_SHORT).show();
    }

    @PermissionCanceled()
    private void canceled() {
        Toast.makeText(getContext(), "在Fragment中请求(打电话)权限被取消!", Toast.LENGTH_SHORT).show();
    }

}