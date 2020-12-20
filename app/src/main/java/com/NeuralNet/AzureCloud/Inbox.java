package com.NeuralNet.AzureCloud;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.teamcarl.prototype.R;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Inbox extends Activity {



    ListView messageList;
    DataAccess db = new DataAccess();
    User user;

    Button newMessageButton;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        user = (User) intent.getExtras().getSerializable("User");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        class messageInformation{
            int SenderUserId;
            int ReceiverUserId;
            String body;
            int priority;
            String Subject;


            public messageInformation(int sendUserId, int receiverUserId, String body, String subject, int priority){
                this.SenderUserId = sendUserId;
                this.ReceiverUserId = receiverUserId;
                this.body = body;
                this.priority = priority;
                this.Subject = subject;
            }

            public String getBody(){return body;}
            public int getSender(){return SenderUserId;}
            public int getReceiver(){return ReceiverUserId;}
            public String getSubject(){return Subject;}
            public int getPriority(){return priority;}
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.listview);
        messageList = (ListView) findViewById(R.id.messageListNew);
        final List<messageInformation> userMessage = new ArrayList<>();

        String query = "SELECT SenderUserId, ReceiverUserId, Subject, Body, Priority FROM Message WHERE ReceiverUserId =" + user.UserID;

        final ResultSet results = db.getDataTable(query);

        try{

            while(results.next()){

                messageInformation messageInformation = new messageInformation(results.getInt(1), results.getInt(2), results.getString(4), results.getString(3), results.getInt(5));

                userMessage.add(messageInformation);

                adapter.add(messageInformation.getSubject());

                System.out.println(messageInformation.getSubject());

            }

        }catch(Exception e){
            System.err.println(e);
        }

        messageList.setAdapter(adapter);



        messageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(Inbox.this, FullMessage2.class);
                String body = userMessage.get(position).getBody();
                int senderId = userMessage.get(position).getSender();
                int receiverId = userMessage.get(position).getReceiver();
                int priority = userMessage.get(position).getPriority();
                String subject = userMessage.get(position).getSubject();

                intent.putExtra("messageBody", body);
                intent.putExtra("messageSender", senderId);
                intent.putExtra("messageReceiver", receiverId);
                intent.putExtra("messagePriority", priority);
                intent.putExtra("messageSubject", subject);
                intent.putExtra("User", user);

                startActivity(intent);
            }
        });


        newMessageButton = findViewById(R.id.newMessageBtn);


        newMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newMessageActivity();
            }
        });
    }


    public void newMessageActivity(){
        Intent intent = new Intent(this, NewMessage.class);
        intent.putExtra("User", user);
        startActivity(intent);
    }
}