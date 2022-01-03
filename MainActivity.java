package com.example.weightloggingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnEnterWeight = findViewById(R.id.btnEnterWeight);
        btnEnterWeight.setOnClickListener(this);

        Button btnSeeProgress = findViewById(R.id.btnSeeProgress);
        btnSeeProgress.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnEnterWeight:
                System.out.println("Button click!");
                openAddWeightPage();
                break;
            case R.id.btnSeeProgress:
                System.out.println("Seeing progress!");
                openSeeProgessPage();
                break;
            default:
                break;
        }

    }

    public void openSeeProgessPage(){
        Intent seeProgressIntent = new Intent(this, SeeProgressPage.class);
        startActivity(seeProgressIntent);
    }

    public void openAddWeightPage(){
        Intent addWeightIntent = new Intent(this, AddWeightPage.class);
        startActivity(addWeightIntent);
    }

}