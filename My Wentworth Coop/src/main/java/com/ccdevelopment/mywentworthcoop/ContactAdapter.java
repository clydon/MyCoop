package com.ccdevelopment.mywentworthcoop;

import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ContactAdapter extends ArrayAdapter<Contact> {

    Context mContext;
    private List<Contact> contacts;

    public ContactAdapter(Context context, List<Contact> contacts) {
        super(context,R.layout.contact_row_item, contacts);
        this.mContext = context;
        this.contacts = contacts;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
            convertView = mLayoutInflater.inflate(R.layout.contact_row_item, null);
        }

        Contact contact = contacts.get(position);

        TextView nameView = (TextView) convertView.findViewById(R.id.contactTextViewName);
        nameView.setText(contact.getName());

        TextView phoneView = (TextView) convertView.findViewById(R.id.contactTextViewNumber);
        phoneView.setText(contact.getPhone());

        TextView emailView = (TextView) convertView.findViewById(R.id.contactTextViewEmail);
        emailView.setText(contact.getEmail());

        return convertView;
    }
}