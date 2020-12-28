package com.NeuralNet.AzureCloud;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.teamcarl.prototype.R;

import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Activemenu extends Activity {
    ImageButton exit, useactive, newguide, checkmemos, weeklygoals;
    boolean connected;
    User user;
    String activeGuideId;
    TextView brandName, guideTitle, guidePurpose;
    InternalDataAccess ida = new InternalDataAccess();
    String sBrandName, sGuideTitle, sGuidePurpose;
    Random r = new Random();
    int evaluation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();

        try {
            user = (User) intent.getExtras().getSerializable("User");
        } catch (Exception e) {
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activemenu);
        //setTitle("A.I. Life Improvement");

        exit = (ImageButton) findViewById(R.id.exitbutton);
        useactive = (ImageButton) findViewById(R.id.useactivebutton);
        newguide = (ImageButton) findViewById(R.id.newguidebutton);
        checkmemos = (ImageButton) findViewById(R.id.checkmemosbutton);
        brandName = (TextView) findViewById(R.id.brandName);
        guideTitle = (TextView) findViewById(R.id.activeGuideTitle);
        guidePurpose = (TextView) findViewById(R.id.activeGuidePurpose);
        weeklygoals = (ImageButton) findViewById(R.id.weeklyGoalsButton);

        // try to get values for brand and guide if saved in shared pref..
        sGuidePurpose = ida.getSharedPreferencesKeyValue(getApplicationContext(), "active_guide_purpose");
        sGuideTitle = ida.getSharedPreferencesKeyValue(getApplicationContext(), "active_guide_title");
        activeGuideId = ida.getSharedPreferencesKeyValue(getApplicationContext(), "active_guide_id");
        sBrandName = user.BrandName;


        // set text view (UI strings) to the internal data strings
        brandName.setText(sBrandName);
        guidePurpose.setText(sGuidePurpose);
        guideTitle.setText(sGuideTitle);

        // SAVE/UPDATE DATA FOR OFFLINE USE
        //tests to see if there is a connection.
        connectionTest();
        if (connected == true) {
            // save database info to offline data files for offline use (updates offline data files)
            saveOfflineDataFiles();
        }

        // setup buttons on activity
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //Guide Evaluation
                //Added by Rhagavi for testing purpose
                System.out.println("Exit");
                Intent intent;
                evaluation = r.nextInt(6);
                switch(evaluation){
                    case 0:
                        intent = new Intent(Activemenu.this, FinalEvaluation.class);
                        intent.putExtra("User", user);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(Activemenu.this, FinalEvaluation.class);
                        intent.putExtra("User", user);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(Activemenu.this, FinalEvaluation.class);
                        intent.putExtra("User", user);
                        startActivity(intent);
                        break;
                    case 3:
                        String url1 = "http://www.google.com";
                        intent = new Intent(Activemenu.this, Final_Referal.class);

                        intent.putExtra("Url", url1);
                        intent.putExtra("User", user);
                        startActivity(intent);
                        break;
                    case 4:
                        String url2 = "http://www.thisisnottom.com";
                        intent = new Intent(Activemenu.this, Final_Referal.class);
                        intent.putExtra("Url", url2);
                        intent.putExtra("User", user);
                        startActivity(intent);
                        break;
                    case 5:
                        String url3 = "https://www.youtube.com/watch?v=9-3OCc5g5oE";
                        intent = new Intent(Activemenu.this, Final_Referal.class);
                        intent.putExtra("Url", url3);
                        intent.putExtra("User", user);
                        startActivity(intent);
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "no evaluation ready", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        DatabaseHelper.setUser(user);
        useactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // Use Active Guide
                //Added by Rhagavi
                System.out.println("Testing by  Rhagavi");
                // user already logged into the system (has preferences values)
                System.out.println("activeGuideId " + activeGuideId);
                //Added by Rhagavi
                System.out.println("sBrandName " + sBrandName);
                System.out.println("sGuidePurpose " + sGuidePurpose);
                System.out.println("sGuideTitle " + sGuideTitle);

                if (!activeGuideId.isEmpty()) {
                    boolean switchflag = false;
                    DataAccess db = new DataAccess();
                    String checkDBForSwitch = "select Switch from Guide where GuideId = '" + activeGuideId + "'";
                    ResultSet rs = db.getDataTable(checkDBForSwitch);
                    try{
                        if(rs.next());
                        {
                            if(rs.getInt("Switch") == 1)
                                switchflag = true;
                        }
                    }
                    catch(Exception e){
                        System.out.println(e);
                    }
                    if(switchflag)
                    {
                        System.out.println("Going to open the Question.java");
                        Intent intent = new Intent(Activemenu.this, Question.class);
                        startActivity(intent);
                    }
                    else {
                        System.out.println("Going to open the Purpose.java");
                        Intent intent = new Intent(Activemenu.this, Purpose.class);
                        intent.putExtra("User", user);
                        intent.putExtra("guideId", activeGuideId);
                        startActivity(intent);
                    }
                }
                else // no active guides
                {
                    Toast.makeText(getApplicationContext(), "No guide is currently active. Please press 'New Guide'.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        newguide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // Guide Selection
                //Added by Rhagavi
                System.out.println("Clicked on NewGuide");

                Intent intent = new Intent(Activemenu.this, MainMenu.class);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });
        // opens memo button activity
        checkmemos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //Check memos
                //Added by Rhagavi
                System.out.println("Clicked on CheckMemos");

                if(isConnectedOnline()) {
                    Intent intent = new Intent(Activemenu.this, Inbox.class);
                    intent.putExtra("User", user);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Cannot check messages at this time. You must be online to check messages.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        weeklygoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Added by Rhagavi
                System.out.println("Clicked on WeeklyGoals");

                Intent intent = new Intent(Activemenu.this, DealAMealList.class);
                startActivity(intent);
            }
        });
    }

    //function to test connectivity
    void connectionTest() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else
            connected = false;

    }

    //function to loop through a result set
    public List resultSetToArrayList(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        List<Map<String, Object>> list = new ArrayList<>();
        while (rs.next()) {
            Map<String, Object> row = new HashMap<>(columns);
            for (int i = 1; i <= columns; ++i) {
                row.put(md.getColumnName(i), rs.getObject(i));
            }
            list.add(row);
        }
        return list;
    }

    public byte[] getBytesFromQueryPipeDelimited(String sql) {
        DataAccess db = new DataAccess();
        int columnCount = 0;
        String data = "";
        //Query for obtaining questions
        ResultSet dataTable = db.getDataTable(sql);
        if (dataTable != null) {
            try {
                while (dataTable.next()) // loop through each row (first row is always empty/null, so we do next() right away)
                {
                    try { // get column count in row
                        columnCount = dataTable.getMetaData().getColumnCount();
                        for (int i = 0; i < columnCount; i++) {
                            data += dataTable.getString(i + 1);
                            // delimit fields by pipe (if not last field)
                            if (i < columnCount - 1) {
                                data += "|";
                            }
                            else// end of row
                            {
                                data += "\r\n";
                            }
                        }
                    } catch (Exception e) {

                    }
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error getting Guides", Toast.LENGTH_SHORT).show();
            }
            byte[] byteArray = data.getBytes();

            return byteArray;
        }
     return null;
    }

    public void saveOfflineDataFiles()
    {
        // update database with latest offline file answers
        if(getApplicationContext().getFileStreamPath("offline_history.txt").exists()) // if offline history exists
            ida.updateOfflineAnswers(getApplicationContext());

        //Query for obtaining questions
        String query2 = "SELECT ISNULL(q.[QuestionId],'')\n" +
                "    ,ISNULL(q.[GuideId],'')\n" +
                "    ,ISNULL(q.[Creater],'')\n" +
                "    ,ISNULL(q.[Eligibility],'')\n" +
                "    ,ISNULL(q.[Switch],'')\n" +
                "    ,ISNULL(q.[QUESTIONTEXt],'')\n" +
                "    ,ISNULL(q.[QuestionLevel],'')\n" +
                "    ,ISNULL(q.[LinkQuestion],'')\n" +
                "    ,ISNULL(q.[ResponseTrigger],'')\n" +
                "    ,ISNULL(h.historyid,'')    ,ISNULL(h.psychoanalyst,''), ISNULL(h.severity,''), ISNULL(h.userid,''), ISNULL(h.response,'')\n" +
                "FROM [dbo].[Question] q \n" +
                "JOIN GUIDE g ON g.guideId = q.guideid \n" +
                "LEFT JOIN USERS u on u.brandNumber = g.BrandNumber \n" +
                " OR (g.guideId in (SELECT guideid from guidehistory gh \n" +
                " Where u.userId = gh.userid)) \n" +
                "LEFT JOIN History h on h.userid = u.userid AND h.questionid = q.questionid \n" +
                "WHERE u.userID = '" + user.UserID + "'";
        byte[] questionFileInfo = getBytesFromQueryPipeDelimited(query2);

        //Query for obtaining guides, guides tha the user has completed from referral history, plus guides available in users brand.
        query2 = "SELECT \n" +
                "       ISNULL(g.[GuideName],'')\n" +
                "      ,ISNULL(g.[GuideId],'')\n" +
                "      ,ISNULL(g.[Seq],'')\n" +
                "      ,ISNULL(g.[Weight],'')\n" +
                "      ,ISNULL(g.[Switch],'')\n" +
                "      ,ISNULL(g.[BrandNumber],'')\n" +
                "      ,ISNULL(g.purpose,'')\n" +
                "      ,ISNULL(g.guideeditor#,'')\n" +
                "  FROM [dbo].[Guide] g\n" +
                "  LEFT JOIN USERS u on u.brandNumber = g.BrandNumber \n" +
                "\t\t\tAND   u.brandNumber = '" + user.BrandNumber + "' \n" +
                "\t\t\tOR (g.guideId in (SELECT guideid from guidehistory gh \n" +
                "\t\t\t\t\t\t\t  Where u.userId = gh.userid)) \n" +
                "  WHERE u.userID = '" + user.UserID + "'";
        byte[] guideFileInfo = getBytesFromQueryPipeDelimited(query2);

        //file names
        String questionFile = "questions.txt";
        String guideFile = "guideFile.txt";

        FileOutputStream outputStream;
        FileOutputStream outputStream1;
        //writes to question file
        try {
            outputStream = openFileOutput(questionFile, Context.MODE_PRIVATE);
            outputStream.write(questionFileInfo);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //writes to guide file
        try {
            outputStream1 = openFileOutput(guideFile, Context.MODE_PRIVATE);
            outputStream1.write(guideFileInfo);
            outputStream1.close();
        } catch (Exception e) {
            e.printStackTrace();
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
