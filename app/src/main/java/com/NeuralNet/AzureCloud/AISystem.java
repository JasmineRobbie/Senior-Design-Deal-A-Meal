package com.NeuralNet.AzureCloud;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.teamcarl.prototype.R;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Random;

public class AISystem extends Activity {

    TextView protein, fat, satFat, carbs, sugar, servingAmount, AITextBox;

    ImageView UpBtn, downBtn, foodImage;

    Button consumeButton;

    int servingTotal;
    int totalCalories = 0;
    int unitCalories,intProtein, intFat, intSatFat, intCarbs, intSugar;
    String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_i_system);


        DataAccess db = new DataAccess();

        protein = findViewById(R.id.proteinBoxAI);
        fat = findViewById(R.id.fatBoxAI);
        satFat = findViewById(R.id.satFatBoxAI);
        carbs = findViewById(R.id.carbsBoxAI);
        sugar = findViewById(R.id.sugarBoxAI);
        servingAmount = findViewById(R.id.AIServing);
        AITextBox = findViewById(R.id.AITextBox);


        //Images

        UpBtn = findViewById(R.id.arrowwUpAI);
        downBtn = findViewById(R.id.arrowDownAI);
        foodImage = findViewById(R.id.AIFoodImage);

        //Button

        consumeButton = findViewById(R.id.consumeButtonAI);

        int result = 0;
        UpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(servingTotal < 0){
                    servingTotal = 0;
                    servingTotal++;
                }else{
                    servingTotal++;
                    //let's try changing the values
                    protein.setText(String.valueOf(intProtein * servingTotal));
                    fat.setText(String.valueOf(intFat * servingTotal));
                    satFat.setText(String.valueOf(intSatFat * servingTotal));
                    carbs.setText(String.valueOf(intCarbs * servingTotal));
                    sugar.setText(String.valueOf(intSugar * servingTotal));
                }

                String servingTotalString = String.valueOf(servingTotal);
                servingAmount.setText(servingTotalString);

            }
        });


        downBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(servingTotal <= 0){
                    servingTotal = 0;
                }else{
                    servingTotal--;
                    //let's try changing the values
                    protein.setText(String.valueOf(intProtein * servingTotal));
                    fat.setText(String.valueOf(intFat * servingTotal));
                    satFat.setText(String.valueOf(intSatFat * servingTotal));
                    carbs.setText(String.valueOf(intCarbs * servingTotal));
                    sugar.setText(String.valueOf(intSugar * servingTotal));
                }

                String servingTotalString = String.valueOf(servingTotal);
                servingAmount.setText(servingTotalString);


            }
        });
        consumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //Let's test out the values
               System.out.println("Units Calories: " + unitCalories);
               System.out.println("Protein: " + intProtein);
               System.out.println("Fat: " + intFat);
               System.out.println("SatFat: " + intSatFat);
               System.out.println("Carbs: " + intCarbs);
               System.out.println("Sugar: " + intSugar);
               System.out.println("Serving total: " + servingTotal);
               if(servingTotal > 0){
                   //may need to call a class
                   insertIntoEatenMeals(unitCalories,intProtein,intFat,intSatFat,intCarbs,intSugar,servingTotal, description);
                   Toast.makeText(getApplicationContext(), "Meal Consumed", Toast.LENGTH_SHORT).show();
                   finish(); // let's test this out
               }
               else if (servingTotal == 0){
                   Toast.makeText(getApplicationContext(), "You have not consumed anything!!", Toast.LENGTH_SHORT).show();
                   finish(); // let's test this out
               }
               else{
                   System.out.println("There's an error with servingTotal!!!");
               }
            }
        });

        String getCaloriesQuery = "SELECT Units_Calories, servingAmount FROM EatenMeals WHERE UserId =" + User.UserID;

        final ResultSet results = db.getDataTable(getCaloriesQuery);

        ArrayList<String> calorieString = new ArrayList<>();
        ArrayList<String> numberOfSServings = new ArrayList<>();

        try{

            while(results.next()){

                calorieString.add(results.getString(1));
                numberOfSServings.add(results.getString(2));
            }

        }catch(Exception e){
            System.err.println(e);
        }

        for(int i = 0; i < calorieString.size(); i++){
            System.out.println("Total calories for " + i + " is: " + (Integer.parseInt(calorieString.get(i)) * Integer.parseInt(numberOfSServings.get(i))) );
            totalCalories += (Integer.parseInt(calorieString.get(i)) * Integer.parseInt(numberOfSServings.get(i)));
        }

        //Let's test out the totalCalories from database
        System.out.println("Total calories: " + totalCalories);

        if(totalCalories <= 1000){
            result = upToThousandCalories(totalCalories);
            AITextBox.setText("Looks like you're under 1000 calories, eat this to bump up your calories!");

        }else if(totalCalories > 1000 && totalCalories < 1600){
            result = upToThousandCalories(totalCalories);
            AITextBox.setText("Looks like you're under 2000 calories, eat this to bump up your calories!");

        }else if(totalCalories >=1600 && totalCalories <2000){
            result = meetingTheGoal(totalCalories);
            AITextBox.setText("Looks like you're close to hitting your daily calorie intake! Have this for today");
        }
        else if(totalCalories == 2000){
            result = reachedTheGoal(totalCalories);
            AITextBox.setText("You have meet you daily expectation. Hungry?? Try this!!");
        }
        else if(totalCalories > 2000){
            result = reachedTheGoal(totalCalories);
            AITextBox.setText("You're over your daily calorie intake, try having something small!");

        }else{
            System.out.println("Else statement executed");
        }

        String query = "SELECT Units_Calories, Units_Protein, Units_Fat, Units_SatFat, Units_Carbs, Units_Sugar, Description FROM Today_Meals WHERE Today_Meals_ID =" + result;
        final ResultSet rs = db.getDataTable(query);

        try{

            while(rs.next()){
                unitCalories = rs.getInt(1);

                protein.setText(rs.getString(2));
                intProtein = rs.getInt(2);

                fat.setText(rs.getString(3));
                intFat = rs.getInt(3);

                satFat.setText(rs.getString(4));
                intSatFat = rs.getInt(4);

                carbs.setText(rs.getString(5));
                intCarbs = rs.getInt(5);

                sugar.setText(rs.getString(6));
                intSugar = rs.getInt(6);

                description = rs.getString(7);

            }

        }catch(Exception e){
            System.out.println(e);
        }

    }
    //Let's call the class where you store consumed info into database
    public void insertIntoEatenMeals(int unitCalories,int intProtein, int intFat, int intSatFat, int intCarbs, int intSugar,int servingTotal, String description){
        //Accessing database
                    System.out.println("UserID: " + User.UserID);
                   DataAccess db = new DataAccess();
                   String calories, protein, fat, satFat, carbs, sugar,servingTotalString, userID;

                   calories = String.valueOf(unitCalories);
                   protein = String.valueOf(intProtein);
                   fat = String.valueOf(intFat);
                   satFat = String.valueOf(intSatFat);
                   carbs = String.valueOf(intCarbs);
                   sugar = String.valueOf(intSugar);
                   servingTotalString = String.valueOf(servingTotal);
                   userID = User.UserID;
                   System.out.println("Description: " + description);

        String query = "INSERT INTO EatenMeals(UserId, Description, Units_Calories, " +
                "Units_Protein, Units_Fat, Units_SatFat, Units_Carbs, Units_Sugar, servingAmount) \n"
                + "VALUES ( '" + userID + "' , '" + description + "' , '" + calories +
                "' , '" + protein + "' , '" + fat + "' , '" + satFat + "' , '" +
                carbs + "' , '" + sugar + "' , '" + servingTotalString +"') \n";

        db.executeNonQuery(query);
        System.out.println("Entered eaten meal to database");

    }
    //Let's call the class where the totalCalories is up to 1599
    public int upToThousandCalories(int totalCalories){
        Random foodNum = new Random();
        int resultValue = foodNum.nextInt(6) + 1;

        switch(resultValue){

            case 1:
                foodImage.setImageResource(R.drawable.orangejuice);
                break;
            case 2:
                foodImage.setImageResource(R.drawable.englishmuffin);
                break;
            case 3:
                foodImage.setImageResource(R.drawable.taco);
                break;
            case 4:
                foodImage.setImageResource(R.drawable.roastbeef);
                break;
            case 5:
                foodImage.setImageResource(R.drawable.snickers);
                break;
            case 6:
                foodImage.setImageResource(R.drawable.sandwich);
                break;
            default:
                break;
        }
        return resultValue;
    }
    //Let's call the class where the totalCalories is from 1600 to 1999 calories
    public int meetingTheGoal(int totalCalories){
        Random foodNum = new Random();
        int resultInt = foodNum.nextInt(5) + 1;

        switch(resultInt){

            case 1:
                foodImage.setImageResource(R.drawable.orangejuice);
                break;
            case 2:
                foodImage.setImageResource(R.drawable.englishmuffin);
                break;
            case 3:
                foodImage.setImageResource(R.drawable.taco);
                break;
            case 4:
                foodImage.setImageResource(R.drawable.snickers);
                break;
            case 5:
                foodImage.setImageResource(R.drawable.sandwich);
                break;
            default:
                break;
        }
        return resultInt;
    }
    //Let's call the class where the totalCalories is exactly 2000 calories and above
    public int reachedTheGoal(int totalCalories){
        Random foodNum = new Random();
        int resultInt = foodNum.nextInt(4) + 1;

        switch(resultInt){

            case 1:
                foodImage.setImageResource(R.drawable.orangejuice);
                break;
            case 2:
                foodImage.setImageResource(R.drawable.englishmuffin);
                break;
            case 3:
                foodImage.setImageResource(R.drawable.taco);
                break;
            case 4:
                foodImage.setImageResource(R.drawable.snickers);
                break;
            default:
                break;
        }
        return resultInt;
    }
}