package com.example.pc.androidsensors.Presenters;

import com.example.pc.androidsensors.Data.PreferenceImpl;

/**
 * Created by pc on 13/05/2020.
 */

public class StepCounterActivityPresenter {

    private View view;
    private PreferenceImpl preference;

    public StepCounterActivityPresenter(View view, PreferenceImpl preference){
        this.view = view;
        this.preference = preference;
    }

   public int getGoal(){
        return  this.preference.getGoal();
   }

   public void setGoal(int goal){
       this.preference.setGoal( goal );
   }

  public int getStepSize(){
       return this.preference.getStepSize();
  }

  public void setStepSize(int stepSize){
      this.preference.setStepSize( stepSize );
  }

    public interface View{

        void displayGoalDialog();
        void displayStepSizeDialog();
        void displayAppSettings();


    }

    public interface Service{
        public void startService();
        public void stopService();


    }
}
