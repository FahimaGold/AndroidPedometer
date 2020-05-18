package com.example.pc.androidsensors.Util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.pc.androidsensors.R;
import com.example.pc.androidsensors.Services.SensorListener;
import com.example.pc.androidsensors.Views.StepsCounterActivity;

/**
 * Created by pc on 16/05/2020.
 */

//Displaying the steps progress when app is not in foreground
public class NotificationHelper {

    private  int progress;
    private int goal;
    private Context context;

    public NotificationHelper(Context context){
        this.context = context;


    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createCountingStepsNotification() {

        String idChannel = "my_channel_01";
        Intent i = new Intent(context, StepsCounterActivity.class  );
        PendingIntent piDismiss = PendingIntent.getActivity(context,0,i,0);
        //build notification;

        final NotificationManager mNotificationManager = ( NotificationManager ) context.getSystemService( Context.NOTIFICATION_SERVICE );

        NotificationChannel mChannel = null;
        // The id of the channel.

        //Notification priority is referred to as priotirity starting from android Oreo
        int importance = NotificationManager.IMPORTANCE_NONE;

        final NotificationCompat.Builder builder =  new NotificationCompat.Builder( context, idChannel );
        builder.setContentTitle( "Keep going" )
                .setSmallIcon( android.R.drawable.ic_dialog_info )
                .setContentIntent( piDismiss )
                .setAutoCancel( true )
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentText(context.getString( R.string.notification_msg_1 ) + " "+(goal - progress)+ " " + context.getString( R.string.notification_msg_2 )+ " " + goal  );


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel( idChannel, "Counting Steps", importance );
            // Configure the notification channel.
            mChannel.setDescription( context.getString( R.string.stpes_notification_desc ) );
            mChannel.enableLights( true );
            mChannel.setLightColor( Color.RED );
            mNotificationManager.createNotificationChannel( mChannel );
        } else {
            builder.setContentTitle( "Counting Steps" )
                    .setPriority( NotificationCompat.PRIORITY_LOW)
                    .setLights( Color.YELLOW, 500, 5000 )
                    .setOngoing(true)
                    .setOnlyAlertOnce(true)
                    .setVibrate(null)
                    .setProgress(goal, progress, true);
        }

        mNotificationManager.notify( 3, builder.build() );

        //Displaying progress
        new Thread( new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(2000);
                    if(progress < goal){

                        builder.setProgress(goal, progress, false);
                        SystemClock.sleep(1000);
                        mNotificationManager.notify(3, builder.build());

                    }
                    else {
                        //
                        builder.setContentText( context.getString( R.string.goal_reached_message ))
                                .setProgress( goal, goal, false )
                                .setOngoing( false );
                        mNotificationManager.notify( 3, builder.build() );
                    }
            }
        }).start();
    }

}
