package com.math4.user.mathplace;

import java.util.Date;

public class Chat {

    private String sender;
    private String receiver;
    private String message;
    private long messageTime;

    Chat(String sender, String receiver, String message){
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.messageTime = new Date().getTime();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
