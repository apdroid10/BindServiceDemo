package com.apuroopa.bindservicedemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button startService;
    private Button stopService;
    private Button bindServiceBtn;
    private Button unbindServiceBtn;
    private Button getRandomNumberBtn;
    private Intent serviceIntent;
    private String TAG = MainActivity.class.getName();

    private MyBoundService myBoundService;
    private boolean isServiceBound;
    private ServiceConnection serviceConnection;

    private TextView threadCountTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Log.i(TAG," "+Thread.currentThread().getId());

        startService = findViewById(R.id.startService);
        stopService= findViewById(R.id.stopService);
        bindServiceBtn = findViewById(R.id.bindService);
        unbindServiceBtn = findViewById(R.id.unBindService);
        getRandomNumberBtn = findViewById(R.id.getRandomNumber);
        threadCountTextView = findViewById(R.id.TVthreadCount);

        serviceIntent = new Intent(getApplicationContext(),MyBoundService.class);

        startService.setOnClickListener(this);
        stopService.setOnClickListener(this);
        bindServiceBtn.setOnClickListener(this);
        unbindServiceBtn.setOnClickListener(this);
        getRandomNumberBtn.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.startService:
                System.out.println("-------on start service clicked");
                startService(serviceIntent);
                break;

            case R.id.stopService:
                stopService(serviceIntent);
                break;

            case R.id.bindService:
                bindService();
                break;

            case R.id.unBindService:
                unBindService();
                break;

            case R.id.getRandomNumber:
                setRandomNumber();
                break;
        }
    }

    public void bindService(){

        if(serviceConnection==null){
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    isServiceBound = true;
                    MyBoundService.MyServiceBinder myServiceBinder = (MyBoundService.MyServiceBinder)service;
                    myBoundService = myServiceBinder.getService();
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    isServiceBound = false;
                }
            };

            bindService(serviceIntent,serviceConnection,BIND_AUTO_CREATE);
        }
    }

    public void unBindService(){
        if(isServiceBound){
            unbindService(serviceConnection);
            Log.i(TAG," unBindService called ");

            isServiceBound = false;
        }

    }

    public void setRandomNumber(){

        if(isServiceBound){
            threadCountTextView.setText(""+myBoundService.getRandomNumber());
        }else {
            threadCountTextView.setText("Service Not Bound");
        }
    }
}
