package com.wanna_drink.wannadrink.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.github.bassaer.chatmessageview.models.Message;
import com.github.bassaer.chatmessageview.views.ChatView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wanna_drink.wannadrink.R;
import com.wanna_drink.wannadrink.entities.ChatUser;
import com.wanna_drink.wannadrink.entities.Drink;
import com.wanna_drink.wannadrink.entities.MessageInformation;
import com.wanna_drink.wannadrink.functional.App;

import java.util.Calendar;
import java.util.Objects;

import static com.wanna_drink.wannadrink.functional.App.chatBuddy;
import static com.wanna_drink.wannadrink.functional.App.currentUserId;
import static com.wanna_drink.wannadrink.functional.App.mUser;
import static com.wanna_drink.wannadrink.functional.App.lastChatBuddy;

public class ChatActivity extends AppCompatActivity {
    private static final String TAG = "ChatActivity";
    public static DatabaseReference chatsDB;
    public static DatabaseReference messagesDB;
    ChatUser me;
    ChatUser buddy;
    ChatView chatView;
    String currentChatID;
    ChildEventListener mChildEventListener;

    @Override
    public void onStart() {
        super.onStart();
        chatsDB = FirebaseDatabase.getInstance().getReference().child("chats");

        chatView = (ChatView) findViewById(R.id.chat_view);

        //Check if it's the first time we open the Chat, or if we switched to the chat with other chatBuddy
        //  * get or create new chat in Firebase
        //  * update or create chat UI-users
        //  * reinit the chat
        if ((lastChatBuddy == null) || (!Objects.equals(chatBuddy.getId(), lastChatBuddy.getId()))) {
            chatsDB.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                    String chatId1 = mUser.getId() + "::" + chatBuddy.getId();
                    String chatId2 = chatBuddy.getId() + "::" + mUser.getId();
                    //TODO: check the difference && and &
                    if (!dataSnapshot.hasChild(chatId1) && !dataSnapshot.hasChild(chatId2)) {
                        //initiate new chat
                        currentChatID = chatId1;
                        chatsDB.child(currentChatID).setValue(currentChatID);
                    } else {
                        if (dataSnapshot.hasChild(chatId1))
                            currentChatID = chatId1;
                        else
                            currentChatID = chatId2;
                    }
                    messagesDB = chatsDB.child(currentChatID).child("messages");
                    if (mChildEventListener != null) messagesDB.removeEventListener(mChildEventListener);
                    mChildEventListener = new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            MessageInformation mi;
                            mi = dataSnapshot.getValue(MessageInformation.class);
                            Calendar calendar = Calendar.getInstance();
                            //If it's my message
                            if (currentUserId.equals(mi.getAuthorId())) {
                                calendar.setTimeInMillis(mi.getTimestampLong());
                                final Message message = new Message.Builder()
                                        .setUser(me) // Sender
                                        .setRightMessage(true) // This message Will be shown right side.
                                        .setMessageText(mi.getText()) //Message contents
                                        .setCreatedAt(calendar)
                                        .build();
                                chatView.send(message);
                            } else {
                                calendar.setTimeInMillis(mi.getTimestampLong());
                                final Message message = new Message.Builder()
                                        .setUser(buddy) // Sender
                                        .setRightMessage(false) // This message Will be shown right side.
                                        .setMessageText(mi.getText()) //Message contents
                                        .setCreatedAt(calendar)
                                        .build();
                                chatView.receive(message);
                            }
                        }

                        @Override //All the overridden methods
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                        public void onChildRemoved(DataSnapshot dataSnapshot) {}
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                        public void onCancelled(DatabaseError databaseError) {}
                    };
                    messagesDB.addChildEventListener(mChildEventListener);
                    createChatUsersUI();

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w("Firebase", "loadPost:onCancelled", databaseError.toException());
                }
            }); //End of addListenerForSingleValueEvent


            // If we switched to a Chat with another user, reload the messages
            if ((lastChatBuddy == null) || (!Objects.equals(chatBuddy.getId(), lastChatBuddy.getId()))) {
                chatView.getMessageView().removeAll(); // Clear the chat
            }
            lastChatBuddy = chatBuddy;
        }

        chatView.setOnClickSendButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageInformation mi = new MessageInformation(mUser.getId(), chatView.getInputText());
                messagesDB.push().setValue(mi);
                chatView.setInputText("");
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }

    @Override
    public void onBackPressed() {
        // Going back to MapsActivity
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    private void createChatUsersUI() {
//Fill in visuals for CHAT users
        String myId = "0";
        //TODO: Change to our FirebaseStorage link

        String myName = mUser.getCurrentName();

//        Bitmap myIcon = BitmapFactory.decodeResource(getResources(), R.drawable.face_2);
        Bitmap myIcon = BitmapFactory.decodeResource(getResources(), Drink.getImage(mUser.getCurrentDrinkId()));

        String yourName = chatBuddy.getCurrentName();

        String yourId = "1";
//        Bitmap yourIcon = BitmapFactory.decodeResource(getResources(), R.drawable.face_1);
        Bitmap yourIcon = BitmapFactory.decodeResource(getResources(), Drink.getImage(chatBuddy.getCurrentDrinkId()));

        me = new ChatUser(myId, myName, myIcon);
        buddy = new ChatUser(yourId, yourName, yourIcon);
    }
}
