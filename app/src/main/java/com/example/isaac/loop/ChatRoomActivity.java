package com.example.isaac.loop;

/**
 * Created by isaac on 4/13/2017.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatRoomActivity extends Activity {

    private FirebaseListAdapter<String> mAdapter;
    private String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom_activity);

        currentUsername = getIntent().getStringExtra("username");

        ListView users = (ListView) findViewById(R.id.userListView);
        Button signOut = (Button) findViewById(R.id.signoutButton);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ChatRoomActivity.this, IntroActivity.class);
                Toast.makeText(ChatRoomActivity.this, "You have been signed out!",
                        Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        loadFromFirebase(users);
        setListOnClick(users);
    }

    /**
     * Loads the users from Firebase.
     *
     * @param list The list of users
     */
    public void loadFromFirebase(final ListView list) {
        FirebaseApp.initializeApp(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("users");
        mAdapter = new FirebaseListAdapter<String>(this, String.class, android.R.layout.simple_expandable_list_item_1, ref.orderByKey()) {
            @Override
            protected void populateView(View view, String username, int position) {
                if (!username.equals(currentUsername)) {
                    TextView entry = ((TextView) view.findViewById(android.R.id.text1));
                    entry.setText(username);
                    entry.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    TextView entry = ((TextView) view.findViewById(android.R.id.text1));
                    entry.setText(username + "(You)");
                    entry.setTextColor(Color.parseColor("#3da5d8"));
                    entry.setOnClickListener(null);

                }
            }
        };
        mAdapter.notifyDataSetChanged();
        list.setAdapter(mAdapter);

    }

    /**
     * Defines what to do when a user is clicked in the list view
     *
     * @param list The list of users
     */
    public void setListOnClick(final ListView list) {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Intent intent = new Intent(ChatRoomActivity.this, MainActivity.class);

                intent.putExtra("currentUsername", currentUsername);

                String targetUserName = (String) parent.getItemAtPosition(position);
                intent.putExtra("targetUsername", targetUserName);

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }
}

