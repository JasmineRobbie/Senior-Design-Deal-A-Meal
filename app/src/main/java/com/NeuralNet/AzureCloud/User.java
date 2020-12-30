package com.NeuralNet.AzureCloud;

import android.content.Context;
import android.util.Log;

import com.NeuralNet.AzureCloud.DataAccess;
import com.NeuralNet.AzureCloud.InternalDataAccess;

import java.io.Serializable;
import java.sql.ResultSet;

//import android.support.annotation.NonNull;


/**
 * Created by Tank Residents on 12/10/2016.
 */

public class User implements Serializable {

    public static String UserID;
    public static String Permissions;
    public static String Password;
    public static String FirstName;
    public static String LastName;
    public static String BrandNumber;
    public static String BrandLink;
    public static String BrandName;
    public static String Email;
    public static String PhoneNumber;
    public static String Country;
    public static String State;
    public static String City;
    public static String Street;
    //newly added
    public static String BMI;
    public static String Weight;
    public static String Goal;
    public static String Guide;
    public static String AllowedCalories;
    public static String Allergies;


    public boolean isAnonymous;


    public User(String UserID, Context context)
    {
        this.UserID = UserID;
        setUserInfo(UserID, context);

    }
    private void setUserInfo(String UserID, Context context)
    {
        DataAccess db = new DataAccess();
        InternalDataAccess ida = new InternalDataAccess();

        try {
            String query = "\n" +
                    "SELECT ISNULL(Permissions,''), ISNULL(Email,''), StayAnonymous, ISNULL(FirstName,''), ISNULL(LastName,''), ISNULL(b.BrandId,'')\n" +
                    ", ISNULL(b.BrandName,'')\n" +
                    ", ISNULL(b.BrandLink,'')\n" +
                    ", ISNULL(PhoneNumber,'')\n" +
                    ", ISNULL(Country,'')\n" +
                    ", ISNULL(State,'')\n" +
                    ", ISNULL(City,'')\n" +
                    ", ISNULL(StreetAddress,'')\n" +
                    ", ISNULL(Password,'')\n" +
                    //newly added
                    ", ISNULL(BMI,'')\n" +
                    ", ISNULL(Weight,'')\n" +
                    ", ISNULL(Goal,'')\n" +
                    ", ISNULL(Guide,'')\n" +
                    ", ISNULL(AllowedCalories,'')\n" +
                    ", ISNULL(Allergies,'')\n" +

                    "FROM USERS u\n" +
                    "JOIN BRAND b ON u.brandNumber = b.brandId\n" +
                    "WHERE userid = '" + UserID + "'";
            ResultSet result = db.getDataTable(query);
            if(result.next()) {
                Permissions = result.getString(1);
                Email = result.getString(2);
                isAnonymous = result.getBoolean(3);
                FirstName = result.getString(4);
                LastName = result.getString(5);
                BrandNumber = result.getString(6);
                BrandName = result.getString(7);
                BrandLink = result.getString(8);
                PhoneNumber = result.getString(9);
                Country = result.getString(10);
                State = result.getString(11);
                City = result.getString(12);
                Street = result.getString(13);
                Password = result.getString(14);
                //Newly added
                BMI = result.getString(15);
                Weight = result.getString(16);
                Goal = result.getString(17);
                Guide = result.getString(18);
                AllowedCalories = result.getString(19);
                Allergies = result.getString(20);
            }
        }
        catch(Exception e)
        {
            Log.e("error in setUserInfo", e.getMessage());
        }
        if(BrandNumber == null || BrandNumber.length() == 0)
            BrandNumber = ida.getSharedPreferencesKeyValue(context, "brand_number");
        if(BrandName == null || BrandName.length() == 0)
            BrandName = ida.getSharedPreferencesKeyValue(context, "brand_name");
        if(BrandLink == null || BrandLink.length() == 0)
            BrandLink = ida.getSharedPreferencesKeyValue(context, "brand_link");
    }

}