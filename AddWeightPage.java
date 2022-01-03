package com.example.weightloggingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;
import java.util.Map;
import java.util.Set;

public class AddWeightPage extends Activity implements View.OnClickListener{
    private EditText inputWeightView;
    private SharedPreferences sharedPreferences;
    private long lastClickTime = 28800001;
    private boolean firstClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_weight_activity);

        //get views and add onClickListener
        Button submitWeightBtn = findViewById(R.id.submitWeightBtn);
        submitWeightBtn.setOnClickListener(this);

        Button seeProgressBtn = findViewById(R.id.seeProgressBtn);
        seeProgressBtn.setOnClickListener(this);

        //get editText and get user input from it
        inputWeightView = findViewById(R.id.editTextWeight);

        //get clear data button
        Button clearDataBtn = findViewById(R.id.clearDataBtn);
        clearDataBtn.setOnClickListener(this);

        //make file for weights and date
        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        //get timer going
        final long bootTime = SystemClock.elapsedRealtime();
        lastClickTime = bootTime;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitWeightBtn:

                Long timeNow = SystemClock.elapsedRealtime();
                Long timeElapsed = SystemClock.elapsedRealtime() - lastClickTime;
                lastClickTime = timeNow;

                SharedPreferences.Editor editor = sharedPreferences.edit();

                Map<String, ?> sharedPrefsMap = sharedPreferences.getAll();

                //if key hasn't been added yet
                if(!sharedPrefsMap.containsKey("time_elapsed")){
                    //set "time_elapsed" value if it doesn't exist yet
                    editor.putLong("time_elapsed", 28800000);
                    editor.commit();
                }
                else{
                    //update total time elapsed since weight last entered
                    long fullTimeElapsed = sharedPreferences.getLong("time_elapsed", 0);
                    fullTimeElapsed += timeElapsed;
                    editor.putLong("time_elapsed", fullTimeElapsed);
                    editor.commit();
                }

                long timeToCompare = sharedPreferences.getLong("time_elapsed", 0);
                System.out.println(timeToCompare);
                if (timeToCompare < 28800000){
                    long timeToWait = 28800000 - timeToCompare;
                    int minutes = (int) ((timeToWait / (1000*60)) % 60);
                    int hours   = (int) ((timeToWait / (1000*60*60)) % 24);
                    String text = "Please wait " + hours + " hours and " + minutes + " minutes";
                    Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
                }
                else{
                    editor.putLong("time_elapsed", 0);
                    editor.commit();
                    //this will always be a double because of the EditText we used
                    String weight = inputWeightView.getText().toString();

                    //get date in ms
                    long millis=System.currentTimeMillis();
                    String date = String.valueOf(millis);

                    //put date and weight into records
                    editor.putString(date, weight);
                    editor.commit();

                    Toast.makeText(this, "Weight Submitted!", Toast.LENGTH_SHORT).show();
                    firstClick = true;
                }

                break;

            case R.id.seeProgressBtn:
                toSeeProgressPage();
                break;
            case R.id.clearDataBtn:
                clearData();
                break;

            default:
                break;
        }
    }

    public void toSeeProgressPage(){
        Intent intent = new Intent(this, SeeProgressPage.class);
        startActivity(intent);
    }

    public void clearData(){
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //how to clear data
        editor.clear();
        editor.commit();
    }
}
