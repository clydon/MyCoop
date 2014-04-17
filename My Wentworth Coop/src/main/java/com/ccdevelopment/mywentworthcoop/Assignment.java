package com.ccdevelopment.mywentworthcoop;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

@ParseClassName("Assignment")
public class Assignment extends ParseObject{

    public Assignment(){

    }

    public boolean isCompleted(){
        return getBoolean("completed");
    }

    public void setCompleted(boolean complete){
        put("completed", complete);
    }

    public void setUsername(String currentUsername){
        put("username", currentUsername);
    }

    public String getUsername(){
        return getString("username");
    }

    public void setDueDate(Date dueDate){
        put("dueDate", dueDate);
    }

    public Date getDueDate(){
        return getDate("dueDate");
    }

    public String getDescription(){
        return getString("description");
    }

    public void setDescription(String description){
        put("description", description);
    }
}