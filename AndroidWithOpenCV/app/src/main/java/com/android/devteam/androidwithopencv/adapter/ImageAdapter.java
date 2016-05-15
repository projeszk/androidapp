package com.android.devteam.androidwithopencv.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 *With this class we set the images for our gallery.
 *
 * @author  Gábor Szanyi
 * @version 1.0
 * @since   2016-05-15
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    static final String appDirectoryName = "AOC";
    static final File imageRoot = new File(android.os.Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES), appDirectoryName);
    // references to our images
    private List<String> mThumbIds;
    Logger log=Logger.getLogger(ImageAdapter.class.getName());


    public ImageAdapter(Context c) {
        mContext = c;
        if(!imageRoot.exists())
        {
            log.info("Create directory for the images:");
            imageRoot.mkdirs();
        }
        mThumbIds=getAllShownImagesPath();
    }

    public int getCount() {
        return mThumbIds.size();
    }

    public Object getItem(int position) {
        return mThumbIds.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    /**
     * This method gave us the images for the Gallery's gridview
     * @param position
     * @param convertView
     * @param parent
     * @return one of the images from our directory (AOC)
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        } else {
            imageView = (ImageView) convertView;
        }
        File imageFile = new File(imageRoot+"/"+mThumbIds.get(position));
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(imageFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap preview_bitmap = BitmapFactory.decodeStream(fis, null, options);
        imageView.setImageBitmap(preview_bitmap);

        return imageView;
    }


    /**
     * This method gave us the list of files from the imageRoot directory
     * @return List of files
     */
    private ArrayList<String> getAllShownImagesPath() {
        return new ArrayList<String>(Arrays.asList(imageRoot.list()));
    }


}