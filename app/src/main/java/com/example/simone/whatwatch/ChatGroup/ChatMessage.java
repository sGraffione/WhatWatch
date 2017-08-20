package com.example.simone.whatwatch.ChatGroup;

import java.util.Date;

public class ChatMessage {

    private String messageText;
    private String messageUser;
    private long messageTime;
    private String firebaseId;

    public ChatMessage(String messageText, String messageUser, String firebaseId) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.firebaseId = firebaseId;

        // Initialize to current time
        messageTime = new Date().getTime();
    }

    public ChatMessage(){

    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }
}