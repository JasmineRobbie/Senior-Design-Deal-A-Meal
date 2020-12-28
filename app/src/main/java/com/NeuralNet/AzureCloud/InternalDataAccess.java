package com.NeuralNet.AzureCloud;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.io.FileInputStream;
import java.util.regex.Pattern;

/**
 * Created by Tank Residents on 12/9/2017.
 */

public class InternalDataAccess {
        int rowCount, columnCount;

        public String getSharedPreferencesKeyValue(Context appContext, String key)
        {
            SharedPreferences sharedPref = appContext.getSharedPreferences("sharedData", appContext.MODE_PRIVATE);
            String value = sharedPref.getString(key, "");
            return value;
        }

        public void savePreferencesValue(Context appContext, String key, String value)
        {
            SharedPreferences sharedPref = appContext.getSharedPreferences("sharedData", appContext.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(key, value);
            editor.apply();
        }
    public String[][] fileStringDataToArray(String fileData) {
        String[][] dataTableArray = null;

        if(fileData != null && fileData.length() > 0) {
            String[] rows = fileData.split(Pattern.quote("\r\n"));
            String[] columns = rows[0].split(Pattern.quote("|"));

            columnCount = columns.length;
            rowCount = rows.length;

            dataTableArray = new String[rows.length][14];  // we will always use 14 columns until the active menu query to get questions data is changed.

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

        public void updateOfflineAnswers(Context appContext)
        {
            String userId, questionId, severity, feeling;

            // read data from answers file
            try {
                String[][] answerData = null;
                String answerText = "";
                FileInputStream fis1 = appContext.openFileInput("offline_history.txt");
                int size = fis1.available();
                byte[] buffer = new byte[size];
                fis1.read(buffer);
                fis1.close();
                answerText = new String(buffer);
                answerData = fileStringDataToArray(answerText);

                // for each row in data file, upload data to database
                for(int i = 0; i < answerData.length; i++) {
                    //String answerString = currentQuestion.QuestionId + "|" + severity.getSelectedItem().toString().replace("'", "''") + "|"
                            //+ user.UserID + "|" + feeling.getSelectedItem().toString().replace("'", "''") + "\r\n";
                    questionId = answerData[i][0];
                    severity = answerData[i][1];
                    userId = answerData[i][2];
                    feeling = answerData[i][3];

                    DataAccess db = new DataAccess();
                    String query = "IF NOT EXISTS (\n" +
                            "\tSELECT * \n" +
                            "\tFROM HISTORY\n" +
                            "\twhere UserId = '" + userId + "'\n" +
                            "\tand questionid = " + questionId + "\n" +
                            ")\n" +
                            "BEGIN\n" +
                            "\tINSERT INTO HISTORY (questionid, psychoanalyst, severity, userid, response)\n" +
                            "\tVALUES (" + questionId + ", '', '" + severity.replace("'", "''") + "', '" + userId + "', '" + feeling.replace("'", "''") + "')\n" +
                            "END\n" +
                            "ELSE\n" +
                            "BEGIN\n" +
                            "\tUPDATE HISTORY\n" +
                            "\tSET severity = '" + severity.replace("'", "''") + "'\n" +
                            "\t, response = '" + feeling.replace("'", "''") + "'\n" +
                            "\twhere questionid = " + questionId + "\n" +
                            "\t  and userid = '" + userId + "'\n" +
                            "END";

                    String result = db.executeNonQuery(query);

                    // if no errors during database query
                    if (!result.contains("error")) {

                    } else {
                        Toast.makeText(appContext, "Error uploading offline Question Answers!", Toast.LENGTH_SHORT).show();
                    }
                }
                // answer saved successfully, clear file...
                try {
                    appContext.deleteFile("offline_history.txt");
                } catch (Exception e) {
                }
            } catch (Exception e) {
                Toast.makeText(appContext, "Error getting offline Questions", Toast.LENGTH_SHORT).show();
            }
        }

}
