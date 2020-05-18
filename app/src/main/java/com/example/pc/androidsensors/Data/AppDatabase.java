package com.example.pc.androidsensors.Data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

/**
 * Created by pc on 15/05/2020.
 */

@Database(entities = {StepTracker.class}, version = 1  )
public abstract class AppDatabase extends RoomDatabase{
   public abstract StepTrackerDao stepTrackerDao();
}
