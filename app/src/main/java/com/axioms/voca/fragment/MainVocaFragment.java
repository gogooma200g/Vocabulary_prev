package com.axioms.voca.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.axioms.voca.R;
import com.axioms.voca.activity.MainActivity;
import com.axioms.voca.activity.VocaListActivity;
import com.axioms.voca.activity.VocaListManageActivity;
import com.axioms.voca.base.GlobalApplication;
import com.axioms.voca.customview.CustomEditeText;
import com.axioms.voca.sync.DataSyncThread;
import com.axioms.voca.tts.SpeechHelper;
import com.axioms.voca.util.CommUtil;
import com.axioms.voca.util.LogUtil;
import com.axioms.voca.util.StringUtil;
import com.axioms.voca.util.ToastUtil;
import com.axioms.voca.vo.VoVoca;
import com.axioms.voca.vo.VoVocaListArray;
import com.rbrooks.indefinitepagerindicator.IndefinitePagerIndicator;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kiel1 on 2018-10-17.
 */

public class MainVocaFragment extends Fragment implements View.OnClickListener, MainActivity.OnBackPressedListener {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private final int CHECK_CODE = 0x1;

    private SpeechHelper speaker;
    private  MainActivity mainActivity = null;
    private ArrayList<VoVoca> vocaArrayList = new ArrayList<>();
    private ArrayList<VoVoca> allVocaArrayList = new ArrayList<>();

    private enum State {
        All, Memorial, NoMemorial
    }

    private enum PlayState {
        All, Vocabulary, Mean
    }

    private PlayState playState = PlayState.All;
    private State currentState = State.All;

    @BindView(R.id.sliding_layout) SlidingUpPanelLayout slidingLayout;
    @BindView(R.id.viewPager) ViewPager mViewPager;
    @BindView(R.id.ll_vocaCard) View ll_vocaCard;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipe_refresh_layout;
    @BindView(R.id.btn_addWords) AppCompatButton btn_addWords;
    @BindView(R.id.et_eng) CustomEditeText et_eng;
    @BindView(R.id.et_kor) CustomEditeText et_kor;
    @BindView(R.id.btn_voca_change) AppCompatImageButton btn_voca_change;
    @BindView(R.id.btn_voca_list) AppCompatImageButton btn_voca_list;
    @BindView(R.id.btn_setting) LinearLayout btn_setting;
    @BindView(R.id.btn_remove_eng) ImageButton btn_remove_eng;
    @BindView(R.id.btn_remove_kor) ImageButton btn_remove_kor;
    @BindView(R.id.btn_confirm) LinearLayout btn_confirm;
    @BindView(R.id.tv_currentNum) TextView tv_currentNum;
    @BindView(R.id.btn_state) Button btn_state;
    @BindView(R.id.btn_play) LinearLayout btn_play;
    @BindView(R.id.ib_play) ImageButton ib_play;

    private Animation animationUp;
    private VocaPagerAdapter vocaPagerAdapter;
    private int currentPos = 0;

