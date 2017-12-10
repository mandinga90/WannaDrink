package com.wanna_drink.wannadrink.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.wanna_drink.wannadrink.R;
import com.wanna_drink.wannadrink.functional.App;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.wanna_drink.wannadrink.functional.App.getSaveUsername;
import static com.wanna_drink.wannadrink.functional.App.getUsername;
import static com.wanna_drink.wannadrink.functional.App.saveUsername;

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


}
