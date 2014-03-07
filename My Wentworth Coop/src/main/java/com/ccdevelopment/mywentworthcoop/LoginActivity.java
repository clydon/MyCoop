package com.ccdevelopment.mywentworthcoop;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends Activity {
    // Declare Variables
    Button loginbutton;
    Button signup;
    String usernametxt;
    String passwordtxt;
    EditText password;
    EditText username;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from main.xml
        setContentView(R.layout.activity_login);
        // Locate EditTexts in main.xml
        username = (EditText) findViewById(R.id.fieldUser);
        password = (EditText) findViewById(R.id.fieldPass);

        // Locate Buttons in main.xml
        loginbutton = (Button) findViewById(R.id.buttonLogin);
        signup = (Button) findViewById(R.id.buttonSignUp);

        // Login Button Click Listener
        loginbutton.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                // Retrieve the text entered from the EditText
                usernametxt = username.getText().toString();
                passwordtxt = password.getText().toString();

                // Send data to Parse.com for verification
                ParseUser.logInInBackground(usernametxt, passwordtxt,
                        new LogInCallback() {
                            public void done(ParseUser user, ParseException e) {
                                if (user != null) {
                                    // If user exist and authenticated, send user to Welcome.class
                                    Intent intent = new Intent(
                                            LoginActivity.this,
                                            HubActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(),
                                            "Successfully Logged in",
                                            Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "Login Unsuccessful. If new user, use SignUp button.",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
        // Sign up Button Click Listener
        signup.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                // Retrieve the text entered from the EditText
                usernametxt = username.getText().toString();
                passwordtxt = password.getText().toString();

                // Force user to fill up the form
                if (usernametxt.equals("") && passwordtxt.equals("")) {
                    Toast.makeText(getApplicationContext(),
                            "Please complete the sign up form",
                            Toast.LENGTH_LONG).show();

                } else {
                    // Save new user data into Parse.com Data Storage
                    ParseUser user = new ParseUser();
                    user.setUsername(usernametxt);
                    user.setPassword(passwordtxt);
                    user.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                // Show a simple Toast message upon successful registration
                                Toast.makeText(getApplicationContext(),
                                        "Successfully Signed up, please log in.",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Sign up Error", Toast.LENGTH_LONG)
                                        .show();
                            }
                        }
                    });
                }

            }
        });

    }
}

























/*

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
        */
/*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         *//*

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


*/
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
*//*


}*/
