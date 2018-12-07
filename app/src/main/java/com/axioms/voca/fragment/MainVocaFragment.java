package com.axioms.voca.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.axioms.voca.R;
import com.axioms.voca.activity.MainActivity;
import com.axioms.voca.activity.VocaListActivity;
import com.axioms.voca.activity.VocaManageActivity;
import com.axioms.voca.base.GlobalApplication;
import com.axioms.voca.sync.DataSyncThread;
import com.axioms.voca.util.CommUtil;
import com.axioms.voca.util.LogUtil;
import com.axioms.voca.vo.VoVoca;
import com.axioms.voca.vo.VoVocaList;
import com.axioms.voca.vo.VoVocaListArray;
import com.rbrooks.indefinitepagerindicator.IndefinitePagerIndicator;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by kiel1 on 2018-10-17.
 */

public class MainVocaFragment extends Fragment implements View.OnClickListener, MainActivity.OnBackPressedListener {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private ArrayList<VoVoca> vocaArrayList = new ArrayList<>();

    private SlidingUpPanelLayout slidingLayout;
    private ViewPager mViewPager;
    private View ll_vocaCard;
    private SwipeRefreshLayout swipe_refresh_layout;

    public MainVocaFragment() {}

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
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity)context).setOnBackPressedListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_voca, container, false);
        LogUtil.i("onCreateView");

        ll_vocaCard = rootView.findViewById(R.id.ll_vocaCard);
        swipe_refresh_layout = rootView.findViewById(R.id.swipe_refresh_layout);
        swipe_refresh_layout.setColorSchemeResources(R.color.orangish, R.color.yellow,
                android.R.color.holo_purple, R.color.gradient_bg_start);
        swipe_refresh_layout.setOnRefreshListener(onRefreshListener);

        slidingLayout = (SlidingUpPanelLayout) rootView.findViewById(R.id.sliding_layout);
        slidingLayout.setMinFlingVelocity(500);
        //slidingLayout.setAnchorPoint((float) 0.9);

        slidingLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
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
        });

        VocaPagerAdapter vocaPagerAdapter = new VocaPagerAdapter(getActivity().getSupportFragmentManager());

        mViewPager = (ViewPager) rootView.findViewById(R.id.voca_conts);
        mViewPager.setAdapter(vocaPagerAdapter);
        IndefinitePagerIndicator indicator = rootView.findViewById(R.id.indicator);
        indicator.attachToViewPager(mViewPager);

        Toolbar mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_main_search);
        mToolbar.inflateMenu(R.menu.menu_main);

        setHasOptionsMenu(true);

        rootView.findViewById(R.id.btn_voca_change).setOnClickListener(this);
        rootView.findViewById(R.id.btn_voca_list).setOnClickListener(this);
        return rootView;
    }

    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            new DataSyncThread(getContext(), dataSyncListener).run();
        }
    };

    private ArrayList<VoVoca> getTestData() {
        String msg = null;
        try {
            msg = CommUtil.getData(getContext(), "vocabulary.json");
            VoVocaListArray voVocaListArray = GlobalApplication.getGson().fromJson(msg, VoVocaListArray.class);
            return  voVocaListArray.getVOCALIST_LIST().get(0).getVOCA_LIST();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    DataSyncThread.DataSyncListener dataSyncListener = new DataSyncThread.DataSyncListener() {
        @Override
        public void success(String response) {
            getTestData();
            swipe_refresh_layout.setRefreshing(false);
        }

        @Override
        public void fail() {
            swipe_refresh_layout.setRefreshing(false);
        }
    };

    @Override
    public boolean onBack() {
        if (slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            return true;
        }else
            return false;
    }

    boolean isEnaHandler = true;

    @SuppressLint("HandlerLeak")
    Handler panelClosedHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LogUtil.i("msg :: " + msg);

            if(PANELLAYOUT_OPEN_VOCA_CHANGE == msg.what) {
                startActivity(new Intent(getActivity(), VocaManageActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.no_change);
            }else if(PANELLAYOUT_OPEN_VOCA_LIST == msg.what) {
                startActivity(new Intent(getActivity(), VocaListActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.no_change);
            }
            isEnaHandler = true;
        }
    };

    private final int PANELLAYOUT_OPEN_VOCA_CHANGE = 1000;
    private final int PANELLAYOUT_OPEN_VOCA_LIST = 1001;

    @Override
    public void onClick(View view) {

        if(!isEnaHandler) return;

        switch (view.getId()){
            case R.id.btn_voca_change :

                if(SlidingUpPanelLayout.PanelState.EXPANDED ==  slidingLayout.getPanelState()) {
                    changePanelAndHandler(PANELLAYOUT_OPEN_VOCA_CHANGE);
                }
                else{
                    startActivity(new Intent(getActivity(), VocaManageActivity.class));
                    getActivity().overridePendingTransition(R.anim.slide_up, R.anim.no_change);
                }
                break;

            case R.id.btn_voca_list :

                if(SlidingUpPanelLayout.PanelState.EXPANDED ==  slidingLayout.getPanelState()) {
                    changePanelAndHandler(PANELLAYOUT_OPEN_VOCA_LIST);
                }
                else{
                    startActivity(new Intent(getActivity(), VocaListActivity.class));
                    getActivity().overridePendingTransition(R.anim.slide_up, R.anim.no_change);
                }
                break;
        }
    }

    private void changePanelAndHandler(int state) {
        isEnaHandler = false;
        slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        panelClosedHandler.sendEmptyMessageDelayed(state, 200);
    }

    public void setAdapterData(ArrayList<VoVoca> arrayListData) {
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














