package com.conghuy.mychat.dto;

/**
 * Created by maidinh on 5/26/2017.
 */

public class StatusLogin {
    int status;
    int StringId;

    public StatusLogin() {
    }

    public StatusLogin(int status, int stringId) {
        this.status = status;
        StringId = stringId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStringId() {
        return StringId;
    }

    public void setStringId(int stringId) {
        StringId = stringId;
    }
}
