package com.bryanrady.lib_permission.core;

import android.content.Context;
import android.util.Log;

import com.bryanrady.lib_permission.annotation.NeedPermission;
import com.bryanrady.lib_permission.annotation.PermissionCanceled;
import com.bryanrady.lib_permission.annotation.PermissionDenied;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import androidx.fragment.app.Fragment;

/**
 * 定义切面
 * Created by Administrator on 2019/2/26.
 */

@Aspect
public class PermissionAspect {

    private static final String TAG = "PermissionAspect";

    @Pointcut("execution(@com.bryanrady.lib_permission.annotation.NeedPermission * *(..)) && @annotation(needPermission)")
    public void requestPermission(NeedPermission needPermission) {

    }

    @Around("requestPermission(needPermission)")
    public void aroundJointPoint(final ProceedingJoinPoint joinPoint, NeedPermission needPermission) throws Throwable{
        Context context = null;
        final Object aThis = joinPoint.getThis();
        if(aThis instanceof Context){
            context = (Context) aThis;
        }else if (aThis instanceof Fragment) {
            context = ((Fragment) aThis).getActivity();
        } else if (aThis instanceof android.app.Fragment) {
            context = ((android.app.Fragment) aThis).getActivity();
        } else {

        }

        if (context == null || needPermission == null) {
            Log.d(TAG, "PermissionAspect : context is null || permission is null");
            return;
        }

        final Context finalContext = context;

        String[] permissions = needPermission.value();
        int requestCode = needPermission.requestCode();
        //请求权限
        PermissionActivity.requestPermission(context, permissions, requestCode, new IPermission() {
            @Override
            public void granted() {
                try {
                    joinPoint.proceed();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            @Override
            public void denied() {
                PermissionUtils.invokAnnotation(aThis, PermissionCanceled.class);
            }

            @Override
            public void canceled() {
                PermissionUtils.invokAnnotation(aThis, PermissionDenied.class);
                PermissionUtils.goToMenu(finalContext);
            }
        });

    }

}
