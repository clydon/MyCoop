package com.ccdevelopment.mywentworthcoop;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Post")
public class JournalPost extends ParseObject{

    public JournalPost(){

    }

    private String date;


    public String getTitle() {
        return getString("Title");
    }

    public void setTitle(String title) {
        put("Title", title);
    }

    public String getDescription() {
        return getString("Message");
    }

    public void setDescription(String description) {
        put("Message", description);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean getIsPrivate(){
        return getBoolean("isPrivate");
    }

    public void setIsPrivate(boolean isPrivate){
        put("isPrivate", isPrivate);
    }

    public void setUsername(String currentUsername){
        put("username", currentUsername);
    }

    public String getUsername(){
        return getString("username");
    }

}