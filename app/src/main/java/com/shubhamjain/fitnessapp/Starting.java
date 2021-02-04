package com.shubhamjain.fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Starting extends AppCompatActivity {
    EditText enterHeight, enterWeight;
    String height, weight;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        button = findViewById(R.id.button);
        enterHeight = findViewById(R.id.enterHeight);
        enterWeight = findViewById(R.id.enterWeight);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                height = enterHeight.getText().toString();
                weight = enterWeight.getText().toString();

                if (height == "0" || height == null) {
                    enterHeight.setError("Cant be zero or empty");
                }

                if (weight == "0" || weight == null) {
                    enterWeight.setError("Cant be zero or empty");
                } else {
                    InputMethodManager imm = (InputMethodManager)getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    Intent intent = new Intent(getApplication(), MainActivity.class);
                    intent.putExtra("height", height);
                    intent.putExtra("weight", weight);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });


    }
}