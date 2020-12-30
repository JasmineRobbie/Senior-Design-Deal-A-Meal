package com.NeuralNet.AzureCloud;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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


        Random foodNum = new Random();
        int result = foodNum.nextInt(6) + 1;




        UpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(servingTotal < 0){
                    servingTotal = 0;
                    servingTotal++;
                }else{
                    servingTotal++;
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
                }

                String servingTotalString = String.valueOf(servingTotal);
                servingAmount.setText(servingTotalString);

            }
        });

        String getCaloriesQuery = "SELECT Units_Calories FROM EatenMeals WHERE UserId =" + User.UserID;

        final ResultSet results = db.getDataTable(getCaloriesQuery);

        ArrayList<String> calorieString = new ArrayList<>();

        try{

            while(results.next()){

                calorieString.add(results.getString(1));
            }

        }catch(Exception e){
            System.err.println(e);
        }

        for(int i = 0; i < calorieString.size(); i++){
            totalCalories += Integer.parseInt(calorieString.get(i));

        }



        if(totalCalories <= 1000){

            AITextBox.setText("Looks like youre under 1000 calories, eat this to bump up your calories!");

        }else if(totalCalories > 1000){
            AITextBox.setText("Looks like youre under 2000 calories, eat this to bump up your calories!");

        }else if(totalCalories < 2000){
            AITextBox.setText("Looks like you're close to hitting your daily calorie intake! Have this for today");

        }else if(totalCalories > 2000){
            AITextBox.setText("You're over your faily calorie intake, try having something small!");

        }else{
            System.out.println("Else statement executed");
        }

        String query = "SELECT Units_Calories, Units_Protein, Units_Fat, Units_SatFat, Units_Carbs, Units_Sugar FROM Today_Meals WHERE Today_Meals_ID =" + result;
        final ResultSet rs = db.getDataTable(query);

        try{

            while(rs.next()){
                protein.setText(rs.getString(2));
                fat.setText(rs.getString(3));
                satFat.setText(rs.getString(4));
                carbs.setText(rs.getString(5));
                sugar.setText(rs.getString(6));

            }

        }catch(Exception e){
            System.out.println(e);
        }




        switch(result){

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







    }






}