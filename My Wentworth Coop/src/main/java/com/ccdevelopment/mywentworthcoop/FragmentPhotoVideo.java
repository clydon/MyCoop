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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class FragmentPhotoVideo extends Fragment {

    ListView listView;
    PictureAdapter adapter;

    View dialogView;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    byte[] snapshotByteArray;
    Bitmap snapshotBmp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null){
            // onCreate First Time
        } else {
            // onCreate Subsequent Time
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_photovideo, container, false);

        ParseObject.registerSubclass(Picture.class);

        listView = (ListView) rootView.findViewById(R.id.listViewPhotos);
        adapter = new PictureAdapter(getActivity(), new ArrayList<Picture>());
        listView.setAdapter(adapter);

        Button buttonCreateNewPhoto = (Button) rootView.findViewById(R.id.buttonAddNew);
        buttonCreateNewPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewPhotoDialog();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View view, int position, long id) {
                final Picture picture = adapter.getItem(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Warning");
                builder.setMessage("Are you sure you want to delete this?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        picture.deleteInBackground(new DeleteCallback() {
                            public void done(ParseException e) {
                                if (e == null) {
                                    adapter.remove(picture);
                                    getActivity().finish();
                                    Intent myIntent = new Intent(getActivity(), ViewPagerActivity.class);
                                    myIntent.putExtra("FirstTab", 0);
                                    startActivity(myIntent);
                                }
                                else
                                    Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { // Do nothing, User Cancelled Delete
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                if(picture.getUsername().equals(ParseUser.getCurrentUser().getUsername())) {
                    alert.show();
                }else{
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                    alertBuilder.setTitle("Warning");
                    alertBuilder.setMessage("You can not delete a picture that you did not make yourself.");
                    alertBuilder.setCancelable(false);
                    alertBuilder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    AlertDialog alertDialog = alertBuilder.create();
                    alertDialog.show();
                }

                return true;
            }
        });

        updateData();
        return rootView;
    }

    public void updateData() {
        ParseQuery<Picture> query = ParseQuery.getQuery(Picture.class);
        query.orderByDescending("updatedAt");

        query.findInBackground(new FindCallback<Picture>() {
            @Override
            public void done(List<Picture> pictures, ParseException e) {
                if (pictures != null){
                    adapter.clear();
                    adapter.addAll(pictures);
                } else {
                    Toast.makeText(getActivity(), "Something went wrong! Try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void openNewPhotoDialog() {
        dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_photo, null);

        final EditText editText = (EditText) dialogView.findViewById(R.id.editTextTitle);
        Button buttonTakePhoto = (Button) dialogView.findViewById(R.id.buttonTakePhoto);
        buttonTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,
                        CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });

        final AlertDialog.Builder db = new AlertDialog.Builder(getActivity());
        db.setView(dialogView);
        db.setTitle("Add New Photo");
        db.setPositiveButton("SAVE", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (snapshotByteArray != null) {
                            savePhoto(snapshotByteArray, String.valueOf(editText.getText()));
                        }
                    }
                });
        db.setNegativeButton("CANCEL", new
                DialogInterface.OnClickListener() {
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

                snapshotByteArray = byteArray;
                snapshotBmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                ImageView imageView = (ImageView) dialogView.findViewById(R.id.imagePreview);
                imageView.setImageBitmap(snapshotBmp);
            }
        }
    }

    private void savePhoto(final byte[] data, final String title) {
        Bitmap snapshotImage = BitmapFactory.decodeByteArray(data, 0, data.length);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        snapshotImage.compress(Bitmap.CompressFormat.JPEG, 100, bos);

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
                    final Picture picture = new Picture();

                    picture.setTitle(title);
                    picture.setUsername(ParseUser.getCurrentUser().getUsername());
                    picture.setPhoto(photoFile);

                    picture.saveInBackground(new SaveCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                adapter.insert(picture,0);
//                                getActivity().finish();
//                                Intent myIntent = new Intent(getActivity(), ViewPagerActivity.class);
//                                myIntent.putExtra("FirstTab", 0);
//                                startActivity(myIntent);
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