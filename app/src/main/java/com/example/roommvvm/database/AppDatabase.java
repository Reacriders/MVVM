package com.example.roommvvm.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {UrlEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UrlDao urlDao();
}
