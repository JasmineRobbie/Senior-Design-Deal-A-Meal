package com.NeuralNet.AzureCloud;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.teamcarl.prototype.R;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GuideContent extends Fragment {

    Random r;
    InternalDataAccess ida = new InternalDataAccess();
    ImageButton newMessage, qBack, qNext, evaluate;
    TextView questionLabel, questionNum;
    Spinner severity;
    ArrayAdapter<String> severityAdapter;
    int rowCount, columnCount;
    Spinner feeling;
    ArrayAdapter<String> feelingAdapter;
    Question currentQuestion;
    User user;
    int questionId, randy;
    boolean isLastQuestion;
    String guideName, Questnum;

    // constructor
    public static final GuideContent newInstance(User user, String questionId, boolean isLastQuestion)
    {
        GuideContent f = new GuideContent();
        Bundle bdl = new Bundle(3);
        bdl.putSerializable("User", user);
        bdl.putInt("QuestionId", Integer.valueOf(questionId));
        bdl.putBoolean("IsLastQuestion", isLastQuestion);
        f.setArguments(bdl);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        user = (User)getArguments().getSerializable("User");
        guideName = getArguments().getString("guideId");
        questionId = getArguments().getInt("QuestionId");
        isLastQuestion = getArguments().getBoolean("IsLastQuestion");
        currentQuestion = new Question(questionId, user.UserID, isLastQuestion, getContext());
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_guide_content, container, false);
        return rootView;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            Activity a = getActivity();
            if(a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }
    @Override
    public void onStart(){
        super.onStart();
        View view = getView();

        // set labels/text/buttons
        questionLabel = (TextView) view.findViewById(R.id.questionLabel);
        questionLabel.setText(currentQuestion.QuestionText);
        questionNum=(TextView) view.findViewById(R.id.text_questionNum);
        questionNum.setText("Question #" + currentQuestion.QuestionId);

        newMessage=(ImageButton)view.findViewById(R.id.newMessage);
        evaluate=(ImageButton)view.findViewById(R.id.evaluate);
        qNext=(ImageButton)view.findViewById(R.id.btn_question_next);
        qBack=(ImageButton)view.findViewById(R.id.btn_question_back);

        severity = (Spinner) view.findViewById(R.id.severitySpinner);
        List<String> severityList = new ArrayList<String>();
        severityList.add("Purpose to Life - Extreme");
        severityList.add("Feel Very Strongly");
        severityList.add("Feel Strongly");
        severityList.add("Average (normal)");
        severityList.add("Feel Weakly");
        severityList.add("Feel Very Weakly");
        severityList.add("Does not matter to me");
        severityAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, severityList);
        severityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        severity.setAdapter(severityAdapter);

        feeling = (Spinner) view.findViewById(R.id.feelingSpinner);
        List<String> feelingList = new ArrayList<String>();
        feelingList.add("I Choose Not to Answer");
        feelingList.add("YES - TRUE");
        feelingList.add("NO - Not TRUE");
        feelingList.add("I Dont't Know / Undecided");
        feelingList.add("It is a Stupid Question");
        feelingAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, feelingList);
        feelingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        feeling.setAdapter(feelingAdapter);

        Questnum =  questionNum.getText().toString();
        // set spinners with previous answers if not null/empty
        // if response/feeling has previous answer history
        if(currentQuestion.Response != null && !currentQuestion.Response.isEmpty())
        {
            if(currentQuestion.Response.toLowerCase().contains("yes"))
            {
                feeling.setSelection(1, true);
            }
            else if (currentQuestion.Response.toLowerCase().contains("not true"))
            {
                feeling.setSelection(2, true);
            }
            else if (currentQuestion.Response.toLowerCase().contains("undecided"))
            {
                feeling.setSelection(3, true);
            }
            else if (currentQuestion.Response.toLowerCase().contains("not to answer"))
            {
                feeling.setSelection(0, true);
            }
            else if (currentQuestion.Response.toLowerCase().contains("stupid"))
            {
                feeling.setSelection(4, true);
            }
        }
        // if severity has previous question history selection
        if(currentQuestion.Severity != null && !currentQuestion.Severity.isEmpty())
        {
            if(currentQuestion.Severity.toLowerCase().contains("extreme"))
            {
                severity.setSelection(0, true);
            }
            else if (currentQuestion.Severity.toLowerCase().contains("very strongly"))
            {
                severity.setSelection(1, true);
            }
            else if (currentQuestion.Severity.toLowerCase().contains("feel strongly"))
            {
                severity.setSelection(2, true);
            }
            else if (currentQuestion.Severity.toLowerCase().contains("average"))
            {
                severity.setSelection(3, true);
            }
            else if (currentQuestion.Severity.toLowerCase().contains("feel weakly"))
            {
                severity.setSelection(4, true);
            }
            else if (currentQuestion.Severity.toLowerCase().contains("very weakly"))
            {
                severity.setSelection(5, true);
            }
            else if (currentQuestion.Severity.toLowerCase().contains("not matter"))
            {
                severity.setSelection(6, true);
            }
        }
        else // no severity saved, use default
        {
            severity.setSelection(3, true);
        }

        // button clicks
        newMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connectionTest()) {
                    String guideName = ida.getSharedPreferencesKeyValue(getContext(), "active_guide_name");
                    String guideEditor = ida.getSharedPreferencesKeyValue(getContext(), "active_guide_editor");
                    String msgSubject = guideName + " - " + currentQuestion.QuestionText;

                    Intent intent = new Intent(GuideContent.this.getActivity(), NewMessage.class);
                    intent.putExtra("MessageInfoCurrentUser", user);
                    intent.putExtra("MessageInfoSubject", msgSubject);
                    intent.putExtra("MessageInfoReceiverUser", guideEditor);

                    //intent.putExtra("Question", question);
                    startActivity(intent);
                }
                    else {
                    Toast.makeText(getContext(), "Cannot send message at this time. You must be online to send messages.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        evaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save question answers to user history
                saveQuestionAnswers();
                // exits to main purpose menu
                getActivity().finish();
            }
        });
        qBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save question answers to user history
                saveQuestionAnswers();

                ((Guide)getActivity()).onBackPressed();
            }
        });
        qNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save question answers to user history
                saveQuestionAnswers();

                // if it is not the last question in the guide, then goto next question
                if(!isLastQuestion)
                    ((Guide)getActivity()).onNextPressed();
                else // call evaluate function to have AI determine users next outcome
                    evaluate.callOnClick();
            }
        });
    }
    public void saveQuestionAnswers() {
        if (connectionTest()) {
            DataAccess db = new DataAccess();
            String query = "IF NOT EXISTS (\n" +
                    "\tSELECT * \n" +
                    "\tFROM HISTORY\n" +
                    "\twhere UserId = '" + user.UserID + "'\n" +
                    "\tand questionid = " + currentQuestion.QuestionId + "\n" +
                    ")\n" +
                    "BEGIN\n" +
                    "\tINSERT INTO HISTORY (questionid, psychoanalyst, severity, userid, response)\n" +
                    "\tVALUES (" + currentQuestion.QuestionId + ", '', '" + severity.getSelectedItem().toString().replace("'", "''") + "', '" + user.UserID + "', '" + feeling.getSelectedItem().toString().replace("'", "''") + "')\n" +
                    "END\n" +
                    "ELSE\n" +
                    "BEGIN\n" +
                    "\tUPDATE HISTORY\n" +
                    "\tSET severity = '" + severity.getSelectedItem().toString().replace("'", "''") + "'\n" +
                    "\t, response = '" + feeling.getSelectedItem().toString().replace("'", "''") + "'\n" +
                    "\twhere questionid = " + currentQuestion.QuestionId + "\n" +
                    "\t  and userid = '" + user.UserID + "'\n" +
                    "END";

            String result = db.executeNonQuery(query);

            // if no errors during database query
            if (!result.contains("error")) {

                // answer saved successfully, do nothing...
            } else {
                Toast.makeText(getActivity(), "Error saving question answers!", Toast.LENGTH_SHORT).show();
            }
        } else // offline mode
        {
            //reads guide file
            try {
                FileOutputStream outputStream;
                //writes to question file
                try {
                    // get string of data to save
                    String answerString = currentQuestion.QuestionId + "|" + severity.getSelectedItem().toString().replace("'", "''") + "|"
                            + user.UserID + "|" + feeling.getSelectedItem().toString().replace("'", "''") + "\r\n";
                    byte[] answerInfo = answerString.getBytes();
                    outputStream = getContext().openFileOutput("offline_history.txt", Context.MODE_APPEND);
                    outputStream.write(answerInfo);
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                Toast.makeText(getContext(), "Error writing offline answer for question. ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean connectionTest() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        } else
            return false;

    }
}
