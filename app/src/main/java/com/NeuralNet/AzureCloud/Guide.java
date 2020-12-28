package com.NeuralNet.AzureCloud;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
/*import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;*/
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.teamcarl.prototype.R;

import java.io.FileInputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Guide extends FragmentActivity {

    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;
    User user;
    String guideId;
    int currentQuestion;
    int rowCount, columnCount;
    public List<String> guideQuestionIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // get data from previous screen
        Intent intent = getIntent();
        user = (User) intent.getExtras().getSerializable("User");
        guideId = intent.getExtras().getString("guideId");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        // get number of questions in the current guide
        setQuestionIdsForGuide();

        // get data to send to fragment (Guide Content)
        List<Fragment> fragments = getFragments();

        // if more than 0 questions in guide, display question view page.
        if(fragments.size() > 0) {
            // Instantiate a ViewPager and a PagerAdapter.
            mPager = (ViewPager) findViewById(R.id.pager);
            mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), fragments);
            mPager.setAdapter(mPagerAdapter);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "This guide has no questions to answer." , Toast.LENGTH_SHORT).show();
            Intent intent2 = new Intent(Guide.this, Purpose.class);
            intent2.putExtra("guideId", guideId);
            intent2.putExtra("User", user);
            startActivity(intent2);
        }
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> fragments;

        public ScreenSlidePagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        // used to set question position in guide, and create question fragment
        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }
    }
    private void setQuestionIdsForGuide() {
        // if user is online/connected to internet
        guideQuestionIds = new ArrayList<>();
        if (connectionTest()) {

            DataAccess db = new DataAccess();
            try {
                String query = "" +
                        "SELECT q.questionId \n" +
                        "FROM Guide g\n" +
                        "join question q on g.guideid = q.guideid\n" +
                        "where g.guideid = " + guideId +
                        " ORDER BY q.sequence";

                ResultSet result = db.getDataTable(query);
                while (result.next()) {
                    guideQuestionIds.add(result.getString(1));
                }
            } catch (Exception e) {
                Log.e("err in Guide.getCoun()", e.getMessage());
            }
        } else { // read offline file
            //reads question file
            try {
                String[][] questionData = null;
                String questionText = "";
                FileInputStream fis1 = openFileInput("questions.txt");
                int size = fis1.available();
                byte[] buffer = new byte[size];
                fis1.read(buffer);
                fis1.close();
                questionText = new String(buffer);

                questionData = fileStringDataToArray(questionText);

                for (int i = 0; i < rowCount; i++) {
                    if(guideId.equals(questionData[i][1])) {
                        guideQuestionIds.add(questionData[i][0]);
                    }
                }

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error getting offline Questions", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private List<Fragment> getFragments(){
        List<Fragment> fList = new ArrayList<Fragment>();
        boolean isLastQuestion = false;
        // store all fragment content into list for each question id in guide
        for(int i = 0; i < guideQuestionIds.size(); i++)
        {
            // if last question of guide (question list)
            if(i == guideQuestionIds.size() - 1)
            {
                isLastQuestion = true;
            }
            fList.add(GuideContent.newInstance(user, guideQuestionIds.get(i), isLastQuestion));
        }

        return fList;
    }

    public void onNextPressed () {
        // if there is a next iten to goto
        if(mPager.getCurrentItem() < guideQuestionIds.size()) {
            // bring user to next question...
            mPager.setCurrentItem(mPager.getCurrentItem() + 1);

            // if user on last question of guide...
            if(mPager.getCurrentItem() == guideQuestionIds.size() - 1)
                Toast.makeText(getApplicationContext(), "You are on the last question of the guide." , Toast.LENGTH_SHORT).show();
        }
    }

    public String[][] fileStringDataToArray(String fileData) {
        String[][] dataTableArray = null;

        if(fileData != null && fileData.length() > 0) {
            String[] rows = fileData.split(Pattern.quote("\r\n"));
            String[] columns = rows[0].split(Pattern.quote("|"));

            dataTableArray = new String[rows.length][14]; // we will always use 14 columns until the active menu query to get questions data is changed.

            rowCount = rows.length;

            for(int i = 0; i < rows.length; i++)
            {
                columns = rows[i].split(Pattern.quote("|"));
                columnCount = columns.length;
                for(int j = 0; j < columns.length; j++)
                {
                    dataTableArray[i][j] = columns[j];
                }
            }
        }
        return dataTableArray;
    }
    public boolean connectionTest() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        } else
            return false;

    }
}
