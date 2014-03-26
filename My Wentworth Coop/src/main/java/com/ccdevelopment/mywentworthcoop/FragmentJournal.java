package com.ccdevelopment.mywentworthcoop;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class FragmentJournal extends Fragment {

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
        View view = inflater.inflate(R.layout.fragment_journal, container, false);


        final ParseQueryAdapter<ParseObject> adapter = new ParseQueryAdapter<ParseObject>(getActivity(), "Post");
        adapter.setTextKey("Title");

        final ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(adapter);


        Button button = (Button) view.findViewById(R.id.buttonPut);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                newPost();
                Toast.makeText(getActivity(),"Put finished!",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void newPost() {
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View textEntryView = factory.inflate(R.layout.dialog_newpost, null);

        final EditText input1 = (EditText) textEntryView.findViewById(R.id.editText);
        final EditText input2 = (EditText) textEntryView.findViewById(R.id.editText2);
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

                                // Save the post and return
                                post.saveInBackground();    //todo add error handling
                                getActivity().finish();
                                Intent myIntent = new Intent(getActivity(), ViewPagerActivity.class);
                                myIntent.putExtra("FirstTab", 1);
                                startActivity(myIntent);
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
}
