package com.example.pc.androidsensors.Views;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.Sensor;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.pc.androidsensors.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Button btn;
    MainActivityFragment mainActivityFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        sensorManager = (SensorManager)getSystemService( Context.SENSOR_SERVICE );
        List<Sensor> deviceSensors = sensorManager.getSensorList( Sensor.TYPE_ALL );
        for (int i=0; i< deviceSensors.size();i++)
            Log.i("capteurs","capteurs " + deviceSensors.get( i ));
        mainActivityFragment = new MainActivityFragment();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace( R.id.mainFragmentHolder, mainActivityFragment );
        fragmentTransaction.commit();



    }
}
