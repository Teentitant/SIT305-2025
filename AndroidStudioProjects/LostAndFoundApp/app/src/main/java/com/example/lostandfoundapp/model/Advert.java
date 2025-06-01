package com.example.lostandfoundapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "adverts")
public class Advert {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String postType;
    public String name;
    public String phone;
    public String description;
    public String date;
    public String location;


    public Advert(String postType, String name, String phone, String description, String date, String location) {
        this.postType = postType;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.date = date;
        this.location = location;
    }


    public int getId() {
        return id;
    }

}
