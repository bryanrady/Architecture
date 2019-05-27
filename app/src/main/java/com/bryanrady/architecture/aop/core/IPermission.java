package com.bryanrady.architecture.aop.core;

/**
 * Created by wangqingbin on 2019/2/26.
 */

public interface IPermission {

    /**
     * 已授权
     */
    void granted();

    /**
     * 权限被拒绝
     */
    void denied();

    /**
     * 权限被取消
     */
    void canceled();
}
