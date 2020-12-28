package com.NeuralNet.AzureCloud;
import android.os.StrictMode;
//import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import android.support.v7.app.ActionBarActivity;


/**
 * Created by Tank Residents on 12/10/2016.
 */

public class DataAccess implements Serializable{
    Connection con;
    String un,pass,db,ip;

    public DataAccess(){
        un = "AIMobile";
        pass = "pwLifeCoach2021";
        db = "AGI_LifeCoach";
        ip = "lifecoach2021.database.windows.net/";
    }

    public String executeScalarQuery(String query) // expect only one row and cowlumn to be returned
    {
        try{
            con = connectionclass(un, pass, db, ip);
            if (con == null)
            {
                return "Check Your Internet Access!";
            }
            else
            {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                String result = rs.getString(0);
                return result;
            }
        }
        catch (Exception e)
        {
            return e.getMessage();
        }
    }

    public String executeProcScalar(String query) // expect only one row and cowlumn to be returned
    {
        String resultText = "";
        try{
            con = connectionclass(un, pass, db, ip);
            if (con == null)
            {
                return "Check Your Internet Access!";
            }
            else
            {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                if (rs != null && rs.next()) {
                    resultText = rs.getString("ss");
                }
                return resultText;
            }
        }
        catch (Exception e)
        {
            return e.getMessage();
        }
    }

    public String executeNonQuery(String query) // expect only one row and cowlumn to be returned
    {
        try{
            con = connectionclass(un, pass, db, ip);
            if (con == null)
            {
                return "Error: Check Your Internet Access!";
            }
            else
            {
                Statement stmt = con.createStatement();
                int result = stmt.executeUpdate(query);
                return Integer.toString(result);
            }
        }
        catch (Exception e)
        {
            return "error: " + e.getMessage();
        }
    }

    public ResultSet getDataTable(String query) // expect only one row and cowlumn to be returned
    {
        try{
            con = connectionclass(un, pass, db, ip);
            if (con == null)
            {
                // error "Check Your Internet Access!";
                Log.e("connection is failing", "git gud dummy");
            }
            else
            {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                return rs;
            }
            con.close();
            con = null;
        }
        catch (SQLException se)
        {
            Log.e("error here 1 : ", se.getMessage());
        }
        catch (Exception e)
        {
            Log.e("error here 2 : ", e.getMessage());
            //return e.getMessage();
        }
        return null;
    }

    public Connection connectionclass(String user, String password, String database, String server)
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://" + server + database + ";user=" + user+ ";password=" + password + ";";
            connection = DriverManager.getConnection(ConnectionURL);
        }
        catch (SQLException se)
        {
            Log.e("error here 1 : ", se.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            Log.e("error here 2 : ", e.getMessage());
        }
        catch (Exception e)
        {
            Log.e("error here 3 : ", e.getMessage());
        }
        return connection;
    }
}
