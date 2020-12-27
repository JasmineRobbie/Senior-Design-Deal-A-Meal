package com.NeuralNet.AzureCloud;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.teamcarl.prototype.R;

public class UserProfile extends Activity {
    Button aboutMeBtn, myGoalsBtn, profileHelpBtn, profileReturnBtn, profileMemoBtn, resetPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        profileReturnBtn = findViewById(R.id.profileReturnBtn);
        aboutMeBtn = findViewById(R.id.aboutMeBtn);
        myGoalsBtn = findViewById(R.id.myGoalsBtn);

        resetPlan = findViewById(R.id.resetPlanBtn);

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


        resetPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this, ResetPlan.class);

                startActivity(intent);
            }
        });
    }
}


