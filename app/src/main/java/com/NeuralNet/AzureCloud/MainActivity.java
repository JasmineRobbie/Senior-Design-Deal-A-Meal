package com.NeuralNet.AzureCloud;

//import androidx.appcompat.app.AlertDialog;
import android.app.AlertDialog;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.teamcarl.prototype.R;

import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Hashtable;

public class MainActivity extends Activity {
    private String m_Text = "";
    private String p_Text = "";
    private String p_Text2 = "";
    private String f_Text = "";
    private String l_Text = "";
    private int brandId, backDoor5;
    public static Hashtable<Integer, Integer> timeWeekBars = new Hashtable<Integer, Integer>();
    private Button monButton, tueButton, wedButton, thuButton, friButton, satButton, sunButton;
    private ImageButton monIButton, tueIButton, wedIButton, thuIButton, friIButton, satIButton, sunIButton;
    private ImageView BackdoorLogoButton;
    private int priorset = 0;
    private TextView bDisplay;
    DataAccess db = new DataAccess();
    private DatabaseHelper dbh = new DatabaseHelper(this);
    public static final String DAY_ID = "montra_name_id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = this;
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("gib brand code");
        backDoor5=0;
        final LinearLayout linLay = new LinearLayout(this);
        linLay.setOrientation(LinearLayout.VERTICAL);
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        final View dialogTextInput = layoutInflaterAndroid.inflate(R.layout.dialog, null);// Set up the input
      //  final EditText input = (EditText)findViewById (R.id.userInputDialog0);
        //  final EditText pwd  =  (EditText)findViewById (R.id.userInputDialog);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
      //  input.setInputType(InputType.TYPE_CLASS_TEXT /*| InputType.TYPE_TEXT_VARIATION_PASSWORD*/);
      //  pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

// Set up the buttons
        Calendar cal = Calendar.getInstance();
        int currWeek = cal.get(Calendar.WEEK_OF_YEAR);
        if(dbh.checkdates()) {
            Cursor cursor = dbh.getAllData();
            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
            {
                int s = cursor.getInt(cursor.getColumnIndex("STARTTIME"));
                int e = cursor.getInt(cursor.getColumnIndex("ENDTIME"));
                int id = cursor.getInt(cursor.getColumnIndex("ID"));
                String not = cursor.getString(cursor.getColumnIndex("NOTES"));
                int color = cursor.getInt(cursor.getColumnIndex("MAJORCAT"));
                int min = cursor.getInt(cursor.getColumnIndex("MINORCAT"));


                String nam = cursor.getString(cursor.getColumnIndex("NAME"));
                int p = cursor.getInt(cursor.getColumnIndex("PRIORITY"));
                String comCode = cursor.getString(cursor.getColumnIndex("CODECHAR"));
                int comNum = cursor.getInt(cursor.getColumnIndex("CODENUM"));
                String query2add = "insert into GoalHistory (UserID, UploadDate, Gminor, Title, StartTime, EndTime, Note, ComCode, Counter)\n" +
                "VALUES ('" + dbh.getUID() + "', '" + cal.get(Calendar.WEEK_OF_YEAR) + "', '" + min + "', '" + nam + "', '" + s + "', '" + e + "', '" + not + "', '" + comCode + "', '" + comNum+ "')";
                db.executeNonQuery(query2add);
        }
        }
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final EditText input = (EditText)dialogTextInput.findViewById (R.id.userInputDialog0);
                final EditText fname = (EditText)dialogTextInput.findViewById(R.id.userInputDialogFname);
                final EditText lname = (EditText)dialogTextInput.findViewById(R.id.userInputDialogLname);
                final EditText pwd  =  (EditText)dialogTextInput.findViewById (R.id.userInputDialog);
                final EditText pwd2  =  (EditText)dialogTextInput.findViewById (R.id.userInputDialog1);
                m_Text = input.getText().toString();
                p_Text = pwd.getText().toString();
                p_Text2 = pwd2.getText().toString();
                f_Text = fname.getText().toString();
                l_Text = lname.getText().toString();

