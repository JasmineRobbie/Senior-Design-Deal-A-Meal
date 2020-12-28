package com.NeuralNet.AzureCloud;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.teamcarl.prototype.R;

import java.sql.ResultSet;

public class Final_Referal extends Activity {
    ImageButton newMessageBtn, sendresponsebutton, acceptRefferal;
    User user;
    String url;
    InternalDataAccess ida = new InternalDataAccess();
    TextView brandNameText, activeInfoText;
    //Added by Rhagavi
    int ActiveGuide;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final__referal);
        setTitle("A.I. Life Improvement");
        // get data from previous screen
        Intent intent = getIntent();
        user = (User) intent.getExtras().getSerializable("User");
        url = intent.getExtras().getString("Url");

        //Let's implement the User & Active Info
        DataAccess db = new DataAccess();
        //Testing
        System.out.println("user.BrandName: " + user.BrandName);
        System.out.println("user.BrandLink: " + user.BrandLink);
        System.out.println("user.BrandNumber: "+ user.BrandNumber);
        System.out.println("user.UserID: " + user.UserID);
        //Added to figure out the Active Guide value
        String queryActiveGuide = "SELECT ActiveGuide \n" +
                "FROM brand \n" +
                "WHERE BrandId = '" + user.BrandNumber + "'";
        ResultSet activeGuideResult = db.getDataTable(queryActiveGuide);
        try{
            while(activeGuideResult.next()){
                ActiveGuide = activeGuideResult.getInt(1);
            }
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), "error to calculate for ActiveGuide #", Toast.LENGTH_SHORT).show();
        }
        System.out.println("ActiveGuide #: " + ActiveGuide);
        //end of finding out for ActiveGuide

        //Let's add the Active Guide info onto the Guide Evaluation
        activeInfoText = (TextView)findViewById(R.id.ActiveGuideText);
        activeInfoText.setText("User ID: "+ user.UserID +
                "\n " + "BrandLink: " + user.BrandLink + "\n" +
                "Brand Number: " + user.BrandNumber + "\n" +
                "Brand Name: " + user.BrandName + "\n" +
                "Active Guide #: " + ActiveGuide);
        //end of implementing info for Active Guide and User


        TextView link = (TextView)findViewById(R.id.textView5);
        link.setText("please visit " + url + " by pressing the accept referal button in the top right");
        // on new message button click
        brandNameText = (TextView)findViewById(R.id.brandName);
        brandNameText.setText(user.BrandName);
        newMessageBtn=(ImageButton)findViewById(R.id.mainMenu);
        newMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Final_Referal.this, Activemenu.class);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });
        sendresponsebutton=(ImageButton)findViewById(R.id.sendresponse);
        sendresponsebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnectedOnline()) {
                    String guideName = ida.getSharedPreferencesKeyValue(getApplicationContext(), "active_guide_name");
                    String guideId = ida.getSharedPreferencesKeyValue(getApplicationContext(), "active_guide_id");
                    String guideEditor = ida.getSharedPreferencesKeyValue(getApplicationContext(), "active_guide_editor");

                    Intent intent = new Intent(Final_Referal.this, NewMessage.class);
                    String msgSubject = "Guide: " + guideName + " (#" + guideId + ")";
                    intent.putExtra("MessageInfoCurrentUser", user);
                    intent.putExtra("MessageInfoReceiverUser", guideEditor);
                    intent.putExtra("MessageInfoSubject", msgSubject);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Cannot send message at this time. You must be online to send messages.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        acceptRefferal=(ImageButton)findViewById(R.id.acceptReferral);
        acceptRefferal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent (Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });
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
