package com.ccdevelopment.mywentworthcoop;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class FragmentPeople extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_people, container, false);

        final EditText mName = (EditText) rootview.findViewById(R.id.editName);
        final EditText mEmail = (EditText) rootview.findViewById(R.id.editEmail);
        final EditText mPhone = (EditText) rootview.findViewById(R.id.editPhone);
        Button buttonsave = (Button) rootview.findViewById(R.id.btnSave);
        Button buttonclear = (Button) rootview.findViewById(R.id.btnClear);

        buttonsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                intent.putExtra(ContactsContract.Intents.Insert.NAME, mName.getText().toString());
                intent.putExtra(ContactsContract.Intents.Insert.EMAIL, mEmail.getText());
                intent.putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK);
                intent.putExtra(ContactsContract.Intents.Insert.PHONE, mPhone.getText());
                intent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
                startActivity(intent);
            }
        });

        buttonclear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mName.setText("");
                mEmail.setText("");
                mPhone.setText("");
            }
        });
        return rootview;
    }
}
