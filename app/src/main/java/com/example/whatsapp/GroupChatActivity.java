package com.example.whatsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class GroupChatActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ImageButton SendMessageButton;
    private EditText userMessageInput;
    private ScrollView mScrollView;
    private TextView displayTextMessages;

    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef, GroupNameRef, GroupMessageKeyRef;

    private String currentGroupName, currentUserID, currentUserName, currentData, currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);


        currentGroupName = getIntent().getExtras().get("groupName").toString();
        Toast.makeText(GroupChatActivity.this, currentGroupName, Toast.LENGTH_SHORT).show();


        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        GroupNameRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupName);





        InitializeFields();
        
        GetUserInfo();


        SendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SaveMessageInfoToDatabase();

                userMessageInput.setText("");
            }
        });
    }



    private void InitializeFields() {

        mToolbar = (Toolbar) findViewById(R.id.group_chat_bar_layout);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(currentGroupName);

        SendMessageButton = (ImageButton) findViewById(R.id.send_message_button);
        userMessageInput = (EditText) findViewById(R.id.input_group_message);
        displayTextMessages = (TextView) findViewById(R.id.group_chat_text_display);
        mScrollView = (ScrollView) findViewById(R.id.my_scroll_view);

    }

    private void GetUserInfo() {

        UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists())
                {
                    currentUserName = dataSnapshot.child("name").getValue().toString();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void SaveMessageInfoToDatabase() {

        String message = userMessageInput.getText().toString();
        String messageKEY = GroupNameRef.push().getKey();

        if (TextUtils.isEmpty(message))
        {
            Toast.makeText(this, "Please write message first....", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Calendar calForData = Calendar.getInstance();
            SimpleDateFormat currentDataFormat = new SimpleDateFormat("MMM dd, yyyy");
            currentData = currentDataFormat.format(calForData.getTime());

            Calendar calForTime = Calendar.getInstance();
            SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm a");
            currentTime = currentTimeFormat.format(calForData.getTime());


            HashMap<String, Object> groupMessageKey = new HashMap<>();
            GroupNameRef.updateChildren(groupMessageKey);

            GroupMessageKeyRef = GroupNameRef.child(messageKEY);

            HashMap<String, Object> messageInfoMap = new HashMap<>();
                messageInfoMap.put("name",currentUserName);
                messageInfoMap.put("message",message);
                messageInfoMap.put("data",currentData);
                messageInfoMap.put("time",currentTime);
            GroupMessageKeyRef.updateChildren(messageInfoMap);

        }
    }
}
