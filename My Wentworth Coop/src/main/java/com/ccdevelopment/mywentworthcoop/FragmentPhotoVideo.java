package com.ccdevelopment.mywentworthcoop;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class FragmentPhotoVideo extends Fragment {

    View rootView, dialog_layout;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    byte[] takenPhoto;
    Bitmap takenPhotoBmp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_photovideo, container, false);

        ParseQueryAdapter.QueryFactory<ParseObject> factory =
                new ParseQueryAdapter.QueryFactory<ParseObject>() {
                    public ParseQuery create() {
                        ParseQuery query = new ParseQuery("Picture");
                        query.orderByDescending("createdAt");
                        return query;
                    }
                };

        MyAdapter adapter = new MyAdapter(getActivity(), factory);

        adapter.setTextKey("Title");
        adapter.setImageKey("Photo");

        ListView listView = (ListView) rootView.findViewById(R.id.listViewPhotos);
        listView.setAdapter(adapter);

        Button addNew = (Button) rootView.findViewById(R.id.buttonAddNew);
        addNew.setOnClickListener(new View.OnClickListener(){
            @Override
        public void onClick(View v) {
                openNewPhotoDialog();
            }
        });

        return rootView;
    }

   private class MyAdapter extends ParseQueryAdapter<ParseObject> {
        private Context context;

        public MyAdapter(
                Context context,
                com.parse.ParseQueryAdapter.QueryFactory<ParseObject> queryFactory) {
            super(context, queryFactory);
            this.context = context;
        }

        @Override
        public View getItemView(ParseObject object, View v, ViewGroup parent) {
            if (v == null) {
                v = View.inflate(this.context, R.layout.photo_row_item, null);
            }
            TextView titleView = (TextView) v.findViewById(R.id.textViewTitle);
            titleView.setText(object.get("Title").toString());

            TextView dateView = (TextView) v.findViewById(R.id.textViewDate);
            dateView.setText(new SimpleDateFormat("MMM d, h:mm").format(object.getUpdatedAt()));

            TextView authorView = (TextView) v.findViewById(R.id.textViewAuthor);
            authorView.setText(object.get("username").toString());

            ParseImageView imageView = (ParseImageView)v.findViewById(R.id.imageView);
            ParseFile file = object.getParseFile("Photo");
            if(file != null) {
                imageView.setParseFile(file);
                imageView.loadInBackground();
            }
            return v;
        }
    }

    private void openNewPhotoDialog() {
        dialog_layout = getActivity().getLayoutInflater().inflate(R.layout.dialog_photo, null);

        final EditText editText = (EditText) dialog_layout.findViewById(R.id.editTextTitle);
        Button addPhoto = (Button) dialog_layout.findViewById(R.id.buttonTakePhoto);
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,
                        CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });

        final AlertDialog.Builder db = new AlertDialog.Builder(getActivity());
        db.setView(dialog_layout);
        db.setTitle("Add New Photo");
        db.setPositiveButton("SAVE", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(takenPhoto != null){
                            saveScaledPhoto(takenPhoto, String.valueOf(editText.getText()));
                        }
                    }
                });
        db.setNegativeButton("CANCEL", new
                DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        db.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                takenPhoto = byteArray;
                takenPhotoBmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                ImageView imageView = (ImageView) dialog_layout.findViewById(R.id.imagePreview);
                imageView.setImageBitmap(takenPhotoBmp);
            }
        }
    }

    private void saveScaledPhoto(final byte[] data, final String title) {
        Bitmap mealImage = BitmapFactory.decodeByteArray(data, 0, data.length);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        mealImage.compress(Bitmap.CompressFormat.JPEG, 100, bos);

        byte[] scaledData = bos.toByteArray();

        final ParseFile photoFile = new ParseFile("coop_picture.jpg", scaledData);
        photoFile.saveInBackground(new SaveCallback() {

            public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(getActivity(),
                            "Error saving: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
                else {
                    ParseObject picObject = new ParseObject("Picture");
                    picObject.put("Title", title);
                    picObject.put("Author", ParseUser.getCurrentUser());
                    picObject.put("username", ParseUser.getCurrentUser().getUsername());
                    picObject.put("Photo", photoFile);
                    picObject.saveInBackground(new SaveCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                getActivity().finish();
                                Intent myIntent = new Intent(getActivity(), ViewPagerActivity.class);
                                myIntent.putExtra("FirstTab", 0);
                                startActivity(myIntent);
                            } else {
                                Toast.makeText(getActivity(),"Something went wrong!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}