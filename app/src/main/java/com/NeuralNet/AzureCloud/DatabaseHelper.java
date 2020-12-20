package com.NeuralNet.AzureCloud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static User user;
    public static final String DATABASE_NAME = "Day.db";
    public static final String MON_TABLE = "This_day";
    public static final String USERINFO = "User_info";
    public static final String SURVEYANS = "Survey_Answers";
    public static final String COL_1 = "NAME";
    public static final String COL_2 = "MAJORCAT";
    public static final String COL_3 = "MINORCAT";
    public static final String COL_4 = "STARTTIME";
    public static final String COL_5 = "ENDTIME";
    public static final String COL_6 = "PRIORITY";
    public static final String COL_7 = "NOTES";
    public static final String COL_8 = "BRANDCODE";
    public static final String COL_9 = "PASSWORD";
    public static final String COL_10 = "GUIDEID";
    public static final String COL_11 = "QUESTION";
    public static final String COL_12 = "ANSWER";
    public static final String COL_13 = "CODECHAR";
    public static final String COL_14 = "CODENUM";
    public static final String COL_15 = "LWEEK";
    public static final String COL_16 = "UID";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + MON_TABLE +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,MAJORCAT INTEGER,MINORCAT INTEGER, STARTTIME INTEGER, ENDTIME INTEGER,PRIORITY INGEGER, NOTES TEXT, CODECHAR TEXT, CODENUM INTEGER)");
        db.execSQL("create table " + USERINFO +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, BRANDCODE TEXT, PASSWORD TEXT, GUIDEID INTEGER, LWEEK INTEGER, UID INTEGER)");
        db.execSQL("create table " + SURVEYANS +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, QUESTION INTEGER, ANSWER INTEGER, MAJORCAT INTEGER, MINORCAT INTEGER)");
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_8, "null");
        contentValues.put(COL_9, "null");
        db.insert(USERINFO, null, contentValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+MON_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+USERINFO);
        db.execSQL("DROP TABLE IF EXISTS "+SURVEYANS);
        onCreate(db);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //DEBUG FUNCTION ONLY, DO NOT INCLUDE IN PRODUCTION RELEASE
    //RESETS THE WHOLE DATABASE, ALL DATA LOST!!!!111!!!1111!!1
    //DO NOT SPAGHETTI
    public void resetDB()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+MON_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+USERINFO);
        db.execSQL("DROP TABLE IF EXISTS "+SURVEYANS);
        onCreate(db);
    }
    public void addUID (int UID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_16,UID);
        long res = db.update(USERINFO, contentValues, "ID = ?",new String[] { "1" });
    }

    public int getUID(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select " + COL_16 + " from " + USERINFO, null);
        res.moveToFirst();
        return res.getInt(0);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    public boolean insertData(String name,int majorcat,int minorcat, int stime, int etime, int priority, String notes, String cod, int num) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,name);
        contentValues.put(COL_2,majorcat);
        contentValues.put(COL_3,minorcat);
        contentValues.put(COL_4, stime);
        contentValues.put(COL_5, etime);
        contentValues.put(COL_6, priority);
        contentValues.put(COL_7, notes);
        contentValues.put(COL_13, cod);
        contentValues.put(COL_14, num);
        long result = db.insert(MON_TABLE,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+MON_TABLE,null);
        return res;
    }

    public Cursor getLatestTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+MON_TABLE+" WHERE ID = (SELECT MAX(ID) FROM "+MON_TABLE +")" ,null);
        return res;
    }

    public boolean updateData(String id, String name,int majorcat, int minorcat, int stime, int eTime, int priority, String notes, String cod, int num) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,name);
        contentValues.put(COL_2,majorcat);
        contentValues.put(COL_3,minorcat);
        contentValues.put(COL_4,stime);
        contentValues.put(COL_5, eTime);
        contentValues.put(COL_6, priority);
        contentValues.put(COL_7, notes);
        contentValues.put(COL_13, cod);
        contentValues.put(COL_14, num);
        db.update(MON_TABLE, contentValues, "ID = ?",new String[] { id });
        return true;
    }

    public boolean login(String brandingCode, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Calendar cal = Calendar.getInstance();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_8, brandingCode);
        contentValues.put(COL_9, password);
        contentValues.put(COL_15, cal.get(Calendar.WEEK_OF_YEAR));
        long res = db.update(USERINFO, contentValues, "ID = ?",new String[] { "1" });
        return true;
    }

    public boolean enterGuide (int gID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_10, gID);
        long res = db.update(USERINFO, contentValues, "ID = ?",new String[] { "1" });
        return true;
    }

    public Cursor getQuestions ()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+SURVEYANS, null);
        return res;
    }

    public String getBrandingCode() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select " + COL_8 + " from " + USERINFO, null);
        res.moveToFirst();
        String s = res.getString(0);
        return s;
    }

    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(MON_TABLE, "ID = ?",new String[] {id});
    }
    public boolean firstTime(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select " + COL_8 + " from " + USERINFO, null);
        res.moveToFirst();
        String s = res.getString(0);
        return (s.equals("null"));
    }
    //return true if a week has passed
    public boolean checkdates(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select " + COL_15 + " from " + USERINFO, null);
        res.moveToFirst();
        int lasDate = res.getInt(0);
        ContentValues contentValues = new ContentValues();
        Calendar cal = Calendar.getInstance();
        contentValues.put(COL_15, cal.get(Calendar.WEEK_OF_YEAR));
        long ret = db.update(USERINFO, contentValues, "ID = ?",new String[] { "1" });
        if(lasDate < cal.get(Calendar.WEEK_OF_YEAR))
            return true;
        else
            return false;

    }

    public boolean insertSurveyResults (ArrayList<Integer> name, ArrayList<Integer> val)
    {
        int len = name.size();
        SQLiteDatabase db = this.getWritableDatabase();
        for(int i = 0; i < len; i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_11, name.get(i));
            contentValues.put(COL_12, val.get(i));
            long result = db.insert(SURVEYANS, null, contentValues);
            if(result == -1)
                return false;
        }
        return true;

    }

    public static void setUser(User U)
    {
        user = U;
    }

    public static User getUser()
    {
        return user;
    }
}