package com.example.simone.whatwatch.ChatGroup;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.simone.whatwatch.R;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class ChatGroup extends AppCompatActivity{

    String TAG = "ChatGroup";

    private FirebaseListAdapter<ChatMessage> adapter;
    String id_chat;
    String title;
    private Button exit;
    private Button join;
    private TextView titleView;
    private EditText input;
    FloatingActionButton fab;

    String id_user;

    private DatabaseReference mNotificationDatabase;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mChatGroup;
    private DatabaseReference mUserPerChat;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatgroup_layout);

        mNotificationDatabase = FirebaseDatabase.getInstance().getReference().child("Notification");
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mChatGroup = FirebaseDatabase.getInstance().getReference().child("ChatGroup");
        mUserPerChat = FirebaseDatabase.getInstance().getReference().child("UserPerChat");

        id_user = FirebaseAuth.getInstance().getCurrentUser().getUid();

        id_chat = getIntent().getStringExtra("identifier");
        title = getIntent().getStringExtra("title");
        Log.d(TAG, "title: " + title +" | identifier: " + id_chat);

        exit = (Button) findViewById(R.id.exit);
        join = (Button) findViewById(R.id.join);
        titleView = (TextView) findViewById(R.id.title);
        input = (EditText)findViewById(R.id.input);
        fab = (FloatingActionButton)findViewById(R.id.fab);

        titleView.setText(title);

        DatabaseReference ref = mUserPerChat.child(id_chat);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(id_user)){
                    join.setVisibility(View.INVISIBLE);
                    exit.setVisibility(View.VISIBLE);
                    input.setEnabled(true);
                }else{
                    join.setVisibility(View.VISIBLE);
                    exit.setVisibility(View.INVISIBLE);
                    input.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alert = new AlertDialog.Builder(ChatGroup.this).create();
                alert.setTitle("Do you want to exit from this chat? From now on you won't be able to get any notification from this chatroom!");
                alert.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mUserPerChat.child(id_chat).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                        join.setVisibility(View.VISIBLE);
                        exit.setVisibility(View.INVISIBLE);
                        input.setEnabled(false);
                    }
                });
                alert.setButton(AlertDialog.BUTTON_NEGATIVE, "No way, it's too funny", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alert.dismiss();
                    }
                });
                alert.show();
            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> addUser = new HashMap<>();
                addUser.put("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid());
                mUserPerChat.child(id_chat).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(addUser);

                exit.setVisibility(View.VISIBLE);
                join.setVisibility(View.INVISIBLE);
                input.setEnabled(true);
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database

                if(input.getText().toString().trim().length() > 0) {
                    mChatGroup.child(id_chat).push().setValue(new ChatMessage(input.getText().toString().trim(), FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), id_user));

                    HashMap<String, String> notificationData = new HashMap<>();
                    notificationData.put("from", mCurrentUser.getUid());
                    notificationData.put("chat", id_chat);
                    notificationData.put("text", input.getText().toString());
                    notificationData.put("title", title);
                    mNotificationDatabase.child(id_chat).push().setValue(notificationData).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Notifica caricata con successo");
                        }
                    });
                }
                // Clear the input
                input.setText("");
            }
        });
        displayChatMessage();
    }

    public void displayChatMessage(){
        ListView listOfMessages = (ListView)findViewById(R.id.list_of_messages);
        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,R.layout.messageitem, mChatGroup.child(id_chat)) {
        @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message.xml
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);


                if(model.getFirebaseId().equals(id_user)){
                    RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                    params1.addRule(RelativeLayout.ALIGN_PARENT_END);
                    messageUser.setLayoutParams(params1);

                    params2.addRule(RelativeLayout.ALIGN_PARENT_END);
                    params2.addRule(RelativeLayout.BELOW, R.id.message_user);
                    messageText.setLayoutParams(params2);

                    params3.addRule(RelativeLayout.BELOW, R.id.message_text);
                    params3.addRule(RelativeLayout.ALIGN_PARENT_END);
                    params3.setMargins(0,0,0,0);
                    messageTime.setLayoutParams(params3);
                }else{
                    RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                    params1.addRule(RelativeLayout.ALIGN_PARENT_START);
                    messageUser.setLayoutParams(params1);

                    params2.addRule(RelativeLayout.ALIGN_PARENT_START);
                    params2.addRule(RelativeLayout.BELOW, R.id.message_user);
                    messageText.setLayoutParams(params2);

                    params3.addRule(RelativeLayout.BELOW, R.id.message_text);
                    params3.addRule(RelativeLayout.RIGHT_OF, R.id.message_text);
                    params3.setMargins(30,0,0,0);
                    messageTime.setLayoutParams(params3);
                }

                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser().substring(0, model.getMessageUser().indexOf(' ')));
                // Format the date before showing it
                messageTime.setText(DateFormat.format("HH:mm",model.getMessageTime()));
            }
        };
        listOfMessages.setAdapter(adapter);
    }
}
