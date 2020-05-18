package com.example.pc.androidsensors.Data;

import android.arch.persistence.room.Room;
import android.content.Context;

/**
 * Created by pc on 15/05/2020.
 */

public class DatabaseClient {
    private Context context;
    private static DatabaseClient databaseClient;
    private AppDatabase appDatabase;

    private DatabaseClient (Context context){
        this.context = context;
        //Building the database with Room buildr
        appDatabase = Room.databaseBuilder(context, AppDatabase.class, "pedometer").build();
    }

    //Making sure only one of instance of database is created
    public static synchronized DatabaseClient getInstance(Context context){
        if(databaseClient == null)
            databaseClient = new DatabaseClient( context );
        return databaseClient;
    }

    public AppDatabase getAppDatabase(){
        return appDatabase;
    }
}
