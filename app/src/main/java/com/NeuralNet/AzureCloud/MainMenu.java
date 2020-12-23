package com.NeuralNet.AzureCloud;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.teamcarl.prototype.R;

import java.io.FileInputStream;
import java.io.Serializable;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MainMenu extends Activity implements Serializable {

    List<String> guides = new ArrayList<>();
    List<String> guideIds = new ArrayList<>();
    List<Integer> switches = new ArrayList<>();
    int rowCount, columnCount;
    ListView listView;
    boolean connected;
    ImageButton btn_Messages;
    User user;
    String guideName;
    String guideId;
    int ActiveGuide; // added to figure out the ActiveGuide
    String guidePurpose; //added to try to pass the purpose of the guide
    InternalDataAccess internalData = new InternalDataAccess(); //added to update guide & purpose
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
        connected = false;
        try {
            user = (User) intent.getExtras().getSerializable("User");

        } catch (Exception e) {

        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // sets data for guidename and guide id lists to populate UI.
        System.out.println("afdasdf");
        getGuideLists();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.listview, guides);
        listView = (ListView) findViewById(R.id.guideList);
        listView.setAdapter(adapter);

        // on guide click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // ListView Clicked item index
                int itemPosition = position;
                Intent intent = new Intent(MainMenu.this, Purpose.class);
                guideName = guides.get(position);
                guideId = guideIds.get(position);
                //Testing to check if it displays the Active Guide, Brand Number, Brand link and User Id
                System.out.println("User Id: " + user.UserID);
                System.out.println("Brand Number: " + user.BrandNumber);
                System.out.println("Brand Link: " + user.BrandLink);
                System.out.println("Brand Name: " + user.BrandName);
                System.out.println("guideId: " + guideId);
                System.out.println("guideName: " + guideName);
                DataAccess db = new DataAccess();
                // Now let's find the Guide Purpose
                String queryPurposeGuide = "SELECT Purpose \n" +
                        "FROM Guide \n" +
                        "WHERE GuideID = '" + guideId + "'";
                ResultSet purposeGuideResult = db.getDataTable(queryPurposeGuide);
                try{
                    while(purposeGuideResult.next()){
                        guidePurpose = purposeGuideResult.getString(1);
                    }
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(), "error to get the Guide Purpose", Toast.LENGTH_SHORT).show();
                }
                System.out.println("Guide Purpose: " + guidePurpose);
                //end of finding out for Guide Purpose
                //Let's try to find the ActiveGuide Value
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
                //end of finding ActiveGuide Value

                //Let's try going back to the Activemenu page without crashing
                //Before that, let's save the guide info (and the purpose) using internal data
                internalData.savePreferencesValue(getApplicationContext(),"active_guide_purpose",guidePurpose);
                internalData.savePreferencesValue(getApplicationContext(),"active_guide_title", guideName);
                internalData.savePreferencesValue(getApplicationContext(), "active_guide_id", guideId);
                Intent returnHome = new Intent(MainMenu.this, Activemenu.class);
                startActivity(returnHome);

                //The following lines are commented for now which deals with the survey page
                /*
                if(switches.get(position) == 1)
                {
                    Intent montIntent = new Intent(MainMenu.this, SurveyPage.class);
                    montIntent.putExtra("guide_id", Integer.decode(guideId));
                    System.out.println("offtodasurvey");
                    InternalDataAccess ida = new InternalDataAccess();
                    ida.savePreferencesValue(getApplicationContext(), "active_guide_id", guideId);
                    startActivity(montIntent);
                }
                else {
                    intent.putExtra("guideName", guideName);
                    intent.putExtra("guideId", guideId);
                    intent.putExtra("User", user);
                    //Adding
                    //intent.putExtra("");
                    startActivity(intent);
                }
                */
            }
        });

        btn_Messages = (ImageButton) findViewById(R.id.Messages);
        btn_Messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connectionTest()) {
                    Intent intent = new Intent(MainMenu.this, Messages.class);
                    intent.putExtra("User", user);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Cannot check messages at this time. You must be online to check messages.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public boolean connectionTest() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        } else
            return false;

    }

    public void getGuideLists() {
        // if user is online/connected to internet
        if (connectionTest()) {
            System.out.println("user.UserID: " + user.UserID);
            System.out.println("BrandNumber: " + user.BrandNumber);
            DataAccess db = new DataAccess();
            String query =
                    "SELECT g.GuideName, g.GuideId, g.Switch \n" + // query gets guides from brand that user is in
                            "FROM Guide g\n" +
                            "JOIN USERS u ON u.BrandNumber = g.BrandNumber\n" +
                            "AND u.UserId = '" + user.UserID + "'\n" +
                            "AND u.BrandNumber = '" + user.BrandNumber + "'\n" ;
            ResultSet result = db.getDataTable(query);
            if (result != null) {
                try {
                    while (result.next()) // loop through each row (first row is always empty/null, so we do next() right away)
                    {
                        System.out.println( "result innot false");
                        guides.add(result.getString(1));
                        guideIds.add(result.getString(2));
                        if(result.getInt(3) == 0)
                            switches.add(0);
                        else
                            switches.add(1);
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error getting online Guides", Toast.LENGTH_SHORT).show();
                }

            }
        } else {
            //reads guide file
            try {
                System.out.println("result falso");
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
                    guides.add(guideData[i][0]);
                    guideIds.add(guideData[i][1]);
                }

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error getting offline Guides", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String[][] fileStringDataToArray(String fileData) {
        String[][] dataTableArray = null;

        if(fileData != null && fileData.length() > 0) {
            String[] rows = fileData.split(Pattern.quote("\r\n"));
            String[] columns = rows[0].split(Pattern.quote("|"));

            columnCount = columns.length;
            rowCount = rows.length;

            dataTableArray = new String[rows.length][8]; // we will always use 8 rows to store guide data unless active menu query changes.

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
}
