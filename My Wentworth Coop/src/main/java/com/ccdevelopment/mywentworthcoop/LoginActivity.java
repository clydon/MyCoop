package com.ccdevelopment.mywentworthcoop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
    EditText editTextUser, editTextPass;
    Button buttonSubmit;
    String username, password;
    ArrayList<NameValuePair> nameValuePairs;
    HttpClient httpClient;
    HttpPost httpPost;
    HttpResponse response;
    HttpEntity httpEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (android.os.Build.VERSION.SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        editTextUser = (EditText) findViewById(R.id.fieldUser);
        editTextPass = (EditText) findViewById(R.id.fieldPass);
        buttonSubmit = (Button) findViewById(R.id.btnSign);

        buttonSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new DoPostRequestAsync().execute();
            }
        });
    }

    private class DoPostRequestAsync extends AsyncTask<URL, Void, String> {
        String retUser;
        String retPass;

        @Override
        protected String doInBackground(URL... urls) {
            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost("http://10.0.0.4/web/index.php");

            username = editTextUser.getText().toString();
            password = editTextPass.getText().toString();

            try {
                nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("username", username));
                nameValuePairs.add(new BasicNameValuePair("password", password));

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                response = httpClient.execute(httpPost);

                if(response.getStatusLine().getStatusCode() == 200){
                    httpEntity = response.getEntity();

                    if (httpEntity != null){
                        InputStream instream = httpEntity.getContent();
                        JSONObject jsonResponse = new JSONObject(convertStreamToString(instream));
                        retUser = jsonResponse.getString("user");
                        retPass = jsonResponse.getString("pass");
                        return jsonResponse.toString();
                    }
                }
            } catch (Exception e){
                return null;
            }
            return null;
        }

        protected void onPostExecute(String result) {
            if(username.equals(retUser) && password.equals(retPass)){
                SharedPreferences sp = getSharedPreferences("logindetails", 0);

                SharedPreferences.Editor spedit = sp.edit();

                spedit.putString("user", username);
                spedit.putString("pass", password); //may not need to store password

                spedit.commit();

                Toast.makeText(getBaseContext(), "SUCCESS!!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getBaseContext(), "Invalid Login Details" , Toast.LENGTH_LONG).show();
            }
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem item1 = menu.findItem(R.id.action_main);
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
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

}