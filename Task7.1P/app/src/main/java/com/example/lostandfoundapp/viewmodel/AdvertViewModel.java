package com.example.lostandfoundapp.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.lostandfoundapp.db.AdvertDao;
import com.example.lostandfoundapp.db.AppDatabase;
import com.example.lostandfoundapp.model.Advert;

import java.util.List;

public class AdvertViewModel extends AndroidViewModel {

    private AdvertDao advertDao;
    private LiveData<List<Advert>> allAdverts;

    public AdvertViewModel(Application application) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        advertDao = db.advertDao();
        allAdverts = advertDao.getAllAdverts();
    }

    public LiveData<List<Advert>> getAllAdverts() {
        return allAdverts;
    }

    public void insert(Advert advert) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            advertDao.insertAdvert(advert);
        });
    }

    public LiveData<Advert> getAdvertById(int id) {
        return advertDao.getAdvertById(id);
    }

    public void deleteAdvertById(int id) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            advertDao.deleteAdvertById(id);
        });
    }
}