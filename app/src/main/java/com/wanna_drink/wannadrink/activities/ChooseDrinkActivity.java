package com.wanna_drink.wannadrink.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;


import com.wanna_drink.wannadrink.R;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.wanna_drink.wannadrink.R.drawable.*;
import static com.wanna_drink.wannadrink.entities.Drink.*;

public class ChooseDrinkActivity extends AppCompatActivity {

    Drawable[] mDrinkImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_drink);

        mDrinkImages = new Drawable[] {
                getResources().getDrawable(any),
                getResources().getDrawable(anysoft),
                getResources().getDrawable(anystrong),
                getResources().getDrawable(beer),
                getResources().getDrawable(cocktail),
                getResources().getDrawable(wine),
                getResources().getDrawable(tequila),
                getResources().getDrawable(vodka),
                getResources().getDrawable(whiskey),
                getResources().getDrawable(jager),
                getResources().getDrawable(hooka),
                getResources().getDrawable(greentee),};

        GridView gvDrinks = findViewById(R.id.gv_drinks);
        ListAdapter imageAdapter = new ImageAdapter();
        gvDrinks.setAdapter(imageAdapter);
    }

    private void saveDrink (int selectedDrink) {
        SharedPreferences sharedPref = getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.key_drink), selectedDrink);
        editor.commit();
        startActivity(new Intent( ChooseDrinkActivity.this, AvailabilityActivity.class));
    }

    private class ImageAdapter implements ListAdapter {
//        public ImageAdapter(ChooseDrinkActivity chooseDrinkActivity, Drawable[] drinkImages) {
//        }

        @Override
        public boolean areAllItemsEnabled() {
            return true;
        }

        @Override
        public boolean isEnabled(int position) {
            return true;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {}

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer){}

        @Override
        public int getCount() {
            return mDrinkImages.length;
        }

        @Override
        public Object getItem(int position) {
            return mDrinkImages[position];
        }

        @Override
        public long getItemId(int position) {
            return mDrinkImages[position].hashCode();
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final int drinkIndex = position;
            ImageView imageView = new ImageView(ChooseDrinkActivity.this);
            imageView.setImageDrawable(mDrinkImages[position]);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveDrink(drinkIndex);
                }
            });
            return imageView;
        }

        @Override
        public int getItemViewType(int position) {
            return 1;
        }

        @Override
        public int getViewTypeCount() {
            return getCount();
        }

        @Override
        public boolean isEmpty() {
            return false;
        }
    }
}
