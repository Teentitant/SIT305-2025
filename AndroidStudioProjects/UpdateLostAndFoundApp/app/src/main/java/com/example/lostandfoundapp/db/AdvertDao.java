package com.example.lostandfoundapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.lostandfoundapp.model.Advert;

import java.util.List;

@Dao
public interface AdvertDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAdvert(Advert advert); // For suspend use with Kotlin Coroutines, in Java this is blocking or use ListenableFuture/RxJava

    @Query("SELECT * FROM adverts ORDER BY id DESC")
    LiveData<List<Advert>> getAllAdverts();

    @Query("SELECT * FROM adverts WHERE id = :advertId")
    LiveData<Advert> getAdvertById(int advertId);

    @Query("DELETE FROM adverts WHERE id = :advertId")
    void deleteAdvertById(int advertId); // Blocking operation
}