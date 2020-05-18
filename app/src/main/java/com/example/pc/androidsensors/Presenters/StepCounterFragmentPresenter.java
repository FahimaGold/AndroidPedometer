package com.example.pc.androidsensors.Presenters;

import com.example.pc.androidsensors.Data.DatabaseQuery;
import com.example.pc.androidsensors.Data.PreferenceImpl;
import com.example.pc.androidsensors.Data.StepTracker;

/**
 * Created by pc on 08/05/2020.
 */

//Presenter of the fragment that displays steps
public class StepCounterFragmentPresenter {

    private StepTracker stepTracker ;
    private PreferenceImpl preference;
    private View view;
    private DatabaseQuery databaseQuery;

    public StepCounterFragmentPresenter(View view, PreferenceImpl preference){
        this.view = view;
        this.preference = preference;


    }

    public void updateChart(){
        this.view.updateChart( this.stepTracker );
    }

    public StepTracker getCurrentData(){

        return this.stepTracker;
    }

    public int getGoal(){
        return  this.preference.getGoal();
    }


   public void setStepTracker(StepTracker stepTracker){
        this.stepTracker = stepTracker;
   }


   //This interface that interacts with the view
    public interface View{
      public void updateChart(StepTracker stepTracker);
      public void alertSensorNotFound();
    }


    public DatabaseQuery getDatabaseQuery() {
        return databaseQuery;
    }

    public void setDatabaseQuery(DatabaseQuery databaseQuery) {
        this.databaseQuery = databaseQuery;
    }
}
