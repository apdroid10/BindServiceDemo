package com.apuroopa.bindservicedemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Random;

public class MyBoundService extends Service {


    private String TAG = "MyService";
    private int mRandomNumber;
    private boolean isRandomGeneratorOn;

    private final int Max = 100;
    private final int Min = 0;

    class MyServiceBinder extends Binder // or implement IBinder
    {
        public MyBoundService getService(){

            return MyBoundService.this;
        }

    }

    private IBinder mBinder = new MyServiceBinder();
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.i(TAG," onBind ");
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"Service Destroyed");

        stopRandomNumberGenerator();
    }

    public void startRandomNumberGenerator(){

        while(isRandomGeneratorOn){

            try {
                Thread.sleep(1000);
                if(isRandomGeneratorOn){

                    mRandomNumber = new Random().nextInt(Max) + Min;
                    Log.i(TAG,"Thread ID is " +Thread.currentThread().getId()+" Random no' is "+ mRandomNumber);

                }
            } catch (InterruptedException e) {
                Log.i(TAG,"Thread Interrupted");

                e.printStackTrace();
            }

        }
    }

    public void stopRandomNumberGenerator(){
        isRandomGeneratorOn = false;
    }

    public int getRandomNumber(){

        return mRandomNumber;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // stopSelf();
        Log.i(TAG," onStartCommand");

        isRandomGeneratorOn  = true;


        new Thread(new Runnable(){
            @Override
            public void run() {
                startRandomNumberGenerator();

            }

        }
        ).start();

        return START_STICKY;
    }
}
