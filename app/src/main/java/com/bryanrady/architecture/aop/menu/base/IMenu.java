package com.bryanrady.architecture.aop.menu.base;


import android.content.Context;
import android.content.Intent;

public interface IMenu {

    /**
     * 通往设置中的请求权限页面
     * @param context
     * @return
     */
    Intent getMenuIntent(Context context);

}
