package com.example.sakshi.addcontact_contentprovider_acad;

import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText name, number;
    Button add, contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = (EditText)findViewById(R.id.ContactName);
        number = (EditText)findViewById(R.id.number);
        add=(Button)findViewById(R.id.add);
        contacts=(Button)findViewById(R.id.Contacts);
        //creting arrayList
        final ArrayList<ContentProviderOperation> ops =
                new ArrayList<ContentProviderOperation>();

        final int rawContactID = ops.size();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contactName = name.getText().toString();
                String contactNumber = number.getText().toString();

                // Adding insert operation to operations list
                // to insert a new raw contact in the table ContactsContract.RawContacts
                ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                        .build());

                // Adding insert operation to operations list
                // to insert display name in the table ContactsContract.Data
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                        .withValue(ContactsContract.Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
                        .withValue(StructuredName.DISPLAY_NAME, contactName)
                        .build());

                // Adding insert operation to operations list
                // to insert Mobile Number in the table ContactsContract.Data
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                        .withValue(ContactsContract.Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
                        .withValue(Phone.NUMBER, contactNumber)
                        .withValue(Phone.TYPE, CommonDataKinds.Phone.TYPE_MOBILE)
                        .build());
                try{
                    // Executing all the insert operations as a single database transaction
                    getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                    Toast.makeText(getBaseContext(), "Contact is successfully added", Toast.LENGTH_SHORT).show();
                }catch (RemoteException e) {
                    e.printStackTrace();
                }catch (OperationApplicationException e) {
                    e.printStackTrace();
                }

            }
        });
        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contacts = new Intent(Intent.ACTION_VIEW, ContactsContract.Contacts.CONTENT_URI);
                // Starting the activity
                startActivity(contacts);
            }
        });
    }
}
