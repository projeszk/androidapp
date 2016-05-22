package com.android.devteam.androidwithopencv;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.devteam.androidwithopencv.network.ImageToServerAsynctask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.concurrent.ExecutionException;
/**
 *With this class we can save the image what we got to the gallery's AOC folder.
 *
 * @author  Gábor Szanyi
 * @version 1.0
 * @since   2016-05-15
 */
public class Result extends Activity  implements View.OnClickListener {

    //own folder what we make in the gallery.
    static final String appDirectoryName = "AOC";
    static final File imageRoot = new File(Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES), appDirectoryName);

    /**
     * set our buttons and view.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        byte[] byteArray = getIntent().getByteArrayExtra("image");
        final Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        ImageView ivPhoto = (ImageView)findViewById (R.id.ivPhoto);
        Button saveBtn=(Button)findViewById(R.id.SavePhoto);
        Button removeBtn=(Button)findViewById(R.id.removePhoto);
        ivPhoto.setImageBitmap(bmp);
        saveBtn.setOnClickListener(this);
        removeBtn.setOnClickListener(this);

    }

    /**
     * Here we use the view that get the Buttons id and use their function, like save the image to the gallery, or if we don't want we go back to previous screen with
     * the remove button.
     * @param view we use this to get button's id what we pressed and use it's function.
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.SavePhoto: {
                if(!imageRoot.exists())
                imageRoot.mkdirs();
                File image = new File(imageRoot, new Date().getTime() + ".jpg");
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(image);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap img = BitmapFactory.decodeStream(fis);
                break;
            }
            case R.id.removePhoto: {
                finish();
                break;
            }

        }
    }
}
