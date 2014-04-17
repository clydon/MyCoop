package com.ccdevelopment.mywentworthcoop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.parse.ParseUser;


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
        WebView viewPhotoVideo = (WebView) this.findViewById(R.id.webViewPhotoVideo);
        WebView viewJournal = (WebView) this.findViewById(R.id.webViewJournal);
        WebView viewPeople = (WebView) this.findViewById(R.id.webViewPeople);
        WebView viewAssignments = (WebView) this.findViewById(R.id.webViewAssignments);

        viewPhotoVideo.loadUrl("file:///android_asset/index1.html");
        viewJournal.loadUrl("file:///android_asset/index2.html");
        viewPeople.loadUrl("file:///android_asset/index3.html");
        viewAssignments.loadUrl("file:///android_asset/index4.html");

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
