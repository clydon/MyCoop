package com.ccdevelopment.mywentworthcoop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class AssignmentsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignments);

        /*Button button = findViewById(R.id.buttonResCode);

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new DoPostRequestAsync().execute();
            }
        });*/
    }

    private class DoPostRequestAsync extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... urls) {


            return null;
        }

        protected void onPostExecute(String result) {


        }
    }

    private static String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem item1 = menu.findItem(R.id.action_mainmenu);
        Intent intent1 = new Intent(this, MainActivity.class );
        item1.setIntent(intent1);

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

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_mainmenu:
                startActivity(new Intent(this, MainActivity.class));
                return true;

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

            case R.id.action_settings:
                Toast.makeText(getBaseContext(), "Settings", Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
*/



}