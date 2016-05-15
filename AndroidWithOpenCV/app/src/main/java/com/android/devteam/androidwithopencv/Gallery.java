package com.android.devteam.androidwithopencv;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.android.devteam.androidwithopencv.adapter.ImageAdapter;

/**
 *With this class we can see the images from the gallery's AOC folder.
 *
 * @author  Gábor Szanyi
 * @version 1.0
 * @since   2016-05-15
 */
public class Gallery extends Fragment {
    public static final String TAG="Gallery";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=View.inflate(getActivity(), R.layout.activity_gallery, null);
        GridView gridview = (GridView) v.findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(getActivity()));
        return v;
    }

}
