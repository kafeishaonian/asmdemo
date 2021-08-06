package com.example.ioc.impl;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EventBase(listenerSetter = "setOnClickListener",
        listenerType = View.OnClickListener.class,
        callBackMethod = "onClick")
public @interface OnClick {
    /**
     * 点击事件控件数组
     *
     * @return
     */
    int[] value() default -1;
}
