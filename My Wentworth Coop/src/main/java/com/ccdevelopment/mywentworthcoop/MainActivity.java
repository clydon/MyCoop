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


public class MainActivity extends Activity {
    long startTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            //long startTime;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    startTime = System.nanoTime();

                if(event.getAction() == MotionEvent.ACTION_UP){
                    long elapseTime = (System.nanoTime() - startTime) / 1000000;
                    if (elapseTime > longPressTime){ // long press
                        Intent myIntent = new Intent(MainActivity.this, ViewPagerActivity.class);
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
                    startTime = System.nanoTime();

                if(event.getAction() == MotionEvent.ACTION_UP){
                    long elapseTime = (System.nanoTime() - startTime) / 1000000;
                    if (elapseTime > longPressTime){ // long press
                        Intent myIntent = new Intent(MainActivity.this, ViewPagerActivity.class);
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
                    startTime = System.nanoTime();

                if(event.getAction() == MotionEvent.ACTION_UP){
                    long elapseTime = (System.nanoTime() - startTime) / 1000000;
                    if (elapseTime > longPressTime){ // long press
                        Intent myIntent = new Intent(MainActivity.this, ViewPagerActivity.class);
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
                    startTime = System.nanoTime();

                if(event.getAction() == MotionEvent.ACTION_UP){
                    long elapseTime = (System.nanoTime() - startTime) / 1000000;
                    if (elapseTime > longPressTime){ // long press
                        Intent myIntent = new Intent(MainActivity.this, ViewPagerActivity.class);
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

//        MenuItem item1 = menu.findItem(R.id.action_mainmenu);
//        Intent intent1 = new Intent(this, MainActivity.class );
//        item1.setIntent(intent1);

        MenuItem item2 = menu.findItem(R.id.action_login);
        Intent intent2 = new Intent(this, LoginActivity.class );
        item2.setIntent(intent2);

        MenuItem item3 = menu.findItem(R.id.action_login);
        Intent intent3 = new Intent(this, PhotoVideoActivity.class );
        item3.setIntent(intent3);

        MenuItem item4 = menu.findItem(R.id.action_login);
        Intent intent4 = new Intent(this, JournalActivity.class );
        item4.setIntent(intent4);

        MenuItem item5 = menu.findItem(R.id.action_login);
        Intent intent5 = new Intent(this, PeopleActivity.class );
        item5.setIntent(intent5);

        MenuItem item6 = menu.findItem(R.id.action_login);
        Intent intent6 = new Intent(this, AssignmentsActivity.class );
        item6.setIntent(intent6);

        MenuItem item7 = menu.findItem(R.id.action_viewpager);
        Intent intent7 = new Intent(this, ViewPagerActivity.class );
        item7.setIntent(intent7);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
//            case R.id.action_mainmenu:
//                startActivity(new Intent(this, MainActivity.class));
//                return true;

            case R.id.action_login:
                startActivity(new Intent(this, LoginActivity.class));
                return true;

            case R.id.action_photoVideo:
                startActivity(new Intent(this, PhotoVideoActivity.class));
                return true;

            case R.id.action_journal:
                startActivity(new Intent(this, JournalActivity.class));
                return true;

            case R.id.action_people:
                startActivity(new Intent(this, PeopleActivity.class));
                return true;

            case R.id.action_assignments:
                startActivity(new Intent(this, AssignmentsActivity.class));
                return true;

            case R.id.action_viewpager:
                startActivity(new Intent(this, ViewPagerActivity.class));
                return true;

            case R.id.action_settings:
                Toast.makeText(getBaseContext(), "Settings", Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
