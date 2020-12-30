package com.NeuralNet.AzureCloud;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.teamcarl.prototype.R;

public class UserProfile extends Activity {
    Button aboutMeBtn, myGoalsBtn, helpButton, profileReturnBtn, profileMemoBtn, mealHistoryButton, resetPlanButton;
    TextView mealHistoryTextview, resetPlanTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        AlertDialog.Builder b1 = new AlertDialog.Builder(UserProfile.this);
        b1.setTitle("Help");
        b1.setMessage("This page gives you the option to view your personal information page and view " +
                "your goals page. Just click on a button to redirect!");
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

        profileReturnBtn = findViewById(R.id.profileReturnBtn);
        aboutMeBtn = findViewById(R.id.aboutMeBtn);
        myGoalsBtn = findViewById(R.id.myGoalsBtn);
        helpButton = findViewById(R.id.helpBtn);
        profileMemoBtn = findViewById(R.id.profileMemoBtn);
        mealHistoryButton = findViewById(R.id.mealHistoryBtn);
        mealHistoryTextview = findViewById(R.id.mealHistoryTextView);
        resetPlanTextView = findViewById(R.id.resetPlanTV);
        resetPlanButton = findViewById(R.id.resetPlanBtn);


        //about me button
        aboutMeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfile.this, AboutMe.class);
                startActivity(intent);
            }
        });

        //return button
        profileReturnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfile.this, DealAMealList.class);
                startActivity(intent);
            }
        });

        //My Goals button
        myGoalsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfile.this, MyGoals.class);
                startActivity(intent);
            }
        });

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertdup.show();
            }
        });

        mealHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfile.this, HistoryLog.class);
                startActivity(intent);
            }
        });

        resetPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfile.this, ResetPlan.class);
                startActivity(intent);
            }
        });
    }
}


