package com.NeuralNet.AzureCloud;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.teamcarl.prototype.R;

public class Bistromenu extends Activity {

    Button cuisineButtonTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bistromenu);

        cuisineButtonTwo = (Button) findViewById(R.id.cuisineButtonTwo);

        cuisineButtonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPointOfConsumption();
            }
        });

    }


    void openPointOfConsumption(){
        Intent intent = new Intent(this, Point_of_Consumption.class);

        startActivity(intent);

    }
}