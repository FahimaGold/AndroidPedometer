package com.example.pc.androidsensors.Data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Created by pc on 15/05/2020.
 */

@Dao
public interface StepTrackerDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(StepTracker stepTracker);

    @Query( "SELECT MAX(date) as date, step_size, step FROM step_tracker;" )
    Single<StepTracker> getLastEntry();

    @Query( "SELECT * FROM step_tracker ORDER BY  date LIMIT 7;" )
    Flowable<List<StepTracker>> getLastWeek();

    @Query( "SELECT * FROM step_tracker ORDER BY  date LIMIT 30;" )
    Flowable<List<StepTracker>> getLastMonth();

    @Query( "SELECT COUNT(*) FROM step_tracker;" )
    Single<Integer> getNbRows();

}
