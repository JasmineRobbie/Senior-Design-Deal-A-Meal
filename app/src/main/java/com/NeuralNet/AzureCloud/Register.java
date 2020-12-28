package com.NeuralNet.AzureCloud;

import android.app.Activity;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.teamcarl.prototype.R;

//import java.sql.Date;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Date;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Register extends Activity {

    ImageButton bRegister;

    EditText brand, password, confirmpassword;
    int user, brandInfo, brandId, ActiveGuide; //still working on ActiveGuide
    //Adding variables by Rhagavi
    int countReg, countMax;
    Date regDateClosed;

    String passwordInfo, spassword, sconfirmpassword, sBrandLink, hasuser, haspass;
    DataAccess db = new DataAccess();
    DatabaseHelper dbh = new DatabaseHelper(this);
    User user1 = null; // probably not be using at the moment
    InternalDataAccess internalData = new InternalDataAccess();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        brand = (EditText) findViewById(R.id.brandEdit);
        password = (EditText) findViewById(R.id.passwordEdit);
        confirmpassword = (EditText) findViewById(R.id.confirmPass);
        bRegister = (ImageButton) findViewById(R.id.RegisterButton);


        password.setText(internalData.getSharedPreferencesKeyValue(getApplicationContext(), "password"));
        if (!password.getText().toString().isEmpty()) {
            // log user in (using saved file data)
            // get user data
            Intent intent = new Intent(Register.this, login.class);
            //intent.putExtra("User", user);
            try {
                startActivity(intent);
            } catch (Exception e) {
                Log.e("error in login act", e.getMessage());
            }
        }


        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spassword = password.getText().toString();
                sconfirmpassword = confirmpassword.getText().toString();
                sBrandLink = brand.getText().toString();


                //checks if passwords match
                if (spassword.equals(sconfirmpassword) == false) {

                    Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_SHORT).show();
                    System.out.println(spassword + sconfirmpassword);
                    //checks if password is at least 6 characters
                } else if (spassword.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password needs at least 6 characters", Toast.LENGTH_SHORT).show();
                }
                //checks if brand field is empty
                else if (sBrandLink.isEmpty()){
                Toast.makeText(getApplicationContext(), "Enter a brand code", Toast.LENGTH_SHORT).show();


                }
                else if (!doesBrandExist(sBrandLink))
                {
                    Toast.makeText(getApplicationContext(), "The Brand Number entered does not exist. Please enter valid Brand Number.", Toast.LENGTH_SHORT).show();
                }
                //Added by Rhagavi
                //Add more error messages conditions for registration date & count registration
                // 1) Add the condition where it passes the registration date with the error message
                // 2) Add the condition where number of registrations has exceeded with the message

                //calls validRegDate function to determine if the registration date has passed
                else if (!validRegDate(sBrandLink, password.getText().toString())){
                    System.out.println("Testing through if else for registration date");
                    Toast.makeText(getApplicationContext(), "Registration for this guide is closed!!", Toast.LENGTH_SHORT).show();
                }
                //calls numOfRegExceeded to determine if it exceeded
                //if returned as false, that's means that it has been exceed, therefore this condition will execute
                else if (!numOfRegExceeded(sBrandLink, password.getText().toString())){
                    System.out.println("The number of users has been exceeded. Testing through the if-else");
                    Toast.makeText(getApplicationContext(), "We're out of spots to register you in for this guide.", Toast.LENGTH_SHORT).show();
                }
                else {
                    //Added by Rhagavi for testing purpose
                    System.out.println("Default Active Guide: " + ActiveGuide);

                    String query = "insert into users (Password, BrandNumber)\n" +
                            "VALUES ('" + password.getText() + "', '" + brandId + "')" +
                            "SELECT @@IDENTITY, '" + password.getText() + "'";

                    String result = db.executeNonQuery(query);

                    //Added by Rhagavi
                    //Let's find the Active Guide value (along with Reg_Date_Closed and Cnt_Reg)
                    String queryActiveGuide = "SELECT ActiveGuide, Reg_Date_Closed, Cnt_Reg \n" +
                            "FROM brand \n" +
                            "WHERE BrandId = '" + brandId + "'";
                    dbh.login(sBrandLink, password.getText().toString());
                    ResultSet activeGuideResult = db.getDataTable(queryActiveGuide);
                    try{
                        while(activeGuideResult.next()){
                            ActiveGuide = activeGuideResult.getInt(1);
                            regDateClosed = activeGuideResult.getDate(2);
                            countReg = activeGuideResult.getInt(3);
                        }
                    }catch(Exception e){
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                    }

                    //Test the value of ActiveGuide, Reg_Date_Closed, and CntReg
                    System.out.println("ActiveGuide Value now is: " + ActiveGuide);
                    System.out.println("Reg_Date_Closed is: " + regDateClosed);
                    System.out.println("Cnt_Reg is: " + countReg);
                    // End adding by Rhagavi


                    if (!result.contains("error")) {
                        //result.getString(1) + " " + result.getString(2),
                        Toast.makeText(getApplicationContext(), "User Added", Toast.LENGTH_SHORT).show();

                        String queryUserID = "SELECT UserID, Password, BrandNumber \n" +
                                "FROM Users\n" +
                                "WHERE password = '" + password.getText() + "' AND BrandNumber = '" + brandId + "'";
                        dbh.login(sBrandLink, password.getText().toString());
                        ResultSet userResult = db.getDataTable(queryUserID);
                        try {
                            while (userResult.next()) {
                                user = userResult.getInt(1);
                                dbh.addUID(userResult.getInt(1));
                                passwordInfo = userResult.getString(2);
                            }
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                        }
                        
                        Intent intent = new Intent(Register.this, RegisterInfo.class);
                        intent.putExtra("userID", Integer.toString(user));
                        intent.putExtra("brandNumber", Integer.toString(brandId));
                        intent.putExtra("password", passwordInfo);

                        //Added by Rhagavi to save the values to the database
                        internalData.savePreferencesValue(getApplicationContext(),"password",passwordInfo);
                        internalData.savePreferencesValue(getApplicationContext(),"brandNumber", Integer.toString(brandId));
                        internalData.savePreferencesValue(getApplicationContext(), "userID", Integer.toString(user));

                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Error Registering", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean doesBrandExist(String brand)
    {
        // query database to see if a result is retured from searching brand number/id
        //Note: Cnt_Req will be tested at the same to know the original room
        String queryBrandId = "select brandId, Cnt_Reg from brand where brandLink = '" + brand + "'";
        ResultSet userResult = db.getDataTable(queryBrandId);
        //Added by Rhagavi for testing purpose
        System.out.println("Printing out the user results: ");
        System.out.println(userResult);

        try {
            if (userResult.next()) {
                brandId = userResult.getInt(1);
                countReg = userResult.getInt(2);
                System.out.println("Original Cnt_Reg count: " + countReg);

                //Now implementing the increment of Cnt_Reg
                String updateCountReg = "UPDATE brand\n"+
                                        "SET Cnt_Reg = Cnt_Reg + 1 \n"+
                                        "WHERE brandLink = '" + brand + "'";
                db.executeNonQuery(updateCountReg);
                //Let's test it out to see if updating the values worked
                String queryCountReg = "select Cnt_Reg from brand where brandLink = '" + brand + "'";
                ResultSet updateCntRegResult = db.getDataTable(queryCountReg);
                try{
                    if(updateCntRegResult.next()){
                        countReg = updateCntRegResult.getInt(1);
                        System.out.println("Updated!! New Cnt_Reg is: " + countReg);
                    }
                    else{
                        System.out.println("Unable to get the updated Cnt_Reg");
                    }
                }catch(Exception e){
                    System.out.println("Catch. Error!!");
                }

                return true;
            }
            else
                return false;

        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            System.out.println(e);
            return false;
        }
    }
    //Need to get this thing working
    //Add private boolean for registration date BETTER WORK OR ELSE
    private boolean validRegDate(String BrandLink, String password){
        //Added by Rhagavi
        //Let's find  Reg_Date_Closed
        String queryActiveGuide2 = "SELECT Reg_Date_Closed \n" +
                "FROM brand \n" +
                "WHERE BrandId = '" + brandId + "'";
        dbh.login(BrandLink, password);
        ResultSet activeGuideResult2 = db.getDataTable(queryActiveGuide2);
        try{
            while(activeGuideResult2.next()){
                regDateClosed = activeGuideResult2.getDate(1);
            }
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
        }

        //Test the value of ActiveGuide, Reg_Date_Closed, and CntReg
        System.out.println("Reg_Date_Closed is: " + regDateClosed);
        // End adding by Rhagavi

        //Rhagavi: Let's start with the registration date
        System.out.println("Test date # 1: ");
        DateFormat dform = new SimpleDateFormat("yyyy-MM-dd");
        Date obj = new java.util.Date();
        System.out.println(dform.format(obj));
        //Check if the registration date is before today (current date)
        //If it is, return false. If not, return true
        if (regDateClosed.before(obj)){
            System.out.println("Registration for this guide is closed!!");
            return false;
        }
        else{
            return true;
        }
    }
    //add private boolean to check if the number of registrations exceeded
    private boolean numOfRegExceeded (String BrandLink, String password){
        //Added by Rhagavi
        //Let's find  Cnt_Reg and Cnt_Max
        String queryReg = "SELECT Cnt_Reg, Cnt_Max \n" +
                "FROM brand \n" +
                "WHERE BrandId = '" + brandId + "'";
        dbh.login(BrandLink, password);
        ResultSet RegResult = db.getDataTable(queryReg);
        try{
            while(RegResult.next()){
                countReg = RegResult.getInt(1);
                countMax = RegResult.getInt(2);
            }
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
        }

        //Test the value of ActiveGuide, Reg_Date_Closed, and CntReg
        System.out.println("Cnt_Reg is: " + countReg);
        System.out.println("Cnt_Max is: " + countMax);
        // End adding by Rhagavi

        //Let's do the comparison
        //Check if countReg exceeded countMax
        if((countReg) > countMax){
            System.out.println("Number of users exceeded");
            return false;
        }
        else{
            System.out.println("Not yet exceeded");
            return true;
        }
    }
}

