package com.wanna_drink.wannadrink.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.bassaer.chatmessageview.model.User;
import com.github.bassaer.chatmessageview.models.Message;
import com.github.bassaer.chatmessageview.views.ChatView;
import com.github.bassaer.chatmessageview.views.MessageView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.wanna_drink.wannadrink.R;
import com.wanna_drink.wannadrink.entities.Drink;
import com.wanna_drink.wannadrink.entities.MessageInformation;
import com.wanna_drink.wannadrink.functional.App;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;
import static com.wanna_drink.wannadrink.functional.App.currentUserUId;
import static com.wanna_drink.wannadrink.functional.App.mUser;
import static com.wanna_drink.wannadrink.functional.App.previousTalkBuddy;
import static com.wanna_drink.wannadrink.functional.App.talkBuddy;

public class ChatActivity extends AppCompatActivity {
    private static final String TAG = "ChatActivity";
    public static DatabaseReference chatsDB;
    public static DatabaseReference messagesDB;
    com.github.bassaer.chatmessageview.model.User me;
    com.github.bassaer.chatmessageview.model.User buddy;
    ChatView chatView;
    String currentChatID;
    ChildEventListener mChildEventListener;

    @Override
    public void onStart() {
        super.onStart();
        chatsDB = FirebaseDatabase.getInstance().getReference().child("chats");

        chatView = (ChatView) findViewById(R.id.chat_view);

        //Check if it's the first time we open the Chat, or if we switched to the chat with other buddy
        //  * get or create new chat in Firebase
        //  * update or create chat UI-users
        //  * reinit the chat
        if ((previousTalkBuddy == null) || (!Objects.equals(talkBuddy.getUId(), previousTalkBuddy.getUId()))) {
            chatsDB.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                    String chatId1 = mUser.getUId() + "::" + talkBuddy.getUId();
                    String chatId2 = talkBuddy.getUId() + "::" + mUser.getUId();
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
                            if (currentUserUId.equals(mi.getAuthorUId())) {
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
            if ((previousTalkBuddy == null) || (!Objects.equals(talkBuddy.getUId(), previousTalkBuddy.getUId()))) {
//TODO: 2. It hangs when you switch to another buddy
                // Clear the chat
                //TODO: When bassers solves the issue, put back  *** chatView.getMessageView().init(); ***
//                ((ConstraintLayout)findViewById(R.id.parentLayout)).removeView(chatView);
//                AttributeSet attr;
//                ChatView.LayoutParams layoutParams = new ChatView.LayoutParams(ChatView.LayoutParams.MATCH_PARENT, ChatView.LayoutParams.MATCH_PARENT);
//                attr = (ChatView.AttributeSet) layoutParams;
//                chatView = new ChatView(this, attr);
//                ((ConstraintLayout)findViewById(R.id.parentLayout)).addView(chatView);
//                List<Message> msglist = new ArrayList<Message>();
//                chatView.getMessageView().init(msglist);
//                chatView.removeAllViews();
                chatView.getMessageView().init();
                chatView.getMessageView().refresh();


            }
            previousTalkBuddy = talkBuddy;
        }

        chatView.setOnClickSendButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageInformation mi = new MessageInformation(mUser.getUId(), chatView.getInputText());
                messagesDB.push().setValue(mi);
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
        //When we click Back button, we do NOT destroy the chat, just reorder the stack
        // so if we go back to it - we don't have to rebuild the chat and reload all the messages
        Intent intent = new Intent(this, MapsActivity.class);
        intent.setFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    private void createChatUsersUI() {
//Fill in visuals for CHAT users
        int myId = 0;
        //TODO: Change to our FirebaseStorage link

//        Bitmap myIcon = BitmapFactory.decodeResource(getResources(), R.drawable.face_2);
        Bitmap myIcon = BitmapFactory.decodeResource(getResources(), Drink.getImage(mUser.getDrinkID()));
        String myName = mUser.getName();

        int yourId = 1;
//        Bitmap yourIcon = BitmapFactory.decodeResource(getResources(), R.drawable.face_1);
        Bitmap yourIcon = BitmapFactory.decodeResource(getResources(), Drink.getImage(talkBuddy.getDrinkID()));
        String yourName = talkBuddy.getName();

        me = new com.github.bassaer.chatmessageview.model.User(myId, myName, myIcon);
        buddy = new com.github.bassaer.chatmessageview.model.User(yourId, yourName, yourIcon);
    }
}
