package com.NeuralNet.AzureCloud;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.teamcarl.prototype.R;

import java.sql.ResultSet;

public class EditUserInfo extends Activity {
    EditText editPassword, editFirstName, editLastName, editEmail, editPhone,
            editState, editCity, editStreet, editCountry;
    Button editUserReturnBtn, submitUserInfoBtn, helpButton;

    DataAccess db = new DataAccess();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user_info);

        AlertDialog.Builder b1 = new AlertDialog.Builder(EditUserInfo.this);
        b1.setTitle("Help");
        b1.setMessage("Here you will view your personal information. You are able to change" +
                "and update the information displayed if you choose. Just click on the text box" +
                "to make changes.");
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

        editUserReturnBtn = findViewById(R.id.editUserReturnBtn);
        submitUserInfoBtn = findViewById(R.id.submitUserInfoBtn);
        editPassword = findViewById(R.id.editPassword);
        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        editEmail = findViewById(R.id.editEmail);
        editPhone = findViewById(R.id.editPhone);
        editCountry = findViewById(R.id.editCountry);
        editState = findViewById(R.id.editState);
        editCity = findViewById(R.id.editCity);
        editStreet = findViewById(R.id.editStreet);
        helpButton = findViewById(R.id.helpBtn);

        //Return button
        editUserReturnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditUserInfo.this, AboutMe.class);
                startActivity(intent);
            }
        });

        //If user clicks submit button, check any new info entered
        submitUserInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitNewPassword();
                submitNewFirstName();
                submitNewLastName();
                submitNewEmail();
                submitNewPhoneNumber();
                submitNewCountry();
                submitNewState();
                submitNewCity();
                submitNewStreet();
                Toast.makeText(getApplicationContext(), "Submitted", Toast.LENGTH_SHORT).show();
            }
        });

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertdup.show();
            }
        });
    }

    public void submitNewPassword(){
        final String passwordInput = editPassword.getText().toString();

        if(!User.Password.equals(passwordInput) && !passwordInput.equals("")){
            String query =
                    ("UPDATE USERS SET Password = " + passwordInput + " WHERE userid = " + User.UserID);
            db.executeNonQuery(query);
        }
        System.out.println(passwordInput);

    }

    public void submitNewFirstName(){
        final String firstNameInput = editFirstName.getText().toString();

        if(!User.FirstName.equals(firstNameInput) && !firstNameInput.equals("")){
            String query =
                    ("UPDATE USERS SET FirstName = '" + firstNameInput + "' WHERE userid = " + User.UserID);
            db.executeNonQuery(query);
        }
        System.out.println(firstNameInput);

    }

    public void submitNewLastName(){
        final String lastNameInput = editLastName.getText().toString();

        if(!User.LastName.equals(lastNameInput) && !lastNameInput.equals("")){
            String query =
                    ("UPDATE USERS SET LastName = '" + lastNameInput + "' WHERE userid = " + User.UserID);
            db.executeNonQuery(query);
        }
        System.out.println(lastNameInput);
    }

    public void submitNewEmail() {
        final String emailInput = editEmail.getText().toString();

        if (!User.Email.equals(emailInput) && !emailInput.equals("")) {
            String query =
                    ("UPDATE USERS SET Email = '" + emailInput + "' WHERE userid = " + User.UserID);
            db.executeNonQuery(query);
        }
    }

    public void submitNewPhoneNumber(){
        final String phoneInput = editPhone.getText().toString();

        if(!User.PhoneNumber.equals(phoneInput) && !phoneInput.equals("")){
            String query =
                    ("UPDATE USERS SET PhoneNumber = " + phoneInput + " WHERE userid = " + User.UserID);
            db.executeNonQuery(query);
        }
    }

    public void submitNewCountry() {
        final String countryInput = editCountry.getText().toString();

        if (!User.Country.equals(countryInput) && !countryInput.equals("")) {
            String query =
                    ("UPDATE USERS SET Country = '" + countryInput + "' WHERE userid = " + User.UserID);
            db.executeNonQuery(query);
        }
    }

    public void submitNewState() {
        final String stateInput = editState.getText().toString();

        if (!User.State.equals(stateInput) && !stateInput.equals("")) {
            String query =
                    ("UPDATE USERS SET State = '" + stateInput + "' WHERE userid = " + User.UserID);
            db.executeNonQuery(query);
        }
    }

    public void submitNewCity() {
        final String cityInput = editCity.getText().toString();

        if (!User.City.equals(cityInput) && !cityInput.equals("")) {
            String query =
                    ("UPDATE USERS SET City = '" + cityInput + "' WHERE userid = " + User.UserID);
            db.executeNonQuery(query);
        }
    }

    public void submitNewStreet() {
        final String streetInput = editStreet.getText().toString();

        if (!User.Street.equals(streetInput) && !streetInput.equals("")) {
            String query =
                    ("UPDATE USERS SET StreetAddress = '" + streetInput + "' WHERE userid = " + User.UserID);
            db.executeNonQuery(query);
        }
    }
}
