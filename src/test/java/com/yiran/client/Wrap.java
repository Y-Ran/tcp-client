package com.yiran.client;

public class Wrap<T> {

    public Obj<T> create(Config<T> config) {
        return new Obj<>(config);
    }
}
