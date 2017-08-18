package com.example.simone.whatwatch.ChatGroup;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.simone.whatwatch.R;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class ChatGroup extends AppCompatActivity{

    String TAG = "ChatGroup";

    private FirebaseListAdapter<ChatMessage> adapter;
    String id_chat;
    private DatabaseReference mNotificationDatabase;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mChatGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatgroup_layout);

        mNotificationDatabase = FirebaseDatabase.getInstance().getReference().child("Notification");
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mChatGroup = FirebaseDatabase.getInstance().getReference().child("ChatGroup");

        id_chat = getIntent().getStringExtra("identifier");

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);
                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database

                mChatGroup.child(id_chat).push().setValue(new ChatMessage(input.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getDisplayName()));
                HashMap<String, String> notificationData = new HashMap<>();
                notificationData.put("from", mCurrentUser.getUid());
                notificationData.put("chat", id_chat);
                notificationData.put("text", input.getText().toString());
                mNotificationDatabase.child(id_chat).push().setValue(notificationData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Notifica caricata con successo");
                    }
                });
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

                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",model.getMessageTime()));
            }
        };
        listOfMessages.setAdapter(adapter);
    }
}
