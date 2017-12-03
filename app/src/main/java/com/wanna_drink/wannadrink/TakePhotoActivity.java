package com.wanna_drink.wannadrink;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class TakePhotoActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath;

//    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        dispatchTakePictureIntent();
//        mImageView = (ImageView) findViewById(R.id.img_photo);
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
//            mImageView.setImageBitmap(imageBitmap);
//            testSharedPreferences();
//            startActivity(new Intent(TakePhotoActivity.this, ChooseDrinkActivity.class));
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            saveImageInSharedPref(byteArray);

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
