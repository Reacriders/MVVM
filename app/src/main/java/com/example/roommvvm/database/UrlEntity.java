package com.example.roommvvm.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "urls")
public class UrlEntity implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "url")
    public String url;

    public String getName() {
        return url;
    }

    @ColumnInfo(name = "date")
    public Integer date;

    public UrlEntity(String url, Integer date) {
        this.url = url;
        this.date = date;
    }
}
