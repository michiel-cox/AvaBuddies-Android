package com.projectsoa.avabuddies.data.models;

import com.stfalcon.chatkit.commons.models.IMessage;

import java.util.Date;

public class Message implements IMessage {

    protected int id;
    protected int chatId;
    protected int senderId;
    protected String message;
    protected Date creationDate;

    public int getId() {
        return id;
    }

    public int getChatId() {
        return chatId;
    }

    public int getSender() {
        return senderId;
    }

    public String getMessage() {
        return message;
    }

    public Date getCreationDate() {
        return creationDate;
    }
}