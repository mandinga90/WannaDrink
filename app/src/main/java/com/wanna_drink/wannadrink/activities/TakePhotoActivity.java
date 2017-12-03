package com.wanna_drink.wannadrink.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;

import com.wanna_drink.wannadrink.R;

import java.io.ByteArrayOutputStream;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class TakePhotoActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

            byte[] byteArray = stream.toByteArray();
//            saveImageInSharedPref(byteArray);
//            uploadImageToServer();
            startActivity(new Intent(TakePhotoActivity.this, ChooseDrinkActivity.class));
        }
    }

    private void saveImageInSharedPref(byte[] binaryImg) {

        SharedPreferences sharedPref = getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();

        //https://stackoverflow.com/questions/19556433/saving-byte-array-using-sharedpreferences
        String binaryImgString = Base64.encodeToString(binaryImg, Base64.DEFAULT);

        editor.putString(getString(R.string.key_photo),binaryImgString);
        editor.commit();
    }

//    private void testSharedPreferences() {
//        SharedPreferences sharedPref = getDefaultSharedPreferences(getApplicationContext());
//        String name = sharedPref.getString(getString(R.string.key_name), "");
//        Toast.makeText(this,"name " + name,Toast.LENGTH_LONG);
//    }
}
