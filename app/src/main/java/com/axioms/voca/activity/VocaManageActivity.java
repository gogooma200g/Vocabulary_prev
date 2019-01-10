package com.axioms.voca.activity;

import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.axioms.voca.R;
import com.axioms.voca.adapter.VocaListAdapter;
import com.axioms.voca.vo.VoVocaList;
import com.axioms.voca.vo.VoVocaListArray;

import butterknife.BindView;


/**
 * Created by kiel1 on 2018-10-31.
 */

public class VocaManageActivity extends ToolbarBaseActivity implements View.OnClickListener {

    public interface KeyboardListener {
        void onShowKeyboard(boolean state);
    }

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.ll_bottom) LinearLayout ll_bottom;
    @BindView(R.id.btn_advertise) LinearLayout btn_advertise;
    @BindView(R.id.btn_addwords) LinearLayout btn_addwords;

    private VocaListAdapter adapter;
    private Animation animationUp;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_voca_manage;
    }

    @Override
    protected void init() {
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setDataList();

        btn_addwords.setOnClickListener(this);

        animationUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        animationUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ll_bottom.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public int setTitle() {
        return R.string.title_word_manage;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_addwords) {
            adapter.addNewWord();
            recyclerView.smoothScrollToPosition(0);
        }
    }

    private void setDataList() {
        if(adapter == null) {
            adapter = new VocaListAdapter(VoVocaListArray.getInstance().getVOCALIST_LIST());
            adapter.setOnKeyboardState(keyboardListener);
            recyclerView.setAdapter(adapter);
        }
    }

    KeyboardListener keyboardListener = new KeyboardListener() {
        @Override
        public void onShowKeyboard(boolean state) {
            if(state) {
                ll_bottom.setVisibility(View.GONE);
            }else{
                ll_bottom.startAnimation(animationUp);
            }
        }
    };


    private void clearSelectedAdapter() {
        adapter.removeNewWord();
        for(VoVocaList voVocaList : VoVocaListArray.getInstance().getVOCALIST_LIST()) {
            voVocaList.setModifying(false);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        clearSelectedAdapter();
        overridePendingTransition(R.anim.no_change, R.anim.slide_down);
    }
}
