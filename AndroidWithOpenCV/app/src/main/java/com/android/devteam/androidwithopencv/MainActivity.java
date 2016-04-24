package com.android.devteam.androidwithopencv;

import java.io.File;
import java.io.FileInputStream;
import java.util.logging.Logger;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends Activity {
    private final int CAMERA_IMAGE_REQUEST = 101;
    private final String IMAGEPATH =Environment.getExternalStorageDirectory().
            getAbsolutePath( ) + "/tmp _image.jpg";
    Logger log=Logger.getLogger(MainActivity.class.getName());
    //ImageToServer imageToServer=new ImageToServer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnTakePhoto = (Button) findViewById(R.id.btnTakePhoto);
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                File imageFile = new File(IMAGEPATH);
                Uri imageFileUri = Uri.fromFile(imageFile);
                Intent cameraintent = new Intent( android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                log.info(IMAGEPATH);
                cameraintent.putExtra( android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);
                startActivityForResult(cameraintent, CAMERA_IMAGE_REQUEST);

            }
        }) ;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_IMAGE_REQUEST)
            if (resultCode == RESULT_OK)
            { try
            { File imageFile = new File(IMAGEPATH);
                FileInputStream fis = new FileInputStream(imageFile);
                Bitmap img = BitmapFactory.decodeStream(fis);
                ImageView ivPhoto = ( ImageView) findViewById (R.id.ivPhoto);
                ivPhoto.setImageBitmap(img);
            }
            catch (Exception e) {

            }
            }
            else if (resultCode == RESULT_CANCELED)
                Toast.makeText(this, ("cancelled"),
                        Toast.LENGTH_LONG) .show();

    }
}

