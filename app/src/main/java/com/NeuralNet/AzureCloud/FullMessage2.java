package com.NeuralNet.AzureCloud;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.teamcarl.prototype.R;

import org.w3c.dom.Text;

public class FullMessage2 extends Activity {


    Button backBtn, replyBtn, deleteBtn;
    TextView subjectBox, messageBox;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_message2);


        Intent intent = getIntent();

        String Body = intent.getExtras().getString("messageBody");
        String Subject = intent.getExtras().getString("messageSubject");
        int senderId = intent.getExtras().getInt("messageSender");
        int receiverId = intent.getExtras().getInt("messageReceiver");
        int priority = intent.getExtras().getInt("messagePriority");

        user = (User)intent.getExtras().getSerializable("User");

        subjectBox = findViewById(R.id.subjectBox);
        messageBox = findViewById(R.id.messageBox);

        messageBox.setText(Body);
        subjectBox.setText(Subject);

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }





}