package com.NeuralNet.AzureCloud;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.teamcarl.prototype.R;

public class Bistromenu extends Activity {

    Button cuisineButtonTwo;
    Button helpButton, returnButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bistromenu);

        AlertDialog.Builder b1 = new AlertDialog.Builder(Bistromenu.this);
        b1.setTitle("Help");
        b1.setMessage("The bistro page allows you to check the items on your menu, each meal ticket," +
                "your food storage, live recipes and the cuisine for today.");
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

        cuisineButtonTwo = (Button) findViewById(R.id.cuisineButtonTwo);
        helpButton = (Button) findViewById(R.id.helpBtn);
        returnButton = (Button) findViewById(R.id.returnBtn);

        cuisineButtonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPointOfConsumption();
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
                Intent intent = new Intent(Bistromenu.this, DealAMealList.class);
                startActivity(intent);
            }
        });

    }


    void openPointOfConsumption(){
        Intent intent = new Intent(this, Point_of_Consumption.class);

        startActivity(intent);

    }

}