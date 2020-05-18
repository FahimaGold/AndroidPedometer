package com.example.pc.androidsensors.Data;

import android.content.Context;
import android.util.Log;

import java.util.concurrent.Callable;

import androidx.annotation.MainThread;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by pc on 16/05/2020.
 */

public class DatabaseQuery {

    private Context context;
    public DatabaseQuery(Context context){
       this.context = context;
    }
    public  void addEntry(final StepTracker stepTracker) {
        Observable observable = Observable.just( stepTracker );
        observable.subscribeOn( Schedulers.io() )

                .subscribe( new Observer<StepTracker>() {


                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(StepTracker stepTracker) {
                        Log.i( "insertion","insertion taking place..." );
                        DatabaseClient.getInstance( context ).getAppDatabase()
                                .stepTrackerDao()
                                .insert( stepTracker );
                        Log.i( "insertion","insertion took place" );
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                } );
    }


    public StepTracker getLastEntry(){
        final StepTracker[] st = new StepTracker[1];
        DatabaseClient
                .getInstance(context )
                .getAppDatabase()
                .stepTrackerDao()
                .getLastEntry()
                .observeOn( AndroidSchedulers.mainThread())
                .subscribeOn( Schedulers.io() )
                .subscribe( new Consumer<StepTracker>() {
                    @Override
                    public void accept(StepTracker stepTracker) throws Exception {
                          st[0] = stepTracker;
                        Log.i( "chek","st " + st[0].getStep() );
                    }
                } );

       return st[0];
    }
}
