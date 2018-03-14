package com.conghuy.mychat.dto;

import java.util.List;

/**
 * Created by maidinh on 6/7/2017.
 */

public class ListUser {
    List<LoginDto>user;
    int success;
    String message="";

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<LoginDto> getUser() {
        return user;
    }

    public void setUser(List<LoginDto> user) {
        this.user = user;
    }
}
