package com.NeuralNet.AzureCloud;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.teamcarl.prototype.R;

import java.sql.ResultSet;

public class MyGoals extends Activity{
    Button myGoalsReturnButton, goalsEditButton;

    TextView myWeightText, myBMIText, myGoalText, allowedCaloriesText;

    String Userid = User.UserID;

    //Accessing database and selecting information (selecting specific user logged in Userid)
    DataAccess db = new DataAccess();
    final ResultSet rs = db.getDataTable("SELECT BMI, Weight, Goal, Guide, AllowedCalories FROM Users WHERE UserId = " + Userid);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_goals);

        myGoalsReturnButton = findViewById(R.id.myGoalsReturnButton);
        goalsEditButton = findViewById(R.id.goalsEditButton);
        myWeightText = findViewById(R.id.myWeightText);
        myBMIText = findViewById(R.id.myBMIText);
        myGoalText = findViewById(R.id.myGoalText);
        allowedCaloriesText = findViewById(R.id.allowedCaloriesText);

        try {
            System.out.println(rs);
        } catch (Exception e) {
            System.out.println("OOPS Something went wrong.");
        }

        //Return button
        myGoalsReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyGoals.this, UserProfile.class);
                startActivity(intent);
            }
        });

        //Edit Goals button
        goalsEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyGoals.this, EditMyGoals.class);
                startActivity(intent);
            }
        });

        displayUserInfo();

    }

    public void displayUserInfo(){
        //Accessing static variables from user.java
        if(User.Weight.isEmpty()){
            myWeightText.setText("N/A");
        }
        else{
            myWeightText.setText(User.Weight);
            System.out.println("Weight is:" + User.Weight);
        }

        if(User.BMI.isEmpty()){
            myBMIText.setText("N/A");
        }
        else{
            myBMIText.setText(User.BMI);
        }

        if(User.Goal.isEmpty()){
            myGoalText.setText("N/A");
        }
        else{
            myGoalText.setText(User.Goal);
        }

        if(User.AllowedCalories.isEmpty()){
            allowedCaloriesText.setText("N/A");
        }
        else{
            allowedCaloriesText.setText(User.AllowedCalories);
        }

    }
}
