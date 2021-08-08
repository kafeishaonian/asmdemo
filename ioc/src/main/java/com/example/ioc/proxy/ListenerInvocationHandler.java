package com.example.ioc.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 动态代理
 */

public class ListenerInvocationHandler implements InvocationHandler{
    //代理的真实对象
    private Object context;
    private Map<String,Method> methodMap;

    public ListenerInvocationHandler(Object context, Map<String, Method> methodMap) {
        this.context = context;
        this.methodMap = methodMap;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String name = method.getName();
        //决定是否需要进行代理
        Method metf = methodMap.get(name);
        if(metf!=null){
            return metf.invoke(context,args);
        }else{
            return metf.invoke(proxy,args);
        }
    }
}