    public static boolean isVisibleWords = true;
    public static boolean isVisibleMeans = true;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        animationUp = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);
        animationUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(isShowKeyboard) {
                    btn_confirm.setVisibility(View.VISIBLE);
                } else {
                    btn_setting.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //TTS Setting
        checkTTS();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.i("resultCode :  " + resultCode);
        if(requestCode == CHECK_CODE){
            if(resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS){
                speaker = new SpeechHelper(getContext(), utteranceProgressListener);
            }else {
                Intent install = new Intent();
                install.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(install);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
        mainActivity.setOnBackPressedListener(this);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_voca, container, false);
        ButterKnife.bind(this, rootView);
        LogUtil.i("onCreateView");

        ll_vocaCard = rootView.findViewById(R.id.ll_vocaCard);
        swipe_refresh_layout = rootView.findViewById(R.id.swipe_refresh_layout);
        swipe_refresh_layout.setColorSchemeResources(R.color.orangish, R.color.yellow,
                android.R.color.holo_purple, R.color.gradient_bg_start);
        swipe_refresh_layout.setOnRefreshListener(onRefreshListener);
        swipe_refresh_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                LogUtil.i("getXPrecision : "  + event.getXPrecision());
                LogUtil.i("getYPrecision : "  + event.getYPrecision());
                return false;
            }
        });

        slidingLayout.setMinFlingVelocity(500);
        //slidingLayout.setAnchorPoint((float) 0.9);
        slidingLayout.addPanelSlideListener(panelSlideListener);

        vocaPagerAdapter = new VocaPagerAdapter(getChildFragmentManager());

        mViewPager.setAdapter(vocaPagerAdapter);
        IndefinitePagerIndicator indicator = rootView.findViewById(R.id.indicator);
        indicator.attachToViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(onPageChangeListener);
        tv_currentNum.setText("1/" + vocaArrayList.size());

        Toolbar mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_main_search);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        });
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        //mToolbar.inflateMenu(R.menu.menu_main);
        setHasOptionsMenu(true);

        btn_play.setOnClickListener(this);

        btn_voca_change.setOnClickListener(this);
        btn_voca_list.setOnClickListener(this);

        et_eng.setOnBackPressListener(onBackPressListener);
        et_kor.setOnBackPressListener(onBackPressListener);
        et_eng.setOnClickListener(this);
        et_kor.setOnClickListener(this);

        btn_remove_eng.setOnClickListener(this);
        btn_remove_kor.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);
        btn_addWords.setOnClickListener(this);
        btn_state.setOnClickListener(this);

        return rootView;
    }



    @Override
    public void onDestroy() {
        speaker.destroy();
        super.onDestroy();
    }

    //    private TextWatcher textWatcher = new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            LogUtil.i("beforeTextChanged : " + charSequence);
