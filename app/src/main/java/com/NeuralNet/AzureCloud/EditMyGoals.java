package com.NeuralNet.AzureCloud;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.teamcarl.prototype.R;

import java.sql.ResultSet;

public class EditMyGoals extends Activity{
    Button editMyGoalsReturnBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_my_goals);

        editMyGoalsReturnBtn = findViewById(R.id.editMyGoalsReturnBtn);

        //Return button
        editMyGoalsReturnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditMyGoals.this, MyGoals.class);
                startActivity(intent);
            }
        });

    }
}
