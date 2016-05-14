package com.android.devteam.androidwithopencv;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Gallery extends Fragment {
    public static final String TAG="Gallery";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=View.inflate(getActivity(), R.layout.activity_gallery, null);

        return v;
    }

}
