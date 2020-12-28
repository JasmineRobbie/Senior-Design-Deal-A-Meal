package com.NeuralNet.AzureCloud;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import java.lang.Object;
import java.lang.Number;
import java.math.BigDecimal;
import com.teamcarl.prototype.R;

import java.math.MathContext;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class EditMyGoals extends Activity implements OnItemSelectedListener{
    //Accessing database and selecting information
    DataAccess db = new DataAccess();
    String BMIString;

    Button editMyGoalsReturnBtn, calculateBMIbtn, goalsEditSubmitBtn, helpButton, sendMemoButton;
    EditText myWeightEditInput;
    TextView myNewCalculatedBMI;
    Spinner myHeightFeetInput, myHeightInchesInput, chooseGoalInput;
    double userHeight, userWeight, calculatedBMI;

    List<String> heightInFeet = new ArrayList<String>();
    List<String> heightInInches = new ArrayList<String>();
    List<String> goals = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_my_goals);

        AlertDialog.Builder b1 = new AlertDialog.Builder(EditMyGoals.this);
        b1.setTitle("Help");
        b1.setMessage("In the Edit My Goals page, begin by entering your personal health information like" +
                "your weight and height to calculate your BMI. You will then choose to what your goal is." +
                "After choosing your goal, your allowed calories will be displayed.");
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

        editMyGoalsReturnBtn = findViewById(R.id.editMyGoalsReturnBtn);
        calculateBMIbtn = findViewById(R.id.calculateBMIbtn);
        myWeightEditInput = findViewById(R.id.myWeightEditInput);
        myNewCalculatedBMI = findViewById(R.id.myNewCalculatedBMI);
        goalsEditSubmitBtn = findViewById(R.id.goalsEditSubmitBtn);
        helpButton = findViewById(R.id.helpBtn);
        sendMemoButton = findViewById(R.id.sendMemoBtn);

        //Spinner declaration
        myHeightFeetInput = findViewById(R.id.myHeightFeetInput);
        myHeightInchesInput = findViewById(R.id.myHeightInchesInput);
        chooseGoalInput = findViewById(R.id.chooseGoalInput);

        myHeightFeetInput.setOnItemSelectedListener(this);
        myHeightInchesInput.setOnItemSelectedListener(this);
        chooseGoalInput.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        heightInFeet.add("3");
        heightInFeet.add("4");
        heightInFeet.add("5");
        heightInFeet.add("6");

        heightInInches.add("0");
        heightInInches.add("1");
        heightInInches.add("2");
        heightInInches.add("3");
        heightInInches.add("4");
        heightInInches.add("5");
        heightInInches.add("6");
        heightInInches.add("7");
        heightInInches.add("8");
        heightInInches.add("9");
        heightInInches.add("10");
        heightInInches.add("11");

        goals.add("Lose Weight");
        goals.add("Maintain Weight");
        goals.add("Gain Weight");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, heightInFeet);
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, heightInInches);
        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, goals);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        myHeightFeetInput.setAdapter(dataAdapter);
        myHeightInchesInput.setAdapter(dataAdapter2);
        chooseGoalInput.setAdapter(dataAdapter3);

        //Only calculating BMI button
        calculateBMIbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateBMI();
                Toast.makeText(getApplicationContext(), "Calculated BMI", Toast.LENGTH_SHORT).show();
            }
        });

        //Adding new goals button
        goalsEditSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateBMI();
                setNewGoal();
                Toast.makeText(getApplicationContext(), "Submitted", Toast.LENGTH_SHORT).show();
            }
        });

        //Return button
        editMyGoalsReturnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditMyGoals.this, MyGoals.class);
                startActivity(intent);
            }
        });

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertdup.show();
            }
        });

        sendMemoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openNewMessage();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void calculateBMI(){
        String feet = myHeightFeetInput.getSelectedItem().toString();
        String inches = myHeightInchesInput.getSelectedItem().toString();
        final String weight = myWeightEditInput.getText().toString();

        int h =Integer.parseInt(feet);
        int i =Integer.parseInt(inches);
        int w =Integer.parseInt(weight);

        userHeight = ((h*12) + (i));
        userWeight = w;
        calculatedBMI = (703 * userWeight) / (userHeight * userHeight);

        //Round value
        BigDecimal better = new BigDecimal(calculatedBMI);
        BigDecimal roundedValue = better.round(new MathContext(3));

        System.out.println("Height= " + h + " Inches=" + i + " Weight= " + w);
        System.out.println("User Height= " + userHeight + " User Weight=" + userWeight);

        BMIString = String.valueOf(roundedValue);

        myNewCalculatedBMI.setText(BMIString);

        String query = "UPDATE USERS SET BMI = '" + BMIString + "', Weight = '" + userWeight + "' WHERE userid = " + User.UserID;

        try {
            db.executeNonQuery(query);
        } catch (Exception e) {
            System.out.println("OOPS Something went wrong.");
        }
    }

    public void setNewGoal(){
        String goal = chooseGoalInput.getSelectedItem().toString();

        System.out.println("Goal Chosen= " + goal);

        String query = "UPDATE USERS SET Goal = '" + goal + "' WHERE userid = " + User.UserID;

        try {
            db.executeNonQuery(query);
        } catch (Exception e) {
            System.out.println("OOPS Something went wrong.");
        }
    }

    void openNewMessage(){
        Intent intent = new Intent(EditMyGoals.this, Inbox.class);

        startActivity(intent);
    }


    //Calculate allowed calorie intake based on BMI and goal
    //Take parameters BMI and goal
    //going to have to double some functions from both previous functions

}
