package com.example.ori9933.minesweeper;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;


import java.security.Provider;

public class GyroscopeSensorListener extends Service implements SensorEventListener {
    private boolean isLocationInitialized = false;
    float x_roll,y_pitch,z_yaw;
    private final int ALLOWED_OFFSET = 2;
    private final int DELAY = 1000000;
    private SensorManager sManager;
    private final IBinder myBinder = new MyLocalBinder();


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
        {
            return;
        }

        float x_roll = event.values[2];
        float y_pitch = event.values[1];
        float z_yaw = event.values[0];

        if(isLocationInitialized == false){
            this. x_roll = x_roll;
            this.y_pitch = y_pitch;
            this.z_yaw = z_yaw;
            isLocationInitialized = true;
        }
        else if(Math.abs(this. x_roll - x_roll) > ALLOWED_OFFSET ||
                Math.abs(this. y_pitch - y_pitch) > ALLOWED_OFFSET ||
                Math.abs(this. z_yaw - z_yaw) > ALLOWED_OFFSET){
            GameManager.getInstance().OnOrientationDeviation();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void Reset() {
        isLocationInitialized = false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if(sManager == null){
            sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),DELAY);
        }
        return myBinder;
    }


    public class MyLocalBinder extends Binder {
        GyroscopeSensorListener getService() {
            return GyroscopeSensorListener.this;
        }
    }
}
