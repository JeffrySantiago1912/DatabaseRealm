package com.example.bd_realm.apps;

import android.app.Application;

import com.example.bd_realm.models.Board;
import com.example.bd_realm.models.Note;

import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

//inherits Application packet "Android-app"
public class MyApplication extends Application {

    //Method for sobreescribir//
    // Theme of the configuration//
    //this class is always executed before our mainActivity
       public static AtomicInteger BoardID = new AtomicInteger();
       public static AtomicInteger NoteID = new AtomicInteger();

      @Override
      public void onCreate() {
      super.onCreate();

      //Method of date base configuration
      setUpRealmConfig();

      Realm realm = Realm.getDefaultInstance();
      BoardID = getIdByTable(realm, Board.class);
      NoteID = getIdByTable(realm, Note.class);
      realm.close();

        }

        //Configure Date Base
        private void setUpRealmConfig(){

            // initialize Realm
            Realm.init(getApplicationContext());

            // create your Realm configuration
            RealmConfiguration config = new RealmConfiguration
                    .Builder()
                    .deleteRealmIfMigrationNeeded()
                    .build();
            Realm.setDefaultConfiguration(config);
        }

        //Class Generic
        private  <T extends RealmObject> AtomicInteger getIdByTable(Realm realm, Class<T> anyClass){
            RealmResults<T> results = realm.where(anyClass).findAll();
            return (results.size()> 0) ? new AtomicInteger(results.max("id").intValue()) : new AtomicInteger();
    }


}


