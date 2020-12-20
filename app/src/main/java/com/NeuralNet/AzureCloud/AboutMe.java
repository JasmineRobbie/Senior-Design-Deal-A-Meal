package com.NeuralNet.AzureCloud;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.teamcarl.prototype.R;

public class AboutMe extends Activity {
    public static TextView aboutMeUserID_input, aboutMePassword_input, aboutMeFirstName_input, aboutMeLastName_input,
            aboutMeEmail_input, aboutMePhone_input, aboutMeState_input, aboutMeCity_input, aboutMeStreet_input, aboutMeCountry_input;

    Button aboutMeReturnBtn, aboutMeEditInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_me);

        aboutMeUserID_input = findViewById(R.id.aboutMeUserID_input);
        aboutMePassword_input = findViewById(R.id.aboutMePassword_input);
        aboutMeFirstName_input = findViewById(R.id.aboutMeFirstName_input);
        aboutMeLastName_input = findViewById(R.id.aboutMeLastName_input);
        aboutMeEmail_input = findViewById(R.id.aboutMeEmail_input);
        aboutMeReturnBtn = findViewById(R.id.aboutMeReturnBtn);
        aboutMeEditInfo = findViewById(R.id.aboutMeEditInfo);
        aboutMePhone_input = findViewById(R.id.aboutMePhone_input);
        aboutMeCountry_input = findViewById(R.id.aboutMeCountry_input);
        aboutMeState_input = findViewById(R.id.aboutMeState_input);
        aboutMeCity_input = findViewById(R.id.aboutMeCity_input);
        aboutMeStreet_input = findViewById(R.id.aboutMeStreet_input);

        //Return button
        aboutMeReturnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AboutMe.this, UserProfile.class);
                startActivity(intent);
            }
        });

        //Edit info button
        aboutMeEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AboutMe.this, EditUserInfo.class);
                startActivity(intent);
            }
        });

        //Accessing static variables from user.java
        if(User.UserID.isEmpty()){
            aboutMeUserID_input.setText("N/A");
        }
        else{
            aboutMeUserID_input.setText(User.UserID);
        }

        if (User.Password.isEmpty()){
            aboutMePassword_input.setText("N/A");
        }
        else{
            aboutMePassword_input.setText(User.Password);
        }

        if(User.FirstName.isEmpty()){
            aboutMeFirstName_input.setText("N/A");
        }
        else{
            aboutMeFirstName_input.setText(User.FirstName);
        }

        if(User.LastName.isEmpty()){
            aboutMeLastName_input.setText("N/A");
        }
        else{
            aboutMeLastName_input.setText(User.LastName);
        }

        if(User.Email.isEmpty()){
            aboutMeEmail_input.setText("N/A");
        }
        else{
            aboutMeEmail_input.setText(User.Email);
        }

       if(User.PhoneNumber.isEmpty()){
            aboutMePhone_input.setText("N/A");
        }
        else{
            aboutMePhone_input.setText(User.PhoneNumber);
        }

        if(User.Country.isEmpty()){
            aboutMeCountry_input.setText("N/A");
        }
        else{
            aboutMeCountry_input.setText(User.Country);
        }

        if(User.State.isEmpty()){
            aboutMeState_input.setText("N/A");
        }
        else{
            aboutMeState_input.setText(User.State);
        }

        if(User.City.isEmpty()){
            aboutMeCity_input.setText("N/A");
        }
        else{
            aboutMeCity_input.setText(User.City);
        }

        if(User.Street.isEmpty()){
            aboutMeStreet_input.setText("N/A");
        }
        else{
            aboutMeStreet_input.setText(User.Street);
        }
    }
}
