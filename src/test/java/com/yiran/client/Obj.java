package com.yiran.client;

public class Obj<T> {

    public T t;

    public Obj(Config<T> config) {
    }

    public String print() {
        return String.valueOf(t);
    }

}
