package com.krish.assignment.utils;

/**
 * Created by Shashi on 10/9/2017.
 */

public interface OnResponseListener<T> {

    void onResponse(T response, WebServices.ApiType URL, boolean isSucces, int code);

}