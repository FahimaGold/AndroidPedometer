package com.example.pc.androidsensors.Data;

import java.util.Date;

/**
 * Created by pc on 13/05/2020.
 */

public interface IPreference {

     void setGoal(int goal);
     int getGoal();
     void setStepSize(int stepSize);
     int getStepSize();
     void setOffset(int offset);
     void setLastSavedDate(long today);
     long getLastSavedDate();
     int  getOffset();
     void setLastSavedSteps(int steps);
     int getLastSavedSteps();
     boolean isStart();
     void setStart(boolean start);




}
