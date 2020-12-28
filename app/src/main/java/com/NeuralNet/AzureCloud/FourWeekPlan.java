package com.NeuralNet.AzureCloud;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.teamcarl.prototype.R;

import java.sql.ResultSet;

public class FourWeekPlan extends Activity {

    Button previousDayBtn, nextDayBtn, eraseButton, memoButton, helpButton;
    ViewFlipper viewFlipper;

    ImageView hourOne, hourTwo, hourThree, hourFour, hourFive, hourSix, hourSeven, hourEight,hourNine,hourTen, hourEleven, hourTwelve;
    ImageView hourOneAN, hourTwoAN, hourThreeAN, hourFourAN, hourFiveAN, hourSixAN, hourSevenAN, hourEightAN,hourNineAN, hourTenAN, hourElevenAN, hourTwelveMN;


    ImageView breakfastBtn, lunchBtn, dinnerBtn, snacksBtn, extrasBtn, beveragesBtn, favoritesBtn, healthButton, offTheGridButton, displayImage;

    TextView textPurpose, hourSelector, mealOne,mealTwo,mealThree,mealFour,mealFive;

    Button hourUp, hourDown, enterButton;

    String weekDays[] = {"Sunday", "Monday", "Tuesday","Wednesday", "Thursday", "Friday", "Saturday"};
    int currentDayOfTheWeek = 1;

    String currentDay = "";

    int chooseHour = 1;

    String chosenHour = "";
    int chosenHourInt = 0;

