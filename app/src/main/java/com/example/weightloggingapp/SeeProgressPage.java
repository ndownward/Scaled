package com.example.weightloggingapp;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Map;
import java.util.Set;

public class SeeProgressPage extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_progress_activity);

        Button addWeightBtn = findViewById(R.id.addWeightBtn);
        addWeightBtn.setOnClickListener(this);

        Button mainPageBtn = findViewById(R.id.mainPageBtn);
        mainPageBtn.setOnClickListener(this);

        //get all SharedPreference data
        SharedPreferences getSharedPrefs =
                getApplicationContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        Map<String, ?> sharedPrefsMap = getSharedPrefs.getAll();
        Set keysTemp = sharedPrefsMap.keySet();
        keysTemp.remove("time_elapsed");
        Object[] keys = keysTemp.toArray();
        System.out.println("Keys length: " + keys.length);

        GraphView graph = findViewById(R.id.graph);
        //allow three digits to fit on screen
        GridLabelRenderer glr = graph.getGridLabelRenderer();
        glr.setPadding(32);

        for (Map.Entry<String, ?> entry : sharedPrefsMap.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue().toString());
        }

        DataPoint[] dataPoints = new DataPoint[keys.length];
        int j = 0;
        for (int i = 0; i < keys.length; i++){
            //weight, date
            String tempX = (String)keys[i];
            long x = Long.valueOf(tempX);

            String tempY = (String)sharedPrefsMap.get(keys[i]);
            double y = Double.valueOf(tempY);

            //add values to series
            //x will always be larger than the last
            System.out.println("x: " + x);
            System.out.println("y: " + y);

            dataPoints[j] = new DataPoint(x, y);
            j++;

        }
        //helper method to pass og lo and hi
        dataPoints = sortArray(dataPoints, 0, dataPoints.length);

        for (int i = 0; i < dataPoints.length; i++){
            System.out.println(dataPoints[i].toString());
        }

        //format x labels
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        // only 3 because of the space
        if (dataPoints.length < 5){
            graph.getGridLabelRenderer().setNumHorizontalLabels(1);
        }
        else{
            graph.getGridLabelRenderer().setNumHorizontalLabels(4);
        }
        //rotate labels
        graph.getGridLabelRenderer().setHorizontalLabelsAngle(135);
        //graph it!
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPoints);
        //make datapoints visible
        graph.addSeries(series);

        /*invisible x labels
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);*/

        // activate horizontal zooming and scrolling
        graph.getViewport().setScalable(true);

        // activate horizontal scrolling
        graph.getViewport().setScrollable(true);

        //set graph title
        graph.setTitle("Progress");

        /* set theme
        graphView.setTitleTextSize()
        graphView.setTitleColor()
        */
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case (R.id.addWeightBtn):
                openEnterWeightPage();
                break;
            case (R.id.mainPageBtn):
                openMainPage();
                break;
            default:
                break;
        }
    }

    public void openEnterWeightPage(){
        System.out.println("Hello!");
        Intent intent = new Intent(this, AddWeightPage.class);
        startActivity(intent);
    }

    public void openMainPage(){
        System.out.println("HI!");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    public static DataPoint[] sortArray(DataPoint[] arr, int lo, int hi){
        if (lo == hi){
            return arr;
        }
        int index = lo;
        DataPoint min = arr[lo];
        System.out.println("min: " + min.toString());
        for (int i = lo; i < arr.length; i++){
            if (arr[i].getX() < min.getX()){
                min = arr[i];
                index = i;
            }
        }
        //store element in index 0
        DataPoint temp = arr[lo];
        //set new index 0
        arr[lo] = min;
        //add data to end of array
        arr[index] = temp;

        return sortArray(arr, lo+1, hi);
    }

}
