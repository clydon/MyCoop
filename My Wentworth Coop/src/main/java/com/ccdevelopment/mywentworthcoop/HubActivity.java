package com.ccdevelopment.mywentworthcoop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;


public class HubActivity extends Activity {

    long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);

        // Retrieve current user from Parse.com
        ParseUser currentUser = ParseUser.getCurrentUser();

        // Convert currentUser into String
        String struser = currentUser.getUsername();

        Toast.makeText(getBaseContext(), "Signed in as: " + struser, Toast.LENGTH_LONG).show();

        loadWebViews();
    }

    private void loadWebViews() {
        final WebView viewPhotoVideo = (WebView) this.findViewById(R.id.webViewPhotoVideo);
        final WebView viewJournal = (WebView) this.findViewById(R.id.webViewJournal);
        final WebView viewPeople = (WebView) this.findViewById(R.id.webViewPeople);
        final WebView viewAssignments = (WebView) this.findViewById(R.id.webViewAssignments);


        viewPhotoVideo.getSettings().setJavaScriptEnabled(true);
        viewJournal.getSettings().setJavaScriptEnabled(true);
        viewPeople.getSettings().setJavaScriptEnabled(true);
        viewAssignments.getSettings().setJavaScriptEnabled(true);

        ParseQuery<ParseObject> photoQuery = ParseQuery.getQuery("Picture");
        photoQuery.whereEqualTo("Author", ParseUser.getCurrentUser());
        photoQuery.countInBackground(new CountCallback() {
            public void done(int count, ParseException e) {
                if (e == null) {
                    viewPhotoVideo.loadUrl("file:///android_asset/index1.html?count=" + count);
                } else {
                    viewPhotoVideo.loadUrl("file:///android_asset/index1.html?count=-1");
                }
            }
        });
        ParseQuery<ParseObject> journalQuery = ParseQuery.getQuery("Post");
        journalQuery.whereEqualTo("user", ParseUser.getCurrentUser());
        journalQuery.countInBackground(new CountCallback() {
            public void done(int count, ParseException e) {
                if (e == null) {
                    viewJournal.loadUrl("file:///android_asset/index2.html?count="+count);
                }else{
                    viewJournal.loadUrl("file:///android_asset/index2.html?count=-1");
                }
            }
        });
        ParseQuery<ParseObject> contactQuery = ParseQuery.getQuery("Contact");
        contactQuery.whereEqualTo("user", ParseUser.getCurrentUser());
        contactQuery.countInBackground(new CountCallback() {
            public void done(int count, ParseException e) {
                if (e == null) {
                    viewPeople.loadUrl("file:///android_asset/index3.html?count="+count);
                }else{
                    viewPeople.loadUrl("file:///android_asset/index3.html?count=-1");
                }
            }
        }); //todo

        ParseQuery<ParseObject> assignmentQuery = ParseQuery.getQuery("Assignment");
        assignmentQuery.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        assignmentQuery.countInBackground(new CountCallback() {
            public void done(int count, ParseException e) {
                if (e == null) {
                    viewAssignments.loadUrl("file:///android_asset/index4.html?count="+count);
                }else{
                    viewAssignments.loadUrl("file:///android_asset/index4.html?count=-1");
                }
            }
        });

        final int longPressTime = 500;

        // view.getSettings().setJavaScriptEnabled(true);

        viewPhotoVideo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    time = System.nanoTime();

                if(event.getAction() == MotionEvent.ACTION_UP){
                    long elapseTime = (System.nanoTime() - time) / 1000000;
                    if (elapseTime > longPressTime){ // long press
                        Intent myIntent = new Intent(HubActivity.this, ViewPagerActivity.class);
                        myIntent.putExtra("FirstTab", 0);
                        startActivity(myIntent);
                    } else { // short press

                    }
                    return true;
                }   return false;
            }
        });

        viewJournal.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    time = System.nanoTime();

                if(event.getAction() == MotionEvent.ACTION_UP){
                    long elapseTime = (System.nanoTime() - time) / 1000000;
                    if (elapseTime > longPressTime){ // long press
                        Intent myIntent = new Intent(HubActivity.this, ViewPagerActivity.class);
                        myIntent.putExtra("FirstTab", 1);
                        startActivity(myIntent);
                    } else { // short press

                    }
                    return true;
                }   return false;
            }
        });

        viewPeople.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    time = System.nanoTime();

                if(event.getAction() == MotionEvent.ACTION_UP){
                    long elapseTime = (System.nanoTime() - time) / 1000000;
                    if (elapseTime > longPressTime){ // long press
                        Intent myIntent = new Intent(HubActivity.this, ViewPagerActivity.class);
                        myIntent.putExtra("FirstTab", 2);
                        startActivity(myIntent);
                    } else { // short press

                    }
                    return true;
                }   return false;
            }
        });

        viewAssignments.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    time = System.nanoTime();

                if(event.getAction() == MotionEvent.ACTION_UP){
                    long elapseTime = (System.nanoTime() - time) / 1000000;
                    if (elapseTime > longPressTime){ // long press
                        Intent myIntent = new Intent(HubActivity.this, ViewPagerActivity.class);
                        myIntent.putExtra("FirstTab", 3);
                        startActivity(myIntent);
                    } else { // short press

                    }
                    return true;
                }   return false;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                ParseUser.logOut();
                finish();
                Toast.makeText(getBaseContext(), "Logged Out!", Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
