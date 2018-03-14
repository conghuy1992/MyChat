package com.conghuy.mychat.dto;

import java.util.List;

/**
 * Created by maidinh on 6/6/2017.
 */

public class MessageDto {
    int success;
    String message = "";
    List<MessageDtoDetails> messageList;

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

    public List<MessageDtoDetails> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<MessageDtoDetails> messageList) {
        this.messageList = messageList;
    }
}
