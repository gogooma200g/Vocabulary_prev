package com.axioms.voca.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.axioms.voca.R;

/**
 * Created by kiel1 on 2018-10-19.
 */

public class VocaFragment extends Fragment {

    public VocaFragment() {

    }

    public VocaFragment newInstance(int position) {
        return new VocaFragment();
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_voca, container, false);

//        Toolbar mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
//        mToolbar.setNavigationIcon(R.drawable.ic_main_search);
        setHasOptionsMenu(true);
        return rootView;
    }
}








