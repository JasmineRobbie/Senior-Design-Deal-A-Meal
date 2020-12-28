package com.NeuralNet.AzureCloud;
import android.app.Activity;
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
    String BMIString, AllowedCalories;

    Button editMyGoalsReturnBtn, calculateBMIbtn, goalsEditSubmitBtn;
    EditText myWeightEditInput, myAgeInput;
    TextView myNewCalculatedBMI, allowedCaloriesUpdatedText;
    Spinner myHeightFeetInput, myHeightInchesInput, chooseGoalInput, chooseGenderInput, chooseAllergiesInput;
    double userHeight, userWeight, calculatedBMI;

    List<String> heightInFeet = new ArrayList<String>();
    List<String> heightInInches = new ArrayList<String>();
    List<String> goals = new ArrayList<String>();
    List<String> gender = new ArrayList<String>();
    List<String> allergies = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_my_goals);

        editMyGoalsReturnBtn = findViewById(R.id.editMyGoalsReturnBtn);
        calculateBMIbtn = findViewById(R.id.calculateBMIbtn);
        myWeightEditInput = findViewById(R.id.myWeightEditInput);
        myNewCalculatedBMI = findViewById(R.id.myNewCalculatedBMI);
        goalsEditSubmitBtn = findViewById(R.id.goalsEditSubmitBtn);
        allowedCaloriesUpdatedText = findViewById(R.id.allowedCaloriesUpdatedText);
        myAgeInput = findViewById(R.id.myAgeInput);

        //Spinner declaration
        myHeightFeetInput = findViewById(R.id.myHeightFeetInput);
        myHeightInchesInput = findViewById(R.id.myHeightInchesInput);
        chooseGoalInput = findViewById(R.id.chooseGoalInput);
        chooseGenderInput = findViewById(R.id.chooseGenderInput);
        chooseAllergiesInput = findViewById(R.id.chooseAllergiesInput);

        myHeightFeetInput.setOnItemSelectedListener(this);
        myHeightInchesInput.setOnItemSelectedListener(this);
        chooseGoalInput.setOnItemSelectedListener(this);
        chooseGenderInput.setOnItemSelectedListener(this);
        chooseAllergiesInput.setOnItemSelectedListener(this);

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

        gender.add("Male");
        gender.add("Female");

        allergies.add("Peanuts");
        allergies.add("Lactose");
        allergies.add("Fish");
        allergies.add("ShellFish");
        allergies.add("Soy");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, heightInFeet);
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, heightInInches);
        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, goals);
        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gender);
        ArrayAdapter<String> dataAdapter5 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, allergies);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        myHeightFeetInput.setAdapter(dataAdapter);
        myHeightInchesInput.setAdapter(dataAdapter2);
        chooseGoalInput.setAdapter(dataAdapter3);
        chooseGenderInput.setAdapter(dataAdapter4);
        chooseAllergiesInput.setAdapter(dataAdapter5);

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

/*    public void setNewGoal(){
        String goal = chooseGoalInput.getSelectedItem().toString();

        System.out.println("Goal Chosen= " + goal);

        String query = "UPDATE USERS SET Goal = '" + goal + "' WHERE userid = " + User.UserID;

        try {
            db.executeNonQuery(query);
        } catch (Exception e) {
            System.out.println("OOPS Something went wrong.");
        }
    }*/

    public void setNewGoal(){
        double calories = 0.0;
        System.out.print("SETTING NEW GOAL");

        String goal = chooseGoalInput.getSelectedItem().toString();
        String userGender = chooseGenderInput.getSelectedItem().toString();
        String feet = myHeightFeetInput.getSelectedItem().toString();
        String inches = myHeightInchesInput.getSelectedItem().toString();
        final String weight = myWeightEditInput.getText().toString();
        final String userAge = myAgeInput.getText().toString();

        int h =Integer.parseInt(feet);
        int i =Integer.parseInt(inches);
        int w =Integer.parseInt(weight);
        double age=Double.parseDouble(userAge);

        userHeight = ((h*12) + (i));    //in inches
        userWeight = w; //in pounds
        calculatedBMI = (703 * userWeight) / (userHeight * userHeight);

        //Round value
        BigDecimal better = new BigDecimal(calculatedBMI);
        BigDecimal roundedValue = better.round(new MathContext(3));

        BMIString = String.valueOf(roundedValue);

//Imperial formula for men
//BMR = 66.47 + ( 6.24 × weight in pounds ) + ( 12.7 × height in inches ) − ( 6.755 × age in years )

//Imperial formula for women
//BMR = 655.1 + ( 4.35 × weight in pounds ) + ( 4.7 × height in inches ) − ( 4.7 × age in years )


        //Calculating Calories

        //Gender is Male
        if(userGender == "Male" && goal == "Lose Weight"){
            calories = (655.1 + (4.35 * userWeight) + (4.7 * userHeight) - (4.7 * age));
            calories *= .85;
        }
        if(userGender == "Male" && goal == "Maintain Weight"){
            calories = (66.47 + (6.24 * userWeight) + (12.7 * userHeight) - (6.755 * age));
        }
        if(userGender == "Male" && goal == "Gain Weight"){
            calories = (655.1 + (4.35 * userWeight) + (4.7 * userHeight) - (4.7 * age));
            calories *= 1.2;
        }


        //Gender is Female
        if(userGender == "Female" && goal == "Lose Weight"){
            calories = (655.1 + (4.35 * userWeight) + (4.7 * userHeight) - (4.7 * age));
            calories *= .80;
        }
        if (userGender == "Female" && goal == "Maintain Weight"){
            calories = (655.1 + (4.35 * userWeight) + (4.7 * userHeight) - (4.7 * age));
        }
        if(userGender == "Female" && goal == "Gain Weight"){
            calories = (655.1 + (4.35 * userWeight) + (4.7 * userHeight) - (4.7 * age));
            calories *= 1.15;
        }

        System.out.print("Calories: " + calories);

        AllowedCalories = String.valueOf(calories);

        allowedCaloriesUpdatedText.setText(AllowedCalories);

        String query = "UPDATE USERS SET AllowedCalories = '" + AllowedCalories + "', Goal = '" + goal + "' WHERE userid = " + User.UserID;

        try {
            db.executeNonQuery(query);
        } catch (Exception e) {
            System.out.println("OOPS Something went wrong.");
        }
    }
}
