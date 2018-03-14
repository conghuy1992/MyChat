package com.conghuy.mychat.dto;

import java.io.Serializable;

/**
 * Created by maidinh on 6/6/2017.
 */

public class RoomDtoDetails implements Serializable {
    public int chat_room_id;
    public String name = "";
    public int user_one;
    public int user_two;
    public String last_msg = "";
    public String created_at = "";
    public int success;
    public String image_noti;
    public String avatar = "";
    public long milliseconds;
    public boolean is_background = false;

    public int message_id;
    public String timestamp = "";
    public int user_id_sent;
    public int type;
}
