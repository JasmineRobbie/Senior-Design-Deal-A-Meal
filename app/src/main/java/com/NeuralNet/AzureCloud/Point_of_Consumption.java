package com.NeuralNet.AzureCloud;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.teamcarl.prototype.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.sql.ResultSet;
import java.util.Random;

public class Point_of_Consumption extends Activity{

    TextView dateID, timeID, servingAmount, foodDescription, calories, protein, fat, sat_fat, carbs, sugar;
    Button buttonUp, buttonDown, leftButton, rightButton, consumedMealButton, helpButton,
            breakfastButton, lunchButton, dinnerButton, snacksButton, XtrasButton;
    ImageView foodImage;

    //setting all times to false unless otherwise clicked by user
    boolean breakfast = false, lunch = false, dinner = false, snacks = false, xtras = false;

    String UserId = User.UserID;

    //Adding all food drawable images in an array
    int[] myImageList = new int[]{R.drawable.orangejuice, R.drawable.englishmuffin, R.drawable.taco,
            R.drawable.roastbeef, R.drawable.snickers, R.drawable.sandwich};

    String descriptionObject, caloriesObject, proteinObject, fatObject, sat_fatObject, carbsObject, sugarObject, today_meals_ID_Object, BLDSXObject, finalServingAmountObject;
    String BREAKFAST, LUNCH, DINNER, SNACK, XTRA;
    int counter = 0;

    Random rand = new Random();
    int n = rand.nextInt(6);

