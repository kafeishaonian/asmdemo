package com.example.ioc.impl;

import android.widget.AdapterView;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@EventBase(listenerSetter = "setOnItemClickListener",
        listenerType = AdapterView.OnItemClickListener.class,
        callBackMethod = "onItemClick")
public @interface OnItemClick {
    int [] value();
}