package com.example.pc.androidsensors.Views;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.androidsensors.Data.DatabaseClient;
import com.example.pc.androidsensors.Data.DatabaseQuery;
import com.example.pc.androidsensors.Data.PreferenceImpl;
import com.example.pc.androidsensors.Data.StepTracker;
import com.example.pc.androidsensors.Presenters.StepCounterFragmentPresenter;
import com.example.pc.androidsensors.R;
import com.example.pc.androidsensors.Services.SensorListener;
import com.example.pc.androidsensors.Util.SensorListenerClass;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class StepsCounterFragment extends Fragment implements  StepCounterFragmentPresenter.View{

    public static StepCounterFragmentPresenter stepCounterFragmentPresenter;
    private SensorListenerClass sensorListenerClass;
    private PieChart pieChart;
    private PreferenceImpl preference;
    private DatabaseQuery databaseQuery;

    public StepsCounterFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_steps_counter, container, false );

        pieChart = (view).findViewById( R.id.chart );
        preference = new PreferenceImpl( getContext() );
        stepCounterFragmentPresenter = new StepCounterFragmentPresenter( this, preference );
       // sensorListenerClass = new SensorListenerClass(getContext(), stepCounterFragmentPresenter );
        sensorListenerClass = SensorListenerClass.getInstance( getContext(), stepCounterFragmentPresenter );
        sensorListenerClass.registerSensor();
        if(sensorListenerClass.isSensorNotFound())
            alertSensorNotFound();
        databaseQuery = new DatabaseQuery( getContext() );
        stepCounterFragmentPresenter.setDatabaseQuery( databaseQuery );
        updateChart( sensorListenerClass.getStepTracker());

        return view;
    }
    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void updateChart(StepTracker stepTracker) {

        int goal = stepCounterFragmentPresenter.getGoal();
        if(stepTracker == null){
            stepTracker = new StepTracker( preference.getLastSavedSteps(), preference.getLastSavedDate(), preference.getStepSize());
        }

        pieChart.setRotationEnabled(true);

        pieChart.setHoleRadius(50f);
        pieChart.setTransparentCircleAlpha(0);


        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();
            //Number of steps achieved
            yEntrys.add(new PieEntry(  stepTracker.getStep(),1));
               if(goal - stepTracker.getStep(  ) > 0) {
                   //Number of  steps away from goal
                   yEntrys.add( new PieEntry( (goal- stepTracker.getStep()), 2 ) );

               }
        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor( getContext().getString( R.color.green ) ));
        colors.add(Color.parseColor( getContext().getString( R.color.red ) ));

        pieDataSet.setColors(colors);

        //add legend to chart
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        //legend.setPosition( Legend.LegendPosition.LEFT_OF_CHART);

        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);

        pieChart.invalidate();

    }

    //Show this alert to user when required sensor not found
    @Override
    public void alertSensorNotFound() {
        FrameLayout layout = new FrameLayout(getActivity());

        new AlertDialog.Builder(getContext())
                .setView(layout)
                .setTitle( "Sensor not found" )
                .setNegativeButton( android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        getActivity().finish();
                    }
                } )
                .create()
                .show();
    }







    @Override
    public void onStart() {
        super.onStart();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onResume() {
        super.onResume();



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorListenerClass.unregisterSensor();
    }


}
