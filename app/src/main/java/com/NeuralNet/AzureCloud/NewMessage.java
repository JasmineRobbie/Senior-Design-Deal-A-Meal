package com.NeuralNet.AzureCloud;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.teamcarl.prototype.R;

import java.io.Serializable;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class NewMessage extends Activity implements Serializable {

    ImageButton sendMsg, backBtn;
    EditText subject, body;
    Spinner priority, userSpinner;
    ArrayAdapter<String> priorityAdapter;

    DataAccess db = new DataAccess();
    User user;
    String msgSubject, msgBody, msgReceiverUser, msgType, QuestionSubject, guideId;
    int messageId;
    public static Hashtable<String, Integer> userMapping = new Hashtable<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // get variables from previous activity
        Intent intent = getIntent();
        if(intent.getExtras().getSerializable("MessageInfoCurrentUser") != null)
            user = (User)intent.getExtras().getSerializable("MessageInfoCurrentUser");
        msgType = "";
        if(intent.getExtras().getString("MessageInfoSubject") != null)
            msgSubject = intent.getExtras().getString("MessageInfoSubject");
        if(intent.getExtras().getString("MessageInfoBody") != null)
            msgBody = intent.getExtras().getString("MessageInfoBody");
        if(intent.getExtras().getString("MessageInfoId") != null)
            messageId = Integer.parseInt(intent.getExtras().getString("MessageInfoId"));
        if(intent.getExtras().getString("MessageInfoReceiverUser") != null)
            msgReceiverUser = intent.getExtras().getString("MessageInfoReceiverUser");

        guideId = intent.getExtras().getString("guideId");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);
        // get activity UI controls
        subject = (EditText) findViewById(R.id.tbSubject);
        body = (EditText) findViewById(R.id.tbBody);
        // if reply, add RE: to subject...
        // set priority spinner (drop down)
        subject.setText(msgSubject);
        priority = (Spinner) findViewById(R.id.priority);
        userSpinner = (Spinner) findViewById(R.id.userSpinner);
        List<String> priorityList = new ArrayList<String>();
        final List<String> listUsers = new ArrayList<>();



        String query = "SELECT UserId, FirstName FROM USERS WHERE UserID < 6";
        final ResultSet result = db.getDataTable(query);

        try{

            while(result.next()){

                String name = result.getString(2);
                userMapping.put(name, result.getInt(1));
                listUsers.add(name);




            }

        }catch(Exception e){
            System.err.println(e);
        }

        for (int i = 0; i<userMapping.size(); i++){
            System.out.println(userMapping.get(i));
        }
        final ArrayAdapter<String> userLists = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listUsers);
        userLists.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        userSpinner.setAdapter(userLists);


        userSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //messageId = userMapping.get()

                int userIndex = i;


                String result = listUsers.get(userIndex);

                System.out.println(userMapping.get(result));

                msgReceiverUser = userMapping.get(result).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView){

            }


        });

        priorityList.add("Low Importance");
        priorityList.add("Medium Importance");
        priorityList.add("High Importance");
        priorityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, priorityList);
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priority.setAdapter(priorityAdapter);

        // send button click
        sendMsg = (ImageButton) findViewById(R.id.btnSend);
        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int msgPriority = 0;
                    if(priority.getSelectedItem().toString().toLowerCase().contains("high"))
                    {
                        msgPriority = 1;
                    }
                    else if(priority.getSelectedItem().toString().toLowerCase().contains("med"))
                    {
                        msgPriority = 2;
                    }
                    else if(priority.getSelectedItem().toString().toLowerCase().contains("low"))
                    {
                        msgPriority = 3;
                    }
                    // query to insert into MESSAGE table.
                    String query = "INSERT INTO MESSAGE (SenderUserId, ReceiverUserId, Subject, Body, Priority)\n" +
                            "VALUES('" + user.UserID + "', '" + msgReceiverUser + "', LEFT('" + subject.getText() + "',254),\n" +
                            "'" + body.getText() + "'," + msgPriority + ")";

                    String result = db.executeNonQuery(query);
                    if(Integer.parseInt(result) > 0)
                        Toast.makeText(getApplicationContext(), "Message Sent Successfully!", Toast.LENGTH_SHORT).show();
                    else
                        throw new Error("Error updating db. Message was not able to send. ");
                    finish();
                }
                catch(Exception e)
                {
                    Toast.makeText(getApplicationContext(), "Error!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println(e);
                    System.out.println(body.getText());
                    System.out.println(user.UserID);
                    System.out.println(msgReceiverUser);
                    System.out.println(subject.getText());
                }
            }
        });

        // back button
//        backBtn = (ImageButton) findViewById(R.id.btnBack);
//        backBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    finish();
//                }
//                catch(Exception e)
//                {
//                    Toast.makeText(getApplicationContext(), "Error finish(); backBtn OnClickListener!" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }
}
