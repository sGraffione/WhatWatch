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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


//Questa è la classe che visualizza effettivamente la chat ed utilizza un adapter di Firebase
//FirebaseDatabase è una classe che ho importato per permettere di recuperare e salvare i dati sul database di firebase legato al mio account
//Dalla console di firebase possiamo creare e cancellare le caht o singoli messaggi.
//Credo ci sia anche la possibilità di creare già degli id differenti per differenziare diverse chat ma non ne sono sicuro, in
//ogni caso dobbiamo vedere se riusciamo ad instanziare diverse chat e come accedere alle singole.
//Vedete cosa riuscite a fare.. Al momento ho aggiunto un bottone nella watchedList che rimanda alla chat, createne un altro
//e fate in modo di avere due chat diverse
//
//Good Luck
public class ChatGroup extends AppCompatActivity{

    private FirebaseListAdapter<ChatMessage> adapter;
    String id_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatgroup_layout);

        id_chat = getIntent().getStringExtra("identifier");

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);
                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database

                FirebaseDatabase.getInstance().getReference(id_chat).push().setValue(new ChatMessage(input.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getDisplayName()));
                // Clear the input
                input.setText("");
            }
        });
        displayChatMessage();
    }

    public void displayChatMessage(){
        ListView listOfMessages = (ListView)findViewById(R.id.list_of_messages);
        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,R.layout.messageitem, FirebaseDatabase.getInstance().getReference(id_chat)) {
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
