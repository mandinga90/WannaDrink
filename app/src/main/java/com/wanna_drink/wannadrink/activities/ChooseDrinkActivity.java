package com.wanna_drink.wannadrink.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import com.wanna_drink.wannadrink.R;
import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.wanna_drink.wannadrink.functional.App.mDrinkImages;

public class ChooseDrinkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_drink);

        GridView gvDrinks = findViewById(R.id.gv_drinks);
        ListAdapter imageAdapter = new ImageAdapter();
        gvDrinks.setAdapter(imageAdapter);
    }

    private void saveDrink (int selectedDrink) {
        SharedPreferences sharedPref = getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("drinkId", selectedDrink);
        editor.commit();
        startActivity(new Intent( ChooseDrinkActivity.this, sessionTimeActivity.class));
    }

    private class ImageAdapter implements ListAdapter {
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
            final int drinkId = position;
            ImageView imageView = new ImageView(ChooseDrinkActivity.this);
            imageView.setImageDrawable(mDrinkImages[position]);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveDrink(drinkId);
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
