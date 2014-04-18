package com.ccdevelopment.mywentworthcoop;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class FragmentJournal extends Fragment {

    ListView listview;
    JournalAdapter adapter;

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
        View rootView = inflater.inflate(R.layout.fragment_journal, container, false);

        ParseObject.registerSubclass(JournalPost.class);

        listview = (ListView) rootView.findViewById(R.id.journalListView);
        adapter = new JournalAdapter(getActivity(), new ArrayList<JournalPost>());
        listview.setAdapter(adapter);

        Button button = (Button) rootView.findViewById(R.id.buttonPut);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                newPost();
            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View view, int position, long id) {
                final JournalPost journalPost = adapter.getItem(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Warning");
                builder.setMessage("Are you sure you want to delete this?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        journalPost.deleteInBackground(new DeleteCallback() {
                            public void done(ParseException e) {
                                if (e == null)
                                    adapter.remove(journalPost);
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
                if(journalPost.getUsername().equals(ParseUser.getCurrentUser().getUsername())) {
                    alert.show();
                }else{
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                    alertBuilder.setTitle("Warning");
                    alertBuilder.setMessage("You can not delete a post that you did not make yourself.");
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

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final JournalPost journalPost = adapter.getItem(position);

                LayoutInflater factory = LayoutInflater.from(getActivity());
                final View textEntryView = factory.inflate(R.layout.dialog_newpost, null);

                final EditText input1 = (EditText) textEntryView.findViewById(R.id.editText);
                final EditText input2 = (EditText) textEntryView.findViewById(R.id.editText2);
                final Switch switchPrivate = (Switch) textEntryView.findViewById(R.id.switchPrivate);
                switchPrivate.setChecked(journalPost.getIsPrivate());
                input1.setText(journalPost.getTitle());
                input2.setText(journalPost.getDescription());
                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

                alert.setTitle("Edit Post")
                        .setView(textEntryView)
                        .setPositiveButton("Update",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        journalPost.setTitle(input1.getText().toString());
                                        journalPost.setDescription(input2.getText().toString());
                                        journalPost.setIsPrivate(switchPrivate.isChecked());
                                        journalPost.setUsername(ParseUser.getCurrentUser().getUsername());

                                        // Save the post and return
                                        journalPost.saveInBackground(new SaveCallback() {
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    adapter.insert(journalPost, 0);
                                                    //delete below
                                                    getActivity().finish();
                                                    Intent myIntent = new Intent(getActivity(), ViewPagerActivity.class);
                                                    myIntent.putExtra("FirstTab", 1);
                                                    startActivity(myIntent);
                                                } else
                                                    Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                        )
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
                                    }
                                }
                        );
                if(journalPost.getUsername().equals(ParseUser.getCurrentUser().getUsername())) {
                    alert.show();
                }
                else{
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                    alertBuilder.setTitle("Warning");
                    alertBuilder.setMessage("You can not edit a post that you did not make yourself.");
                    alertBuilder.setCancelable(false);
                    alertBuilder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    AlertDialog alertDialog = alertBuilder.create();
                    alertDialog.show();
                }
            }
        });

            updateData();

            return rootView;
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
                                final JournalPost journalPost = new JournalPost();
                                journalPost.setTitle(input1.getText().toString());
                                journalPost.setDescription(input2.getText().toString());
                                journalPost.setIsPrivate(switchPrivate.isChecked());
                                journalPost.setUsername(ParseUser.getCurrentUser().getUsername());

                                // Save the post and return
                                post.saveInBackground(new SaveCallback() {
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            adapter.insert(journalPost,0);
                                            //delete below
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


    public void updateData() {
            ParseQuery<JournalPost> publicQuery = ParseQuery.getQuery(JournalPost.class);
            ParseQuery<JournalPost> privateQuery = ParseQuery.getQuery(JournalPost.class);

            privateQuery.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
            publicQuery.whereEqualTo("isPrivate", false);

            List<ParseQuery<JournalPost>> queries = new ArrayList<ParseQuery<JournalPost>>();
            queries.add(publicQuery);
            queries.add(privateQuery);
            ParseQuery<JournalPost> mainQuery = ParseQuery.or(queries);
            mainQuery.orderByDescending("updatedAt");

            mainQuery.findInBackground(new FindCallback<JournalPost>() {
                @Override
                public void done(List<JournalPost> journalPosts, ParseException e) {
                    if (journalPosts != null) {
                        adapter.clear();
                        adapter.addAll(journalPosts);
                    } else {
                        Toast.makeText(getActivity(), "Something went wrong! Try again!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    }
}
