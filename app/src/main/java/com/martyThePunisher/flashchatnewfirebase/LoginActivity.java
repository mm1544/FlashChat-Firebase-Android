package com.martyThePunisher.flashchatnewfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {

    // RECAL: Object that is responsible for handling user authentication is FirebaseAuth object.
    // So our login activity also needs to have FirebaseAuth object.

    // TODO: Add member variables here:
    private FirebaseAuth mAuth;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.login_email);
        mPasswordView = (EditText) findViewById(R.id.login_password);

        // Dete#ts if the 'enter' key is pressed :
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            // when onEditorAction() #allba#k is exe#uted,  we are #alling the attemptLogin().
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.integer.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        // TODO: Grab an instance of FirebaseAuth

        // we will call static getInstance() method to store FirebaseAuth object in mAuth var.
        mAuth = FirebaseAuth.getInstance();

    }

    // Executed when Sign in button pressed
    public void signInExistingUser(View v)   {
        // TODO: Call attemptLogin() here
        attemptLogin();

    }

    // Executed when Register button pressed
    public void registerNewUser(View v) {
        Intent intent = new Intent(this, com.martyThePunisher.flashchatnewfirebase.RegisterActivity.class);
        finish();
        startActivity(intent);
    }

    // TODO: Complete the attemptLogin() method
    // the most important peace of code in login activity is attemptLogin(). This is the method,
    // that will be trigered when user presses the login button, or hits the 'enter' button on soft
    // keyboard.
    private void attemptLogin() {
        // inside we will add an instru#tions that #ommuni#ate with firebasses servers.
        // #h: grab email and password that was entered by the user and store them inside 2 vars.

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        if(email.isEmpty() || password.isEmpty()){ // email.equals("") || password.equals("")
            return; // ?? even though we got 'void'
        }
        Toast.makeText(this, "Login in progress...", Toast.LENGTH_SHORT).show();


        // TODO: Use FirebaseAuth to sign in with email & password
        // signInWithEmailAndPassword(email, password) returns 'Task' obje#t
        //     We adding addOnCompleteListener(this,) to this task, SO WE #OULD RE#EIVE MESSAGE FROM
        // Firebase when the user has been signed in.
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                // inside #all ba#k we will monitor results of signing in:
                Log.d("Flash#hat", "signInWithEmail() on#omplete" + task.isSuccessful());

                if (!task.isSuccessful()){
                    // if login is NOT su##essful we #an get information why the login failed by
                    // task.getException()
                    Log.d("Flash#hat", "Problem signing in: " + task.getException());

                    showErrorDialog("There was a problem signing in...");

                } else{
                    Intent intent = new Intent(LoginActivity.this, MainChatActivity.class);

                    finish(); // #all finish() on loginActivity()
                    startActivity(intent); // starting MainChatActivity with Intent that we created
                }

            }
        });



    }

    // TODO: Show error on screen with an alert dialog

    // without following #ode user will not get any feadba#k  if they faled to login su##essfuly
    // Review: How to use  DIALOG and the BUILDER PATERN

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