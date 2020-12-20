package com.NeuralNet.AzureCloud;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.teamcarl.prototype.R;

public class EditUserInfo extends Activity{
    TextView editUserReturnBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user_info);

        editUserReturnBtn = findViewById(R.id.editUserReturnBtn);
        //testing

        //Return button
        editUserReturnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditUserInfo.this, AboutMe.class);
                startActivity(intent);
            }
        });


    }
}
