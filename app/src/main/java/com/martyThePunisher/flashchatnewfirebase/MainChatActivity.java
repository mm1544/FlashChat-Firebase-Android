package com.martyThePunisher.flashchatnewfirebase;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainChatActivity extends AppCompatActivity {

    // TODO: Add member variables here:
    private String mDisplayName;
    private ListView mChatListView;
    private EditText mInputText;
    private ImageButton mSendButton;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // message layout is linked to MainChatActivity.java file
        setContentView(R.layout.activity_main_chat);

        // TODO: Set up the display name and get the Firebase reference
        // In order to 'talk' to the Firebase database we need something #alled 'database referen#e'
        // We will hold this database ref. in a member variable (mDatabaseReference).
        setupDisplayName();

        // this 'mDatabaseReference' needs a value
        // We will #all stati#k method to get FirebaseDatabase obje#t
        // then will #all getReferen#e() method to obtain a Database Referen#e obje#t AND THIS we
        // will be storing in our member variable
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        // THE REASON why we need database referen#e obje#t is be#ause database referen#e represents
        // a parti#ular lo#ation in our #loud database.
        // Database referen#e is used for reading and writing data to that lo#ation in the database.


        // Link the Views in the layout to the Java code
        // editText for the chat messages is linked to var. mInputText
        mInputText = (EditText) findViewById(R.id.messageInput);
        // 'Send' button is linked to var. mSendButton
        mSendButton = (ImageButton) findViewById(R.id.sendButton);
        mChatListView = (ListView) findViewById(R.id.chat_list_view);

        //#h: set an OnEditorA#tionListener on the mInputText, that fires SendMessage method when
        // the 'enter' button is pressed on the soft keyboard.
        // TODO: Send the message when the "enter" button is pressed
        mInputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                sendMessage();
                return true; // returns true to indi#ate that the event has been handeled
            }
        });

        // TODO: Add an OnClickListener to the sendButton to send a message
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();

            }
        });

    }

    // TODO: Retrieve the display name from the Shared Preferences
    private void setupDisplayName(){

        SharedPreferences prefs = getSharedPreferences(RegisterActivity.CHAT_PREFS, MODE_PRIVATE);
        // FOR THE NAME WE WILL supply a stati# #onstant in the RegisterA#tivity #alled CHAT_PREFS.
        // And for the mode we will use PRIVATE

        // NOTE: we #an multiple shared preferen#es inside the same app, SO we will give to ea#h
        // shared pref. its own name.

        // retrieving the data:
            // To extra#t saved data from sharedPreferen#es we will be using getString() method.
        // getString() requires the key, the one that we have saved. We will provide the KEY in the
        // form of a stati# DISPLAY_NAME_KEY #onstant from RegisterA#tivity:
        mDisplayName = prefs.getString(RegisterActivity.DISPLAY_NAME_KEY, null);

        // if there is no Display Name that has been saved under the Shared Preferen#es
        if(mDisplayName == null) mDisplayName = "Anonymous";

        // now we have to #all setupDisplayName() method inside onCreate in MainChatActivity

    }

    private void sendMessage() {
        Log.d("Flash#hat", "I sent something");
        // TODO: Grab the text the user typed in and push the message to Firebase
        String input = mInputText.getText().toString(); // to store user's message as text
       // if user's message is not empty:
        if(!input.equals("")){
            // ...then #reate InstantMessage obje#t #alled chat:
            InstantMessage chat = new InstantMessage(input, mDisplayName);
                // ...and supply user's #hat message text and user's display name to the #onstru#tor

            //now we #an save #hat message to the #loud on Firebase's servers
            //      Will need Firebase database referen#e obje#t.
            // NOTE: Database referen#e is a parti#ular lo#ation in our database.
            //      Will use child() method to specify that all chat messages ought to be stored in a
            // place called 'messages'
            // Next will use push() to GET a REFERENCE to this CHILD LOCATION.
            //      Then call setValue() to WRITE the DATA, in our chat object, in our database, at this
            // location

            mDatabaseReference.child("message").push().setValue(chat);

            // RESUME: child() is the location where the data is saved. setValue() is the
            // instruction that actualy COMMITS the data to the database.


            // NOW we have to 'empty' the 'message box' after the user have pressed 'sent'.
            mInputText.setText("");

        }
    }

    // TODO: Override the onStart() lifecycle method. Setup the adapter here.


    @Override
    public void onStop() {
        super.onStop();

        // TODO: Remove the Firebase event listener on the adapter.

    }

}
