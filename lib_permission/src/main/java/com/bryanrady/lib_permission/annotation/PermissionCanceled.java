package com.bryanrady.lib_permission.annotation;

import com.bryanrady.lib_permission.core.PermissionUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Administrator on 2019/2/26.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PermissionCanceled {

    int requestCode() default PermissionUtils.DEFAULT_REQUEST_CODE;

}
