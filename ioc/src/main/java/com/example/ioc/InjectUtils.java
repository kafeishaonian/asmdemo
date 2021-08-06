package com.example.ioc;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.example.ioc.impl.ContentView;
import com.example.ioc.impl.EventBase;
import com.example.ioc.impl.ViewInject;
import com.example.ioc.proxy.ListenerInvocationHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;


public class InjectUtils {

    /**
     * activity注解
     * @param activity
     */
    public static void inject(Activity activity) {
        inject(new ViewFinder(activity), activity);
    }

    /**
     * fragment注解
     * @param view
     * @param fragment
     */
    public static void inject(View view, Fragment fragment) {
        inject(new ViewFinder(view), fragment);
    }

    public static void inject(View view, Context context) {
        inject(new ViewFinder(view), context);
    }

    public static void inject(ViewFinder finder, Object object) {
        injectLayout(object);
        injectView(finder, object);
        injectEvents(finder, object);
    }

    /**
     * Activity加载布局
     *
     * @param context
     */
    private static void injectLayout(Object context) {
        int layoutId = 0;
        Class<?> clazz = context.getClass();
        ContentView contentView = clazz.getAnnotation(ContentView.class);
        if (contentView != null){
            layoutId = contentView.value();
            try {
                Method method = clazz.getMethod("setContentView", int.class);
                method.setAccessible(true);
                method.invoke(context, layoutId);
            } catch (Exception e){

            }
        }
    }

    /**
     * Activity加载view
     *
     * @param context
     */
    private static void injectView(ViewFinder finder, Object context) {
        Class<?> clazz = context.getClass();
        //获取activity类中所有的成员变量
        Field[] fields = clazz.getDeclaredFields();
        //遍历
        for (Field field : fields) {
            field.setAccessible(true);
            //得到成员变量的注解
            ViewInject viewInject = field.getAnnotation(ViewInject.class);
            if (viewInject != null) {
                //获取对应的id
                int valueId = viewInject.value();
                //反射获取控件
                View viewById = finder.findViewById(valueId);
                if (viewById == null) {
                    continue;
                }
                //反射调用方法
                try {
                    field.set(context, viewById);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 事件注入
     *
     * @param context
     */
    private static void injectEvents(ViewFinder finder, Object context) {
        Class<?> clazz = context.getClass();
        //获取activity里面所有的方法
        Method[] methods = clazz.getDeclaredMethods();
        //循环遍历
        for (Method method : methods) {
            //获取方法上面所有的注解
            Annotation[] annotations = method.getAnnotations();
            //循环遍历所有的注解
            for (Annotation annotation : annotations) {
                //获取注解类型
                Class<?> anntionType = annotation.annotationType();
                //获取注解上面的注解
                EventBase eventBase = anntionType.getAnnotation(EventBase.class);
                if (eventBase == null) {
                    continue;
                }
                //开始获取事件三要素  通过反射注入进去
                String listenerSetter = eventBase.listenerSetter();
                Class<?> listenerType = eventBase.listenerType();
                String callMethod = eventBase.callBackMethod();
                //方法名与方法method进行对应关系
                Map<String, Method> methodMap = new HashMap<>();
                methodMap.put(callMethod, method);

                try {
                    Method valueMethod = anntionType.getDeclaredMethod("value");
                    int[] viewIds = (int[]) valueMethod.invoke(annotation);
                    //遍历获取到的id
                    for (int viewId : viewIds) {
                        //通过反射获取相应的view控件
                        View view = finder.findViewById(viewId);
                        if (view == null) {
                            continue;
                        }
                        Method setListener = view.getClass().getMethod(listenerSetter, listenerType);
                        ListenerInvocationHandler handle = new ListenerInvocationHandler(context, methodMap);
                        Object proxy = Proxy.newProxyInstance(listenerType.getClassLoader(),
                                new Class[]{listenerType},
                                handle);

                        setListener.invoke(view, proxy);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
