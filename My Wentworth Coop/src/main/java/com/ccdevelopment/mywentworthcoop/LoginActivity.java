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
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
    EditText etUser, etPass;

    String username, password;
    HttpClient httpClient;

    HttpPost httpPost;
    ArrayList<NameValuePair> nameValuePairs;
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

        etUser = (EditText) findViewById(R.id.fieldUser);
        etPass = (EditText) findViewById(R.id.fieldPass);
        Button btn_submit = (Button) findViewById(R.id.btnSign);

        btn_submit.setOnClickListener(new OnClickListener() {
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

            username = etUser.getText().toString();
            password = etPass.getText().toString();

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}