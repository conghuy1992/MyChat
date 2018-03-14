package com.conghuy.mychat.dto;

import java.util.List;

/**
 * Created by maidinh on 6/7/2017.
 */

public class GetRoomIdDto {
    List<GetRoomIdDetailsDto> roomList;
    int success;
    String message = "";

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public List<GetRoomIdDetailsDto> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<GetRoomIdDetailsDto> roomList) {
        this.roomList = roomList;
    }
}