    //Accessing database and selecting information
    DataAccess db = new DataAccess();
    final ResultSet rs = db.getDataTable("SELECT description, Units_Calories, Units_Protein, Units_Fat, Units_SatFat, Units_Carbs, Units_Sugar, Today_Meals_ID, BLDSX, servingAmount FROM TODAY_MEALS WHERE Today_Meals_ID = " + n);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_of__consumption);

        AlertDialog.Builder b1 = new AlertDialog.Builder(Point_of_Consumption.this);
        b1.setTitle("Help");
        b1.setMessage("In this page, you will input the current meal you are consuming. You will begin by selecting" +
                "your meal and inputting the calories, protein, saturated fat, carbs and sugar." +
                "You will then select if this meal is being eaten for breakfast, lunch, dinner, etc." +
                "You are also required to put in the amount of servings for it. Once you are finished," +
                " click 'consumed meal'.");
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

        //Declaration of XML variables
        dateID = findViewById(R.id.dateID);
        timeID = findViewById(R.id.timeID);
        servingAmount = findViewById(R.id.servingAmount);
        buttonUp = findViewById(R.id.buttonUp);
        buttonDown = findViewById(R.id.buttonDown);
        leftButton = findViewById(R.id.leftButton);
        rightButton = findViewById(R.id.rightButton);
        consumedMealButton = findViewById(R.id.consumedMealButton);
        helpButton = findViewById(R.id.helpBtn);
        breakfastButton = findViewById(R.id.breakfastButton);
        lunchButton = findViewById(R.id.lunchButton);
        dinnerButton = findViewById(R.id.dinnerButton);
        snacksButton = findViewById(R.id.snacksButton);
        XtrasButton = findViewById(R.id.XtrasButton);
        foodImage = findViewById(R.id.foodImage);
        foodDescription = findViewById(R.id.foodDescription);
        calories = findViewById(R.id.calories);
        protein = findViewById(R.id.protein);
        fat = findViewById(R.id.fat);
        sat_fat = findViewById(R.id.sat_fat);
        carbs = findViewById(R.id.carbs);
        sugar = findViewById(R.id.sugar);

        //setting date/time
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf_date = new SimpleDateFormat("MM-dd");
        SimpleDateFormat sdf_time = new SimpleDateFormat("HH:mm");
        String formatted_date = sdf_date.format(calendar.getTime());
        String formatted_time = sdf_time.format(calendar.getTime());

        dateID.setText(formatted_date);
        timeID.setText(formatted_time);
        foodImage.setImageResource(R.drawable.taco);

        //Adding all food drawable images in an array
        int[] myImageList = new int[]{R.drawable.orangejuice, R.drawable.englishmuffin, R.drawable.taco,
                R.drawable.roastbeef, R.drawable.snickers, R.drawable.sandwich};

        //setting initial serving amount num to 0
        servingAmount.setText("0");

        //calling serving amount function to increment/decrement with button click
        changeServingAmount();

        nextMeal();   //initial set of meal display (random)

        //calling right/left buttons to allow user to change foods
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextMeal();
                Toast.makeText(getApplicationContext(), "Right Btn Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextMeal();
                Toast.makeText(getApplicationContext(), "Left Btn Clicked", Toast.LENGTH_SHORT).show();
            }
        });


        breakfastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                breakfast = true;
                lunch = false;
                dinner = false;
                snacks = false;
                xtras = false;

                Toast.makeText(getApplicationContext(), "Breakfast Btn Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        lunchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                breakfast = false;
                lunch = true;
                dinner = false;
                snacks = false;
                xtras = false;

                Toast.makeText(getApplicationContext(), "Lunch Btn Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        dinnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                breakfast = false;
                lunch = false;
                dinner = true;
                snacks = false;
                xtras = false;

                Toast.makeText(getApplicationContext(), "Dinner Btn Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        snacksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                breakfast = false;
                lunch = false;
                dinner = false;
                snacks = true;
                xtras = false;

                Toast.makeText(getApplicationContext(), "Snacks Btn Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        XtrasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                breakfast = false;
                lunch = false;
                dinner = false;
                snacks = false;
                xtras = true;

                Toast.makeText(getApplicationContext(), "Xtras Btn Clicked", Toast.LENGTH_SHORT).show();
            }
        });




        //Enter desired meal into Eaten_Meals database
        consumedMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(breakfast){
                    BLDSXObject = "BREAKFAST";
                }
                else if(lunch){
                    BLDSXObject = "LUNCH";
                }
                else if(dinner){
                    BLDSXObject = "DINNER";
                }
                else if(snacks){
                    BLDSXObject = "SNACK";
                }
                else if(xtras){
                    BLDSXObject = "XTRA";
                }

                finalServingAmountObject = finalServingAmount;

                Toast.makeText(getApplicationContext(), "Consumed Btn Clicked", Toast.LENGTH_SHORT).show();

                String query = "INSERT INTO EatenMeals(description, Units_Calories, Units_Protein, Units_Fat, Units_SatFat, Units_Carbs, Units_Sugar, BLDSX, servingAmount, UserId)" +
                        " VALUES ('" + descriptionObject + "','" + caloriesObject + "', '" + proteinObject + "', '" + fatObject + "', '" + sat_fatObject + "', '" + carbsObject
                        + "', '" + sugarObject + "', '" + BLDSXObject + "', '" + finalServingAmountObject + "', '" + UserId + "') ;\n" ;

                db.executeNonQuery(query);
                System.out.println("Entered eaten meal to database");
            }
        });

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertdup.show();
            }
        });

    }

    //
    //
    //All defined functions
    //
    //
    public void changeServingAmount(){
        buttonUp();
        buttonDown();
    }

    String finalServingAmount;

    //Increment serving amount
    private void buttonUp() {
        //Increment serving amount
        buttonUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                counter++;
                servingAmount.setText(Integer.toString(counter));
                Toast.makeText(getApplicationContext(), "Up Btn Clicked", Toast.LENGTH_SHORT).show();

                finalServingAmount = String.valueOf(counter);
            }
        });
    }

    //Decrement serving amount
    private void buttonDown() {
        buttonDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //prevent negative serving amount
                if (counter < 1) {
                    counter = 0;
                }
                else {
                    counter--;
                    servingAmount.setText(Integer.toString(counter));
                }
                Toast.makeText(getApplicationContext(), "Down Btn Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Switch meal (image and data)
    private void nextMeal(){
        Random rand = new Random();
        int n = rand.nextInt(6);

        //Accessing database and selecting information
        DataAccess db = new DataAccess();
        final ResultSet rs = db.getDataTable("SELECT description, Units_Calories, Units_Protein, Units_Fat, Units_SatFat, Units_Carbs, Units_Sugar, Today_Meals_ID, BLDSX, servingAmount FROM TODAY_MEALS WHERE Today_Meals_ID = " + n);


        try{
            if (rs.next())
            {
                foodImage.setImageResource(myImageList[n-1]); //using n to match random number with number in image list
                foodDescription.setText(rs.getString(1));
                descriptionObject = rs.getString(1);

                calories.setText(rs.getString(2));
                caloriesObject = rs.getString(2);

                protein.setText(rs.getString(3));
                proteinObject = rs.getString(3);

                fat.setText(rs.getString(4));
                fatObject = rs.getString(4);

                sat_fat.setText(rs.getString(5));
                sat_fatObject = rs.getString(5);

                carbs.setText(rs.getString(6));
                carbsObject = rs.getString(6);

                sugar.setText(rs.getString(7));
                sugarObject = rs.getString(7);

                today_meals_ID_Object = rs.getString(rs.getString(8));

                System.out.println(rs.getString(3));


                System.out.println("set everything");

            }
        }
        catch (Exception e)
        {
            System.err.println(e);
        }

    }

}