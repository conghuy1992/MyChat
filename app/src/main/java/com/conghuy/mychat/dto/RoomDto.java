package com.conghuy.mychat.dto;

import java.util.List;

/**
 * Created by maidinh on 6/6/2017.
 */

public class RoomDto {
    int success;
    List<RoomDtoDetails> room;
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

    public List<RoomDtoDetails> getRoom() {
        return room;
    }

    public void setRoom(List<RoomDtoDetails> room) {
        this.room = room;
    }


}
