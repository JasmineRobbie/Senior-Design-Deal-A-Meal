package com.NeuralNet.AzureCloud;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.teamcarl.prototype.R;

public class fullMessage extends Activity {

    Button btnReply, btnBack, btnDelete;
    String msgSubject, msgBody, msgFromUser;
    User currentUser;
    TextView subject, body;
    int messageId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        DataAccess db = new DataAccess();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_message);
        msgSubject = intent.getExtras().getString("messageSubject");
        msgBody = intent.getExtras().getString("messageBody");
        //messageId = Integer.parseInt(intent.getExtras().getString("MessageInfoId"));
        currentUser = (User) intent.getExtras().getSerializable("MessageInfoCurrentUser");
        msgFromUser = intent.getExtras().getString("MessageInfoFromUser");
        subject = (TextView) findViewById(R.id.subject);
        body = (TextView) findViewById(R.id.body);
        subject.setText(msgSubject);
        body.setText(msgBody);

        // set message as read because we are now reading current message id
        String query = "UPDATE MESSAGE " +
                "SET [read] = 1 " +
                "Where messageid = " + messageId;
        db.executeNonQuery(query);

        // reply to message, start reply
        btnReply=(Button)findViewById(R.id.reply);
        btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fullMessage.this, NewMessage.class);
                intent.putExtra("MessageInfoSubject", "RE: " + msgSubject);
                intent.putExtra("MessageInfoBody", msgBody);
                intent.putExtra("MessageInfoId", Integer.toString(messageId));
                intent.putExtra("MessageInfoReceiverUser", msgFromUser);
                intent.putExtra("MessageInfoCurrentUser", currentUser);
                startActivity(intent);
            }
        });

        // back button functionality
        btnBack=(Button)findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
