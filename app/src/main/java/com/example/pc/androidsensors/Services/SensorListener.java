package com.example.pc.androidsensors.Services;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.pc.androidsensors.BuildConfig;
import com.example.pc.androidsensors.Data.DatabaseQuery;
import com.example.pc.androidsensors.Data.PreferenceImpl;
import com.example.pc.androidsensors.Data.StepTracker;
import com.example.pc.androidsensors.Util.DateHelper;
import com.example.pc.androidsensors.Util.NotificationHelper;
import com.example.pc.androidsensors.Util.SensorListenerClass;
import com.example.pc.androidsensors.Views.StepsCounterActivity;
import com.github.mikephil.charting.utils.Utils;

import java.util.Calendar;

import java.util.Date;
import java.util.Locale;

/**
 * Created by pc on 30/04/2020.
 */

public class SensorListener extends Service implements SensorEventListener{

    public final static String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE";
    public final static String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE";
    private int currentSteps = 0;
    private Calendar calendar;
    public static StepTracker stepTracker;
    public final static int MICROSECONDS_IN_ONE_MINUTE = 60000000 ;

    private ShutdownReceiver shutdownReceiver;
    private PreferenceImpl preference;
    private long offset;
    private Context context;
    private SensorListenerClass sensorListenerClass;
    private DatabaseQuery databaseQuery;
    private NotificationHelper notificationHelperHelper;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            switch (action) {

                //What we do when the service is started
                case ACTION_START_FOREGROUND_SERVICE:
                    createServiceNotification();
                    notificationHelperHelper = new NotificationHelper( context );

                    calendar = Calendar.getInstance();
                    preference = new PreferenceImpl( context );
                    registerSensor();
                    databaseQuery = new DatabaseQuery( context );
                    databaseQuery.getLastEntry();
                    Log.i( "commence", "service started ..." );
                    Calendar calendar = Calendar.getInstance();
                    Log.i( "voir", "VOIR date : " + calendar.getDisplayName( Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US ) + "-" + calendar.get( Calendar.MONTH ) + "-" + calendar.get( Calendar.YEAR ) );


                    break;
                case ACTION_STOP_FOREGROUND_SERVICE:

                    stopForegroundService();



                    break;


            }

        }

            return START_STICKY;
    }



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

                preference.setOffset( all_steps );
                preference.setStart( false );


            }
            offset = preference.getOffset();
            lastSavedSteps = preference.getLastSavedSteps();

            int year = 2020;
            int month = 5;
            int dayOfMonth = 18;

// reuse the calendar to set user specified date
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            currentSteps = lastSavedSteps + all_steps - offset ;

            notificationHelperHelper.setGoal( preference.getGoal() );
            notificationHelperHelper.setProgress( currentSteps );
            notificationHelperHelper.createCountingStepsNotification();

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.i("acc", "Sensor may not be working properly");

    }



    //Foreground Service Notification
    private void createServiceNotification(){
        Intent i = new Intent( );
        PendingIntent piDismiss = PendingIntent.getActivity(getApplicationContext(),0,i,0);
        //build notification

// Gets an instance of the NotificationManager service
        NotificationManager notificationManager =
                ( NotificationManager ) this.getSystemService( Context.NOTIFICATION_SERVICE );
        //Checking android version: because for android 8 > : We talk about NotificationHelper channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager ) getSystemService( Context.NOTIFICATION_SERVICE );
            NotificationChannel channel = new NotificationChannel( "default", "App.service" ,NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel( channel );
            android.app.Notification.Builder builder = new android.app.Notification.Builder(this,"default")
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle("Pedometer")
                    .setContentText("Pedometer is running")
                    .setAutoCancel( true )
                    .setContentIntent(piDismiss);

            android.app.Notification notification = builder.build();


            startForeground( 1, notification );
        }
        else {
            NotificationCompat.Builder builder1 = new NotificationCompat.Builder( getApplicationContext() )
                    .setContentTitle( "Pedometer" )
                    .setContentText( "Pedometer is running" )
                    .setPriority( NotificationCompat.PRIORITY_DEFAULT )
                    .setAutoCancel( true );
            android.app.Notification notification = builder1.build();
            startForeground( 1, notification );
        }
    }

    private void stopForegroundService()
    {
        Log.d("foregroundservice", "Stop foreground service.");

        // Stop foreground service and remove the notification.

        stopForeground(true);


        stopSelf();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void registerSensor(){
        SensorManager sensorManager = (SensorManager ) context.getSystemService( Context.SENSOR_SERVICE );
        Sensor sensor = sensorManager.getDefaultSensor( Sensor.TYPE_STEP_COUNTER );
        boolean sensorFound = true;
        if(sensor != null){
            //Allowiiing a atching of a dely of 5 min
            sensorManager.registerListener( this, sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER),
                    SensorManager.SENSOR_DELAY_NORMAL, (int) (5 * MICROSECONDS_IN_ONE_MINUTE));

        }
    }
}
