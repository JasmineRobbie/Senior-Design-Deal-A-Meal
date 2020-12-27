package com.NeuralNet.AzureCloud;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.teamcarl.prototype.R;

import java.sql.ResultSet;

public class ResetPlan extends Activity {


    Button noBtn, yesBtn;

    DataAccess db = new DataAccess();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_plan);



        noBtn = findViewById(R.id.resetBtn_No);
        yesBtn = findViewById(R.id.resetBtn_Yes);



        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = User.UserID;
                String deleteQuery = "DELETE FROM EatenMeals WHERE UserId =" + userId;

                try{

                    String executeDelete = db.executeNonQuery(deleteQuery);
                    if(Integer.parseInt(executeDelete) > 0)
                        Toast.makeText(getApplicationContext(), "You have successfully reset your meals", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(), "Error Reseting meals, maybe no meals are currently present", Toast.LENGTH_SHORT).show();
                    finish();

                }catch(Exception e){
                    System.err.print(e);
                }
            }
        });
    }
}