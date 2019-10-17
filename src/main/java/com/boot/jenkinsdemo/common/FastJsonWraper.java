package com.boot.jenkinsdemo.common;

/**
 * Simple to Introduction
 * className:  FastJsonWraper
 *
 * @author: LYL
 * @version: 2019/10/14 15:17
 */
public class FastJsonWraper<T> {
    private T value;

    public FastJsonWraper() {
    }

    public FastJsonWraper(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
