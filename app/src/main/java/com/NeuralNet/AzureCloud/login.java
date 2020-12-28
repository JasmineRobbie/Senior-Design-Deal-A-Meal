package com.NeuralNet.AzureCloud;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

//import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.teamcarl.prototype.R;

import java.io.Serializable;
import java.sql.ResultSet;
import java.util.Random;

public class login extends Activity implements Serializable{

    ImageButton bLogin, bRegister;
    EditText username, password;
    boolean successfulLogin = false;
    InternalDataAccess internalData = new InternalDataAccess();
    DataAccess db = new DataAccess();  // bolded by Carl in the recent email 11/25
    User user = null;
    String Quickuser, Quickkpass; // bolded by Carl in the recent email 11/25
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private String getRandomPassword() {
        String password = "";
        while(password.length() == 0) {
            Random generator = new Random();
            StringBuilder randomStringBuilder = new StringBuilder();
            int randomLength = generator.nextInt(5);
            char tempChar;
            for (int i = 0; i < randomLength; i++) {
                tempChar = (char) (generator.nextInt(96) + 32);
                randomStringBuilder.append(tempChar);
            }
            password = randomStringBuilder.toString().replace(" ", "");
        }
        return password;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        DataAccess db = new DataAccess();
        bLogin = (ImageButton) findViewById(R.id.btnLogin);
        bRegister = (ImageButton) findViewById(R.id.btnRegister);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);


        //username.setText(internalData.getSharedPreferencesKeyValue(getApplicationContext(), "username"));
        //password.setText(internalData.getSharedPreferencesKeyValue(getApplicationContext(), "password"));

        Quickuser = internalData.getSharedPreferencesKeyValue(getApplicationContext(), "username"); //bolded by Carl on 11/25
        Quickkpass = internalData.getSharedPreferencesKeyValue(getApplicationContext(), "password"); //bolded by Carl on 11/25

        // user already logged into the system (has preferences values)
        /*if (!username.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
            // log user in (using saved file data)
            // get user data
            user = new User(username.getText().toString(), getApplicationContext());
            Intent intent = new Intent(login.this, Activemenu.class);
            intent.putExtra("User", user);
            try {
                startActivity(intent);
            } catch (Exception e) {
                Log.e("error in login act", e.getMessage());
            }
        }*/
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //Added by Rhagavi for testing purpose
                System.out.println("Clicked on bLogin");
                //bLogin -> Entry Logon button
                if (isConnectedOnline()) {

                    if (username.getText().toString().trim().equals("") || password.getText().toString().trim().equals(""))
                        successfulLogin = false;
                    else {
                        // sets successful login value
                        doesUserExist(username.getText().toString(), password.getText().toString());
                    }
                    // display message to user of login results
                    Toast.makeText(getApplicationContext(), successfulLogin ? "Successfully Logged In!" : "Please enter in a valid user name.", Toast.LENGTH_SHORT).show();

                    // if login was successful, load main menu
                    if (successfulLogin) {
                        // save key values to preferences
                        if(internalData.getSharedPreferencesKeyValue(getApplicationContext(), "password").isEmpty()) {
                            internalData.savePreferencesValue(getApplicationContext(), "password", user.Password);
                            internalData.savePreferencesValue(getApplicationContext(), "brand_number", user.BrandNumber);
                            internalData.savePreferencesValue(getApplicationContext(), "brand_link", user.BrandLink);
                            internalData.savePreferencesValue(getApplicationContext(), "brand_name", user.BrandName);
                            internalData.savePreferencesValue(getApplicationContext(), "username", user.UserID);
                        }
                        // launch main menu
                        Intent intent = new Intent(login.this, Activemenu.class);
                        intent.putExtra("User", user);
                        try {
                            startActivity(intent);
                        } catch (Exception e) {
                            Log.e("error in login act", e.getMessage());
                        }
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please connect to internet to login. ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                //Added by Rhagavi for testing purpose
                System.out.println("Clicked on bRegister");
                // bRegister -> Express Logon button
                Quickuser = internalData.getSharedPreferencesKeyValue(getApplicationContext(), "username");
                Quickkpass = internalData.getSharedPreferencesKeyValue(getApplicationContext(), "password");
                        if (!Quickuser.isEmpty() && !Quickkpass.toString().isEmpty()) {
            // log user in (using saved file data)
            // get user data
            user = new User(Quickuser, getApplicationContext());
            Intent intent = new Intent(login.this, Activemenu.class);
            intent.putExtra("User", user);
            try {
                startActivity(intent);
            } catch (Exception e) {
                Log.e("error in login act", e.getMessage());
            }
        }
                else {
                            Toast.makeText(getApplicationContext(), "Login once to use express. ", Toast.LENGTH_SHORT).show();
                        }
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("login Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private void doesUserExist(String username, String password)
    {
        try {
            // Change below query according to your own database.
            String query = "select * from users where UserId = '" + username + "' and password = '" + password + "' ";
            ResultSet queryResult = db.getDataTable(query);
            if (queryResult != null && queryResult.next()) {
                successfulLogin = true;

                // create new user object
                user = new User(username, getApplicationContext());
                user.Password = password;

            } else {
                successfulLogin = false;
            }

        } catch (Exception ex) {
            successfulLogin = false;
        }
    }
    public boolean isConnectedOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        } else
            return false;
    }
}
