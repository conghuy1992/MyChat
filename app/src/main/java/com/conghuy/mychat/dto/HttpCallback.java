package com.conghuy.mychat.dto;

/**
 * Created by maidinh on 5/25/2017.
 */

public interface HttpCallback {
    void onSuccess(String response);
    void onFail(String error);
}
