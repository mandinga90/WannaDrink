package com.wanna_drink.wannadrink;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wanna_drink.wannadrink.functional.EmailValidator;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class EnterEmailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_email);

        Button btNext = (Button) findViewById(R.id.bt_next);
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edEmail = (EditText) findViewById(R.id.ed_email);
                String email = edEmail.getText().toString();
                if(! email.isEmpty() && EmailValidator.validate(email)){
                    saveEmail(email);
                    startActivity(new Intent(EnterEmailActivity.this, TakePhotoActivity.class));
                }
                else{
                    Toast.makeText(EnterEmailActivity.this, R.string.msg_no_email_entered, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveEmail(String email) {
        SharedPreferences sharedPref = getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.key_email),email);
        editor.commit();
    }
}
