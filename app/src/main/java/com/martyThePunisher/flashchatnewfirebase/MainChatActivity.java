package com.martyThePunisher.flashchatnewfirebase;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;


public class MainChatActivity extends AppCompatActivity {

    // TODO: Add member variables here:
    private String mDisplayName;
    private ListView mChatListView;
    private EditText mInputText;
    private ImageButton mSendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);

        // TODO: Set up the display name and get the Firebase reference
        setupDisplayName();


        // Link the Views in the layout to the Java code
        mInputText = (EditText) findViewById(R.id.messageInput);
        mSendButton = (ImageButton) findViewById(R.id.sendButton);
        mChatListView = (ListView) findViewById(R.id.chat_list_view);

        // TODO: Send the message when the "enter" button is pressed


        // TODO: Add an OnClickListener to the sendButton to send a message

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

        // TODO: Grab the text the user typed in and push the message to Firebase

    }

    // TODO: Override the onStart() lifecycle method. Setup the adapter here.


    @Override
    public void onStop() {
        super.onStop();

        // TODO: Remove the Firebase event listener on the adapter.

    }

}
