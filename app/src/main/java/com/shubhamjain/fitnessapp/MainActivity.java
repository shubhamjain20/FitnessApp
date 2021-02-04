package com.shubhamjain.fitnessapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView stepsView, distanceCovered, caloriesBurned;
    SensorManager sensorManager;
    Sensor sensor;

    int distance, finalSteps, calorie;
    float strideLength, previousSteps, steps, calorieVal = 0f;
    boolean isStart = false, isClosedOnce = true, backPressed = false;

    Toast backToast;
    String height, weight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        Intent intent = getIntent();
        height = intent.getStringExtra("height");
        weight = intent.getStringExtra("weight");

        if (height != null)
            strideLength = Float.valueOf(height) * 0.415f;

        SharedPreferences sharedPreferences = getSharedPreferences("MYPREFERENCES", MODE_PRIVATE);
        steps = sharedPreferences.getFloat("mySteps", 0f);
        isClosedOnce = sharedPreferences.getBoolean("isClosedOnce", true);

        stepsView = findViewById(R.id.stepsView);
        caloriesBurned = findViewById(R.id.caloriesBurned);
        distanceCovered = findViewById(R.id.distanceCovered);

        backToast = Toast.makeText(this, "Press again to exit and reset steps", Toast.LENGTH_SHORT);


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (sensor != null) {

            sensorManager.registerListener(new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {

                    finalSteps = (int) (sensorEvent.values[0] - steps);
                    previousSteps = sensorEvent.values[0];
                    distance = (int) (Float.valueOf(stepsView.getText().toString()) * (strideLength / 39) / 2);
                    calorie = (int) (Float.valueOf(stepsView.getText().toString()) * calorieVal);

                    if (isStart) {

                        stepsView.setText(String.valueOf(finalSteps));
                        distanceCovered.setText(String.valueOf(distance));
                        caloriesBurned.setText(String.valueOf(calorie));

                    }

                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {
                    //blank
                }
            }, sensor, SensorManager.SENSOR_DELAY_UI);

        } else {

            Toast.makeText(getApplicationContext(), "Sensor not Found", Toast.LENGTH_SHORT).show();

        }

        if (isClosedOnce) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Close the app for the first time to reset sensors.")
                    .setPositiveButton("Close App", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            MainActivity.this.finishAffinity();
                        }
                    }).create().show();

        }


        if (!isClosedOnce) {

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Select an option-")
                    .setPositiveButton("Walking", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            calorieVal = 0.045f;
                        }
                    }).setNegativeButton("Running", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    calorieVal = 0.75f;
                }
            }).create().show();

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Press Start!")
                    .setPositiveButton("Start", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            isStart = true;
                        }
                    }).create().show();

        }


    }

    @Override
    protected void onStop() {

        super.onStop();
        SharedPreferences sharedPreferences = getSharedPreferences("MYPREFERENCES", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("mySteps", previousSteps);
        editor.putBoolean("isClosedOnce", false);
        editor.commit();

    }

    @Override
    public void onBackPressed() {
        if (backPressed) {
            super.onBackPressed();
            backToast.cancel();
        } else {
            backToast.show();
            backPressed = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backPressed = false;

                }
            }, 2000);
        }


    }


}