package com.projectsoa.avabuddies.data.LocalStorage;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ChatMessageModel.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ChatMessageDAO chatMessageDAO();
}
