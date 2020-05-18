package com.example.pc.androidsensors.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by pc on 15/05/2020.
 */

public class ShutdownReceiver extends BroadcastReceiver {

    public  ShutdownReceiver(){

    }
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Fahima","shutdown event ...");
    }
}
