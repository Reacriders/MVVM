package com.example.roommvvm.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface UrlDao {
    @Query("SELECT * FROM urls")
    List<UrlEntity> getAll();

    @Insert
    long insertUrl(UrlEntity urlEntity);

    @Query("DELETE FROM urls WHERE date > :top")
    void deleteUrlBySalary(Integer top);

    @Delete
    void deleteUrl(UrlEntity urlEntity);
}