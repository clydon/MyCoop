package com.ccdevelopment.mywentworthcoop;

import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AssignmentAdapter extends ArrayAdapter<Assignment> {

    private Context mContext;
    private List<Assignment> mAssignments;

    public AssignmentAdapter(Context context, List<Assignment> objects) {
        super(context, R.layout.assignment_row_item, objects);
        this.mContext = context;
        this.mAssignments = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
            convertView = mLayoutInflater.inflate(R.layout.assignment_row_item, null);
        }

        Assignment assignment = mAssignments.get(position);

        TextView descriptionView = (TextView) convertView.findViewById(R.id.assignment_description);
        descriptionView.setText(assignment.getDescription());

        TextView dueDateView = (TextView) convertView.findViewById(R.id.assignment_duedate);
        dueDateView.setText(new SimpleDateFormat("EEE, MMM d").format(assignment.getDueDate()));

        TextView authorView = (TextView) convertView.findViewById(R.id.assignment_author);
        authorView.setText(assignment.getUsername());

        if(assignment.isCompleted()){
            descriptionView.setPaintFlags(descriptionView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            descriptionView.setTypeface(null, Typeface.NORMAL);
            dueDateView.setTypeface(null, Typeface.NORMAL);
            authorView.setTypeface(null, Typeface.NORMAL);
            descriptionView.setTextColor(Color.GRAY);
            dueDateView.setTextColor(Color.GRAY);
            authorView.setTextColor(Color.GRAY);
        }
        else{
            descriptionView.setPaintFlags(descriptionView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            descriptionView.setTypeface(null, Typeface.BOLD);
            dueDateView.setTypeface(null, Typeface.BOLD);
            authorView.setTypeface(null, Typeface.BOLD);
            descriptionView.setTextColor(Color.BLACK);
            dueDateView.setTextColor(Color.BLACK);
            authorView.setTextColor(Color.BLACK);
        }
        return convertView;
    }
}