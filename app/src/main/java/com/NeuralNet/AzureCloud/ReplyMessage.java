package com.NeuralNet.AzureCloud;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.teamcarl.prototype.R;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

public class ReplyMessage extends Activity {

    EditText messageBody, subjectBody;
    Button sendReplyBtn;
    TextView confirmationReplyBox;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_message);

        DataAccess db = new DataAccess();
        User user;
        messageBody = findViewById(R.id.ReplyMessageBox);
        subjectBody = findViewById(R.id.replySubjectBox);
        sendReplyBtn = findViewById(R.id.sendReplyBtn);

        Intent intent = getIntent();

        class messageInformation{
            int SenderUserId;
            int ReceiverUserId;
            String body;
            String Subject;


            public messageInformation(int sendUserId, int receiverUserId, String body, String subject){
                this.SenderUserId = sendUserId;
                this.ReceiverUserId = receiverUserId;
                this.body = body;
                this.Subject = subject;
            }

            public String getBody(){return body;}
            public int getSender(){return SenderUserId;}
            public int getReceiver(){return ReceiverUserId;}
            public String getSubject(){return Subject;}

        }


        int senderId = intent.getExtras().getInt("NewSender");
        int receiverId = intent.getExtras().getInt("NewReceiver");

        String query = "SELECT FirstName FROM USERS WHERE UserId = " + receiverId;

        final ResultSet rs = db.getDataTable(query);

        confirmationReplyBox = findViewById(R.id.confirmReply);

        try{

            if(rs.next()){
                confirmationReplyBox.setText("Reply to: " + rs.getString(1));
            }


        }catch(Exception e){
            System.err.println(e);
        }



        sendReplyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();

                int senderId = intent.getExtras().getInt("NewSender");
                int receiverId = intent.getExtras().getInt("NewReceiver");

                String messageBodyTemp = messageBody.getText().toString();
                String subjectBodyTemp = subjectBody.getText().toString();


                sendReply(senderId, receiverId, messageBodyTemp,subjectBodyTemp);
            }
        });





    }


    public void sendReply(int senderID, int receiverId, String messageBody, String subjectBody){


        try{

            DataAccess db = new DataAccess();
            String query = "INSERT INTO MESSAGE (SenderUserId, ReceiverUserId, Subject, Body)\n" +
                    "VALUES('" + senderID + "','" + receiverId +"','" + subjectBody +"','" + messageBody + "')";

            String result = db.executeNonQuery(query);
            if(Integer.parseInt(result) > 0)
                Toast.makeText(getApplicationContext(), "Message Sent Successfully!", Toast.LENGTH_SHORT).show();
            else
                throw new Error("Error updating db. Message was not able to send. ");
            finish();
        }catch(Exception e){
            System.err.println(e);
        }



    }
}