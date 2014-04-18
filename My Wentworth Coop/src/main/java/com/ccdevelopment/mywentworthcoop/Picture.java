package com.ccdevelopment.mywentworthcoop;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("Picture")
public class Picture extends ParseObject{

    public Picture(){

    }

    public String getTitle() {
        return getString("Title");
    }

    public void setTitle(String title) {
        put("Title", title);
    }

    public ParseFile getPhoto(){
        return getParseFile("Photo");
    }

    public void setPhoto(ParseFile photo){
        put("Photo", photo);
    }

    public void setUsername(String currentUsername){
        put("username", currentUsername);
    }

    public String getUsername(){
        return getString("username");
    }

}