package com.NeuralNet.AzureCloud;

import com.teamcarl.prototype.R;

import java.util.*;

public class Goal {
    private int ID;
    private int major;
    private int minor;
    private String name;
    private int start;
    private int end;
    private int priority;
    private String notes;
    //enum from 0-7 for all the values
    public static Hashtable<Integer, String> majorValues = new Hashtable<Integer, String>();
    //enum for the colors for all the major categories, this one is finished
    public static Hashtable<Integer, Integer> majorColors = new Hashtable<Integer, Integer>();
    //enum for the colors for major categories when rounded box backgrounds are involved
    public static Hashtable<Integer, Integer> majorBackgrounds = new Hashtable<Integer, Integer>();
    //enum for all minor categories from category 0 (Mindset)
    public static String minorCats[][] = {
            /*MINDSET*/{"Goal Related", "Future Planning", "Focus", "Reflection", "Growth"},
            /*EDUCATION*/{"Courses", "Research", "Self Improvement", "Continuous Learning", "Trade"},
            /*SOCIAL*/{"Friends","Family","Professional","Relationship","Support Network"},
            /*DUTIES*/{"Chores","Appointments","Commitments","Tasks", "Other"},
            /*HEALTH*/{"Physical Activity", "Mental Health", "Emotional Health", "Diet", "Environment"},
            /*TRANQUILITY*/{"Hobbies","Vacation","Down Time", "Relaxation", "Fun"},
            /*OTHER*/{"Personal", "Academic", "Professional", "Social", "Health"},
    };
    //This will be decided on later and is not necessary now
    public static Hashtable<Integer, String> minorValues0 = new Hashtable<Integer, String>();
    public Goal(){
        ////////////////////////////////////////////////////////////////////////////////////////////
        //Instantiate enums
        majorColors.put(0, R.color.mindsetColor);
        majorColors.put(1, R.color.educationColor);
        majorColors.put(2, R.color.socialColor);
        majorColors.put(3, R.color.dutiesColor);
        majorColors.put(4, R.color.healthColor);
        majorColors.put(5, R.color.tranquilityColor);
        majorColors.put(6, R.color.restColor);

        majorValues.put(0, "Mindset");
        majorValues.put(1, "Education");
        majorValues.put(2, "Social");
        majorValues.put(3, "Duties");
        majorValues.put(4, "Health");
        majorValues.put(5, "Tranquility");
        majorValues.put(6, "Rest");

        majorBackgrounds.put(0, R.drawable.rounded_corner_mind);
        majorBackgrounds.put(1, R.drawable.rounded_corner_edu);
        majorBackgrounds.put(2, R.drawable.rounded_corner_soc);
        majorBackgrounds.put(3, R.drawable.rounded_corner_dut);
        majorBackgrounds.put(4, R.drawable.rounded_corner_heal);
        majorBackgrounds.put(5, R.drawable.rounded_corner_tranq);
        majorBackgrounds.put(6, R.drawable.rounded_corner_rest);



        //TODO: Map the rest of the minor values
    }
    public Goal (int inId, int inMajor, int inMinor, String inName, int inStart, int
            inEnd, int inPriority, String inNotes)
    {
        ////////////////////////////////////////////////////////////////////////////////////////////
        //Create a goal object, and instantiate goal values
        ID = inId;
        major = inMajor;
        minor = inMinor;
        name = inName;
        start = inStart;
        end = inEnd;
        priority = inPriority;
        notes = inNotes;
        Goal G = new Goal();


    }

}
