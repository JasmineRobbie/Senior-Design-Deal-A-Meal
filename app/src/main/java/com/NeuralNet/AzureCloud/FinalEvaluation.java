package com.NeuralNet.AzureCloud;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.teamcarl.prototype.R;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FinalEvaluation extends Activity {

    ImageButton newMessageBtn, sendresponsebutton, urllink;
    User user;
    String referralText = "";
    TextView referralText_UI, brandNameText, activeInfoText;
    int referralType = 0;
    InternalDataAccess ida = new InternalDataAccess();
    //Added by Rhagavi
    int ActiveGuide;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DataAccess db = new DataAccess();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_evaluation);
        // get data from previous screen
        Intent intent = getIntent();
        user = (User) intent.getExtras().getSerializable("User");
        // set brand name
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

        brandNameText = (TextView)findViewById(R.id.brandName);
        brandNameText.setText(user.BrandName);
        if(isConnectedOnline()) {
            // update database with latest offline file answers
            if (getApplicationContext().getFileStreamPath("offline_history.txt").exists()) // if offline history exists
                ida.updateOfflineAnswers(getApplicationContext());

            // GET RANDOM EVALUTION REFERRAL
            try {
                // Change below query according to your own database.
                String query = "exec pEvaluationReferral '" + user.UserID + "'";
                ResultSet queryResult = db.getDataTable(query);
                try {
                    if (queryResult != null && queryResult.next()) {
                        referralText = queryResult.getString(1);
                    }
                } catch (Exception e) {

                }

                if (referralText != null && referralText.length() > 0) {
                    // processes referral
                    // if valid URL

                    int referralInt = 0;
                    try {
                        referralInt = Integer.parseInt(referralText);
                    } catch (Exception e) {
                    }
                    if (URLUtil.isValidUrl(referralText)) {
                        urllink = (ImageButton) findViewById(R.id.urllink);
                        urllink.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(referralText));
                                startActivity(browserIntent);
                            }
                        });
                    }
                    // else is an integer, refer user to guide
                    else if (referralInt > 0) {
                        // insert history of a referral guide
                        insertGuideHistory(user.UserID, referralText);
                        // button to guide purpose (referred guide from evaluation)
                        urllink = (ImageButton) findViewById(R.id.urllink);
                        urllink.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(FinalEvaluation.this, Purpose.class);
                                intent.putExtra("guideId", referralText);
                                intent.putExtra("User", user);
                                startActivity(intent);
                            }
                        });
                    }
                    // DISPLAY REFERRAL TEXT
                    else {
                        // this is already done in the next line of code, but this case is here in case it is used for other purposes in future.
                    }
                    // sets referral text to the database string returned from pEvaluationReferral...
                    referralText_UI = (TextView) findViewById(R.id.referralText);
                    referralText_UI.setText(referralText);
                } else {
                }

            } catch (Exception ex) {

            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Cannot evaluate at this time. You must be online to evaluate.", Toast.LENGTH_SHORT).show();
        }
        // on new message button click
        newMessageBtn=(ImageButton)findViewById(R.id.mainMenu);
        newMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinalEvaluation.this, Activemenu.class);
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

                    Intent intent = new Intent(FinalEvaluation.this, NewMessage.class);
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


        //btnNewMsg=(Button)findViewById(R.id.btnNewMessage);
        //btnNewMsg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(FinalEvaluation.this, NewMessage.class);
//                //intent.putExtra("User", user);
//                //intent.putExtra("Question", question);
//                startActivity(intent);
//            }
//        });
    }

    public void insertGuideHistory(String userId, String guideId)
    {
        System.out.println("Inserting into GuideHistory in Final Evaluation!!");

        DataAccess db = new DataAccess();
        String query = "IF NOT EXISTS (\n" +
                "\tSELECT * \n" +
                "\tFROM GUIDEHISTORY\n" +
                "\twhere UserId = '" + userId+ "'\n" +
                "\tand guideId = " + guideId + "\n" +
                ")\n" +
                "BEGIN\n" +
                "INSERT INTO GUIDEHISTORY\n" +
                "(GUIDEID, USERID)\n" +
                "VALUES('" + guideId + "','" + userId + "')" +
                "END";

        db.executeNonQuery(query);
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
