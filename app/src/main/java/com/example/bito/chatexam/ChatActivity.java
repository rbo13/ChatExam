package com.example.bito.chatexam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class ChatActivity extends AppCompatActivity {

    private FirebaseListAdapter<ChatMessage> adapter;
    private ListView listView;
    private String loggedInUsername = "";
    private Button btnSubmit;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        final EditText input = (EditText) findViewById(R.id.chatText);
        listView = (ListView) findViewById(R.id.list);
        btnSubmit = (Button) findViewById(R.id.buttonSend);
        btnLogout = (Button) findViewById(R.id.btnLogout);

        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            showLoginView();
        } else {
            // User is already authenticated.
            showAllOldMessages();
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(input.getText().toString().trim().equals("")){
                    Toast.makeText(ChatActivity.this, "Must not be empty!", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .push()
                            .setValue(new ChatMessage(input.getText().toString(),
                                    FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                                    FirebaseAuth.getInstance().getCurrentUser().getUid())
                            );
                    input.setText("");
                }

            }
        });

        // Logout button
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                showLoginView();
            }
        });

    }

    private void showAllOldMessages() {
        loggedInUsername = FirebaseAuth.getInstance().getCurrentUser().getUid();

        adapter = new MessageAdapter(this, ChatMessage.class, R.layout.item_in_message,
                FirebaseDatabase.getInstance().getReference());
        listView.setAdapter(adapter);
    }

    public String getLoggedInUsername() {
        return loggedInUsername;
    }

    private void showLoginView() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
