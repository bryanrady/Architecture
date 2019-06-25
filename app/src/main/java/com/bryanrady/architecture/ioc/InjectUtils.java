package com.bryanrady.architecture.ioc;

import android.view.View;

import com.bryanrady.architecture.ioc.annotation.ContentView;
import com.bryanrady.architecture.ioc.annotation.EventBase;
import com.bryanrady.architecture.ioc.annotation.ViewInject;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Administrator on 2019/6/24.
 */

public class InjectUtils {

    public static void inject(Object activity){
        injectLayout(activity);
        injectView(activity);
        injectClick(activity);
    }

    /**
     * 根据ContentView注解进行布局注入
     * @param activity
     */
    private static void injectLayout(Object activity) {
        Class<?> activityClass = activity.getClass();
        ContentView contentViewAnnotation = activityClass.getAnnotation(ContentView.class);
        if(contentViewAnnotation != null){
            int layoutId = contentViewAnnotation.value();
            try {
                Method setContentViewMethod = activityClass.getMethod("setContentView", int.class);
                setContentViewMethod.invoke(activity,layoutId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据ViewInject注解进行控件注入
     * @param activity
     */
    private static void injectView(Object activity) {
        Class<?> activityClass = activity.getClass();
        Field[] declaredFields = activityClass.getDeclaredFields();
        for (Field field : declaredFields){
            ViewInject viewInjectAnnotation = field.getAnnotation(ViewInject.class);
            if (viewInjectAnnotation != null){
                int viewId = viewInjectAnnotation.value();
                try {
                    Method findViewByIdMethod = activityClass.getMethod("findViewById", int.class);
                    View view = (View) findViewByIdMethod.invoke(activity, viewId);
                    field.setAccessible(true);
                    field.set(activity,view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

    }

    private static void injectClick(Object activity) {
        Class<?> activityClass = activity.getClass();
        Method[] declaredMethods = activityClass.getDeclaredMethods();
        for (Method method : declaredMethods){
            //1.拿到每个方法上的所有注解
            //OnClick onClickAnnotation = method.getAnnotation(OnClick.class); 不能这样写死 否则就只能支持OnClick事件了
            Annotation[] methodAnnotations = method.getAnnotations();
            for (Annotation annotation : methodAnnotations){
                //2.得到各个注解的注解类型
                Class<? extends Annotation> annotationType = annotation.annotationType();
                //3.找到所有注解中的EventType注解
                EventBase eventBaseAnnotation = annotationType.getAnnotation(EventBase.class);
                if(eventBaseAnnotation == null){
                    continue;
                }
                //3.拿到事件3要素
                String listenerSetter = eventBaseAnnotation.listenerSetter();
                Class<?> listenerType = eventBaseAnnotation.listenerType();
                String callbackMethod = eventBaseAnnotation.callbackMethod();

                //  int[] value = onClickAnnotation.value();    //这里也不能这样写死

                try {
                    //通过反射注解中的方法来进行实现 得到注解中的int数组
                    Method valueMethod = annotationType.getDeclaredMethod("value");
                    valueMethod.setAccessible(true);
                    int[] valueArr = (int[]) valueMethod.invoke(annotation);

                    for (int viewId : valueArr){
                        Method findViewByIdMethod = activityClass.getMethod("findViewById", int.class);
                        View view = (View) findViewByIdMethod.invoke(activity, viewId);
                        if(view == null){
                            continue;
                        }
                        //public void setOnClickListener(@Nullable OnClickListener l) {
                        Method setListenerMethod = view.getClass().getMethod(listenerSetter, listenerType);
                        //使用动态代理代理将 setOnClickListener 里面的 onClick方法 转换到 activity中的方法中去
                        ListenerInvocationHandler listenerInvocationHandler = new ListenerInvocationHandler(activity, method, callbackMethod);
                        Object proxyInstance = Proxy.newProxyInstance(
                                listenerType.getClassLoader(),
                                new Class[]{listenerType},
                                listenerInvocationHandler
                        );
                        setListenerMethod.invoke(view,proxyInstance);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

}
