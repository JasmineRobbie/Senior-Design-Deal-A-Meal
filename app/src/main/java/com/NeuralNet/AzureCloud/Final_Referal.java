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

public class Final_Referal extends Activity {
    ImageButton newMessageBtn, sendresponsebutton, acceptRefferal;
    User user;
    String url;
    InternalDataAccess ida = new InternalDataAccess();
    TextView brandNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final__referal);
        setTitle("A.I. Life Improvement");
        // get data from previous screen
        Intent intent = getIntent();
        user = (User) intent.getExtras().getSerializable("User");
        url = intent.getExtras().getString("Url");

        TextView link = (TextView)findViewById(R.id.textView5);
        link.setText("please visit " + url + " by pressing the accept referal button in the top right");
        // on new message button click
        brandNameText = (TextView)findViewById(R.id.brandName);
        brandNameText.setText(user.BrandName);
        newMessageBtn=(ImageButton)findViewById(R.id.mainMenu);
        newMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Final_Referal.this, MainMenu.class);
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
