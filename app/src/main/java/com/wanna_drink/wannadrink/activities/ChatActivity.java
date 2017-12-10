package com.wanna_drink.wannadrink.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.bassaer.chatmessageview.model.User;
import com.github.bassaer.chatmessageview.models.Message;
import com.github.bassaer.chatmessageview.views.ChatView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.wanna_drink.wannadrink.R;
import com.wanna_drink.wannadrink.firebase.MyFirebaseInstanceIDService;
import com.wanna_drink.wannadrink.functional.App;

import static com.wanna_drink.wannadrink.functional.App.mAuth;

public class ChatActivity extends AppCompatActivity {
    User me;
    User you;
    ChatView chatView;

    void initializeDB(){
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Firebase initializeDB", "token: " + refreshedToken);
    }

    void loadMessages(){

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            mAuth.signInAnonymously()
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("Firebase Auth", "signInAnonymously:success");
                                Toast.makeText(ChatActivity.this, "Anonymous authentication succeeded.",
                                        Toast.LENGTH_LONG).show();
                                FirebaseUser currentUser = mAuth.getCurrentUser();
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("Firebase Auth", "signInAnonymously:failure", task.getException());
                                Toast.makeText(ChatActivity.this, "Authentication failed.",
                                        Toast.LENGTH_LONG).show();
                                //updateUI(null);
                            }
                            // ...
                        }
                    });
        }
        else {
            //      updateUI(currentUser);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initializeDB();
        createChatUsers();
        loadMessages();

        chatView = (ChatView) findViewById(R.id.chat_view);

        final Message message1 = new Message.Builder()
                .setUser(me) // Sender
                .setRightMessage(true) // This message Will be shown right side.
                .setMessageText("Hello!") //Message contents
                .build();
        final Message message2 = new Message.Builder()
                .setUser(you) // Sender
                .setRightMessage(false) // This message Will be shown left side.
                .setMessageText("What's up?") //Message contents
                .build();



        chatView.send(message1); // Will be shown right side
        chatView.receive(message2); // Will be shown left side

        chatView.setOnClickSendButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                FirebaseMessaging fm = FirebaseMessaging.getInstance();
//                fm.send(new RemoteMessage.Builder(SENDER_ID + "@gcm.googleapis.com")
//                        .setMessageId(Integer.toString(msgId.incrementAndGet()))
//                        .addData("my_message", "Hello World")
//                        .addData("my_action","SAY_HELLO")
//                        .build());

                Message message3 = new Message.Builder()
                        .setUser(me) // Sender
                        .setRightMessage(true) // This message Will be shown left side.
                        .setMessageText(chatView.getInputText()) //Message contents
                        .build();
                chatView.send(message3);

                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                Log.d("Firebase onClick", "token: " + refreshedToken);

            }
        });

    }

    private void createChatUsers() {
//User id
        int myId = 0;
//User icon
        Bitmap myIcon = BitmapFactory.decodeResource(getResources(), R.drawable.face_2);
//User name
        String myName = mAuth.getCurrentUser().getDisplayName();

        int yourId = 1;
        Bitmap yourIcon = BitmapFactory.decodeResource(getResources(), R.drawable.face_1);
        String yourName = "Emily";

        me = new User(myId, myName, myIcon);
        you = new User(yourId, yourName, yourIcon);
    }
}
