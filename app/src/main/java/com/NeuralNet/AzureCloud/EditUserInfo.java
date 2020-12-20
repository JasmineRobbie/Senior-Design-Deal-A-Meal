package com.NeuralNet.AzureCloud;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.teamcarl.prototype.R;

import java.sql.ResultSet;

public class EditUserInfo extends Activity {
    TextView editPhone;
    DataAccess db = new DataAccess();

    Button editUserReturnBtn, submitUserInfoBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user_info);

        editUserReturnBtn = findViewById(R.id.editUserReturnBtn);
        submitUserInfoBtn = findViewById(R.id.submitUserInfoBtn);
        //testing

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
                if(User.PhoneNumber != (AboutMe.aboutMePhone_input.toString())){
                    String query =
                            ("UPDATE USERS SET PhoneNumber = " + AboutMe.aboutMePhone_input + " WHERE userid = " + User.UserID);
                    db.executeNonQuery(query);
                }
            }
        });
    }
}
