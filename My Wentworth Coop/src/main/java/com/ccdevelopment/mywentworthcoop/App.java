package com.ccdevelopment.mywentworthcoop;
import android.app.Application;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

public class App extends Application {

    @Override public void onCreate() {
        super.onCreate();

        Parse.initialize(this, "PRAaLniiM3AEYwNNwwm16fsPRq0oHiCOyRwl8cK3", "kcLgMl4JcEJItdSij3xe3eiMV2pcARhbPoSSCPtV");
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);


    }
}