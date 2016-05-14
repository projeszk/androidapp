package com.android.devteam.androidwithopencv;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.devteam.androidwithopencv.network.ImageToServerAsynctask;


public class Creator extends Fragment implements OnClickListener {
    private final int CAMERA_IMAGE_REQUEST = 101;
    private final String IMAGEPATH =Environment.getExternalStorageDirectory().
            getAbsolutePath( ) +"/"+new Date().getTime()+"_image.jpg";
    Logger log=Logger.getLogger(Creator.class.getName());
    public static final String TAG="Creator";
    File imageFile;
    ImageView ivPhoto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=View.inflate(getActivity(), R.layout.activity_main, null);
        ivPhoto = ( ImageView)v.findViewById (R.id.ivPhoto);

       // Drawable myDrawable = getResources().getDrawable(R.drawable.noimg);
        //ivPhoto= ( ImageView) v.findViewById (R.id.ivPhoto);
       // ivPhoto.setImageDrawable(myDrawable);

        Button btnTakePhoto = (Button) v.findViewById(R.id.btnTakePhoto);
        Button btnsendPhoto = (Button) v.findViewById(R.id.btnTakePhoto);
        btnTakePhoto.setOnClickListener(this);
        btnsendPhoto.setOnClickListener(this);

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       log.info("RRRRRRR"+(requestCode == CAMERA_IMAGE_REQUEST && resultCode == Activity.RESULT_OK));
        if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            { try
            {    File imageFile = new File(IMAGEPATH);
                FileInputStream fis = new FileInputStream(imageFile);
                Bitmap img = BitmapFactory.decodeStream(fis);
                ivPhoto.setImageDrawable(null); // <--- added to force redraw of ImageView
                ivPhoto.setImageURI(Uri.fromFile(imageFile));

            }
            catch (Exception e) {
            }
            }
    }
}

    @Override
    public void onClick(View view) {

        switch(view.getId())
        {
            case R.id.sendToServer:
            {
                ImageToServerAsynctask imgtoserver=new ImageToServerAsynctask(imageFile,1);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Bitmap bmp= null;
                try {
                    bmp = imgtoserver.execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                Intent resultIntent = new Intent(getActivity(),Result.class);
                resultIntent.putExtra("image",byteArray);
                startActivity(resultIntent);

                break;
            }
            case R.id.btnTakePhoto:
            {
                imageFile = new File(IMAGEPATH);
                Uri imageFileUri = Uri.fromFile(imageFile);
                Intent cameraintent = new Intent( android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                log.info(IMAGEPATH);
                cameraintent.putExtra( android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);
                startActivityForResult(cameraintent, CAMERA_IMAGE_REQUEST);
                break;
            }

        }
}

}
