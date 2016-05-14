package com.android.devteam.androidwithopencv;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

public class Result extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        byte[] byteArray = getIntent().getByteArrayExtra("image");
        final Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        ImageView ivPhoto = (ImageView)findViewById (R.id.ivPhoto);
        Button saveBtn=(Button)findViewById(R.id.SavePhoto);
        Button removeBtn=(Button)findViewById(R.id.removePhoto);

        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MediaStore.Images.Media.insertImage(getContentResolver(), bmp, "Tittle" , "Desc");
            }
        }) ;


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
    }

}
