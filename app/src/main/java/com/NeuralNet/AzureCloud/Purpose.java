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

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.teamcarl.prototype.R;

import java.io.FileInputStream;
import java.sql.ResultSet;
import java.util.Random;
import java.util.regex.Pattern;

public class Purpose extends Activity {
    ImageButton questions, evaluate, newguide, sendmessage;
    User user;
    String guideId, guideEditor;
    int rowCount, columnCount, evaluation;
    String guidePurpose, guideName;
    TextView purposeText, brandNameText;
    Random r = new Random();

    DataAccess db = new DataAccess();
    InternalDataAccess ida = new InternalDataAccess();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purpose);
        //setTitle("A.I. Life Improvement");
        Intent intent = getIntent();
        user = (User) intent.getExtras().getSerializable("User");
        guideId = intent.getExtras().getString("guideId");

        // update guide history if guide never accessed before.
        insertGuideHistory(user.UserID, guideId);

        // get guide details
        getGuideInfo(guideId);

        // set UI objects
        purposeText = (TextView) findViewById(R.id.purposeText);
        purposeText.setText(guidePurpose);
        brandNameText = (TextView) findViewById(R.id.brandName);
        brandNameText.setText(user.BrandName);

        questions = (ImageButton) findViewById(R.id.questionsButton);
        evaluate = (ImageButton) findViewById(R.id.evaluateButton);
        newguide = (ImageButton) findViewById(R.id.newguideButton);
        sendmessage = (ImageButton) findViewById(R.id.sendmessageButton);

        // button click functions
        evaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                evaluation = r.nextInt(6);
                switch(evaluation){
                    case 0:
                        intent = new Intent(Purpose.this, FinalEvaluation.class);
                        intent.putExtra("User", user);
                        startActivity(intent);
                    break;
                    case 1:
                        intent = new Intent(Purpose.this, FinalEvaluation.class);
                        intent.putExtra("User", user);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(Purpose.this, FinalEvaluation.class);
                        intent.putExtra("User", user);
                        startActivity(intent);
                        break;
                    case 3:
                        String url1 = "http://www.google.com";
                        intent = new Intent(Purpose.this, Final_Referal.class);

                        intent.putExtra("Url", url1);
                        intent.putExtra("User", user);
                        startActivity(intent);
                        break;
                    case 4:
                        String url2 = "http://www.thisisnottom.com";
                        intent = new Intent(Purpose.this, Final_Referal.class);
                        intent.putExtra("Url", url2);
                        intent.putExtra("User", user);
                        startActivity(intent);
                        break;
                    case 5:
                        String url3 = "https://www.youtube.com/watch?v=9-3OCc5g5oE";
                        intent = new Intent(Purpose.this, Final_Referal.class);
                        intent.putExtra("Url", url3);
                        intent.putExtra("User", user);
                        startActivity(intent);
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "no evaluation ready", Toast.LENGTH_SHORT).show();
                        break;
                }


               /* intent = new Intent(Purpose.this, FinalEvaluation.class);
                intent.putExtra("User", user);
                startActivity(intent);
                */
            }
        });
        newguide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Purpose.this, MainMenu.class);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });
        sendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnectedOnline()) {
                    Intent intent = new Intent(Purpose.this, NewMessage.class);
                    String msgSubject = "Guide: " + guideName + " (#" + guideId + ")";
                    intent.putExtra("MessageInfoCurrentUser", user);
                    intent.putExtra("MessageInfoReceiverUser", guideEditor);
                    intent.putExtra("MessageInfoSubject", msgSubject);
                    startActivity(intent);
                }
            else {
                    Toast.makeText(getApplicationContext(), "Cannot send message at this time. You must be online to send messages.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        questions.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // ListView Clicked item index
                if(guideId == "0")
                {
                    Toast.makeText(getApplicationContext(), "No questions for this guide. Guide ID 0! ", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(Purpose.this, Guide.class);
                    intent.putExtra("guideId", guideId);
                    intent.putExtra("User", user);
                    startActivity(intent);
                }
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Purpose Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.NeuralNet.AzureCloud/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Purpose Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.NeuralNet.AzureCloud/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    public void insertGuideHistory(String userId, String guideId)
    {
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

        String result = db.executeNonQuery(query);
        // if no errors during database query
        if (!result.contains("error")) {
            // saved history, do nothing
        }
    }

    public void getGuideInfo(String guideId) {
        if (isConnectedOnline()) {
            String sql = "SELECT Purpose, GuideName, guideeditor# FROM GUIDE WHERE GUIDEID = " + guideId;
            ResultSet queryResult = db.getDataTable(sql);
            try {
                if (queryResult != null && queryResult.next()) {
                    guidePurpose = queryResult.getString(1);
                    guideName = queryResult.getString(2);
                    guideEditor = queryResult.getString(3);
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Could not get guide info.", Toast.LENGTH_SHORT).show();
            }
        }
        else // offline mode
        {
            //reads guide file
            try {
                String[][] guideData = null;
                String guideText = "";
                FileInputStream fis1 = openFileInput("guideFile.txt");
                int size = fis1.available();
                byte[] buffer = new byte[size];
                fis1.read(buffer);
                fis1.close();
                guideText = new String(buffer);

                guideData = fileStringDataToArray(guideText);

                for(int i = 0; i < rowCount; i++)
                {
                    String guideIdInRow = guideData[i][1];
                    if(guideId.equals(guideIdInRow))
                    {
                        guideName = guideData[i][0];
                        guidePurpose = guideData[i][6];
                        guideEditor = guideData[i][7];
                    }
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error getting offline Guides", Toast.LENGTH_SHORT).show();
            }
        }

        // setting current guide as the active guide, save guide info
        saveInternalGuideInfo(guideId);
    }
    public void saveInternalGuideInfo(String guideId)
    {
        ida.savePreferencesValue(getApplicationContext(), "active_guide_id", guideId);
        ida.savePreferencesValue(getApplicationContext(), "active_guide_name", guideName);
        ida.savePreferencesValue(getApplicationContext(), "active_guide_purpose", guidePurpose);
        ida.savePreferencesValue(getApplicationContext(), "active_guide_editor", guideEditor);
    }

    public String[][] fileStringDataToArray(String fileData) {
        String[][] dataTableArray = null;

        if(fileData != null && fileData.length() > 0) {
            String[] rows = fileData.split(Pattern.quote("\r\n"));
            String[] columns = rows[0].split(Pattern.quote("|"));

            columnCount = columns.length;
            rowCount = rows.length;
            dataTableArray = new String[rows.length][columns.length];

            for(int i = 0; i < rows.length; i++)
            {
                columns = rows[i].split(Pattern.quote("|"));
                for(int j = 0; j < columns.length; j++)
                {
                    dataTableArray[i][j] = columns[j];
                }
            }
        }
        return dataTableArray;
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
