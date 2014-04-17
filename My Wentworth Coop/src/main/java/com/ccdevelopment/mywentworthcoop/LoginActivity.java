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

    Button buttonLogin;
    Button buttonSignup;
    String usernameText;
    String passwordText;
    EditText editTextPassword;
    EditText username;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.fieldUser);
        editTextPassword = (EditText) findViewById(R.id.fieldPass);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonSignup = (Button) findViewById(R.id.buttonSignUp);

        buttonLogin.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                // Retrieve the text entered from the EditText
                usernameText = username.getText().toString();
                passwordText = editTextPassword.getText().toString();

                // Send data to Parse.com for verification
                ParseUser.logInInBackground(usernameText, passwordText,
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
                        }
                );
            }
        });

        buttonSignup.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                usernameText = username.getText().toString();
                passwordText = editTextPassword.getText().toString();

                if (usernameText.equals("") && passwordText.equals("")) {
                    Toast.makeText(getApplicationContext(),
                            "Please complete the sign up form",
                            Toast.LENGTH_LONG).show();
                } else {
                    // Save new user data into Parse.com Data Storage
                    ParseUser user = new ParseUser();
                    user.setUsername(usernameText);
                    user.setPassword(passwordText);
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