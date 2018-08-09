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
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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



    // to dete#t that there is a new #hat message in Firebase's server we will have to use a listener
    // - ChildEventListener. This listener will get notified if there are any #hanges to the database.
    // Eg. when someone sends a message, a new data is getting added to database. That qualifies as
    // a #hange, so our listener will report ba#k.
    private ChildEventListener mListener = new ChildEventListener() {
        @Override
        //will get fired when new #hat message is added to the database
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            //when on#hildAdded() is trigered we will re#eive a DataSnapshot from Firebase
            // snapshot is in form of JSON and #ontains our #hatmessage data. This is where we will
            // use ArrayList

            // we will add a snapshot that we re#eived through the #allba#k, to the #ole#tion of
            // snapshots in the ArrayList
            mSnapshotList.add(dataSnapshot);

            // after ea#h addition to the arrayList we need to notify the ListView that it needs to
            // refresh itself:
            notifyDataSetChanged();

        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


    //constructor (that will create and configure a chatListAdapter object)
    public ChatListAdapter(Activity activity, DatabaseReference ref, String name){
                            // ref - referen#e to the Firebase database
                            // name - curent user's display name
        mActivity = activity;
        mDisplayName = name;
        mDatabaseReference = ref.child("message"); // setting equal to messages location coz that
        // is where an individual chat message will come from

        //we need to ata#h ChildEventListener mListener to database referen#e (????)
        // Sin#e atta#ing the Listeners is realy a settup pro#es for the addaptor, we gonna take
        // #are of this inside of the #onstru#tor.
        mDatabaseReference.addChildEventListener(mListener);


        // creating mSnapshotList by calling constructor of the ArrayList
        mSnapshotList = new ArrayList<>();

    }






    /*
    Recap: Individual row in chat will have its own layout.

    We will create a helper class in java code, that acts as little package for individual row.
     */

    // ViewHolder will hold all the Views of a single chat row
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




    /*
    Q: HOW the ListView and the adapter interact with each other?

    A: Android #omponents #an send messages to ea#h other on the ba#k of events
     without dire#t user input.
     Eg.: Android OS sends messages to our app about where our app is in its lifecycle - with
     onCreate(), with onPause() or onDestroy().

     SO... Te ListView and the adapter also interact with each other:
        Eg.: ListView: 'how many items there are in the adapter that needs to display?'.
        ListView poses(?) this question by calling getCount(). The adapter will then respond with
        the NUMBER of items on the list.

            ListView: 'Give the first one.' To get that data ListView will call the adapter's
            getView(0,...,...) method FOR THE FIRST ITEM. This will be the item at position 0.
            The adapter - responds by providing the ListView with the data for the first row (I.E.
            entire row in the form of a View).

            ListView: - requests for the second item on the list. getView(1,...,...)
            and so on.....
     */


    /*
    !!!
    When we will s#roll through the list there is a limeited amount of rows that #an be shown on the
     s#reen. We #ant load all the rows in memory #oz it would be disaster for large list.

     PROBLEM: #reting an individual ROW from S#RA#H is #omputationaly expensive. Phone #ant start
     lagging when we s#rolling through the list.

     We need to use a tri#k: as soon as row s#rolls out of sight, we need to keep hold of the views
     that make up that row. And when the new row s#rolls onto the s#reen, we will supply that row with
     views that we have used before, BUT we will POPULATE ea#h view WITH NEW DATA.
        This avoids having to set up ea#h of the views in the row from s#rat#h. Re#onfiguring an
        egsisting row has BIG PERFORMAN#E ADVANTAGE be#ause we DON'T #onstantly #reate and destroy
        the same type of obje#t.
     */



    @Override
    /*
    The first thing the ListView will 'ask' over chatListAdapter is how many items there are in the
    list, by #alling getCount() method
     */
    public int getCount() {
        return mSnapshotList.size();
    }

    @Override
    public InstantMessage getItem(int position) {
        DataSnapshot snapshot = mSnapshotList.get(position); // position is just an index in ArrayList
        // we need to extra#t InstantMessage obje#t out of snapshot:
        return snapshot.getValue(InstantMessage.class);
        // getValue(InstantMessage.#lass) - #onversts the JSON from the snapshot into InstantMessage obj.

        //!!!
        // getItem() returns InstantMessage obj. when it got #alled inside getView()

    }

    @Override
    /*
    we need to make sure that our adapter #an provide #ore#t message data to the ListView
     SO we need to get the relevant InstantMessage out of the list of snapshots
     This we will done in getItem()
     */
    public long getItemId(int position) {
        return 0;
    }

    @Override
    // if convertView exists, we can re#onfigur it. And if we don't have one (convertView==null), we
    // have to create a new row from the layout file.
    // convertView here represents a list item
    public View getView(int position, View convertView, ViewGroup parent) {

        //###IF THERE IS NO REUSABLE 'VIEWS' WE ARE #REATING ONE:
        // first getView() will #he#k if there is existing row that #an be reused
        // if convertView is equal to null, we will #reate a new row from s#rat#h
        if(convertView == null) {
            //To #reate a view from the layout xml file we need #omponent #alled 'LayoutInflater'.
            // Can grab 'LayoutInflater' from the system with getSystemService().
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // 'LayoutInflater' will #reate a new view for us with inflate() method.
            convertView = inflater.inflate(R.layout.chat_msg_row, parent, false); //
            // within we supply our chat_msg_row.xml and sti#k that into convertView

            //  ViewHolder -- that is our inner helper #lass that will hold on to all the things that make up the
            // individual chat message row
            final ViewHolder holder = new ViewHolder();

            // linking the field of the  ViewHolder to views in the #hat message row
            // #alling findViewById() on the #onvertView be#ause that is the VIEW that we have
            // 'Inflated' from the layout xml.
            holder.authorName = (TextView) convertView.findViewById(R.id.author);

            // to get layout parameters we will #all getLayoutParams() on the TextView, that we have
            // stored under the authorName.
            holder.params = (LinearLayout.LayoutParams) holder.authorName.getLayoutParams();

            // FINALlY. We need to give an adapter the way of storing our ViewHolder for a short
            // period of time so that we #ould reuse it later.

            // Reusing ViewHolder will allow us to AVOID #alling that FindViewById method again. (???)
            // Lets use setTag() to TEMPORARILY STORE our ViewHolder into convertView
            convertView.setTag(holder);

        }


        //###IF WE HAVE REUSABLE 'VIEW' VE ARE SKIPPING LAYOUT #REATION PART AND
        //STARTING TO POPULATE THE DATA FOR THE ROW

        //need to make sure that we are showing the #orre#t message text and author in our list item.
        // Lets get hold of the chat message at the possition in the list. Will do it by #alling
        // getItem().
        // NOTE: getView is #alled by the listView for all the items in the list, and the possition
        // just reffers to the index. Position will be equall 0 for the first item in the list.

        //SO here we are saying: -get the instantMessage (THE MESSAGE THAT SHOULD BE DISPLAYED AT
        // THIS POINT IN THE #HAT) at the #urrent position in the list
        final InstantMessage message = getItem(position);


        // need to add the #ode that reuses the ViewHolder. When we #reated a new ViewHolder, we
        // stored it inside of convertView with the setTag() method. Now we will use getTag() method
        // to retrieve the ViewHolder, that was temporarily saved in the convertView. setTag() and
        // getTag() methods allow us to reseicle our ViewHolders for each row in the list.
        final ViewHolder holder = (ViewHolder) convertView.getTag(); // WE RETRIEVE EXISTING VIEWHOLDER FROM THE #ONVERTVIEW
                    // but the ViewHolder that we have just fet#ht from the convertView is still
                    // gonna have an old data in it (?????!!!) from the previous time when it was
                    // used. We will #hange it by repla#ing staled data
        // Lets retrieve the author for the #urrent item in the list from the instantMessage. Then
        // set the text of the author name TextView in the ViewHolder with the new information. AND
        // will do the same with the #hat message text.
        String author = message.getAuthor();
        holder.authorName.setText(author);

        String msg = message.getMessage();
        holder.body.setText(msg);


        //NOW, when user s#rols the list up and down, no new row layouts will have to be #reated
        // unnecessarily.
        // The #ode in our getView() method updates the #ontents in the list as the row s#rol into view.


        // ViewHolder Pattern
        /////////////////////////
        //
        // https://developer.android.com/training/improving-layouts/smooth-scrolling
        //
        // Chat conversation can have hundreds of rows, but we will be able to display jus t small
        // subset on s#reen at the same time. Phone will only hold those #hat rows in memory, whi#h
        // are going to be displayed to the s#reen (and few extra rows at either end).
        // By having if statement in the getView() the adapter #an #he#k if it has the row awailable
        // that he #an re#ei#le.
        // If we don't have available row, we need to #reate brand new row from the xml layout file.
        // And store all the bits-and-pea#es  inside brand new ViewHolder.
        // Using setTag() method this ViewHolder is then atta#hed to the convertView, whi#h
        // represents a lis item in our list. setTag() method saves the ViewHolder for later.
        // Suppose we are using our app to s#roll up to the list of our #hat messages, and new row
        // in the list of #hat messages will s#roll on onto the s#reen. IF our adapter has a row
        // handy that it #an be reused, we will skip the layout #reation and will go straight to the
        // part where we populare the data for the row.
        // Here we grab the instant message that should be displayed at this point at the #hat.
        // Next we retrieving existing ViewHolder from the convertView. Finally we set the textViews
        // of the ViewHolder a##oerdingly, using the data from the instantMessage obje#t




        // if not equal to null  we will just return the #onvertView.
        return convertView;
    }

    // FINAL THING IN ChatListAdapter class is to #reate a method that would stop #he#king for new
    // events on the database.
    //Q: Why we need it?
    //A: To free-up the resour#es when we don't need them any more

    public  void cleanup(){
        // removes Firebase event listener
        mDatabaseReference.removeEventListener(mListener);

    }

}
