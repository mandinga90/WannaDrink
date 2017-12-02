package com.wanna_drink.wannadrink;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btNext = (Button) findViewById(R.id.bt_next);
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edName = (EditText) findViewById(R.id.ed_name);
                String name = edName.getText().toString();
                if(! name.isEmpty()){
                    saveName(name);
                    startActivity(new Intent(MainActivity.this, TakePhotoActivity.class));
                }
                else{
                    Toast.makeText(MainActivity.this, R.string.msg_no_name_entered, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveName(String name) {
        SharedPreferences sharedPref = getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.key_name),name);
        editor.commit();
    }
}
