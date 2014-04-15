package com.ccdevelopment.mywentworthcoop;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;

public class FragmentPhotoVideo extends Fragment {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1888;
    Button buttoncam;
    ImageView imgView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_photovideo, container, false);

        buttoncam = (Button) rootview.findViewById(R.id.btnCamera);
        imgView = (ImageView) rootview.findViewById(R.id.imgPreview);
        buttoncam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,
                        CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });
        return rootview;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                //Store Picture
                ParseFile picture = new ParseFile("Picture", byteArray);
                picture.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Toast.makeText(getActivity(), "Picture Uploaded", Toast.LENGTH_SHORT).show();
                    }
                });

                // Convert ByteArray to Bitmap::
                Bitmap myimg = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                imgView.setImageBitmap(myimg);

                //Create Parse Object for reference
                ParseObject picob = new ParseObject("Picture");
                picob.put("Title", "cooppic");
                picob.put("Author", ParseUser.getCurrentUser());
                picob.put("username", ParseUser.getCurrentUser().getUsername());
                picob.put("Photo", picture);
                picob.saveInBackground();
            }
        }
    }
}