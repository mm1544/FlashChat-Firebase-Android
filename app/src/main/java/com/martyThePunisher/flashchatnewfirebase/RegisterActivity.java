package com.martyThePunisher.flashchatnewfirebase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.AuthResult;


public class RegisterActivity extends AppCompatActivity {

    // Constants
    public static final String CHAT_PREFS = "ChatPrefs";
    public static final String DISPLAY_NAME_KEY = "username";

    // TODO: Add member variables here:
    // UI references.
    private AutoCompleteTextView mEmailView;
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;

    // Firebase instance variables
    // user registration and authentication are handled by an object of type FirebasAuth
    private FirebaseAuth mAuth;
    // we will give mAuth an value when onCreate() method is called for the register activity



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.register_email);
        mPasswordView = (EditText) findViewById(R.id.register_password);
        mConfirmPasswordView = (EditText) findViewById(R.id.register_confirm_password);
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.register_username);

        // Keyboard sign in action
        mConfirmPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.integer.register_form_finished || id == EditorInfo.IME_NULL) {
                    attemptRegistration();
                    return true;
                }
                return false;
            }
        });

        // TODO: Get hold of an instance of FirebaseAuth

        // we will store an instan#e, or an a#tual obje#t of the FirebaseAuth #lass in the mAuth
        // variable
        //!!! INSTEAD of writing mAuth = new FirebaseAuth() to #reate thet obje#t, we are going to
        //! #reate the instan #e by #alling stati# getInstance() method from FirebaseAuth class:

        mAuth = FirebaseAuth.getInstance();

        



    }

    // Executed when Sign Up button is pressed.
    public void signUp(View v) {
        attemptRegistration();
    }

    private void attemptRegistration() {

        // Reset errors displayed in the form.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // TODO: Call create FirebaseUser() here
            createFirebaseUser();

        }
    }

    private boolean isEmailValid(String email) {
        // You can add more checking logic here.
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Add own logic to check for a valid password (minimum 6 characters)
        String confirmPassword = mConfirmPasswordView.getText().toString();
        return confirmPassword.equals(password) && password.length() > 5;
    }

    // TODO: Create a Firebase user
    // create a new method createFirebaseUser(). This method will take care of setting-up a new user
    // on Firebase's server.
    // CH: grab e-mail and password, that user entered, and store them in two variables: - email
    // and password.

    private void createFirebaseUser() {

        //RECAP: getText() doesn't return a String, it returns an obje#t of type 'Editable', so we
        // need toString()
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        // we use FirebaseAuth obje#t to #reate a new Firebase user:
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<com.google.firebase.auth.AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<com.google.firebase.auth.AuthResult> task) {
                // this guy will report back to us if creating a user on Firebase has been successful
                // or if there has been an error.

                Log.d("FlashChat", "createUser onComplete: " + task.isSuccessful());

                if(!task.isSuccessful()){ // if task has not been su##essful
                    Log.d("FlashChat", "user creation failed");

                    showErrorDialog("Resgistration attempt failed");

                } else {
                    saveDisplayName();
                    // need to add an instructions to leave the current screen and head back to
                    // login activity. To do that we will create Intent to navigate back to login
                    // activity.
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    // call finish() at the end of the RegisterActivity and start the login activity
                    // WITH THE INTENT that we just created.
                    finish();
                    startActivity(intent);

                }
            }
        });
        // createUserWithEmailAndPassword() returns ba#k an obje#t of type 'Task', we are going to
        // use this Thask that we got ba#k TO LISTEN FOR AN EVENT. If #reating a user on Firebases'
        // server was su##essfull, an event is triggered and we are going to use that task TO LISTEN
        // FOR THAT EVENT.

        // IN DOCUMENTATION there is a method to add a listener called addOnCompleteListener(Activity
        // activity, OnCompleteListener<TResult>listener). It takes two inputs : Android activity
        // and the listener that we want to add to the task.


    }




    // TODO: Save the display name to Shared Preferences

    // Will be using Android c. SharedPreferences - is a way of saving simple peaces
    // of information as a key:value pairs.

    // simmilar to getInstance() method, this method will take care of object creation and provides
    // an alternative to creating the SharedPreferences using 'new' keyword.

    /*
    public abstract SharedPreferences getSharedPreferences (String name, int mode)
Retrieve and hold the contents of the preferences file 'name', returning a SharedPreferences through
 which you can retrieve and modify its values. Only one instance of the SharedPreferences object is
 returned to any callers for the same name, meaning they will see each other's edits as soon as they
  are made. This method is thead-safe
     */

    private void saveDisplayName(){
        String displayName = mUsernameView.getText().toString();
        SharedPreferences prefs = getSharedPreferences(CHAT_PREFS, 0);
        // to make use of our SharedPreferences object, we first have to inform it that it is going
        // to be edited. By prefs.edit() - the obje#t gets ready to a##ept some data.
        // putString() - will provide data in key:value pair.
        // displayName - is what the user is
        // typed in
        // apply() - to commit the data and safe the information to the device
        prefs.edit().putString(DISPLAY_NAME_KEY, displayName).apply();

    }




    // TODO: Create an alert dialog to show in case registration failed

    //we should provide some sort of feedba#k in form of dialog, if the registration fails. Dialog
    // is some small window that shows users some information or prompt them to do something.

    private  void showErrorDialog(String message){
        // the way to create the dialog is by using the BUILDER PATTERN. In Google documentation:
        // AlertDialog.Builder
        // BUILDER PATTERN is the way of creating an object and configuring it at the same time.

        // We will create an ANONYMOUS alertDialog Builder:
        new AlertDialog.Builder(this)
            // now we can use this anonymous builder to configure our dialog
            .setTitle("Oops")
            .setMessage(message)
            .setPositiveButton(android.R.string.ok, null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();

    }



}
