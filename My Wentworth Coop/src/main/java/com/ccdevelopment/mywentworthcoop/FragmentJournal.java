package com.ccdevelopment.mywentworthcoop;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class FragmentJournal extends Fragment {
    ListView listview;
    List<ParseObject> ob;
    ProgressDialog mProgressDialog;
    ListViewAdapter adapter;
    private List<JournalPost> journalPostList = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new RemoteDataTask().execute();

        if (savedInstanceState == null){
            // onCreate First Time
        } else {
            // onCreate Subsequent Time
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_journal, container, false);

        Button button = (Button) view.findViewById(R.id.buttonPut);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                newPost();
            }
        });

        return view;
    }

    private void newPost() {
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View textEntryView = factory.inflate(R.layout.dialog_newpost, null);

        final EditText input1 = (EditText) textEntryView.findViewById(R.id.editText);
        final EditText input2 = (EditText) textEntryView.findViewById(R.id.editText2);
        final Switch switchPrivate = (Switch) textEntryView.findViewById(R.id.switchPrivate);
        input1.setHint("Title");
        input2.setHint("Your message content goes here.");
        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        alert.setTitle("New Post")
                .setView(textEntryView)
                .setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ParseObject post = new ParseObject("Post");
                                post.put("Title", input1.getText().toString());
                                post.put("Message", input2.getText().toString());
                                post.put("isPrivate", switchPrivate.isChecked());
                                post.put("user", ParseUser.getCurrentUser());
                                post.put("username", ParseUser.getCurrentUser().getUsername());

                                // Save the post and return
                                post.saveInBackground(new SaveCallback() {
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            getActivity().finish();
                                            Intent myIntent = new Intent(getActivity(), ViewPagerActivity.class);
                                            myIntent.putExtra("FirstTab", 1);
                                            startActivity(myIntent);
                                        } else
                                            Toast.makeText(getActivity(),"Something went wrong!",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                            }
                        });
        alert.show();
    }

    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setTitle("Loading MyCo-Op Data");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            //mProgressDialog.show(); //todo
        }

        @Override
        protected Void doInBackground(Void... params) {
            journalPostList = new ArrayList<JournalPost>();
            try {
                ParseQuery<ParseObject> publicQuery = new ParseQuery<ParseObject>("Post");
                ParseQuery<ParseObject> privateQuery = new ParseQuery<ParseObject>("Post");

                privateQuery.whereEqualTo("user", ParseUser.getCurrentUser());
                publicQuery.whereEqualTo("isPrivate", false);

                List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
                queries.add(publicQuery);
                queries.add(privateQuery);
                ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
                mainQuery.orderByDescending("updatedAt");

                ob = mainQuery.find();
                for (ParseObject parsePost : ob) {
                    JournalPost post = new JournalPost();
                    post.setTitle((String) parsePost.get("Title"));
                    post.setDescription((String) parsePost.get("Message"));
                    post.setDate(new SimpleDateFormat("MMM d, h:mm a").format(parsePost.getUpdatedAt()));
                    post.setUsername((String) parsePost.get("username"));
                    post.setIsPrivate((Boolean) parsePost.get("isPrivate"));
                    journalPostList.add(post);
                }
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            listview = (ListView) getActivity().findViewById(R.id.listView);
            adapter = new ListViewAdapter(getActivity(), journalPostList);
            listview.setAdapter(adapter);
            //mProgressDialog.dismiss(); //todo
        }
    }
}
