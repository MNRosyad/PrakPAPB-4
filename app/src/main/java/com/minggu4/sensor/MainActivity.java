package com.minggu4.sensor;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensorLight;
    private Sensor sensorProximity;

    private TextView textLight;
    private TextView textProxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        StringBuilder stringBuilder = new StringBuilder();
        for (Sensor currentSensor: sensorList) {
            stringBuilder.append(currentSensor.getName()).append(System.getProperty("line.separator"));
        }

        TextView sensorText = findViewById(R.id.theText1);
        sensorText.setText(stringBuilder);

        textLight = findViewById(R.id.theText2);
        textProxy = findViewById(R.id.theText3);
        String sensor_error = "No Sensor";

        sensorLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorProximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        if (sensorLight == null) {
            textLight.setText(sensor_error);
        }
        if (sensorProximity == null) {
            textProxy.setText(sensor_error);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (sensorLight != null){
            sensorManager.registerListener(this, sensorLight, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (sensorProximity != null) {
            sensorManager.registerListener(this, sensorProximity, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();
        float currentValue = sensorEvent.values[0];

        switch (sensorType) {
            case Sensor.TYPE_LIGHT:
                textLight.setText(String.format("Light Sensor: %1$.2f", currentValue));
                changeFirstBackground(currentValue);
                changeSecondBackground(currentValue);
                break;
            case Sensor.TYPE_PROXIMITY:
                textProxy.setText(String.format("Proximity Sensor: %1$.2f", currentValue));
                break;
            default:
        }
    }

    private void changeFirstBackground(float currentValue) {
        LinearLayout layout = findViewById(R.id.main_layout);
        if (currentValue > 5.00) layout.setBackgroundColor(Color.RED);
        else if (currentValue <= 5.00) layout.setBackgroundColor(Color.BLUE);
    }
    private void changeSecondBackground(float currentValue) {
        LinearLayout layout = findViewById(R.id.second_layout);
        if (currentValue > 5.00) layout.setBackgroundColor(Color.BLUE);
        else if (currentValue <= 5.00) layout.setBackgroundColor(Color.RED);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //
    }
}