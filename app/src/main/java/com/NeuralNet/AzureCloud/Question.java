package com.NeuralNet.AzureCloud;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.sql.ResultSet;
import java.util.regex.Pattern;
import java.io.FileInputStream;

import static java.lang.Boolean.getBoolean;

/**
 * Created by Tank Residents on 9/23/2017.
 */

public class Question extends Activity {
    public int QuestionId;
    public int GuideId;
    public String Creater;
    public String Eligibility;
    public boolean Switch;
    public String QuestionText;
    public int QuestionLevel;
    public int LinkQuestion;
    public String ResponseTrigger;
    public int HistoryId;
    public String Psychoanalyst;
    public String Severity;
    public String UserId;
    public String Response;
    boolean isLastQuestion;

    private int rowCount, columnCount;
    private Context appContext;

    public Question(int QuestionId, String userid, boolean isLastQuestion, Context appContext)
    {
        this.appContext = appContext;
        UserId = userid;
        setQuestionInfo(QuestionId, isLastQuestion);
    }
    private void setQuestionInfo(int QuestionId, boolean isLastQuestion)
    {
        if (connectionTest()) {
            DataAccess db = new DataAccess();

            try {
                String query = "" +
                        "SELECT q.guideid, q.creater, q.eligibility\n" +
                        "\t\t,q.switch, q.questiontext, q.questionlevel\n" +
                        "\t\t,q.linkquestion, q.responsetrigger, h.historyid\n" +
                        "\t\t, h.psychoanalyst, h.severity, h.userid, h.response\n" +
                        "FROM QUESTION q\n" +
                        "LEFT JOIN History h on h.questionid = q.questionid AND h.userid = '" + UserId + "'\n" +
                        "where q.questionid = " + QuestionId;

                ResultSet result = db.getDataTable(query);
                if (result.next()) {
                    GuideId = result.getInt(1);
                    Creater = result.getString(2);
                    Eligibility = result.getString(3);
                    Switch = result.getBoolean(4);
                    QuestionText = result.getString(5);
                    QuestionLevel = result.getInt(6);
                    LinkQuestion = result.getInt(7);
                    ResponseTrigger = result.getString(8);
                    HistoryId = result.getInt(9);
                    Psychoanalyst = result.getString(10);
                    Severity = result.getString(11);
                    Response = result.getString(13);
                    this.QuestionId = QuestionId;
                    this.isLastQuestion = isLastQuestion;
                }
            } catch (Exception e) {
                Log.e("err in setQuestionInfo", e.getMessage());
            }
        }
        else { // read offline file
            //reads question file
            try {
                String[][] questionData = null;
                String questionText = "";
                FileInputStream fis1 = appContext.openFileInput("questions.txt");
                int size = fis1.available();
                byte[] buffer = new byte[size];
                fis1.read(buffer);
                fis1.close();
                questionText = new String(buffer);
                questionData = fileStringDataToArray(questionText);

                for (int i = 0; i < rowCount; i++) {
                    // if the current question we are getting data for...
                    // get that questions data
                    if(QuestionId == Integer.valueOf(questionData[i][0])) {
                        GuideId = Integer.valueOf(questionData[i][1]);
                        Creater = questionData[i][2];
                        Eligibility = questionData[i][3];
                        Switch = getBoolean(questionData[i][4]);
                        QuestionText = questionData[i][5];
                        QuestionLevel = Integer.valueOf(questionData[i][6]);
                        LinkQuestion = Integer.valueOf(questionData[i][7]);
                        ResponseTrigger = questionData[i][8];
                        HistoryId = Integer.valueOf(questionData[i][9]);
                        Psychoanalyst = questionData[i][10];
                        Severity = questionData[i][11];
                        UserId = questionData[i][12];
                        Response = questionData[i][13];
                        this.QuestionId = QuestionId;
                        this.isLastQuestion = isLastQuestion;
                    }
                }
            } catch (Exception e) {
                Log.e("err in setQuestionInfo", e.getMessage());
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
            dataTableArray = new String[rows.length][14];// we will always use 14 columns until the active menu query to get questions data is changed.

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
    public boolean connectionTest() {

        ConnectivityManager connectivityManager = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        } else
            return false;

    }
}
