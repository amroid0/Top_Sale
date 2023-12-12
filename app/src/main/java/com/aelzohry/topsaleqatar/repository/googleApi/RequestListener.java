package com.aelzohry.topsaleqatar.repository.googleApi;


public interface RequestListener<T> {

    void onSuccess(T data);

    void onFail(String message, int code);
}