                if(checkBrando(m_Text))
                    if(checkpass(p_Text,p_Text2))
                    {

                        String query = "insert into users (FirstName, LastName, Password, BrandNumber)\n" +
                                "VALUES ('" + f_Text + "', '" + l_Text + "', '" + p_Text + "', '" + brandId + "')" +
                                "SELECT @@IDENTITY, '" + p_Text + "'";
                        String queryResult = db.executeNonQuery(query);
                        if(queryResult.contains("error"))
                            Toast.makeText(context, "Something went wrong!"+ dbh.getBrandingCode(), Toast.LENGTH_SHORT).show();
                        else {
                            dbh.login(m_Text, p_Text);
                            Toast.makeText(context, "You are now logged in with code " + dbh.getBrandingCode(), Toast.LENGTH_SHORT).show();
                        }
                        bDisplay.setText(dbh.getBrandingCode());
                      //  System.out.println(queryResult);
                    }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        setContentView(R.layout.activity_main);
//set up the view
        bDisplay = (TextView)findViewById(R.id.curBrandDisplay);
        bDisplay.setText(dbh.getBrandingCode());
        monButton = (Button) findViewById(R.id.Monbutton);
        monButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDayView(0);
            }
        });
        tueButton = (Button) findViewById(R.id.Tuebutton);
        tueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDayView(1);
            }
        });
        wedButton = (Button) findViewById(R.id.Wedbutton);
        wedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDayView(2);
            }
        });
        thuButton = (Button) findViewById(R.id.Thurbutton);
        thuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDayView(3);
            }
        });
        friButton = (Button) findViewById(R.id.Fributton);
        friButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDayView(4);
            }
        });
        satButton = (Button) findViewById(R.id.Satbutton);
        satButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDayView(5);
            }
        });
        sunButton= (Button) findViewById(R.id.Sunbutton);
        sunButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDayView(6);
            }
        });
        LinearLayout ll = findViewById(R.id.Moncolumn);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDayView(0);
            }
        });
        LinearLayout tl = findViewById(R.id.Tuecolumn);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDayView(1);
            }
        });
        LinearLayout wl = findViewById(R.id.Wedcolumn);
        wl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDayView(2);
            }
        });
        LinearLayout thl = findViewById(R.id.Thurscolumn);
        thl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDayView(3);
            }
        });
        LinearLayout fl = findViewById(R.id.Fricolumn);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDayView(4);
            }
        });
        LinearLayout sal = findViewById(R.id.Satcolumn);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDayView(5);
            }
        });
        LinearLayout sul = findViewById(R.id.Suncolumn);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDayView(6);
            }
        });
        /*monIButton = (ImageButton) findViewById(R.id.MonIButton);
        monIButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDayView(0);
            }
        });
        tueIButton = (ImageButton) findViewById(R.id.TueIButton);
        tueIButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDayView(1);
            }
        });
        wedIButton = (ImageButton) findViewById(R.id.WedIButton);
        wedIButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDayView(2);
            }
        });
        thuIButton = (ImageButton) findViewById(R.id.ThuIButton);
        thuIButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDayView(3);
            }
        });
        friIButton = (ImageButton) findViewById(R.id.FriIButton);
        friIButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDayView(4);
            }
        });
        satIButton = (ImageButton) findViewById(R.id.SatIButton);
        satIButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDayView(5);
            }
        });
        sunIButton= (ImageButton) findViewById(R.id.SunIButton);
        sunIButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDayView(6);
            }
        });
       // yeet=(Button) findViewById(R.id.yeet);*/
        Button membutotn = findViewById(R.id.buttonToMemos);
        membutotn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnectedOnline()) {
                    Intent intent = new Intent(MainActivity.this, Messages.class);
                    intent.putExtra("User", DatabaseHelper.getUser());
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Cannot check messages at this time. You must be online to check messages.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
        //PRIORITY BUTTON STUFF
        ImageButton imageUp = findViewById(R.id.upImageButtonMain);
        final TextView prioritytextset = findViewById(R.id.priorityText);
        imageUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priorset ++;
                prioritytextset.setText("Priority: "+ priorset);
                reloadCalendar(167, priorset);
            }
        });
        ImageButton imagedown = findViewById(R.id.downImageButtonMain);
        imagedown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priorset --;
                prioritytextset.setText("Priority: "+ priorset);
                reloadCalendar(167, priorset);
            }
        });
        ImageButton temp = (ImageButton) findViewById(R.id.imageButton);
        /*temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dv = new Intent(context, SurveyPage.class);
                startActivity(dv);
            }
        });*/
        ////////////////////////////////////////////////////////////////////////////////////////////
        //INITIALIZE THE TIME BARS
        int timeweekbarIndex = 1;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_mon_1);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_mon_2);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_mon_3);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_mon_4);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_mon_5);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_mon_6);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_mon_7);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_mon_8);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_mon_9);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_mon_10);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_mon_11);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_mon_12);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_mon_13);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_mon_14);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_mon_15);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_mon_16);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_mon_17);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_mon_18);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_mon_19);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_mon_20);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_mon_21);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_mon_22);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_mon_23);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_mon_24);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_tue_1);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_tue_2);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_tue_3);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_tue_4);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_tue_5);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_tue_6);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_tue_7);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_tue_8);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_tue_9);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_tue_10);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_tue_11);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_tue_12);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_tue_13);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_tue_14);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_tue_15);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_tue_16);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_tue_17);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_tue_18);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_tue_19);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_tue_20);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_tue_21);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_tue_22);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_tue_23);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_tue_24);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_wed_1);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_wed_2);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_wed_3);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_wed_4);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_wed_5);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_wed_6);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_wed_7);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_wed_8);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_wed_9);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_wed_10);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_wed_11);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_wed_12);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_wed_13);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_wed_14);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_wed_15);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_wed_16);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_wed_17);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_wed_18);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_wed_19);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_wed_20);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_wed_21);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_wed_22);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_wed_23);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_wed_24);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_thu_1);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_thu_2);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_thu_3);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_thu_4);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_thu_5);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_thu_6);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_thu_7);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_thu_8);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_thu_9);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_thu_10);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_thu_11);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_thu_12);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_thu_13);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_thu_14);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_thu_15);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_thu_16);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_thu_17);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_thu_18);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_thu_19);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_thu_20);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_thu_21);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_thu_22);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_thu_23);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_thu_24);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_fri_1);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_fri_2);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_fri_3);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_fri_4);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_fri_5);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_fri_6);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_fri_7);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_fri_8);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_fri_9);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_fri_10);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_fri_11);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_fri_12);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_fri_13);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_fri_14);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_fri_15);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_fri_16);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_fri_17);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_fri_18);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_fri_19);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_fri_20);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_fri_21);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_fri_22);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_fri_23);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_fri_24);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sat_1);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sat_2);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sat_3);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sat_4);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sat_5);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sat_6);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sat_7);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sat_8);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sat_9);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sat_10);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sat_11);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sat_12);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sat_13);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sat_14);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sat_15);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sat_16);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sat_17);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sat_18);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sat_19);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sat_20);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sat_21);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sat_22);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sat_23);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sat_24);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sun_1);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sun_2);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sun_3);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sun_4);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sun_5);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sun_6);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sun_7);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sun_8);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sun_9);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sun_10);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sun_11);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sun_12);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sun_13);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sun_14);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sun_15);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sun_16);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sun_17);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sun_18);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sun_19);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sun_20);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sun_21);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sun_22);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sun_23);
        timeweekbarIndex++;
        timeWeekBars.put(timeweekbarIndex, R.id.day_time_bar_sun_24);
        System.out.println("index: " + timeweekbarIndex);
        Goal g = new Goal();
        Cursor cursor = dbh.getAllData();
        TextView curBar;
        for(int i = 1; i < timeweekbarIndex; i++)
        {
            curBar = (TextView)findViewById(timeWeekBars.get(i));
            curBar.setBackgroundColor(Color.TRANSPARENT);
            curBar.setText("");
        }
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
        {

            ////////////////////////////////////////////////////////////////////////////////////////
            //Pull data from DB and put it in IF the data belongs to the selected day
            /*final*/ int s = cursor.getInt(cursor.getColumnIndex("STARTTIME"));
            /*final*/ int e = cursor.getInt(cursor.getColumnIndex("ENDTIME"));
            /*boolean state = false;
                if((s > (day*24)&&(s < ((day+1)*24)))||((e > (day*24))&& e < ((day+1)*24)))
                {
                    state = true;
                    if(s < (day*24))
                        s = 0;
                    else if (e > ((day+1)*24))
                        e = 24;
                }
                s-=(day*24);
                e-=(day*24);
                if(state){*/
                final int id = cursor.getInt(cursor.getColumnIndex("ID"));
                //index[countnum][0] = id;
                /*final*/ String not = cursor.getString(cursor.getColumnIndex("NOTES"));
                /*final*/ int color = cursor.getInt(cursor.getColumnIndex("MAJORCAT"));
                /*final*/ int min = cursor.getInt(cursor.getColumnIndex("MINORCAT"));
                /*final*/ String nam = cursor.getString(cursor.getColumnIndex("NAME"));
                /*final*/ int p = cursor.getInt(cursor.getColumnIndex("PRIORITY"));
                ////////////////////////////////////////////////////////////////////////////////////////
                //Set the boxes
                curBar = (TextView)findViewById(timeWeekBars.get(s));
                //index[countnum][1] = timeBars.get(s);
                //curBar.setText(nam);
                for(int i = s; i < e; i++)
                {



                    curBar = (TextView)findViewById(timeWeekBars.get(i));
                    int newcol = Goal.majorColors.get(color);
                    curBar.setBackgroundColor(getResources().getColor(newcol));
                    /*curBar.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view)
                        {
                            editing = true;
                            editID = id;
                            /*loadGoal(new Goal(id, color, min, nam, s, e, p, not));
                            loadGoal(id);
                        }
                    });*/
                }
               // countnum++;
            }


        BackdoorLogoButton = (ImageView)findViewById(R.id.imageViewLogo);
        BackdoorLogoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(backDoor5 >= 4) {
                    builder.setView(dialogTextInput);
                    if (dialogTextInput.getParent() != null)
                        ((ViewGroup) dialogTextInput.getParent()).removeView(dialogTextInput);
                    builder.show();
                }
                else
                    backDoor5++;
            }
        });

    }


    public boolean checkBrando(String brando)
    {
        String queryBrandId = "select brandId from brand where brandLink = '" + brando + "'";
        ResultSet userResult = db.getDataTable(queryBrandId);
        try{
            if (userResult.next())
            {
                /*Toast.makeText(this, "Brando good", Toast.LENGTH_SHORT).show();*/
                brandId = userResult.getInt(1);
                return true;
            }
            else
                Toast.makeText(this, "Please enter a valid branding code", Toast.LENGTH_SHORT).show();
            System.out.println("hit");
        }
        catch(Exception e) {
            System.out.println("you done goofed");
            System.out.println(e);
        }
        return false;
    }

    public boolean checkpass(String p1, String p2)
    {
        if(p1.length() < 6)
            Toast.makeText(this, "Passwords must be 6 characters long", Toast.LENGTH_SHORT).show();
        else
        {
            if(!p1.equals(p2))
            Toast.makeText(this, "Please make sure your passwords match", Toast.LENGTH_SHORT).show();
            else
                return true;
        }
        return false;
    }

    public void openDayView(int d)
    {
        Intent dv = new Intent(MainActivity.this, DayView.class);
        dv.putExtra(DAY_ID, d);
        startActivity(dv);
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

    public void reloadCalendar(int timeweekbarIndex, int priorsort)
    {
        TextView curBar;
        Cursor cursor = dbh.getAllData();
        for(int i = 1; i < timeweekbarIndex; i++)
        {
            System.out.println(i);
            curBar = (TextView)findViewById(timeWeekBars.get(i));
            curBar.setBackgroundColor(Color.TRANSPARENT);
            curBar.setText("");
        }
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
        {

            ////////////////////////////////////////////////////////////////////////////////////////
            //Pull data from DB and put it in IF the data belongs to the selected day
            /*final*/ int s = cursor.getInt(cursor.getColumnIndex("STARTTIME"));
            /*final*/ int e = cursor.getInt(cursor.getColumnIndex("ENDTIME"));
            /*boolean state = false;
                if((s > (day*24)&&(s < ((day+1)*24)))||((e > (day*24))&& e < ((day+1)*24)))
                {
                    state = true;
                    if(s < (day*24))
                        s = 0;
                    else if (e > ((day+1)*24))
                        e = 24;
                }
                s-=(day*24);
                e-=(day*24);
                if(state){*/
            final int id = cursor.getInt(cursor.getColumnIndex("ID"));
            //index[countnum][0] = id;
            /*final*/ String not = cursor.getString(cursor.getColumnIndex("NOTES"));
            /*final*/ int color = cursor.getInt(cursor.getColumnIndex("MAJORCAT"));
            /*final*/ int min = cursor.getInt(cursor.getColumnIndex("MINORCAT"));
            /*final*/ String nam = cursor.getString(cursor.getColumnIndex("NAME"));
            /*final*/ int p = cursor.getInt(cursor.getColumnIndex("PRIORITY"));
            ////////////////////////////////////////////////////////////////////////////////////////
            //Set the boxes if priority matches
            if(p >= priorsort) {
                curBar = (TextView) findViewById(timeWeekBars.get(s));
                //index[countnum][1] = timeBars.get(s);
                //curBar.setText(nam);
                for (int i = s; i < e; i++) {


                    curBar = (TextView) findViewById(timeWeekBars.get(i));
                    int newcol = Goal.majorColors.get(color);
                    curBar.setBackgroundColor(getResources().getColor(newcol));
                    /*curBar.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view)
                        {
                            editing = true;
                            editID = id;
                            /*loadGoal(new Goal(id, color, min, nam, s, e, p, not));
                            loadGoal(id);
                        }
                    });*/
                }
                // countnum++;
            }
        }

    }
}
