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
import static com.wanna_drink.wannadrink.functional.App.currentUserId;

public class StartActivity extends AppCompatActivity {
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        ((Button)findViewById(R.id.bt_next)).setEnabled(false); //Button is grayed until user has ID

        final EditText edName = (EditText) findViewById(R.id.ed_name);

        //Restoring Username and the CheckBox state from sharedPrefs
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        checkBox.setChecked(getSaveUsername());
        if (getSaveUsername()) {
            edName.setText(getUsername());
        }

        Button btNext = (Button) findViewById(R.id.bt_next);
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edName.getText().toString();
                if(! name.isEmpty()){
                    saveUser(name, checkBox.isChecked());
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
        final FirebaseAuth fbAuth = FirebaseAuth.getInstance();
        FirebaseUser fbUser = fbAuth.getCurrentUser();
        if (fbUser == null) {
            fbAuth.signInAnonymously()
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                ((Button)findViewById(R.id.bt_next)).setEnabled(true);
                                currentUserId = fbAuth.getCurrentUser().getUid();
                            } else {
                                Toast.makeText(StartActivity.this, "Authantication failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            ((Button)findViewById(R.id.bt_next)).setEnabled(true);
            currentUserId = fbUser.getUid();
        }
    }

    void saveUser(String name, boolean saveUsername) {
        SharedPreferences sharedPref = getDefaultSharedPreferences(App.getInstance());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("name",name);
        editor.putString("id", currentUserId);
        editor.putString("email",name+"@gmail.com");
        editor.putBoolean("saveUser",saveUsername);
        editor.putBoolean("available",true);
        editor.commit();
    }

    String getUsername() {
        SharedPreferences sharedPref = getDefaultSharedPreferences(App.getInstance());
        return sharedPref.getString("name","Johny Walker");
    }

    boolean getSaveUsername() {
        SharedPreferences sharedPref = getDefaultSharedPreferences(App.getInstance());
        return sharedPref.getBoolean("saveUser", false);
    }

}
