package com.ccdevelopment.mywentworthcoop;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
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

public class FragmentPeople extends Fragment {

    ListView listview;
    ContactAdapter adapter;

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
        View rootView = inflater.inflate(R.layout.fragment_people, container, false);

        ParseObject.registerSubclass(Contact.class);

        listview = (ListView) rootView.findViewById(R.id.contactListView);
        adapter = new ContactAdapter(getActivity(), new ArrayList<Contact>());
        listview.setAdapter(adapter);

        Button button = (Button) rootView.findViewById(R.id.buttonNewContact);
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
                final Contact contact = adapter.getItem(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Warning");
                builder.setMessage("Are you sure you want to delete this?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        contact.deleteInBackground(new DeleteCallback() {
                            public void done(ParseException e) {
                                if (e == null)
                                    adapter.remove(contact);
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
                alert.show();

                return true;
            }
        });

        
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final Contact contact = adapter.getItem(position);

                LayoutInflater factory = LayoutInflater.from(getActivity());
                final View textEntryView = factory.inflate(R.layout.dialog_newcontact, null);

                final EditText inputName = (EditText) textEntryView.findViewById(R.id.editTextName);
                final EditText inputEmail = (EditText) textEntryView.findViewById(R.id.editTextEmail);
                final EditText inputPhone = (EditText) textEntryView.findViewById(R.id.editTextPhone);
                inputName.setText(contact.getName());
                inputEmail.setText(contact.getEmail());
                inputPhone.setText(contact.getPhone());
                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

                alert.setTitle("Edit Contact")
                        .setView(textEntryView)
                        .setPositiveButton("Update",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        contact.setName(inputName.getText().toString());
                                        contact.setEmail(inputEmail.getText().toString());
                                        contact.setPhone(inputPhone.getText().toString());
                                        contact.setUsername(ParseUser.getCurrentUser().getUsername());

                                        // Save the post and return
                                        contact.saveInBackground(new SaveCallback() {
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    adapter.insert(contact, 0);
//                                                    getActivity().finish();
//                                                    Intent myIntent = new Intent(getActivity(), ViewPagerActivity.class);
//                                                    myIntent.putExtra("FirstTab", 1);
//                                                    startActivity(myIntent);
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
                alert.show();
            }
        });

        updateData();

        return rootView;
    }

    private void newPost() {
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View textEntryView = factory.inflate(R.layout.dialog_newcontact, null);

        final EditText inputName = (EditText) textEntryView.findViewById(R.id.editTextName);
        final EditText inputEmail = (EditText) textEntryView.findViewById(R.id.editTextEmail);
        final EditText inputPhone = (EditText) textEntryView.findViewById(R.id.editTextPhone);
        inputName.setHint("Contact Name");
        inputEmail.setHint("contact@email.com");
        inputPhone.setHint("555-555-5555");
        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        alert.setTitle("New Contact")
                .setView(textEntryView)
                .setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                final Contact contact = new Contact();
                                contact.setName(inputName.getText().toString());
                                contact.setEmail(inputEmail.getText().toString());
                                contact.setPhone(inputPhone.getText().toString());
                                contact.setUsername(ParseUser.getCurrentUser().getUsername());

                                // Save the post and return
                                contact.saveInBackground(new SaveCallback() {
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            adapter.insert(contact,0);
                                            Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                                            intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                                            intent.putExtra(ContactsContract.Intents.Insert.NAME, inputName.getText().toString());
                                            intent.putExtra(ContactsContract.Intents.Insert.EMAIL, inputEmail.getText());
                                            intent.putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK);
                                            intent.putExtra(ContactsContract.Intents.Insert.PHONE, inputPhone.getText());
                                            intent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
                                            startActivity(intent);
//                                            getActivity().finish();
//                                            Intent myIntent = new Intent(getActivity(), ViewPagerActivity.class);
//                                            myIntent.putExtra("FirstTab", 1);
//                                            startActivity(myIntent);
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
        ParseQuery<Contact> query = ParseQuery.getQuery(Contact.class);
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.orderByDescending("Name");

        query.findInBackground(new FindCallback<Contact>() {
            @Override
            public void done(List<Contact> contacts, ParseException e) {
                if (contacts != null) {
                    adapter.clear();
                    adapter.addAll(contacts);
                } else {
                    Toast.makeText(getActivity(), "Something went wrong! Try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
