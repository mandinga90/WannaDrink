package com.wanna_drink.wannadrink;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class AvailabilityActivity extends AppCompatActivity {

    private ImageView[] mIvDrinks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availibility);

        SharedPreferences sharedPref = getDefaultSharedPreferences(getApplicationContext());
        int selectedDrink = sharedPref.getInt(getString(R.string.key_drink), 0);

        ImageView ivDrink1 = findViewById(R.id.iv_drink_1);
        ImageView ivDrink2 = findViewById(R.id.iv_drink_2);
        ImageView ivDrink3 = findViewById(R.id.iv_drink_3);
        ImageView ivDrink4 = findViewById(R.id.iv_drink_4);
        ImageView ivDrink5 = findViewById(R.id.iv_drink_5);

        mIvDrinks = new ImageView[] {ivDrink1, ivDrink2, ivDrink3, ivDrink4, ivDrink5};

        Drawable drinkImg = getResources().getDrawable(Drink.getImage(selectedDrink));

        ivDrink1.setImageDrawable(drinkImg);
        ivDrink2.setImageDrawable(drinkImg);
        ivDrink3.setImageDrawable(drinkImg);
        ivDrink4.setImageDrawable(drinkImg);
        ivDrink5.setImageDrawable(drinkImg);

        SeekBar availabilityLevel = (SeekBar) findViewById(R.id.sb_availability_level);

        availabilityLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateDrinksVisibility(seekBar, progress);
            }

            private void updateDrinksVisibility(SeekBar seekBar, int progress) {

                for(int progressIndex=1; progressIndex<seekBar.getMax(); progressIndex++){

                    if(progressIndex<=(progress-1)){
                        mIvDrinks[progressIndex].setVisibility(View.VISIBLE);
                    }
                    else{
                        mIvDrinks[progressIndex].setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
}
