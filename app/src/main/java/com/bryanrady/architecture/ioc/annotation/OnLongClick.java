package com.bryanrady.architecture.ioc.annotation;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Administrator on 2019/6/24.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@EventBase(listenerSetter = "setOnLongClickListener",listenerType = View.OnLongClickListener.class,callbackMethod = "onLongClick")
public @interface OnLongClick {

    int[] value();

}
