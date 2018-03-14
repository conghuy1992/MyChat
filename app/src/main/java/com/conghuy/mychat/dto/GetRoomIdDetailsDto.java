package com.conghuy.mychat.dto;

/**
 * Created by maidinh on 6/7/2017.
 */

public class GetRoomIdDetailsDto {
    int chat_room_id;
    int user_id;

    public int getChat_room_id() {
        return chat_room_id;
    }

    public void setChat_room_id(int chat_room_id) {
        this.chat_room_id = chat_room_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
