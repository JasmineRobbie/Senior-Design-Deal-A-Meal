package com.NeuralNet.AzureCloud;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
        final int senderId = intent.getExtras().getInt("messageSender");
        final int receiverId = intent.getExtras().getInt("messageReceiver");
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


        replyBtn = findViewById(R.id.replyBtn);

        replyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FullMessage2.this, ReplyMessage.class);

                int newSender = receiverId;
                int newReceiver = senderId;

                System.out.println("Receiver ID: " + receiverId);
                System.out.println("Sender ID: " + senderId);

                System.out.println("New Receiver: " + newReceiver);
                System.out.println("New Sender: " + newSender);

                intent.putExtra("NewReceiver", newReceiver);
                intent.putExtra("NewSender", newSender);


                startActivity(intent);




            }
        });

        deleteBtn = findViewById(R.id.deleteBtn);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();

                DataAccess db = new DataAccess();

                int messageId = intent.getExtras().getInt("messageId");

                try{

                    String query = "DELETE FROM Message WHERE MessageId =" + messageId;

                    String result = db.executeNonQuery(query);
                    if(Integer.parseInt(result) > 0)
                        Toast.makeText(getApplicationContext(), "Message Deleted Sucessfully", Toast.LENGTH_SHORT).show();
                    else
                        throw new Error("Error updating db. Message was not able to get deleted. ");
                    finish();


                }catch(Exception e){
                    System.err.println(e);
                }

            }
        });



    }






}