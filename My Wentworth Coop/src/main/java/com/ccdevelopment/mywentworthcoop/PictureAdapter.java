package com.ccdevelopment.mywentworthcoop;

import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseImageView;

public class PictureAdapter extends ArrayAdapter<Picture> {

    Context mContext;
    private List<Picture> pictures;

    public PictureAdapter(Context context, List<Picture> pictures) {
        super(context,R.layout.photo_row_item, pictures);
        this.mContext = context;
        this.pictures = pictures;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
            convertView = mLayoutInflater.inflate(R.layout.photo_row_item, null);
        }

        Picture picture = pictures.get(position);

        TextView titleView = (TextView) convertView.findViewById(R.id.picTextViewTitle);
        titleView.setText(picture.getTitle());

        TextView dateView = (TextView) convertView.findViewById(R.id.picTextViewDate);
        dateView.setText(new SimpleDateFormat("MMM d, h:mm a").format(picture.getUpdatedAt()));

        TextView usernameView = (TextView) convertView.findViewById(R.id.picTextViewAuthor);
        usernameView.setText(picture.getUsername());

        ParseImageView pictureView = (ParseImageView) convertView.findViewById(R.id.picImageView);
        pictureView.setParseFile(picture.getPhoto());
        pictureView.loadInBackground();

        return convertView;
    }
}