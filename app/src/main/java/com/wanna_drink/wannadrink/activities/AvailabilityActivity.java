package com.wanna_drink.wannadrink.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.wanna_drink.wannadrink.R;
import com.wanna_drink.wannadrink.entities.Drink;

import java.util.Date;

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
                TextView tvProgress = findViewById(R.id.tv_progress);
                tvProgress.setText(String.valueOf(progress+1));
            }

            private void updateDrinksVisibility(SeekBar seekBar, int progress) {

                for(int progressIndex=0; progressIndex<=seekBar.getMax(); progressIndex++){

                    if(progressIndex<=(progress)){
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

        Button btMap = findViewById(R.id.bt_map);
        btMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeekBar availabilityLevel = (SeekBar) findViewById(R.id.sb_availability_level);
                saveHours(availabilityLevel.getProgress()+1);

                startActivity(new Intent(AvailabilityActivity.this, MapsActivity.class));
            }
        });

    }

    private void saveHours (int hours) {
        SharedPreferences sharedPref = getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.key_hours), hours);

        Date until = new Date();
        until.setTime(until.getTime() + hours*60*60*1000);
        editor.putString("availableFrom", new Date().toString());
        editor.putString("availableTill", until.toString());
        editor.commit();
    }
}
