package com.example.pc.androidsensors.Data;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

/**
 * Created by pc on 06/05/2020.
 */

@Entity(tableName = "step_tracker")
public class StepTracker {

    @NonNull
    @ColumnInfo(name = "step")
    private int step;
    @NonNull
    @ColumnInfo(name = "date")
    @PrimaryKey
    private long timeInMilliSeconds;
    @NonNull
    @ColumnInfo(name = "step_size")
    private int stepSize;

    public StepTracker(int currentSteps, long date, int stepSize){
        this.step = currentSteps;
        this.timeInMilliSeconds = date;
        this.stepSize = stepSize;
    }

    public StepTracker(){

    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public long  getTimeInMilliSeconds() {
        return timeInMilliSeconds;
    }

    public void setTimeInMilliSeconds(long date) {
        this.timeInMilliSeconds = date;
    }

    public float calculateDistanceInKm(){
        float distance = stepSize / 100000;
        String s = String.format("%.4f", distance);
        return Float.parseFloat( s );
    }

    @NonNull
    public int getStepSize() {
        return stepSize;
    }

    public void setStepSize(@NonNull int stepSize) {
        this.stepSize = stepSize;
    }
}
