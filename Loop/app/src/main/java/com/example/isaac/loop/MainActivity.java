package com.example.isaac.loop;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends Activity {

    private FirebaseRecyclerAdapter<Message, ChatViewHolder> mAdapter;
    private DatabaseReference ref;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_main);
        final String currentUsername = getIntent().getStringExtra("currentUsername");
        final String targetUsername = getIntent().getStringExtra("targetUsername");

        String chatroomID;
        chatroomID = createChatroomID(currentUsername, targetUsername);

        FirebaseApp.initializeApp(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        ref = database.getReference().child("chatrooms").child(chatroomID);

        RecyclerView messageList = (RecyclerView) findViewById(R.id.userMessageView);
        messageList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        messageList.setLayoutManager(linearLayoutManager);

        Button sendButton = (Button) findViewById(R.id.sendButton);
        final EditText newMessage = (EditText) findViewById(R.id.newMessage);

        sendButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String inputMessage = newMessage.getText().toString();
                        if (!inputMessage.equals("")) {
                            String messageID = ref.push().getKey();
                            String uid = mAuth.getCurrentUser().getUid();
                            Message message = new Message(inputMessage, currentUsername, uid);
                            ref.child(messageID).setValue(message);
                            newMessage.setText("");
                        }
                    }
                });

        loadFromFirebase(messageList);
    }

    /**
     * Compares the strings to determine a unique, but consistent chatroom ID for two users.
     * @param user1 The first user
     * @param user2 The second user
     * @return A chatroom ID
     */
    public String createChatroomID(String user1, String user2) {
        String chatID;
        if (user1.compareTo(user2) > 0) {
            chatID = "chat_" + user1 + "_" + user2;
        } else {
            chatID = "chat_" + user2 + "_" + user1;
        }
        return chatID;
    }

    /**
     * Loads the list of messages from Firebase.
     *
     * @param list The RecyclerView we're attaching the adapter to.
     */
    public void loadFromFirebase(RecyclerView list) {
        mAdapter = new FirebaseRecyclerAdapter<Message, ChatViewHolder>(Message.class, R.layout.message,
                ChatViewHolder.class, ref) {
            @Override
            protected void populateViewHolder(ChatViewHolder viewHolder, Message message, int position) {
                viewHolder.setAuthor(message.getAuthor());
                viewHolder.setMessage(message.getMessage());
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null && message.getUid().equals(currentUser.getUid())) {
                    viewHolder.setMessageColor(true);
                } else {
                    viewHolder.setMessageColor(false);
                }
            }
        };
        mAdapter.notifyDataSetChanged();
        list.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }
}