package com.example.ori9933.minesweeper;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class GyroscopeSensorListener implements SensorEventListener {
    private SensorManager sManager;
    private boolean isLocationInitialized = false;
    float x_roll,y_pitch,z_yaw;
    private final int ALLOWED_OFFSET = 2;
    private final int DELAY = 1000000;

    public GyroscopeSensorListener(SensorManager sManager){
        this.sManager = sManager;
        sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),DELAY);
    }


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
}
