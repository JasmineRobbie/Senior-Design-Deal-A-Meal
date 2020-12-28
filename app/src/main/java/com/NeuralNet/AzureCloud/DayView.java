package com.NeuralNet.AzureCloud;

import android.app.AlertDialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.teamcarl.prototype.R;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class DayView extends Activity {
    private DatabaseHelper dbhelp;
    private Button ent, era, mset, eset, sset, dset, rset, tset, hset, btweek, minor0, minor1, minor2, minor3, minor4, catButton;
    private EditText dc, nt, stime, etime, priority;
    EditText cod;
    EditText codnum;
    private ImageButton bprev, bnext, upbutton, downbutton;
    private TextView tDay;
    private String name, notes, dayname;
    private Spinner spin;
    DataAccess db = new DataAccess();
    private boolean editing = false;
    int goal_id = 0;
    int goal_m_id = 0;
    private int dayInfo, majorcat, minorcat, startime, entime, prior, editID, day, indexlength;
    private int[][] index = new int[24][2];
    int curindex = 0;
    public static Hashtable<Integer, Integer> timeBars = new Hashtable<Integer, Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_view);
        ////////////////////////////////////////////////////////////////////////////////////////////
        //Create alert dialog for timeframe confilicts
        AlertDialog.Builder b1 = new AlertDialog.Builder(DayView.this);
        b1.setMessage("Warning! you have something scheduled during that timeframe");
        b1.setCancelable(true);
        b1.setPositiveButton(
                "Continue",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }
        );
        final AlertDialog alertdup = b1.create();
        ////////////////////////////////////////////////////////////////////////////////////////////
        //Create alert dialog for deleting issues
        AlertDialog.Builder b2 = new AlertDialog.Builder(DayView.this);
        b2.setMessage("Warning! There was an error deleting the object");
        b2.setCancelable(true);
        b2.setPositiveButton(
                "Continue",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }
        );
        final AlertDialog alertdel = b2.create();
        ////////////////////////////////////////////////////////////////////////////////////////////
        //Create alert dialog for empty fields
        AlertDialog.Builder b3 = new AlertDialog.Builder(DayView.this);
        b3.setMessage("Warning! A required field was left empty");
        b3.setCancelable(true);
        b3.setPositiveButton(
                "Continue",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }
        );
        final AlertDialog alertemp = b3.create();
        ////////////////////////////////////////////////////////////////////////////////////////////
        // initialize timebars
        timeBars.put(0,R.id.day_time_bar_1);
        timeBars.put(1,R.id.day_time_bar_2);
        timeBars.put(2,R.id.day_time_bar_3);
        timeBars.put(3,R.id.day_time_bar_4);
        timeBars.put(4,R.id.day_time_bar_5);
        timeBars.put(5,R.id.day_time_bar_6);
        timeBars.put(6,R.id.day_time_bar_7);
        timeBars.put(7,R.id.day_time_bar_8);
        timeBars.put(8,R.id.day_time_bar_9);
        timeBars.put(9,R.id.day_time_bar_10);
        timeBars.put(10,R.id.day_time_bar_11);
        timeBars.put(11,R.id.day_time_bar_12);
        timeBars.put(12,R.id.day_time_bar_13);
        timeBars.put(13,R.id.day_time_bar_14);
        timeBars.put(14,R.id.day_time_bar_15);
        timeBars.put(15,R.id.day_time_bar_16);
        timeBars.put(16,R.id.day_time_bar_17);
        timeBars.put(17,R.id.day_time_bar_18);
        timeBars.put(18,R.id.day_time_bar_19);
        timeBars.put(19,R.id.day_time_bar_20);
        timeBars.put(20,R.id.day_time_bar_21);
        timeBars.put(21,R.id.day_time_bar_22);
        timeBars.put(22,R.id.day_time_bar_23);
        timeBars.put(23,R.id.day_time_bar_24);
        ////////////////////////////////////////////////////////////////////////////////////////////
        // initialize minor buttons
        ////////////////////////////////////////////////////////////////////////////////////////////
        // initialize categories
        catButton = (Button) findViewById(R.id.mindset_day_button);
        String querymajor1 = "select Name from MajorCat where ID = '" + 0 + "'";
        ResultSet userResult = db.getDataTable(querymajor1);
        try{
            if (userResult.next())
            {
                /*Toast.makeText(this, "Brando good", Toast.LENGTH_SHORT).show();*/
                catButton.setText(userResult.getString(1));
            }
            else {
            }
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
        catButton = (Button) findViewById(R.id.education_day_button);
        String querymajor2 = "select Name from MajorCat where ID = '" + 1 + "'";
        userResult = db.getDataTable(querymajor2);
        try{
            if (userResult.next())
            {
                /*Toast.makeText(this, "Brando good", Toast.LENGTH_SHORT).show();*/
                catButton.setText(userResult.getString(1));
            }
            else {
            }
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
        catButton = (Button) findViewById(R.id.social_day_button);
        String querymajor3 = "select Name from MajorCat where ID = '" + 2 + "'";
        userResult = db.getDataTable(querymajor3);
        try{
            if (userResult.next())
            {
                /*Toast.makeText(this, "Brando good", Toast.LENGTH_SHORT).show();*/
                catButton.setText(userResult.getString(1));
            }
            else {
            }
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
        catButton = (Button) findViewById(R.id.duties_day_button);
        String querymajor4 = "select Name from MajorCat where ID = '" + 3 + "'";
        userResult = db.getDataTable(querymajor4);
        try{
            if (userResult.next())
            {
                /*Toast.makeText(this, "Brando good", Toast.LENGTH_SHORT).show();*/
                catButton.setText(userResult.getString(1));
            }
            else {
            }
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
        catButton = (Button) findViewById(R.id.health_day_button);
        String querymajor5 = "select Name from MajorCat where ID = '" + 4 + "'";
        userResult = db.getDataTable(querymajor5);
        try{
            if (userResult.next())
            {
                /*Toast.makeText(this, "Brando good", Toast.LENGTH_SHORT).show();*/
                catButton.setText(userResult.getString(1));
            }
            else {
            }
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
        catButton = (Button) findViewById(R.id.tranquility_day_button);
        String querymajor6 = "select Name from MajorCat where ID = '" + 5 + "'";
        userResult = db.getDataTable(querymajor6);
        try{
            if (userResult.next())
            {
                /*Toast.makeText(this, "Brando good", Toast.LENGTH_SHORT).show();*/
                catButton.setText(userResult.getString(1));
            }
            else {
            }
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
        catButton = (Button) findViewById(R.id.rest_day_button);
        String querymajor7 = "select Name from MajorCat where ID = '" + 6 + "'";
        userResult = db.getDataTable(querymajor7);
        try{
            if (userResult.next())
            {
                /*Toast.makeText(this, "Brando good", Toast.LENGTH_SHORT).show();*/
                catButton.setText(userResult.getString(1));
            }
            else {
            }
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
        ////////////////////////////////////////////////////////////////////////////////////////////
        // initialize minor categories
        spin = (Spinner) findViewById(R.id.minorcatSpinner);
        //spin.setOnItemSelectedListener(this);
        setminorcategories(goal_id);
        ////////////////////////////////////////////////////////////////////////////////////////////
        // initialize database
        dbhelp = new DatabaseHelper(DayView.this);
        //dbhelp.resetDB();
        ////////////////////////////////////////////////////////////////////////////////////////////
        //grab weekday from input and display it
        tDay = (TextView)findViewById(R.id.tvWeekday);
        Intent dv = getIntent();
        day = dv.getIntExtra(MainActivity.DAY_ID, 0);
        /*dayInfo = Integer.decode(day);*/
        switch(day) {
            case 1:
                dayname = "Tuesday";
                break;
            case 2:
                dayname = "Wednesday";
                break;
            case 3:
                dayname = "Thursday";
                break;
            case 4:
                dayname = "Friday";
                break;
            case 5:
                dayname = "Saturday";
                break;
            case 6:
                dayname = "Sunday";
                break;
            default:
                dayname = "Monday";

        }
        tDay.setText(dayname);
        tDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToWeek();
            }
        });
        bprev = (ImageButton)findViewById(R.id.day_left_button);
        bnext =(ImageButton)findViewById(R.id.day_right_button);
        bprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int nd;
                if(day == 0)
                    nd = 6;
                else
                    nd = day-1;
                switchDay(nd);
            }
        });
        bnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int nd;
                if(day==6)
                    nd = 0;
                else
                    nd = day+1;
                switchDay(nd);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
        //back to week lol
        btweek = (Button)findViewById(R.id.backtoweek);
        btweek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToWeek();
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
        //Initialize views/inputs
        /*minor0 = (Button)findViewById(R.id.minorcat0);
        minor1 = (Button)findViewById(R.id.minorcat1);
        minor2 = (Button)findViewById(R.id.minorcat2);
        minor3 = (Button)findViewById(R.id.minorcat3);
        minor4 = (Button)findViewById(R.id.minorcat4);*/
        codnum = (EditText) findViewById(R.id.codeNumber);
        cod = (EditText) findViewById(R.id.fourCharCode);
        priority = (EditText)findViewById(R.id.priority_day_text);
        nt = (EditText)findViewById(R.id.Note_day_field);
        dc = (EditText)findViewById(R.id.day_current_task_text);
        ent = (Button)findViewById(R.id.Enter_Day_button);
        era = (Button)findViewById(R.id.day_erase_button);
        stime = (EditText)findViewById(R.id.start_time_day);
        etime = (EditText)findViewById(R.id.end_time_day);
        //TODO: Input validation?
        //TODO: change the time values to a TimePicker class
        ////////////////////////////////////////////////////////////////////////////////////////////
        //Do mindset button change color and ID
        mset = (Button) findViewById(R.id.mindset_day_button);
        mset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goal_id = 0;
                dc.setBackgroundResource(R.drawable.rounded_corner_mind);
                setminorcategories(goal_id);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
        //Do education button change color and ID
        eset = (Button)findViewById(R.id.education_day_button);
        eset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goal_id = 1;
                dc.setBackgroundResource(R.drawable.rounded_corner_edu);
                setminorcategories(goal_id);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
        //Do social button change color and ID
        sset = (Button)findViewById(R.id.social_day_button);
        sset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goal_id = 2;
                dc.setBackgroundResource(R.drawable.rounded_corner_soc);
                setminorcategories(goal_id);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
        //Do social button change color and ID
        dset = (Button)findViewById(R.id.duties_day_button);
        dset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goal_id = 3;
                dc.setBackgroundResource(R.drawable.rounded_corner_dut);
                setminorcategories(goal_id);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
        //Do social button change color and ID
        hset = (Button)findViewById(R.id.health_day_button);
        hset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goal_id = 4;
                dc.setBackgroundResource(R.drawable.rounded_corner_heal);
                setminorcategories(goal_id);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
        //Do social button change color and ID
        tset = (Button)findViewById(R.id.tranquility_day_button);
        tset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goal_id = 5;
                dc.setBackgroundResource(R.drawable.rounded_corner_tranq);
                setminorcategories(goal_id);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
        //Do social button change color and ID
        rset = (Button)findViewById(R.id.rest_day_button);
        rset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goal_id = 6;
                dc.setBackgroundResource(R.drawable.rounded_corner_rest);
                setminorcategories(goal_id);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
        //do up and down buttons lamo

        upbutton = (ImageButton)findViewById(R.id.day_button_up);
        downbutton = (ImageButton)findViewById(R.id.day_button_down);
        upbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(curindex > 0)
                    curindex--;
                editing = true;
                selections();
            }
        });
        int ec = R.id.day_time_bar_1;
        int ee = R.id.day_time_bar_5;
        downbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(curindex < indexlength-1)
                    curindex++;
                editing = true;
                selections();
            }
        });
        /*minor0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minorcat = 0;
               minor0.setBackgroundResource(R.drawable.rounded_corner_rest);
               minor1.setBackgroundResource(R.drawable.rounded_border);
               minor2.setBackgroundResource(R.drawable.rounded_border);
               minor3.setBackgroundResource(R.drawable.rounded_border);
               minor4.setBackgroundResource(R.drawable.rounded_border);
            }
        });
        minor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minorcat = 1;
                minor1.setBackgroundResource(R.drawable.rounded_corner_rest);
                minor0.setBackgroundResource(R.drawable.rounded_border);
                minor2.setBackgroundResource(R.drawable.rounded_border);
                minor3.setBackgroundResource(R.drawable.rounded_border);
                minor4.setBackgroundResource(R.drawable.rounded_border);
            }
        });
        minor2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minorcat = 2;
                minor2.setBackgroundResource(R.drawable.rounded_corner_rest);
                minor1.setBackgroundResource(R.drawable.rounded_border);
                minor0.setBackgroundResource(R.drawable.rounded_border);
                minor3.setBackgroundResource(R.drawable.rounded_border);
                minor4.setBackgroundResource(R.drawable.rounded_border);
            }
        });
        minor3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minorcat = 3;
                minor3.setBackgroundResource(R.drawable.rounded_corner_rest);
                minor1.setBackgroundResource(R.drawable.rounded_border);
                minor2.setBackgroundResource(R.drawable.rounded_border);
                minor0.setBackgroundResource(R.drawable.rounded_border);
                minor4.setBackgroundResource(R.drawable.rounded_border);
            }
        });
        minor4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minorcat = 4;
                minor4.setBackgroundResource(R.drawable.rounded_corner_rest);
                minor1.setBackgroundResource(R.drawable.rounded_border);
                minor2.setBackgroundResource(R.drawable.rounded_border);
                minor3.setBackgroundResource(R.drawable.rounded_border);
                minor0.setBackgroundResource(R.drawable.rounded_border);
            }
        });*/
        ////////////////////////////////////////////////////////////////////////////////////////////
        //TEMPORARY PORTAL TO INITIAL LOGIN SCREEN.
        //TODO: DELETE THIS CODE BLOCK
        final Context cdev = DayView.this;
       /* Button bdev = (Button) findViewById(R.id.button6);
        bdev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dev = new Intent(cdev, login1.class);
                startActivity(dev);
            }
        });
        Button bsdev = (Button) findViewById(R.id.button7);
        bsdev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dev = new Intent(cdev, GuideSelect.class);
                startActivity(dev);
            }
        });*/

        ////////////////////////////////////////////////////////////////////////////////////////////
        //Enter button things
        //TODO: make minor cat back to 0
        ent.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                boolean pass = true;
                int s;
                int e;
                boolean res = false;
                String tx;
                boolean empty = false;
                //database test stuff
               // data = "";
                name = dc.getText().toString();
                if (name.matches(""))
                    empty = true;
                majorcat = goal_id;
                minorcat = goal_m_id;
                EditText codeText = (EditText) findViewById(R.id.fourCharCode);
                String codeC = codeText.getText().toString();
                EditText codenumber = (EditText) findViewById(R.id.codeNumber);
                int codeN = 0;
                if(!codenumber.getText().toString().matches("")) {
                    codeN = Integer.decode(codenumber.getText().toString());
                }
                tx = stime.getText().toString();
                if(tx.matches(""))
                    empty = true;
                else
                    startime = Integer.decode(tx) + (day*24);
                tx = etime.getText().toString();
                if(tx.matches(""))
                    empty = true;
                else
                    entime = Integer.decode(tx) + (day*24);
                notes = nt.getText().toString();
                tx = priority.getText().toString();
                if(tx.matches(""))
                    empty = true;
                else
                    prior = Integer.decode(tx);
                if(!empty) {
                    Cursor cursor = dbhelp.getAllData();
                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                        s = cursor.getInt(cursor.getColumnIndex("STARTTIME"));
                        e = cursor.getInt(cursor.getColumnIndex("ENDTIME"));
                        if ((startime >= s && startime < e) || (entime > s && entime <= e))
                            if (editing) {
                                if (editID != cursor.getInt(cursor.getColumnIndex("ID")))
                                    pass = false;
                            } else
                                pass = false;


                    }
                    if (editing) {
                        if (pass)
                            res = dbhelp.updateData(String.valueOf(editID), name, majorcat, minorcat, startime, entime, prior, notes, codeC, codeN);
                        editing = false;
                    } else {
                        if (pass)
                            res = dbhelp.insertData(name, majorcat, minorcat, startime, entime, prior, notes, codeC, codeN);
                        else
                            alertdup.show();
                    }
                    //cursor.moveToFirst();
                    // data += cursor.getString(cursor.getColumnIndex("NOTES"));
                    System.out.println(res);
                    initializeColumn();
                    codeText.setText("");
                    codenumber.setText("");
                }
                else
                    alertemp.show();
            }
        });
        era.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editing)
                {
                    int ret = dbhelp.deleteData(String.valueOf(editID));
                    if(ret != 1)
                        alertdel.show();

                }
                initializeColumn();
            }
        });
        setminorcategories(goal_id);
        initializeColumn();
        selections();
    }
    //Refresh column from DB, and update visual method for this will be decided on later
    public void initializeColumn()
    {
        curindex = -1;
        index = new int[24][2];
        int countnum = 0;
        dc.setText("");
        nt.setText("");
        stime.setText("");
        etime.setText("");
        priority.setText("");
        editing = false;
        Goal G = new Goal();
        Cursor cursor = dbhelp.getAllData();
        TextView curBar;
        //Clear schedule boxes
        for(int i = 0; i < 24; i++)
        {
            curBar = (TextView)findViewById(timeBars.get(i));
            curBar.setBackgroundColor(Color.TRANSPARENT);
            curBar.setText("");
        }
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
    {

        ////////////////////////////////////////////////////////////////////////////////////////
        //Pull data from DB and put it in IF the data belongs to the selected day
        /*final*/ int s = cursor.getInt(cursor.getColumnIndex("STARTTIME"));
        /*final*/ int e = cursor.getInt(cursor.getColumnIndex("ENDTIME"));
        boolean state = false;
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
        if(state){
            final int id = cursor.getInt(cursor.getColumnIndex("ID"));
            index[countnum][0] = id;
            /*final*/ String not = cursor.getString(cursor.getColumnIndex("NOTES"));
            /*final*/ int color = cursor.getInt(cursor.getColumnIndex("MAJORCAT"));
            /*final*/ int min = cursor.getInt(cursor.getColumnIndex("MINORCAT"));
            /*final*/ String nam = cursor.getString(cursor.getColumnIndex("NAME"));
            /*final*/ int p = cursor.getInt(cursor.getColumnIndex("PRIORITY"));
            ////////////////////////////////////////////////////////////////////////////////////////
            //Set the boxes
            curBar = (TextView)findViewById(timeBars.get(s));
            index[countnum][1] = timeBars.get(s);
            curBar.setText(nam);
            for(int i = s; i < e; i++)
            {



                curBar = (TextView)findViewById(timeBars.get(i));
                int newcol = Goal.majorColors.get(color);
                curBar.setBackgroundColor(getResources().getColor(newcol));
                curBar.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view)
                    {
                        editing = true;
                        editID = id;
                        /*loadGoal(new Goal(id, color, min, nam, s, e, p, not));*/
                        loadGoal(id);
                    }
                });
            }
            countnum++;
        }}
        indexlength = countnum;
        /*minor0.setBackgroundResource(R.drawable.rounded_border);
        minor1.setBackgroundResource(R.drawable.rounded_border);
        minor2.setBackgroundResource(R.drawable.rounded_border);
        minor3.setBackgroundResource(R.drawable.rounded_border);
        minor4.setBackgroundResource(R.drawable.rounded_border);*/
    }
    public void loadGoal(int id)
    {
        ////////////////////////////////////////////////////////////////////////////////////////////
        //Load goal in from db
        Cursor cursor = dbhelp.getAllData();
        for( cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
        {
            int ident = cursor.getInt(cursor.getColumnIndex("ID"));
            if (ident==id)
            {
                String not = cursor.getString(cursor.getColumnIndex("NOTES"));
                int s = cursor.getInt(cursor.getColumnIndex("STARTTIME"));
                int e = cursor.getInt(cursor.getColumnIndex("ENDTIME"));
                int color = cursor.getInt(cursor.getColumnIndex("MAJORCAT"));
                int min = cursor.getInt(cursor.getColumnIndex("MINORCAT"));
                String nam = cursor.getString(cursor.getColumnIndex("NAME"));
                int p = cursor.getInt(cursor.getColumnIndex("PRIORITY"));
                String code = cursor.getString(cursor.getColumnIndex("CODECHAR"));
                int codn = cursor.getInt(cursor.getColumnIndex("CODENUM"));
                dc.setText(nam);
                Goal G = new Goal();
                dc.setBackgroundResource(Goal.majorBackgrounds.get(color));
                nt.setText(not);
                stime.setText(String.valueOf(s-(day*24)));
                etime.setText(String.valueOf(e-(day*24)));
                priority.setText(String.valueOf(p));
                cod.setText(code);
                codnum.setText(String.valueOf(codn));
                /*minor0.setBackgroundResource(R.drawable.rounded_border);
                minor1.setBackgroundResource(R.drawable.rounded_border);
                minor2.setBackgroundResource(R.drawable.rounded_border);
                minor3.setBackgroundResource(R.drawable.rounded_border);
                minor4.setBackgroundResource(R.drawable.rounded_border);*/
                System.out.println(min);
                spin.setSelection(min);
                /*switch (min) {
                    case 0:
                        minor0.setBackgroundResource(R.drawable.rounded_corner_rest);
                        break;
                    case 1:
                        minor1.setBackgroundResource(R.drawable.rounded_corner_rest);
                        break;
                    case 2:
                        minor2.setBackgroundResource(R.drawable.rounded_corner_rest);
                        break;
                    case 3:
                        minor3.setBackgroundResource(R.drawable.rounded_corner_rest);
                        break;
                    case 4:
                        minor4.setBackgroundResource(R.drawable.rounded_corner_rest);
                        break;
                }*/
            }
        }
    }
    public void switchDay(int nd)
    {
        Intent dv = new Intent(DayView.this, DayView.class);
        dv.putExtra(MainActivity.DAY_ID, nd);
        startActivity(dv);
    }
    public void goToWeek()
    {
        Intent dv = new Intent(DayView.this, MainActivity.class);
        startActivity(dv);
    }
    public void setminorcategories(int cat)
    {
        //boolean into = false;
        //int g = 0;
        /*minor0.setText(Goal.minorCats[cat][0]);
        minor1.setText(Goal.minorCats[cat][1]);
        minor2.setText(Goal.minorCats[cat][2]);
        minor3.setText(Goal.minorCats[cat][3]);
        minor4.setText(Goal.minorCats[cat][4]);*/
        String queryminor = "select ID from MinorCat where MajorCatID = '" + cat + "'";
        ResultSet userResult = db.getDataTable(queryminor);
        try{
            if(userResult.next()) {
                int starID = userResult.getInt(1);
                System.out.println("starID " + starID + "for cat " + cat);
            }

        }
        catch (Exception e)
        {
            System.err.println(e);
        }
        List<String> spinAdd = new ArrayList<String>();
        queryminor = "select Name from MinorCat where MajorCatID = '" + cat + "'";
        userResult = db.getDataTable(queryminor);
        try{
            while (userResult.next()) {
                /*Toast.makeText(this, "Brando good", Toast.LENGTH_SHORT).show();*/
                System.out.println("loop# " );
                spinAdd.add(userResult.getString("Name"));
                //g++;
            }
           // else()
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(DayView.this, android.R.layout.simple_spinner_item, spinAdd);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(dataAdapter);
        spin.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("pos = "+ i);
                goal_m_id = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public void selections(){
        for(int i = 0; i < indexlength; i++)
        {
            TextView dehigh = (TextView)findViewById(index[i][1]);
            if(i == curindex) {
                dehigh.setTextColor(Color.WHITE);
                loadGoal(index[i][0]);
            }
            else
                dehigh.setTextColor(Color.BLACK);
        }
    }
}
