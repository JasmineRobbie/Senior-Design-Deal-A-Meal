package com.NeuralNet.AzureCloud;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.teamcarl.prototype.R;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HistoryLog extends Activity {

    ListView historyLogList;
    DataAccess db = new DataAccess();
    User user;
    TextView totalCalories1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
       // user = (User) intent.getExtras().getSerializable("User");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_log);

        class historyLogInfo {
            String description;
            String calories;

            public historyLogInfo(String description, String calories) {
                this.description = description;
                this.calories = calories;
            }

            public String getDescription() {
                return description;
            }

            public String getCalories() {
                return calories;
            }
        }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.listview);
            historyLogList = (ListView) findViewById(R.id.historyLogListview);
            final List<historyLogInfo> historyMeals = new ArrayList<>();

            String query = "SELECT Description, Units_Calories FROM EatenMeals WHERE UserId =" + User.UserID;
            System.out.println(User.UserID);

            final ResultSet results = db.getDataTable(query);

        try {

            while(results.next()){

                historyLogInfo historyLog = new historyLogInfo(results.getString(1), results.getString(2));
                historyMeals.add(historyLog);

                adapter.add(historyLog.getDescription() + "    " + "Calories: " + historyLog.getCalories());
                //adapter.add(historyLog.getCalories());

                String test1 = results.getString(1);
                String test2 = results.getString(2);
                System.out.println(test1 + test2);

            }

        } catch(Exception e) {
            System.err.println(e);
        }

        historyLogList.setAdapter(adapter);
        totalCalories1 = (TextView) findViewById(R.id.totalCaloriesTextview);

        int totalCalories = 0;
        int tempCalories = 0;
        for (int i = 0; i < historyMeals.size(); i++) {
            tempCalories = Integer.parseInt(historyMeals.get(i).getCalories());
            totalCalories += tempCalories;
        }

        totalCalories1.setText("Total Calories: " + totalCalories);

    }
}