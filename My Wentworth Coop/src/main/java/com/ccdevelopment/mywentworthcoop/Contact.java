package com.ccdevelopment.mywentworthcoop;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Contact")
public class Contact extends ParseObject{

    public Contact(){

    }

    public String getName() {
        return getString("Name");
    }

    public void setName(String name) {
        put("Name", name);
    }

    public String getPhone() {
        return getString("Phone");
    }

    public void setPhone(String phone) {
        put("Phone", phone);
    }

    public String getEmail() {
        return getString("Email");
    }

    public void setEmail(String email) {
        put("Email", email);
    }

    public void setUsername(String currentUsername){
        put("username", currentUsername);
    }

    public String getUsername(){
        return getString("username");
    }

}