package com.example.bd_realm.models;

import com.example.bd_realm.apps.MyApplication;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Board extends RealmObject {

    @PrimaryKey
    private int id;

    @Required
    private String title;

    @Required
    private Date createdAt;

    //Create relation in Boards and Note//
    private RealmList<Note> notes;

    //Realm requires you to have an empty default constructor
    public Board(){

    }

    public Board(String title){
        this.id = MyApplication.BoardID.incrementAndGet();
        this.title = title;
        this.createdAt = new Date();
        this.notes = new RealmList<Note>();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public RealmList<Note> getNotes() {
        return notes;
    }

}
