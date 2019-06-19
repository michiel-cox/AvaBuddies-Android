package com.projectsoa.avabuddies.data.LocalStorage;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.lifecycle.LiveData;

import java.util.List;

@Dao
public interface ChatMessageDAO {
    @Query("SELECT * FROM chatmessagemodel WHERE chat_id IN (:chatId)")
    LiveData<List<ChatMessageModel>> getAllById(String chatId);

    @Insert
    void insertMessage(ChatMessageModel chatMessageModel);
}
