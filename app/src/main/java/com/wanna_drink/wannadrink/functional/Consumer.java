package com.wanna_drink.wannadrink.functional;

public interface Consumer<T> {
    void apply(T t);
    Object get();
}