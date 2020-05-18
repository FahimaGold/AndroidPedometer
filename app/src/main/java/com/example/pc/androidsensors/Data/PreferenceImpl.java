package com.example.pc.androidsensors.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.pc.androidsensors.Util.DateHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by pc on 13/05/2020.
 */

public class PreferenceImpl implements IPreference {
     private SharedPreferences prefs;
     private  Context context;
     private Date date;

     //Storing the number of steps user's set to reach
     private final static String GOAL = "Goal";

    //Storing the step step in centimer, as selected by the user
     private final static String STEP_SIZE = "step_size";

     //Storing the number of steps since the last time the user has boot their device
     private final static String OFFSET = "offset";

     //Storing the date when the last steps have been recorded
     private final static String LAST_SAVED_DATE = "today_date";

     //Storing the number of the last recorded steps
     private final static String LAST_SAVED_STEPS = "last_steps";

     //This is used to control the offset
     private final static String START_STATUS = "start";

     public PreferenceImpl(Context context){
         this.context = context;
         prefs = context.getSharedPreferences(context.getPackageName() + "_my_pref" ,Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS );
         this.date = new Date(  );

     }

    @Override
    public void setGoal(int goal) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt( GOAL, goal );
        editor.apply();
    }

    @Override
    public int getGoal() {
        int goal = prefs.getInt( GOAL, 100 );
        return goal;
    }

    @Override
    public void setStepSize(int stepSize) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt( STEP_SIZE, stepSize );
        editor.apply();
    }

    @Override
    public int getStepSize() {
        int step_size = prefs.getInt( STEP_SIZE, 15 );
        return step_size;
    }

    @Override
    public void setOffset(int offset) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt( OFFSET, offset );
        editor.apply();
    }

    @Override
    public void setLastSavedDate(long today) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong( LAST_SAVED_DATE, today );
        editor.apply();
    }

    @Override
    public long getLastSavedDate() {
        long today = prefs.getLong( LAST_SAVED_DATE, DateHelper.getToday() );
        return today;
    }



    @Override
    public int getOffset() {
        int offset = prefs.getInt( OFFSET,0);
        return offset;
    }

    @Override
    public void setLastSavedSteps(int steps) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt( LAST_SAVED_STEPS, steps );
        editor.apply();
    }

    @Override
    public int getLastSavedSteps() {
        int lastSteps = prefs.getInt( LAST_SAVED_STEPS, 0 );
        return lastSteps;
    }

    @Override
    public boolean isStart() {
        boolean start = prefs.getBoolean( START_STATUS, true );
        return start;
    }

    @Override
    public void setStart(boolean start) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean( START_STATUS, start );
        editor.apply();
    }


}
