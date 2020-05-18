package com.example.pc.androidsensors.Util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.pc.androidsensors.BuildConfig;
import com.example.pc.androidsensors.Data.PreferenceImpl;
import com.example.pc.androidsensors.Data.StepTracker;
import com.example.pc.androidsensors.Presenters.StepCounterFragmentPresenter;
import com.example.pc.androidsensors.Services.SensorListener;

import java.util.Calendar;
import java.util.Date;

import static com.example.pc.androidsensors.Services.SensorListener.MICROSECONDS_IN_ONE_MINUTE;

/**
 * Created by pc on 09/05/2020.
 */

//This class is used to handle the listening to the steps  counter sensor
public class SensorListenerClass implements SensorEventListener {

    //Used to compare dates, if we're in a new day, we reset the number of steps to 0.
    public final static long MILLISECONDS_IN_DAY = 86400000;

    private  StepTracker stepTracker ;
    private Context context;
    private Calendar calendar;
    private PreferenceImpl preference;
    private boolean sensorNotFound = false;

    StepCounterFragmentPresenter StepCounterFragmentPresenter;
    private static SensorListenerClass sensorListenerClass;

    public SensorListenerClass(Context context){
        this.context = context;
        preference = new PreferenceImpl( context );
        calendar = Calendar.getInstance();

    }

    private SensorListenerClass(Context context, StepCounterFragmentPresenter StepCounterFragmentPresenter){
       this.context = context;
       preference = new PreferenceImpl( context );
       calendar = Calendar.getInstance();
       this.StepCounterFragmentPresenter = StepCounterFragmentPresenter;
    }

    public static synchronized SensorListenerClass getInstance(Context context, StepCounterFragmentPresenter stepCounterFragmentPresenter){
        if(sensorListenerClass == null)
            sensorListenerClass = new SensorListenerClass( context, stepCounterFragmentPresenter );
        return sensorListenerClass;
    }


    public StepTracker getStepTracker(){return this.stepTracker;}


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onSensorChanged(SensorEvent event) {
        int currentSteps;
        int lastSavedSteps;
        int offset;
        if (event.values[0] > Integer.MAX_VALUE) {
            if (BuildConfig.DEBUG) Log.i("noR ", "not a real value " + event.values[0]);
            return;
        } else {
            int all_steps = (int)event.values[0];
              if(preference.isStart()) {

                  //Checking if we're in a new day, resetting the number of steps
                  if(DateHelper.getToday() - preference.getLastSavedDate()> MILLISECONDS_IN_DAY ){
                      preference.setLastSavedSteps( 0 );
                  }
                  preference.setOffset( all_steps );
                  preference.setStart( false );


              }
              Log.i( "startval","start value " + preference.isStart() );
            offset = preference.getOffset();
            lastSavedSteps = preference.getLastSavedSteps();
            Log.i( "offset", "off" + offset );
            Log.i( "offset", "ast saved" + lastSavedSteps );

                currentSteps = lastSavedSteps + all_steps - offset ;

            Log.i( "mntn","current steps " + currentSteps );
            Log.i( "mntn","last saved steps " + lastSavedSteps );


            stepTracker = new StepTracker( currentSteps, calendar.getTimeInMillis(), preference.getStepSize());
            if(StepCounterFragmentPresenter != null){
                StepCounterFragmentPresenter.setStepTracker( stepTracker );
                StepCounterFragmentPresenter.updateChart();
            }


        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void registerSensor(){
        SensorManager sensorManager = (SensorManager ) context.getSystemService( Context.SENSOR_SERVICE );
        Sensor sensor = sensorManager.getDefaultSensor( Sensor.TYPE_STEP_COUNTER );
        if(sensor == null)
            sensorNotFound = true;
        else{
            //Allowiiing a atching of a dely of 5 min
            sensorManager.registerListener( this, sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER),
                    SensorManager.SENSOR_DELAY_UI, (int) (5 * MICROSECONDS_IN_ONE_MINUTE));
           sensorNotFound = false;
        }


    }


    //Unregistering sensor
    public void unregisterSensor(){
        SensorManager sensorManager = (SensorManager ) context.getSystemService( Context.SENSOR_SERVICE );
        Sensor sensor = sensorManager.getDefaultSensor( Sensor.TYPE_STEP_COUNTER );

        sensorManager.unregisterListener( this, sensorManager.getDefaultSensor( Sensor.TYPE_STEP_COUNTER ) );
    }

    public StepCounterFragmentPresenter getStepCounterFragmentPresenter() {
        return StepCounterFragmentPresenter;
    }

    public boolean isSensorNotFound() {
        return sensorNotFound;
    }
}
