package com.wanna_drink.wannadrink.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.wanna_drink.wannadrink.R;
import com.wanna_drink.wannadrink.firebase.Chat;

import java.util.Calendar;
import java.util.Date;

import static com.wanna_drink.wannadrink.functional.App.fireDB;
import static com.wanna_drink.wannadrink.functional.App.mAuth;
import static com.wanna_drink.wannadrink.functional.App.userList;

public class ChatListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                // Creating new chat with a first user in the list.
                Date currentTime = Calendar.getInstance().getTime();
                Calendar endCal = Calendar.getInstance();
                endCal.add(Calendar.HOUR, 2);
                Date endTime = endCal.getTime();

                String user1Id = mAuth.getUid();
                String user2Id = (String)userList.get(0).get("Uid");
                Chat chat = new Chat( user1Id, user2Id,"Me","Other One", currentTime, endTime, "Hey there, buddy!", currentTime);
                fireDB.child("chats").child(user1Id+"::"+userList.get(0).get("Uid")).setValue(chat);



            }
        });
    }

}
