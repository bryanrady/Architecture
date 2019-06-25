package com.bryanrady.architecture.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Administrator on 2019/6/24.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)    //表示EventType这个注解是作用在另一个注解上面的
public @interface EventBase {

    //事件三要素: 订阅、事件源、事件类型。

    /**
     * setOnClickListener  订阅
     * @return
     */
    String listenerSetter();

    /**
     * 事件源的Class类型
     * @return
     */
    Class<?> listenerType();

    /**
     * 事件被触发之后，执行的回调方法的事件类型
     * @return
     */
    String callbackMethod();

}
