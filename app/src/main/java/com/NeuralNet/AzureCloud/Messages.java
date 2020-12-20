package com.NeuralNet.AzureCloud;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.teamcarl.prototype.R;


import java.io.Serializable;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Messages extends Activity implements Serializable {

    List<MessageInfo> msgList = new ArrayList<>();
    List<String> subjects = new ArrayList<>();
    Button sortByPriority, clearFilters;
    EditText sentAfterDateBox;
    ArrayAdapter<String> adapter;
    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    Spinner filter;
    ArrayAdapter<String> filterAdapter;
    String filterType = "";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Messages Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    class MessageInfo {
        public String subject;
        public String body;
        public int MessageId;
        public int Priority;
        public Date sentDate;
        public String fromUser;


        public MessageInfo(String subject, int messageId, String body, int Priority, Date sentDate, String fromUser) {
            this.subject = subject;
            this.MessageId = messageId;
            this.body = body;
            this.Priority = Priority;
            this.sentDate = sentDate;
            this.fromUser = fromUser;
        }

        public String getSubject() {
            return subject;
        }
        public String getBody() {
            return body;
        }
        public int getPriority() {
            return Priority;
        }
        public int getMessageId() {
            return MessageId;
        }
        public Date getsentDate() {
            return sentDate;
        }
        public String getFromUser() { return fromUser; }
    }

    ListView listView;
    User user;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        // get data from previous screen
        Intent intent = getIntent();
        user = (User) intent.getExtras().getSerializable("User");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        // insert filters into spinner (order by drop down)
        filter = (Spinner) findViewById(R.id.filter_spinner);
        List<String> filterList = new ArrayList<String>();
        filterList.add("Newest to Oldest");
        filterList.add("Oldest to Newest");
        filterList.add("Not Read Only");
        filterList.add("Is Read Only");
        filterList.add("Priority High to Low");
        filterList.add("Priority Low to High");

        // initial query to get message list
        // default filter type newest to oldest
        filterType = "Newest to Oldest";
        String query = getFilterQuery(filterType);
        adapter = new ArrayAdapter<String>(this,R.layout.listview);
        getAllMessages(query);

        if (adapter != null && adapter.getCount() > 0) {
            listView = (ListView) findViewById(R.id.messageList);
            listView.setAdapter(adapter);

            // open message when subject name is clicked
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // ListView Clicked item index
                    int itemPosition = position;
                    Intent intent = new Intent(Messages.this, fullMessage.class);
                    MessageInfo temp = msgList.get(position);
                    intent.putExtra("MessageInfoSubject", temp.getSubject());
                    intent.putExtra("MessageInfoBody", temp.getBody());
                    intent.putExtra("MessageInfoId", Integer.toString((temp.getMessageId())));
                    intent.putExtra("MessageInfoFromUser", temp.getFromUser());
                    intent.putExtra("MessageInfoCurrentUser", user);
                    startActivity(intent);
                }
            });
        }
        // set filter spinner adapter
        filterAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, filterList);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filter.setAdapter(filterAdapter);
        filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filterType = adapterView.getItemAtPosition((i)).toString();
                // produces SQL query based off of the filter type
                String query = getFilterQuery((filterType));
                getAllMessages(query);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {            }
        });

        // set Sent After Date filter
        sentAfterDateBox = (EditText) findViewById(R.id.sentAfterDateBox);
        // Filter Messages based on sent after date
        sentAfterDateBox.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String query = getFilterQuery(filterType);
                getAllMessages(query);
            }
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            public void afterTextChanged(Editable s) {
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    // function to test converting date, returns true if text is pure date in MM/dd/yyyy format
    private Date ConvertToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
            return convertedDate;
        } catch (ParseException e) {
            return null;
        }
    }

    private void getAllMessages(String query) {
        DataAccess db = new DataAccess();
        ResultSet result = db.getDataTable(query);
        if (result != null) {
            try {
                if(adapter != null && adapter.getCount() > 0)
                    adapter.clear();
                if(msgList != null && msgList.size() > 0)
                    msgList.clear();
                while (result.next()) // loop through each row (first row is always empty/null, so we do next() right away)
                {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    MessageInfo msgInfo = new MessageInfo(result.getString(1), result.getInt(2), result.getString(3), result.getInt(4), formatter.parse(result.getString((5))), result.getString(6));
                    msgList.add(msgInfo);
                    adapter.add(msgInfo.getSubject());
                }
                adapter.notifyDataSetChanged();

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error getting messages: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getFilterQuery(String filterType)
    {
        String query = "SELECT Subject, MessageId, Body, Priority, CreatedDate, SenderUserId \n" +
                "FROM MESSAGE\n" +
                "WHERE receiverUserId = '" + user.UserID + "' AND [active] = 1 ";

        // if there is a filter set for "Messages Sent After"
        // Add filter to query
        if(sentAfterDateBox != null) {
            String sentAfterString = sentAfterDateBox.getText().toString();
            if (!sentAfterString.equals("")) // if text not empty
            {
                Date sentAfterDate = ConvertToDate(sentAfterString.toString());
                Calendar calendar = Calendar.getInstance();
                // if user text is a true date
                if (sentAfterDate != null) {
                    calendar.setTime(sentAfterDate);

                    if (calendar.get(Calendar.YEAR) > 2000) {
                        query += " AND createddate >= '" + df.format(sentAfterDate) + "'\n";
                    }
                }
            }
        }
        // add spinner (dropdown) filter also
        if(filterType.equals("Newest to Oldest"))
            query += " ORDER BY CreatedDate DESC";
        else if(filterType.equals("Oldest to Newest"))
            query += " ORDER BY CreatedDate";
        else if(filterType.equals("Not Read Only"))
            query += " AND [read] = 0 ORDER BY CreatedDate desc";
        else if(filterType.equals("Is Read Only"))
            query += " AND [read] = 1 ORDER BY CreatedDate desc";
        else if(filterType.equals("Priority High to Low")) // 1 is highest priority, and 3 is the lowest
            query += " ORDER BY Priority asc, CreatedDate DESC";
        else if(filterType.equals("Priority Low to High"))
            query += " ORDER BY Priority desc, CreatedDate DESC";

        return query;
    }
}
