package com.axioms.voca.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.axioms.voca.R;
import com.axioms.voca.util.LogUtil;
import com.axioms.voca.vo.VoVocaList;
import com.rbrooks.indefinitepagerindicator.IndefinitePagerIndicator;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;

/**
 * Created by kiel1 on 2018-10-17.
 */

public class MainVocaFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private ArrayList<VoVocaList.VoVoca> vocaArrayList = new ArrayList<>();

    private SlidingUpPanelLayout slidingLayout;
    private ViewPager mViewPager;
    private View ll_vocaCard;

    public MainVocaFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MainVocaFragment newInstance(int position) {
//        MainVocaFragment fragment = new MainVocaFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//        fragment.setArguments(args);
        return  new MainVocaFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_voca, container, false);
        LogUtil.i("onCreateView");

        ll_vocaCard = rootView.findViewById(R.id.ll_vocaCard);

        slidingLayout = (SlidingUpPanelLayout) rootView.findViewById(R.id.sliding_layout);
        //slidingLayout.setAnchorPoint((float) 0.9);

        slidingLayout.addPanelSlideListener(panelSlideListener);

        VocaPagerAdapter vocaPagerAdapter = new VocaPagerAdapter(getActivity().getSupportFragmentManager());

        mViewPager = (ViewPager) rootView.findViewById(R.id.voca_conts);
        mViewPager.setAdapter(vocaPagerAdapter);
        IndefinitePagerIndicator indicator = rootView.findViewById(R.id.indicator);
        indicator.attachToViewPager(mViewPager);

        Toolbar mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_main_search);
        mToolbar.inflateMenu(R.menu.comm);

        setHasOptionsMenu(true);

        rootView.findViewById(R.id.btn_voca_change).setOnClickListener(this);
        rootView.findViewById(R.id.btn_voca_list).setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_voca_change :
                break;

            case R.id.btn_voca_list :
                break;
        }
    }

    SlidingUpPanelLayout.PanelSlideListener panelSlideListener = new SlidingUpPanelLayout.PanelSlideListener(){

        @Override
        public void onPanelSlide(View panel, float slideOffset) {
            float scale = (float) (1 - ((slideOffset * 0.1)));
            //float scale2 = (float) ((slideOffset * 0.2) + 0.8);
            LogUtil.i("onPanelSlide, offset " + slideOffset + " / " + scale);
            ll_vocaCard.setScaleX(scale);
            ll_vocaCard.setScaleY(scale);
        }

        @Override
        public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
            LogUtil.i("onPanelStateChanged " + newState);
        }
    };


    public void setAdapterData(ArrayList<VoVocaList.VoVoca> arrayListData) {
        this.vocaArrayList = arrayListData;
    }

    /**
     * Voca Adapter
     */
    public class VocaPagerAdapter extends FragmentPagerAdapter {

        public VocaPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //LogUtil.i("position : " + position);
            return new VocaFragment().newInstance(position);
        }

        @Override
        public int getCount() {
            return vocaArrayList.size();
        }


    }


}














