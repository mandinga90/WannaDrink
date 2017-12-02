package com.wanna_drink.wannadrink;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.wanna_drink.wannadrink.Drink.*;

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



    public void selected0(View v){ saveDrink(ANY.getValue());}
    public void selected1(View v){ saveDrink(ANYSOFT.getValue());}
    public void selected2(View v){ saveDrink(ANYSTRONG.getValue());}
    public void selected3(View v){ saveDrink(BEER.getValue());}
    public void selected4(View v){ saveDrink(COCKTAIL.getValue());}
    public void selected5(View v){ saveDrink(WINE.getValue());}
    public void selected6(View v){ saveDrink(TEQUILA.getValue());}
    public void selected7(View v){ saveDrink(VODKA.getValue());}
    public void selected8(View v){ saveDrink(WHISKEY.getValue());}
    public void selected9(View v){ saveDrink(JAGER.getValue());}
    public void selected10(View v){ saveDrink(HOOKA.getValue());}
    public void selected11(View v){ saveDrink(GREENTEE.getValue());}
}