//        }
//
//        @Override
//        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            LogUtil.i("onTextChanged : " + charSequence);
//        }
//
//        @Override
//        public void afterTextChanged(Editable editable) {
//            LogUtil.i("afterTextChanged : " + editable.toString());
//        }
//    };

    /**  SlidingPanel Listener **/
    private SlidingUpPanelLayout.PanelSlideListener panelSlideListener = new SlidingUpPanelLayout.PanelSlideListener() {
        @Override
        public void onPanelSlide(View panel, float slideOffset) {
            float scale = (float) (1 - ((slideOffset * 0.1)));
            //float scale2 = (float) ((slideOffset * 0.2) + 0.8);
            //LogUtil.i("onPanelSlide, offset " + slideOffset + " / " + scale);
            LogUtil.i("scale : " + scale);
            ll_vocaCard.setScaleX(scale);
            ll_vocaCard.setScaleY(scale);
        }

        @Override
        public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
            LogUtil.i("onPanelStateChanged " + newState);
            if(newState.equals(SlidingUpPanelLayout.PanelState.COLLAPSED)){
                btn_addWords.setSelected(false);
                hideWordsView();
            }else if(newState.equals(SlidingUpPanelLayout.PanelState.EXPANDED)) {
                btn_addWords.setSelected(true);
                showWordsView();
            }
        }
    };

    /**  Page Change Listener **/
    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            LogUtil.i("onPageScrolled");
        }

        @Override
        public void onPageSelected(int position) {
            currentPos = position;
            tv_currentNum.setText((position + 1) + "/" + vocaArrayList.size());
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            LogUtil.i("onPageScrollStateChanged");
        }
    };

    private TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            String str = textView.getText().toString();
            LogUtil.i("onEditorAction : " + str);
            return false;
        }
    };

    private CustomEditeText.onEditTextListener onBackPressListener = new CustomEditeText.onEditTextListener() {
        @Override
        public void onBackPress() {
            closeKeyboard();
        }

        @Override
        public void onFocusMoved() {
            if(isShowKeyboard) return;
            else showKeyboardState();
        }
    };

    /**
     * showWordsView
     **/
    private void showWordsView() {
        et_eng.setFocusable(true);
        et_eng.setFocusableInTouchMode(true);
        et_kor.setFocusable(true);
        et_kor.setFocusableInTouchMode(true);
        et_eng.requestFocus();
        showKeyboardState();
    }

    /**
     * hideWordsView
     **/
    private void hideWordsView() {
        et_eng.setFocusable(false);
        et_eng.setFocusableInTouchMode(false);
        et_kor.setFocusable(false);
        et_kor.setFocusableInTouchMode(false);
        closeKeyboard();
    }

    boolean isShowKeyboard = false;

    private void showKeyboardState() {
        if(!isShowKeyboard) {
            LogUtil.i("showKeyboardState");
            isShowKeyboard = true;
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
            btn_setting.setVisibility(View.GONE);
            btn_confirm.setVisibility(View.INVISIBLE);
            btn_confirm.startAnimation(animationUp);
        }
    }

    private void closeKeyboard(){
        if(!isShowKeyboard) return;
        LogUtil.i("closeKeyboard");
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        isShowKeyboard = false;
        btn_setting.setVisibility(View.INVISIBLE);
        btn_setting.startAnimation(animationUp);
        btn_confirm.setVisibility(View.GONE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        ((AppCompatActivity) getActivity()).getMenuInflater().inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            mainActivity.setFragment(2);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            new DataSyncThread(getContext(), dataSyncListener).run();
        }
    };

    /** 데이터 가져오기 */
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

    /** 데이터 리로딩 **/
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
                startActivity(new Intent(getActivity(), VocaListManageActivity.class));
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
                /** Go to VocaListManageActivity */
                if(SlidingUpPanelLayout.PanelState.EXPANDED ==  slidingLayout.getPanelState()) {
                    changePanelAndHandler(PANELLAYOUT_OPEN_VOCA_CHANGE);
                }
                else{
                    startActivity(new Intent(getActivity(), VocaListManageActivity.class));
                    getActivity().overridePendingTransition(R.anim.slide_up, R.anim.no_change);
                }
                break;

            case R.id.btn_voca_list :
                /** Go to VocaListActivity */
                if(SlidingUpPanelLayout.PanelState.EXPANDED ==  slidingLayout.getPanelState()) {
                    changePanelAndHandler(PANELLAYOUT_OPEN_VOCA_LIST);
                }
                else{
                    startActivity(new Intent(getActivity(), VocaListActivity.class));
                    getActivity().overridePendingTransition(R.anim.slide_up, R.anim.no_change);
                }
                break;

            case R.id.btn_remove_eng :
                et_eng.setText("");
                break;

            case R.id.btn_remove_kor :
                et_kor.setText("");
                break;

            case R.id.btn_confirm:
                addNewWord();
                break;

            case R.id.btn_setting :
                break;

            case R.id.btn_addWords :
                if(btn_addWords.isSelected()) slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                else slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                break;

            case R.id.btn_state :
                /** memorial State */
                if(State.All == currentState) {
                    currentState = State.Memorial;
                    btn_state.setText(R.string.str_memorize);
                }else if(State.Memorial == currentState) {
                    currentState = State.NoMemorial;
                    btn_state.setText(R.string.str_not_memorize);
                }else if(State.NoMemorial == currentState) {
                    currentState = State.All;
                    btn_state.setText(R.string.str_all);
                }
                stateSetWordArrayList();
                break;

            case R.id.et_eng :
                if(!isShowKeyboard) {
                    et_eng.requestFocus();
                    isShowKeyboard = true;
                    btn_setting.setVisibility(View.GONE);
                    btn_confirm.setVisibility(View.INVISIBLE);
                    btn_confirm.startAnimation(animationUp);
                }
                break;

            case R.id.et_kor :
                et_kor.requestFocus();
                isShowKeyboard = true;
                btn_setting.setVisibility(View.GONE);
                btn_confirm.setVisibility(View.INVISIBLE);
                btn_confirm.startAnimation(animationUp);
                break;

            case R.id.btn_play :
                if(isPlaying) {
                    ib_play.setBackgroundResource(R.drawable.ic_main_play);
                    resetSpeakState();
                    break;
                }
                ib_play.setBackgroundResource(R.drawable.ic_main_stop);
                speaker.setAllowed(true);
                //speaker.speak("We can only extract the phone number of the sender from the message. ");
                speakVoca();

                if(playState == PlayState.All) {
                    nextWords = true;
                    AllSpeakVoca(vocaArrayList.get(currentPos).getWORD(), Locale.US, 0.8f);
                }


                isPlaying = true;
                break;

        }
    }

    boolean canNextWords = false;
    /** Speak Voca **/
    private void speakVoca() {

        String str = "";

        /** All */
        if(playState == PlayState.All) {
            if(canNextWords) {
                str = vocaArrayList.get(currentPos).getMEAN();
                speaker.speak(str, Locale.KOREA, 1.0f);
                canNextWords = false;
            }else{
                str = vocaArrayList.get(currentPos).getWORD();
                speaker.speak(str, Locale.US, 0.8f);
                canNextWords = true;
            }
        }
    }

    private void resetSpeakState() {
        speaker.stop();
        isPlaying = false;
        nextWords = false;
    }

    /**
     * All
     */
    boolean nextWords = false;
    private void AllSpeakVoca(String str, Locale lan, float rate) {
        speaker.speak(str, lan, rate);
    }

    private void checkTTS(){
        Intent check = new Intent();
        check.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(check, CHECK_CODE);
    }

    private boolean isPlaying = false;
    private UtteranceProgressListener utteranceProgressListener = new UtteranceProgressListener() {
        @Override
        public void onStart(String s) {
            LogUtil.i("onStart : " + s);
//            isPlaying = true;
        }

        @Override
        public void onDone(String s) {
            LogUtil.i("onDone : " + s);
            if(playState == PlayState.All) {
                if(nextWords) {
                    //Read Korean
                    handler.sendEmptyMessageDelayed(0, 1000);
                }else{
                    //Read next words
                    currentPos++;
                    mViewPager.setCurrentItem(currentPos);
                    //AllSpeakVoca(vocaArrayList.get(currentPos).getMEAN(), Locale.US, 0.8f);
                }
            }

//            if(vocaArrayList.size() < currentPos) return;
//            if(canNextWords) speakVoca();
        }

        @Override
        public void onError(String s) {

            LogUtil.i("onError : " + s);
        }
    };

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(playState == PlayState.All) {
                nextWords = false;

                AllSpeakVoca(vocaArrayList.get(currentPos).getMEAN(), Locale.KOREA, 1.0f);
            }
        }
    };

    /** All /  Memorial / No Memorial **/
    private void stateSetWordArrayList() {
        vocaArrayList.clear();

        if(State.All == currentState) {                 //전체
            for(VoVoca voVoca : allVocaArrayList) {
                vocaArrayList.add(voVoca);
            }
        }else{
            for(VoVoca voVoca : allVocaArrayList) {
                LogUtil.i("memo yn : " + voVoca.getMEMO_YN());
                if(State.Memorial == currentState) {    //암기
                    if ("Y".equals(voVoca.getMEMO_YN())) {
                        vocaArrayList.add(voVoca);
                    }
                }else{                                  //미암기
                    if ("N".equals(voVoca.getMEMO_YN())) {
                        vocaArrayList.add(voVoca);
                    }
                }
            }
        }
        tv_currentNum.setText((mViewPager.getCurrentItem() + 1) + "/" + vocaArrayList.size());
        vocaPagerAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(0);
    }

    /**
     * add Words 단어추가
     */
    private void addNewWord() {

        if(StringUtil.isNull(et_eng)) {
            ToastUtil.show(getContext(), R.string.str_hint_eng);
            return;
        }

        if(StringUtil.isNull(et_kor)) {
            ToastUtil.show(getContext(), R.string.str_hint_kor);
            return;
        }

        VoVoca voVoca = new VoVoca();
        voVoca.setWORD(StringUtil.getString(et_eng));
        voVoca.setMEAN(StringUtil.getString(et_kor));
        voVoca.setTYPE("I");
        vocaArrayList.add(voVoca);
        et_eng.setText("");
        et_kor.setText("");
        tv_currentNum.setText((mViewPager.getCurrentItem() + 1) + "/" + vocaArrayList.size());
        vocaPagerAdapter.notifyDataSetChanged();

        ToastUtil.show(getContext(), R.string.str_add_word);
        //VocaDAO.getInstance(getContext()).insert(voVoca);
    }

    public void refreshFragment() {
        LogUtil.i("refreshFragment");
        vocaPagerAdapter.notifyDataSetChanged();
    }

    private void changePanelAndHandler(int state) {
        isEnaHandler = false;
        slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        panelClosedHandler.sendEmptyMessageDelayed(state, 200);
    }

    public void setAdapterData(ArrayList<VoVoca> arrayListData) {
        this.allVocaArrayList = arrayListData;
        for(VoVoca voca : allVocaArrayList) {
            this.vocaArrayList.add(voca);
        }
    }

    /**
     * Voca Adapter
     */
    public class VocaPagerAdapter extends FragmentStatePagerAdapter {

        public VocaPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //LogUtil.i("position : " + position);
            return new VocaFragment().newInstance(vocaArrayList.get(position));
        }

        @Override
        public int getCount() {
            return vocaArrayList.size();
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }
    }

}














