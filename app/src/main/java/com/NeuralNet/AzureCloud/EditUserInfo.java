package com.NeuralNet.AzureCloud;
import android.app.Activity;
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
    String temp;
    //TextView editPhone;
    EditText editPhone;
    DataAccess db = new DataAccess();

    Button editUserReturnBtn, submitUserInfoBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user_info);

        editUserReturnBtn = findViewById(R.id.editUserReturnBtn);
        submitUserInfoBtn = findViewById(R.id.submitUserInfoBtn);
        editPhone = findViewById(R.id.editPhone);

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
                final String phoneInput = editPhone.getText().toString();

                if(User.PhoneNumber != (AboutMe.aboutMePhone_input.toString())){
                    String query =
                            ("UPDATE USERS SET PhoneNumber = " + phoneInput + " WHERE userid = " + User.UserID);
                    db.executeNonQuery(query);

                    temp = phoneInput;

                    System.out.println("NUMBER IS: " + phoneInput);
                    System.out.println("NUMBER SET IS: " + temp);

                    Toast.makeText(getApplicationContext(), "Submitted", Toast.LENGTH_SHORT).show();
                }
                AboutMe.aboutMePhone_input.setText(temp);

            }
        });
    }
}