    String selectedMealTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four_week_plan);

        AlertDialog.Builder b1 = new AlertDialog.Builder(FourWeekPlan.this);
        b1.setTitle("Help");
        b1.setMessage("The Four Week Plan page is where you will be able to view all of your consumed meals for " +
                "the day. You can see what you've eaten for breakfast, lunch, dinner, snacks and extras. You can" +
                "change the day of the week on the top left corner with the arrows. Your eaten meals along with" +
                "the time you consumed them will highlight in its coordinated color on one of the time slots on the " +
                "left side of the screen. You can also view your meal tickets for the day as well as write notes" +
                "in a large text box in the center.");
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


        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        previousDayBtn = (Button) findViewById(R.id.previousDayBtn);
        nextDayBtn = (Button) findViewById(R.id.nextDayBtn);

        hourOne = findViewById(R.id.hourOne);
        hourTwo = findViewById(R.id.hourTwo);
        hourThree = findViewById(R.id.hourThree);
        hourFour = findViewById(R.id.hourFour);
        hourFive = findViewById(R.id.hourFive);
        hourSix = findViewById(R.id.hourSix);
        hourSeven = findViewById(R.id.hourSeven);
        hourEight = findViewById(R.id.hourEight);
        hourNine = findViewById(R.id.hourNine);
        hourTen = findViewById(R.id.hourTen);
        hourEleven = findViewById(R.id.hourEleven);
        hourTwelve = findViewById(R.id.hourTwelve);
        memoButton = findViewById(R.id.memoBtn);
        helpButton = findViewById(R.id.helpBtn);

        //Afternoon timeslots

        hourOneAN = findViewById(R.id.hourOneAN);
        hourTwoAN = findViewById(R.id.hourTwoAN);
        hourThreeAN = findViewById(R.id.hourThreeAN);
        hourFourAN = findViewById(R.id.hourFourAN);
        hourFiveAN = findViewById(R.id.hourFiveAN);
        hourSixAN = findViewById(R.id.hourSixAN);
        hourSevenAN = findViewById(R.id.hourSevenAN);
        hourEightAN = findViewById(R.id.hourEightAN);
        hourNineAN = findViewById(R.id.hourNineAN);
        hourTenAN = findViewById(R.id.hourTenAN);
        hourElevenAN = findViewById(R.id.hourElevenAN);
        hourTwelveMN = findViewById(R.id.hourTwelveMN);

        //
        breakfastBtn = findViewById(R.id.breakfastButton);
        lunchBtn = findViewById(R.id.lunchButton);
        dinnerBtn = findViewById(R.id.dinnerButton);
        snacksBtn = findViewById(R.id.snacksButton);
        extrasBtn = findViewById(R.id.extraButton);
        favoritesBtn = findViewById(R.id.favoriteButton);
        healthButton = findViewById(R.id.healthButton);
        offTheGridButton = findViewById(R.id.extraButton);

        textPurpose = findViewById(R.id.purposeText);
        displayImage = findViewById(R.id.displayImage);

        hourSelector = findViewById(R.id.selectHour);


        hourUp = findViewById(R.id.hourUp);
        hourDown = findViewById(R.id.hourDown);

        enterButton = findViewById(R.id.enterButton);


        eraseButton = findViewById(R.id.eraseButton);

        mealOne = findViewById(R.id.mealOne);
        mealTwo = findViewById(R.id.mealTwo);
        mealThree = findViewById(R.id.mealThree);
        mealFour = findViewById(R.id.mealFour);
        mealFive = findViewById(R.id.mealFive);




        displayMeals(currentDay);




        eraseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearDay();
            }
        });
        previousDayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewFlipper.showPrevious();
                currentDayOfTheWeek--;
                if(currentDayOfTheWeek < 1){
                    currentDayOfTheWeek = 1;
                }
                DayOfTheWeek(currentDayOfTheWeek);
                //displayMeals(currentDay);
            }
        });

        nextDayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewFlipper.showNext();
                currentDayOfTheWeek++;

                if(currentDayOfTheWeek > 7){
                    currentDayOfTheWeek = 7;
                }
                DayOfTheWeek(currentDayOfTheWeek);

                //displayMeals(currentDay);
            }
        });


        hourUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseHour++;

                if(chooseHour > 24){
                    chooseHour = 1;
                    hourSelector.setText(String.valueOf(chooseHour));
                }else{
                    hourSelector.setText(String.valueOf(chooseHour));
                }
            }
        });

        hourDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseHour--;

                if(chooseHour < 1){
                    chooseHour = 24;
                    hourSelector.setText(String.valueOf(chooseHour));
                }else{
                    hourSelector.setText(String.valueOf(chooseHour));
                }
            }
        });

        breakfastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayImage.setImageResource(R.drawable.majorcode1);
                textPurpose.setText("Start the Day with a good breakfast");

                chosenHour = hourSelector.getText().toString();

                chosenHourInt = Integer.parseInt(chosenHour);

                selectedMealTime = "breakfast";
            }
        });


        lunchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayImage.setImageResource(R.drawable.majorcode2);
                textPurpose.setText("Lunch time");

                chosenHour = hourSelector.getText().toString();

                chosenHourInt = Integer.parseInt(chosenHour);
                selectedMealTime = "lunch";
            }
        });

        dinnerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayImage.setImageResource(R.drawable.majorcode3);
                textPurpose.setText("Dinner time");

                chosenHour = hourSelector.getText().toString();

                chosenHourInt = Integer.parseInt(chosenHour);

                selectedMealTime = "dinner";
            }
        });

        snacksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                displayImage.setImageResource(R.drawable.majorcode4);
                textPurpose.setText("snack time");

                chosenHour = hourSelector.getText().toString();

                chosenHourInt = Integer.parseInt(chosenHour);

                selectedMealTime = "snacks";

            }
        });

        extrasBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayImage.setImageResource(R.drawable.majorcode5);
                textPurpose.setText("extra food");

                chosenHour = hourSelector.getText().toString();

                chosenHourInt = Integer.parseInt(chosenHour);

                selectedMealTime = "extra";
            }
        });

        favoritesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayImage.setImageResource(R.drawable.majorcode6);
                textPurpose.setText("favorite snack time");

                chosenHour = hourSelector.getText().toString();

                chosenHourInt = Integer.parseInt(chosenHour);
                selectedMealTime = "favorite";
            }
        });

        healthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayImage.setImageResource(R.drawable.majorcode7);
                textPurpose.setText("Health");

                chosenHour = hourSelector.getText().toString();

                chosenHourInt = Integer.parseInt(chosenHour);

                selectedMealTime = "health";
            }
        });

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertdup.show();
            }
        });

        offTheGridButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayImage.setImageResource(R.drawable.majorcode8);
                textPurpose.setText("Off the grid");

                chosenHour = hourSelector.getText().toString();

                chosenHourInt = Integer.parseInt(chosenHour);

                selectedMealTime = "offTheGrid";
            }
        });

        memoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMessagesPage();
            }
        });

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertdup.show();
            }
        });



        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selectedMealTime.equals("breakfast")){
                    switch(chosenHourInt){
                        case 1:
                            hourOne.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                            break;
                        case 2:
                            hourTwo.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                            break;
                        case 3:
                            hourThree.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                            break;
                        case 4:
                            hourFour.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                            break;
                        case 5:
                            hourFive.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                            break;
                        case 6:
                            hourSix.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                            break;
                        case 7:
                            hourSeven.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                            break;
                        case 8:
                            hourEight.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                            break;
                        case 9:
                            hourNine.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                            break;
                        case 10:
                            hourTen.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                            break;
                        case 11:
                            hourEleven.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                            break;
                        case 12:
                            hourTwelve.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                            break;
                        case 13:
                            hourOneAN.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                            break;
                        case 14:
                            hourTwoAN.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                            break;
                        case 15:
                            hourThreeAN.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                            break;
                        case 16:
                            hourFourAN.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                            break;
                        case 17:
                            hourFiveAN.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                            break;
                        case 18:
                            hourSixAN.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                            break;
                        case 19:
                            hourSevenAN.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                            break;
                        case 20:
                            hourEightAN.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                            break;
                        case 21:
                            hourNineAN.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                            break;
                        case 22:
                            hourTenAN.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                            break;
                        case 23:
                            hourElevenAN.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                            break;
                        case 24:
                            hourTwelveMN.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                            break;

                    }

                }else if(selectedMealTime.equals("lunch")){
                    switch(chosenHourInt){
                        case 1:
                            hourOne.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
                            break;
                        case 2:
                            hourTwo.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
                            break;
                        case 3:
                            hourThree.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
                            break;
                        case 4:
                            hourFour.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
                            break;
                        case 5:
                            hourFive.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
                            break;
                        case 6:
                            hourSix.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
                            break;
                        case 7:
                            hourSeven.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
                            break;
                        case 8:
                            hourEight.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
                            break;
                        case 9:
                            hourNine.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
                            break;
                        case 10:
                            hourTen.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
                            break;
                        case 11:
                            hourEleven.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
                            break;
                        case 12:
                            hourTwelve.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
                            break;
                        case 13:
                            hourOneAN.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
                            break;
                        case 14:
                            hourTwoAN.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
                            break;
                        case 15:
                            hourThreeAN.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
                            break;
                        case 16:
                            hourFourAN.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
                            break;
                        case 17:
                            hourFiveAN.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
                            break;
                        case 18:
                            hourSixAN.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
                            break;
                        case 19:
                            hourSevenAN.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
                            break;
                        case 20:
                            hourEightAN.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
                            break;
                        case 21:
                            hourNineAN.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
                            break;
                        case 22:
                            hourTenAN.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
                            break;
                        case 23:
                            hourElevenAN.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
                            break;
                        case 24:
                            hourTwelveMN.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
                            break;

                    }

                    }else if(selectedMealTime.equals("dinner")){
                    switch(chosenHourInt){
                        case 1:
                            hourOne.setBackgroundColor(Color.YELLOW);
                            break;
                        case 2:
                            hourTwo.setBackgroundColor(Color.YELLOW);
                            break;
                        case 3:
                            hourThree.setBackgroundColor(Color.YELLOW);
                            break;
                        case 4:
                            hourFour.setBackgroundColor(Color.YELLOW);
                            break;
                        case 5:
                            hourFive.setBackgroundColor(Color.YELLOW);
                            break;
                        case 6:
                            hourSix.setBackgroundColor(Color.YELLOW);
                            break;
                        case 7:
                            hourSeven.setBackgroundColor(Color.YELLOW);
                            break;
                        case 8:
                            hourEight.setBackgroundColor(Color.YELLOW);
                            break;
                        case 9:
                            hourNine.setBackgroundColor(Color.YELLOW);
                            break;
                        case 10:
                            hourTen.setBackgroundColor(Color.YELLOW);
                            break;
                        case 11:
                            hourEleven.setBackgroundColor(Color.YELLOW);
                            break;
                        case 12:
                            hourTwelve.setBackgroundColor(Color.YELLOW);
                            break;
                        case 13:
                            hourOneAN.setBackgroundColor(Color.YELLOW);
                            break;
                        case 14:
                            hourTwoAN.setBackgroundColor(Color.YELLOW);
                            break;
                        case 15:
                            hourThreeAN.setBackgroundColor(Color.YELLOW);
                            break;
                        case 16:
                            hourFourAN.setBackgroundColor(Color.YELLOW);
                            break;
                        case 17:
                            hourFiveAN.setBackgroundColor(Color.YELLOW);
                            break;
                        case 18:
                            hourSixAN.setBackgroundColor(Color.YELLOW);
                            break;
                        case 19:
                            hourSevenAN.setBackgroundColor(Color.YELLOW);
                            break;
                        case 20:
                            hourEightAN.setBackgroundColor(Color.YELLOW);
                            break;
                        case 21:
                            hourNineAN.setBackgroundColor(Color.YELLOW);
                            break;
                        case 22:
                            hourTenAN.setBackgroundColor(Color.YELLOW);
                            break;
                        case 23:
                            hourElevenAN.setBackgroundColor(Color.YELLOW);
                            break;
                        case 24:
                            hourTwelveMN.setBackgroundColor(Color.YELLOW);
                            break;

                    }

                }else if(selectedMealTime.equals("snacks")){
                    switch(chosenHourInt){
                        case 1:
                            hourOne.setBackgroundColor(Color.GREEN);
                            break;
                        case 2:
                            hourTwo.setBackgroundColor(Color.GREEN);
                            break;
                        case 3:
                            hourThree.setBackgroundColor(Color.GREEN);
                            break;
                        case 4:
                            hourFour.setBackgroundColor(Color.GREEN);
                            break;
                        case 5:
                            hourFive.setBackgroundColor(Color.GREEN);
                            break;
                        case 6:
                            hourSix.setBackgroundColor(Color.GREEN);
                            break;
                        case 7:
                            hourSeven.setBackgroundColor(Color.GREEN);
                            break;
                        case 8:
                            hourEight.setBackgroundColor(Color.GREEN);
                            break;
                        case 9:
                            hourNine.setBackgroundColor(Color.GREEN);
                            break;
                        case 10:
                            hourTen.setBackgroundColor(Color.GREEN);
                            break;
                        case 11:
                            hourEleven.setBackgroundColor(Color.GREEN);
                            break;
                        case 12:
                            hourTwelve.setBackgroundColor(Color.GREEN);
                            break;
                        case 13:
                            hourOneAN.setBackgroundColor(Color.GREEN);
                            break;
                        case 14:
                            hourTwoAN.setBackgroundColor(Color.GREEN);
                            break;
                        case 15:
                            hourThreeAN.setBackgroundColor(Color.GREEN);
                            break;
                        case 16:
                            hourFourAN.setBackgroundColor(Color.GREEN);
                            break;
                        case 17:
                            hourFiveAN.setBackgroundColor(Color.GREEN);
                            break;
                        case 18:
                            hourSixAN.setBackgroundColor(Color.GREEN);
                            break;
                        case 19:
                            hourSevenAN.setBackgroundColor(Color.GREEN);
                            break;
                        case 20:
                            hourEightAN.setBackgroundColor(Color.GREEN);
                            break;
                        case 21:
                            hourNineAN.setBackgroundColor(Color.GREEN);
                            break;
                        case 22:
                            hourTenAN.setBackgroundColor(Color.GREEN);
                            break;
                        case 23:
                            hourElevenAN.setBackgroundColor(Color.GREEN);
                            break;
                        case 24:
                            hourTwelveMN.setBackgroundColor(Color.GREEN);
                            break;

                    }
                }else if(selectedMealTime.equals("favorite")){
                    switch(chosenHourInt){
                        case 1:
                            hourOne.setBackgroundColor(Color.YELLOW);
                            break;
                        case 2:
                            hourTwo.setBackgroundColor(Color.YELLOW);
                            break;
                        case 3:
                            hourThree.setBackgroundColor(Color.YELLOW);
                            break;
                        case 4:
                            hourFour.setBackgroundColor(Color.YELLOW);
                            break;
                        case 5:
                            hourFive.setBackgroundColor(Color.YELLOW);
                            break;
                        case 6:
                            hourSix.setBackgroundColor(Color.YELLOW);
                            break;
                        case 7:
                            hourSeven.setBackgroundColor(Color.YELLOW);
                            break;
                        case 8:
                            hourEight.setBackgroundColor(Color.YELLOW);
                            break;
                        case 9:
                            hourNine.setBackgroundColor(Color.YELLOW);
                            break;
                        case 10:
                            hourTen.setBackgroundColor(Color.YELLOW);
                            break;
                        case 11:
                            hourEleven.setBackgroundColor(Color.YELLOW);
                            break;
                        case 12:
                            hourTwelve.setBackgroundColor(Color.YELLOW);
                            break;
                        case 13:
                            hourOneAN.setBackgroundColor(Color.YELLOW);
                            break;
                        case 14:
                            hourTwoAN.setBackgroundColor(Color.YELLOW);
                            break;
                        case 15:
                            hourThreeAN.setBackgroundColor(Color.YELLOW);
                            break;
                        case 16:
                            hourFourAN.setBackgroundColor(Color.YELLOW);
                            break;
                        case 17:
                            hourFiveAN.setBackgroundColor(Color.YELLOW);
                            break;
                        case 18:
                            hourSixAN.setBackgroundColor(Color.YELLOW);
                            break;
                        case 19:
                            hourSevenAN.setBackgroundColor(Color.YELLOW);
                            break;
                        case 20:
                            hourEightAN.setBackgroundColor(Color.YELLOW);
                            break;
                        case 21:
                            hourNineAN.setBackgroundColor(Color.YELLOW);
                            break;
                        case 22:
                            hourTenAN.setBackgroundColor(Color.YELLOW);
                            break;
                        case 23:
                            hourElevenAN.setBackgroundColor(Color.YELLOW);
                            break;
                        case 24:
                            hourTwelveMN.setBackgroundColor(Color.YELLOW);
                            break;

                    }
                }else if(selectedMealTime.equals("health")){
                    switch(chosenHourInt){
                        case 1:
                            hourOne.setBackgroundColor(Color.parseColor("#800080"));
                            break;
                        case 2:
                            hourTwo.setBackgroundColor(Color.parseColor("#800080"));
                            break;
                        case 3:
                            hourThree.setBackgroundColor(Color.parseColor("#800080"));
                            break;
                        case 4:
                            hourFour.setBackgroundColor(Color.parseColor("#800080"));
                            break;
                        case 5:
                            hourFive.setBackgroundColor(Color.parseColor("#800080"));
                            break;
                        case 6:
                            hourSix.setBackgroundColor(Color.parseColor("#800080"));
                            break;
                        case 7:
                            hourSeven.setBackgroundColor(Color.parseColor("#800080"));
                            break;
                        case 8:
                            hourEight.setBackgroundColor(Color.parseColor("#800080"));
                            break;
                        case 9:
                            hourNine.setBackgroundColor(Color.parseColor("#800080"));
                            break;
                        case 10:
                            hourTen.setBackgroundColor(Color.parseColor("#800080"));
                            break;
                        case 11:
                            hourEleven.setBackgroundColor(Color.parseColor("#800080"));
                            break;
                        case 12:
                            hourTwelve.setBackgroundColor(Color.parseColor("#800080"));
                            break;
                        case 13:
                            hourOneAN.setBackgroundColor(Color.parseColor("#800080"));
                            break;
                        case 14:
                            hourTwoAN.setBackgroundColor(Color.parseColor("#800080"));
                            break;
                        case 15:
                            hourThreeAN.setBackgroundColor(Color.parseColor("#800080"));
                            break;
                        case 16:
                            hourFourAN.setBackgroundColor(Color.parseColor("#800080"));
                            break;
                        case 17:
                            hourFiveAN.setBackgroundColor(Color.parseColor("#800080"));
                            break;
                        case 18:
                            hourSixAN.setBackgroundColor(Color.parseColor("#800080"));
                            break;
                        case 19:
                            hourSevenAN.setBackgroundColor(Color.parseColor("#800080"));
                            break;
                        case 20:
                            hourEightAN.setBackgroundColor(Color.parseColor("#800080"));
                            break;
                        case 21:
                            hourNineAN.setBackgroundColor(Color.parseColor("#800080"));
                            break;
                        case 22:
                            hourTenAN.setBackgroundColor(Color.parseColor("#800080"));
                            break;
                        case 23:
                            hourElevenAN.setBackgroundColor(Color.parseColor("#800080"));
                            break;
                        case 24:
                            hourTwelveMN.setBackgroundColor(Color.parseColor("#800080"));
                            break;

                    }
                }else if(selectedMealTime.equals("offTheGrid")){
                    switch(chosenHourInt){
                        case 1:
                            break;
                        case 2:
                            break;
                        case 3:
                            break;
                        case 4:
                            break;
                        case 5:
                            break;
                        case 6:
                            break;
                        case 7:
                            break;
                        case 8:
                            break;
                        case 9:
                            break;
                        case 10:
                            break;
                        case 11:
                            break;
                        case 12:
                            break;
                        case 13:
                            break;
                        case 14:
                            break;
                        case 15:
                            break;
                        case 16:
                            break;
                        case 17:
                            break;
                        case 18:
                            break;
                        case 19:
                            break;
                        case 20:
                            break;
                        case 21:
                            break;
                        case 22:
                            break;
                        case 23:
                            break;
                        case 24:
                            break;

                    }
                }else if(selectedMealTime.equals("extra")){
                    switch(chosenHourInt){
                        case 1:
                            break;
                        case 2:
                            break;
                        case 3:
                            break;
                        case 4:
                            break;
                        case 5:
                            break;
                        case 6:
                            break;
                        case 7:
                            break;
                        case 8:
                            break;
                        case 9:
                            break;
                        case 10:
                            break;
                        case 11:
                            break;
                        case 12:
                            break;
                        case 13:
                            break;
                        case 14:
                            break;
                        case 15:
                            break;
                        case 16:
                            break;
                        case 17:
                            break;
                        case 18:
                            break;
                        case 19:
                            break;
                        case 20:
                            break;
                        case 21:
                            break;
                        case 22:
                            break;
                        case 23:
                            break;
                        case 24:
                            break;

                    }
                }


            }
        });


    }


    public void openMessagesPage(){
        Intent intent = new Intent(FourWeekPlan.this, NewMessage.class);

        startActivity(intent);

    }

    public void displayMeals(String currentDay){

        DataAccess db = new DataAccess();
        DataAccess d2 = new DataAccess();
        DataAccess d3 = new DataAccess();
        DataAccess d4 = new DataAccess();
        DataAccess d5 = new DataAccess();

        String query = "SELECT description FROM TODAY_MEALS WHERE Today_Meals_ID = 1";

        String query2 = "SELECT description FROM TODAY_MEALS WHERE Today_Meals_ID = 2";

        String query3 = "SELECT description FROM TODAY_MEALS WHERE Today_Meals_ID = 3";

        String query4 = "SELECT description FROM TODAY_MEALS WHERE Today_Meals_ID = 4";

        String query5 = "SELECT description FROM TODAY_MEALS WHERE Today_Meals_ID = 5";

        final ResultSet results = db.getDataTable(query);

        final ResultSet results2 = d2.getDataTable(query2);

        final ResultSet results3 = d3.getDataTable(query3);

        final ResultSet results4 = d4.getDataTable(query4);

        final ResultSet results5 = d5.getDataTable(query5);

        try{
            if(results.next()){
                mealOne.setText(results.getString(1));

            }
        }catch(Exception e){
            System.out.println("Error");
        }

        try{
            if(results2.next()){
                mealTwo.setText(results2.getString(1));

            }
        }catch(Exception e){
            System.out.println("Error");
        }

        try{
            if(results3.next()){
                mealThree.setText(results3.getString(1));

            }
        }catch(Exception e){
            System.out.println("Error");
        }

        try{
            if(results4.next()){
                mealFour.setText(results4.getString(1));

            }
        }catch(Exception e){
            System.out.println("Error");
        }

        try{
            if(results5.next()){
                mealFive.setText(results5.getString(1));

            }
        }catch(Exception e){
            System.out.println("Error");
        }
    }

    public void DayOfTheWeek(int x){

        if(x == 1){
            currentDay = "Sunday";
        }else if(x == 2){
            currentDay = "Monday";
        }else if(x == 3){
            currentDay = "Tuesday";
        }else if(x == 4){
            currentDay = "Wednesday";
        }else if(x == 5){
            currentDay = "Thursday";
        }else if(x == 6){
            currentDay = "Friday";
        }else if(x == 7){
            currentDay = "Saturday";
        }else{
            currentDay = "";
        }


    }


    public void clearDay(){
        hourOne.setBackgroundResource(0);
        hourTwo.setBackgroundResource(0);
        hourThree.setBackgroundResource(0);
        hourFour.setBackgroundResource(0);
        hourFive.setBackgroundResource(0);
        hourSix.setBackgroundResource(0);
        hourSeven.setBackgroundResource(0);
        hourEight.setBackgroundResource(0);
        hourNine.setBackgroundResource(0);
        hourTen.setBackgroundResource(0);
        hourEleven.setBackgroundResource(0);
        hourTwelve.setBackgroundResource(0);
        hourOneAN.setBackgroundResource(0);
        hourTwoAN.setBackgroundResource(0);
        hourThreeAN.setBackgroundResource(0);
        hourFourAN.setBackgroundResource(0);
        hourFiveAN.setBackgroundResource(0);
        hourSixAN.setBackgroundResource(0);
        hourSevenAN.setBackgroundResource(0);
        hourEightAN.setBackgroundResource(0);
        hourNineAN.setBackgroundResource(0);
        hourTenAN.setBackgroundResource(0);
        hourElevenAN.setBackgroundResource(0);
        hourTwelveMN.setBackgroundResource(0);

    }

}