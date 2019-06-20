package com.projectsoa.avabuddies.data.repositories;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.projectsoa.avabuddies.App;
import com.projectsoa.avabuddies.data.LocalStorage.AppDatabase;
import com.projectsoa.avabuddies.data.LocalStorage.ChatMessageModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChatMessageRepository {

    private String DB_NAME = "chat_database";

    private AppDatabase appDatabase;

    public ChatMessageRepository() {
        appDatabase = Room.databaseBuilder(App.getAppContext(), AppDatabase.class, DB_NAME).build();
    }

    public void insertTask(String chatId, String userId, String id, String text, Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'z'");
        ChatMessageModel message = new ChatMessageModel();
        message.chatId = chatId;
        message.userId = userId;
        message.id = id;
        message.text = text;
        message.createdAt = format.format(date);

        insertTask(message);
    }

    public void insertTask(final ChatMessageModel message) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                appDatabase.chatMessageDAO().insertMessage(message);
                return null;
            }
        }.execute();
    }

    public List<ChatMessageModel> getTask(String id) {
        return appDatabase.chatMessageDAO().getAllById(id);
    }
}
