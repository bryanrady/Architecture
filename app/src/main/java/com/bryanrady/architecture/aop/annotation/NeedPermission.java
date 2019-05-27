package com.bryanrady.architecture.aop.annotation;

import com.bryanrady.architecture.aop.core.PermissionUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 给需要权限的方法添加上此注解
 * Created by Administrator on 2019/2/26.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface NeedPermission {

    String[] value();
    int requestCode() default PermissionUtils.DEFAULT_REQUEST_CODE;

}
