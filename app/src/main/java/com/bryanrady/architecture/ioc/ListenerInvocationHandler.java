package com.bryanrady.architecture.ioc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2019/6/25.
 */

public class ListenerInvocationHandler implements InvocationHandler {

    private Object activity;
    private Method activityMethod;
    private String callbackMethod;

    public ListenerInvocationHandler(Object activity, Method activityMethod, String callbackMethod) {
        this.activity = activity;
        this.activityMethod = activityMethod;
        this.callbackMethod = callbackMethod;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals(callbackMethod)){
            return activityMethod.invoke(activity,args);
        }
        return null;
    }

}
