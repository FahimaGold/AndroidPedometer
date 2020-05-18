package com.example.pc.androidsensors.Views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.androidsensors.Data.DatabaseQuery;
import com.example.pc.androidsensors.Data.PreferenceImpl;
import com.example.pc.androidsensors.Presenters.StepCounterActivityPresenter;
import com.example.pc.androidsensors.Presenters.StepCounterFragmentPresenter;
import com.example.pc.androidsensors.R;
import com.example.pc.androidsensors.Services.SensorListener;
import com.example.pc.androidsensors.Util.DateHelper;
import com.example.pc.androidsensors.Util.SensorListenerClass;

public class StepsCounterActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, StepCounterActivityPresenter.View, StepCounterActivityPresenter.Service{


    StepsCounterFragment stepsCounterFragment;
    private NumberPicker numberPicker;
    private PreferenceImpl preference;
    private  FrameLayout layout;
    private LinearLayout linearLayout;
    private TextView cm;
    private NumberPicker stepSizePicker;
    StepCounterActivityPresenter stepCounterActivityPresenter;
    StepCounterFragmentPresenter stepCounterFragmentPresenter;
    private SensorListenerClass sensorListenerClass;
    private DatabaseQuery databaseQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_steps_counter );

        //Attaching Fragement the activity
        stepsCounterFragment = new StepsCounterFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace( R.id.stepCounterHolder, stepsCounterFragment );
        fragmentTransaction.commit();

        this.preference = new PreferenceImpl( this );
        //Initializing the presenter
        stepCounterActivityPresenter = new StepCounterActivityPresenter( this, preference );

        databaseQuery = new DatabaseQuery(getApplicationContext());

        numberPicker = new NumberPicker( this );
        numberPicker.setMinValue( 10 );
        numberPicker.setMaxValue( 100000 );
        numberPicker.setValue( stepCounterActivityPresenter.getGoal() );

        stepSizePicker = new NumberPicker( this );
        stepSizePicker.setMinValue( stepCounterActivityPresenter.getStepSize() );
        stepSizePicker.setMaxValue( 400 );
        stepSizePicker.setValue( stepCounterActivityPresenter.getStepSize() );

        cm = new TextView( this );

        Toolbar toolbar = ( Toolbar ) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        FloatingActionButton fab = ( FloatingActionButton ) findViewById( R.id.fab );
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make( view, "Replace with your own action", Snackbar.LENGTH_LONG )
                        .setAction( "Action", null ).show();
            }
        } );

        DrawerLayout drawer = ( DrawerLayout ) findViewById( R.id.drawer_layout );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawer.addDrawerListener( toggle );
        toggle.syncState();

        NavigationView navigationView = ( NavigationView ) findViewById( R.id.nav_view );
        navigationView.setNavigationItemSelectedListener( this );



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = ( DrawerLayout ) findViewById( R.id.drawer_layout );
        if (drawer.isDrawerOpen( GravityCompat.START )) {
            drawer.closeDrawer( GravityCompat.START );
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        startService();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.steps_counter, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
           displayAppSettings();
        }

        return super.onOptionsItemSelected( item );
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.goal) {

            displayGoalDialog();
        } else if (id == R.id.step_size) {
            displayStepSizeDialog();

        } else if (id == R.id.weekly) {

        } else if (id == R.id.monthly) {


        } else if (id == R.id.stop_counting) {

            stopService();
        }

        DrawerLayout drawer = ( DrawerLayout ) findViewById( R.id.drawer_layout );
        drawer.closeDrawer( GravityCompat.START );
        return true;
    }





    @Override
    public void displayGoalDialog() {
       layout = new FrameLayout(getApplicationContext());
        if(numberPicker.getParent() != null)
            ((ViewGroup )numberPicker.getParent()).removeView(numberPicker);
        layout.addView(numberPicker, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER));

        new AlertDialog.Builder(StepsCounterActivity.this)
                .setView(layout)
                .setTitle( "Set Goal" )
                .setPositiveButton( android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        numberPicker.clearFocus();
                        int value = numberPicker.getValue();

                        stepCounterActivityPresenter.setGoal(value  );


                        //To update the chart immedietly, we need to refresh the fragment that contains the chart
                          stepsCounterFragment.getFragmentManager().beginTransaction().detach(stepsCounterFragment).commit();
                          stepsCounterFragment.getFragmentManager().beginTransaction().attach(stepsCounterFragment).commit();
                    }
                } )
                .setNegativeButton( android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                } )
                .create()
                .show();

    }

    @Override
    public void displayStepSizeDialog() {
        linearLayout = new LinearLayout(getApplicationContext());
        linearLayout.setOrientation( LinearLayout.HORIZONTAL );
        if(stepSizePicker.getParent() != null)
            ((ViewGroup )stepSizePicker.getParent()).removeView(stepSizePicker);
        if( cm.getParent() != null)
            ((ViewGroup )cm.getParent()).removeView(cm);
        linearLayout.addView(stepSizePicker, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER));
        cm.setText( "cm" );
        cm.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM));
        linearLayout.addView(cm, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER));

        new AlertDialog.Builder(StepsCounterActivity.this)
                .setView(linearLayout)
                .setTitle( "Step Size" )
                .setPositiveButton( android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        stepSizePicker.clearFocus();
                        int value = stepSizePicker.getValue();
                        stepCounterActivityPresenter.setStepSize(value  );

                        //To update the chart immedietly, we need to refresh the fragment that contains the chart
                        stepsCounterFragment.getFragmentManager().beginTransaction().detach(stepsCounterFragment).commit();
                        stepsCounterFragment.getFragmentManager().beginTransaction().attach(stepsCounterFragment).commit();
                    }
                } )
                .setNegativeButton( android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                } )
                .create()
                .show();
    }

    @Override
    public void displayAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @Override
    public void startService() {
        Intent intent = new Intent(this, SensorListener.class);
        intent.setAction(SensorListener.ACTION_START_FOREGROUND_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startForegroundService(intent );
        else
            startService(intent);

    }

    @Override
    public void stopService() {

        preference.setStart( true );
        sensorListenerClass = SensorListenerClass.getInstance( getApplicationContext(), StepsCounterFragment.stepCounterFragmentPresenter );
        int lastSav = sensorListenerClass.getStepTracker().getStep();
        preference.setLastSavedSteps( lastSav );
        preference.setLastSavedDate( DateHelper.getToday() );
        //databaseQuery.addEntry( sensorListenerClass.getStepTracker() );
        Intent intent = new Intent(this, SensorListener.class);
        intent.setAction(SensorListener.ACTION_STOP_FOREGROUND_SERVICE);

        getApplicationContext().startService(intent);
        finish();


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }
}
