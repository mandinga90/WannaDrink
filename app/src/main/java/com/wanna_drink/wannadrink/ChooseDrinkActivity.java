package com.wanna_drink.wannadrink;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class ChooseDrinkActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_drink);
    }

    private void saveDrink (int selectedDrink) {
        SharedPreferences sharedPref = getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.key_drink), selectedDrink);
        editor.commit();
        startActivity(new Intent( ChooseDrinkActivity.this, AvailabilityActivity.class));
    }


    public void selected0(View v){ saveDrink(0);}
    public void selected1(View v){ saveDrink(1);}
    public void selected2(View v){ saveDrink(2);}
    public void selected3(View v){ saveDrink(3);}
    public void selected4(View v){ saveDrink(4);}
    public void selected5(View v){ saveDrink(5);}
    public void selected6(View v){ saveDrink(6);}
    public void selected7(View v){ saveDrink(7);}
    public void selected8(View v){ saveDrink(8);}
    public void selected9(View v){ saveDrink(9);}
    public void selected10(View v){ saveDrink(10);}
    public void selected11(View v){ saveDrink(11);}
}
