package com.projectsoa.avabuddies.data.LocalStorage;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class ChatMessageModel {
    @PrimaryKey @NonNull
    public String id;
    @ColumnInfo(name = "chat_id")
    public String chatId;
    @ColumnInfo(name = "user_id")
    public String userId;
    @ColumnInfo(name = "message")
    public String text;
    @ColumnInfo(name = "creation_date")
    public String createdAt;
}
