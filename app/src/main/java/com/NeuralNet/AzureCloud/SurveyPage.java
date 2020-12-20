package com.NeuralNet.AzureCloud;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.teamcarl.prototype.R;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SurveyPage extends Activity {
    private DataAccess db = new DataAccess();
    private int guideIDToSend = -1;
    Context context = this;
    private DatabaseHelper dbh = new DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        System.out.println("surveyhit");
        Intent intent = getIntent();
        int guideid = intent.getIntExtra("guide_id", 0);
        setContentView(R.layout.survey_question_show);
        final LinearLayout lin = (LinearLayout) findViewById(R.id.SurveyQuestionShowLayout);
        final LayoutInflater inflate = LayoutInflater.from(this);
        String queryminor;
        final List<Integer> guideIDList = new ArrayList<Integer>();
        final List<View> inflators = new ArrayList<View>();
        final List<Integer> quesList = new ArrayList<Integer>();
        final List<String> qGoalNames = new ArrayList<String>();
        queryminor = "select QUESTIONTEXt, QuestionId from Question where GuideId = '" + guideid + "' order by Sequence ASC";
        final ResultSet userResult = db.getDataTable(queryminor);
        try{
            while (userResult.next()) {
                /*Toast.makeText(this, "Brando good", Toast.LENGTH_SHORT).show();*/
                System.out.println("loop# " );
                final View questin = inflate.inflate(R.layout.survey_individual_question, null);
                inflators.add(questin);
                final TextView survName = questin.findViewById(R.id.surveyQuestionText);
                survName.setText(userResult.getString("QUESTIONTEXt"));
                qGoalNames.add(userResult.getString("QUESTIONTEXt"));
                quesList.add(userResult.getInt("QuestionId"));
                //guideIDList.add(userResult.getInt("GuideId"));
               /* guidein.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int cc = lin.getChildCount();
                        for(int i = 0; i < cc; i++) {
                            View v = lin.getChildAt(i);
                            if(v == guidein)
                                guideIDToSend = guideIDList.get(i);
                            if(v == guidein)
                                System.out.println("match found" + i);
                            v.setBackgroundResource(R.drawable.rounded_border);
                        }
                        guidein.setBackgroundResource(R.drawable.rounded_corner_rest);
                    }
                });*/

                lin.addView(questin);
                //g++;
            }
            // else()
        }
        catch (Exception e)
        {
            System.err.println(e);
        }

        Button subButton = (Button) findViewById(R.id.SurveySubmitButton);
        subButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Integer> questionNames = new ArrayList<Integer>();
                ArrayList<Integer> surveyans = new ArrayList<Integer>();
                int cCount = lin.getChildCount();
                for(int i = 0; i < cCount; i++) {
                    LinearLayout lay = (LinearLayout) lin.getChildAt(i);
                    RadioGroup rad =  (RadioGroup) lay.getChildAt(1);
                    int select = rad.getCheckedRadioButtonId();
                    RadioButton ans;
                        ans = rad.findViewById(select);
                        System.out.println("INFLATOR # " + i);
                        TextView t = (TextView) lay.getChildAt(0);
                        questionNames.add(quesList.get(i));
                        if(select != -1) {
                            String ansText = ans.getText().toString();
                            if (ansText.equals("Yes"))
                                surveyans.add(1);
                            if (ansText.equals("No"))
                                surveyans.add(0);
                            else
                                surveyans.add(2);

                        }
                    else
                        surveyans.add(2);
                }
                dbh.insertSurveyResults(questionNames, surveyans);
                for(int i = 0; i < questionNames.size(); i++)
                {
                    if(surveyans.get(i) == 1)
                    {
                        String getMinor = "select Minor from TemplateGoals where QuestionId = '" + questionNames.get(i) + "'";
                        ResultSet minorcatTab = db.getDataTable(getMinor);
                        try{
                            if(minorcatTab.next())
                            {
                                int minorcat = minorcatTab.getInt("Minor");
                                String getMajor = "select MajorCatID from MinorCat where ID = '" + minorcat + "'";
                                ResultSet majorcattab = db.getDataTable(getMajor);
                                try
                                {
                                    if(majorcattab.next())
                                    {
                                        int majorcat = majorcattab.getInt("MajorCatID");
                                        String getMajorMinIndex = "select ID from MinorCat where MajorCatID = '" + majorcat + "'";
                                        ResultSet majMinInd = db.getDataTable(getMajorMinIndex);
                                        try {
                                            if(majMinInd.next())
                                                minorcat -= majMinInd.getInt("ID");
                                                dbh.insertData(qGoalNames.get(i), majorcat, minorcat, i + 1, i + 2, 5, "", "", 0);
                                            }
                                        catch(Exception e)
                                        {
                                            System.err.println(e);
                                        }
                                        }
                                }
                                catch(Exception e)
                                {
                                    System.err.println(e);
                                }
                            }
                        }
                        catch(Exception e){
                            System.err.println(e);
                        }
                    }
                }
                Intent dv = new Intent(context, MainActivity.class);
                startActivity(dv);
            }
        });
    }
}
