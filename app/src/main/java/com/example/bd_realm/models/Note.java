package com.example.bd_realm.models;

import com.example.bd_realm.apps.MyApplication;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

//Heradamos de RealmObject si queremos que sea una identida de nuestra base de datos//
public class Note extends RealmObject {


    @PrimaryKey //Semantic Value
    private int id;

    @Required //Semantic Value
    private String description;

    @Required //Semantic Value
    private Date createdAt;

    //Realm requires you to have an empty default constructor
    public Note(){
    }

    public Note (String description){
        //Auto generated ID//
        this.id = MyApplication.NoteID.incrementAndGet();
        this.description = description;
        this.createdAt = new Date();
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

 }
