package com.NeuralNet.AzureCloud;

import android.app.Activity;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.teamcarl.prototype.R;

public class RegisterInfo extends Activity {

    TextView userID, brandNumber, password, rInfoBD, uIDBD;
    String us, br, pa;
    ImageButton login;
    int countBD = 0;
    //Added by Rhagavi
    InternalDataAccess internalData = new InternalDataAccess();
    DataAccess db = new DataAccess();
    User user = null;
    String QuickuserID, Quickkpass, QuickbrandID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_info);
        login = (ImageButton) findViewById(R.id.login); // from login.java
        userID = (TextView) findViewById(R.id.userIDActual);
        brandNumber = (TextView) findViewById(R.id.brandNumberActual);
        password = (TextView) findViewById(R.id.passwordActual);

        //Rhagavi: using these following three lines for testing purpose
        us = (String) intent.getExtras().getString("userID"); // userID
        br = (String) intent.getExtras().getString("brandNumber"); // brandnumber
        pa = (String) intent.getExtras().getString("password"); //password
        Toast.makeText(getApplicationContext(), us+" "+br+" "+pa, Toast.LENGTH_SHORT).show();

        //comment for now
        //userID.setText(us);
        //brandNumber.setText(br);
        //password.setText(pa);

        //Added by Rhagavi for testing purpose through run
        System.out.println("password: " + pa);
        System.out.println("brandNumber: " + br);
        System.out.println("userID: " + us);

        //Added by Rhagavi which the shared preferences key value is saved from register.java
        QuickuserID = internalData.getSharedPreferencesKeyValue(getApplicationContext(), "userID");
        Quickkpass = internalData.getSharedPreferencesKeyValue(getApplicationContext(), "password");
        QuickbrandID = internalData.getSharedPreferencesKeyValue(getApplicationContext(), "brandNumber");

        // Added by Rhagavi for testing purpose through run
        System.out.println("Quickkpass: " + Quickkpass);
        System.out.println("QuickbrandID: " + QuickbrandID);
        System.out.println("QuickuserID: " + QuickuserID);

        //Added by Rhagavi to set these values from the shared preferences key value
        userID.setText(QuickuserID);
        brandNumber.setText(QuickbrandID);
        password.setText(Quickkpass);

        //BACKDOOR
        rInfoBD = (TextView) findViewById(R.id.RegisterInfo);
        uIDBD = (TextView) findViewById(R.id.userID);
        //register info
        rInfoBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countBD++;
            }
        });
        //user ID
        uIDBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(countBD > 5)
                {
                    Intent intent = new Intent(RegisterInfo.this, Register.class);
                    startActivity(intent);
                }
            }
        });
        //login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterInfo.this, login.class);
                startActivity(intent);
            }
        });
    }
}
