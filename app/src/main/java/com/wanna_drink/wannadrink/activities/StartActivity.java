package com.wanna_drink.wannadrink.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wanna_drink.wannadrink.R;
import com.wanna_drink.wannadrink.functional.App;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.wanna_drink.wannadrink.functional.App.currentUserUId;
import static com.wanna_drink.wannadrink.functional.App.getInstance;
import static com.wanna_drink.wannadrink.functional.App.getSaveUsername;
import static com.wanna_drink.wannadrink.functional.App.getUsername;

public class StartActivity extends AppCompatActivity {
    CheckBox cb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        final EditText edName = (EditText) findViewById(R.id.ed_name);

        cb = (CheckBox) findViewById(R.id.checkBox);
        cb.setChecked(getSaveUsername());

        if (getSaveUsername()) {
            edName.setText(getUsername());
        }

        Button btNext = (Button) findViewById(R.id.bt_next);
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edName.getText().toString();
                if(! name.isEmpty()){
                    saveUsername(name,cb.isChecked());
                    startActivity(new Intent(StartActivity.this, TakePhotoActivity.class));
                }
                else{
                    Toast.makeText(StartActivity.this, R.string.msg_no_name_entered, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and make a new try.
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            auth.signInAnonymously()
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                ((Button)findViewById(R.id.bt_next)).setEnabled(true);
                                currentUserUId = auth.getCurrentUser().getUid();
                            } else {
                                Toast.makeText(StartActivity.this, "Authantication failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            ((Button)findViewById(R.id.bt_next)).setEnabled(true);
            currentUserUId = currentUser.getUid();
        }


    }

    public static void saveUsername(String name, boolean saveUsername) {
        SharedPreferences sharedPref = getDefaultSharedPreferences(App.getInstance());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("name",name);
        editor.putString("uId",currentUserUId);
        editor.putString("email",name+"@gmail.com");
        editor.putBoolean("saveUsername",saveUsername);
        editor.putBoolean("available",true);
        editor.commit();
    }

}
