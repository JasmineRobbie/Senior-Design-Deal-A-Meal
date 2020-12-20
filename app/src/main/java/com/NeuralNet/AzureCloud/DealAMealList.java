package com.NeuralNet.AzureCloud;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.teamcarl.prototype.R;

public class DealAMealList extends Activity {


    Button userProfileBtn, pointOfConsumption, fourWeekBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_a_meal_list);

        pointOfConsumption = (Button) findViewById(R.id.cuisineButtonOne);
        fourWeekBtn = (Button) findViewById(R.id.fourWeekTemplateButton);
        userProfileBtn = (Button) findViewById(R.id.userProfileButton);

        System.out.println("testing");
        pointOfConsumption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("Working");
                openBistroMenu();
            }
        });

        fourWeekBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DealAMealList.this, FourWeekPlan.class);
                startActivity(intent);
            }
        });

        userProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DealAMealList.this, UserProfile.class);
                startActivity(intent);
            }
        });
    }


    void openBistroMenu(){
        Intent intent = new Intent(DealAMealList.this, Bistromenu.class);

        startActivity(intent);
    }

}

