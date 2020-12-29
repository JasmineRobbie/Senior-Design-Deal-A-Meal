package com.NeuralNet.AzureCloud;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;


import com.teamcarl.prototype.R;

public class DealAMealList extends Activity {


    Button userProfileBtn, pointOfConsumption, fourWeekBtn, helpButton, returnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_a_meal_list);

        AlertDialog.Builder b1 = new AlertDialog.Builder(DealAMealList.this);
        b1.setTitle("Help");
        b1.setMessage("This page will allow you to choose from a menu to redirect you to your " +
                "custom profile, culinary items, your customized 4 week plan, cuisine, and your current progress.");
        b1.setCancelable(true);
        b1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }
        );
        final AlertDialog alertdup = b1.create();
        //alert dialog

        pointOfConsumption = (Button) findViewById(R.id.cuisineButtonOne);
        fourWeekBtn = (Button) findViewById(R.id.fourWeekTemplateButton);
        userProfileBtn = (Button) findViewById(R.id.userProfileButton);
        helpButton = (Button) findViewById(R.id.helpBtn);
        returnButton = (Button) findViewById(R.id.returnBtn);

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



        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertdup.show();
            }
        });

        //Return button
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DealAMealList.this, Activemenu.class);
                startActivity(intent);
            }
        });

    }


    void openBistroMenu(){
        Intent intent = new Intent(DealAMealList.this, Bistromenu.class);

        startActivity(intent);
    }

}

