package com.martyThePunisher.flashchatnewfirebase;

// FUNCTION of this adapter is to provide the data to the ListView.


/*
ListView is tricky, be#ause it doesn't like to talk to underlying data dire#tly. I.E. wee need a
'middleman' between the ListView and the Chat message data. AND THAT IS adaptor.

Adaptor will serv-up(??) the data for the individual row to be displayed in the LisView.
 */

// adapter will be the bridge between the #hat data message from Firebase and the ListView that
// needs to display the messages.

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

// To make a #hat list adapter and build on top of the work done by Google we will extend BaseAdapter
// RECAP: 'extends' signifies inheritan#e
//          BaseAdapter:
//'public abstract class BaseAdapter' . 'abstract' means that BaseAdapter is meant to be used as a
// templet. It include some stub methods that were left unfinished and it is has to be finished in
// SUBCLASSES

// ArayList - Like an Array it used to store a colection of items. Arrays are fixed in size.
// Individual elements in the array can change but number of entries is fixed.
//
// In contrast to Arrays, an ArrayList can grow and shrink in size.

// Need to specify what kind of data will be stored in ArrayList.

public class ChatListAdapter extends BaseAdapter{

    private Activity mActivity;
    private DatabaseReference mDatabaseReference;
    private  String mDisplayName;
    private ArrayList<DataSnapshot> mSnapshotList; // array list that will hold a DATA SNAPSHOT.
    // DataSnapShot - is a type used by Firebase for passing our data ba#k to our app.
    // SO every time ve read from #loud database we re#eive data in the form of DataSnapshot.


    //constructor (that will create and configure a chatListAdapter object)
    public ChatListAdapter(Activity activity, DatabaseReference ref, String name){
                            // ref - referen#e to the Firebase database
                            // name - curent user's display name
        mActivity = activity;
        mDisplayName = name;
        mDatabaseReference = ref.child("messages"); // setting equal to messages location coz that
        // is where an individual chat message will come from

        // creating mSnapshotList by calling constructor of the ArrayList
        mSnapshotList = new ArrayList<>();

    }



    /*
    Recap: Individual row in chat will have its own layout.

    We will create a helper class in java code, that acts as little package for individual row.
     */

    // ViewHolder will hold all the Views in a single chat row
    static class ViewHolder{
        // adding two TextView fields for this ViewHolder
        TextView authorName;
        TextView body;
        // coz we want to style messages programmaticaly so will add field for Layout parameters
        LinearLayout.LayoutParams params;


        /*
        !!!!!!!
        A class inside a class is refered as an INNER CLASS
         */
    }





    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
