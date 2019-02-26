package com.axioms.voca.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.axioms.voca.R;
import com.axioms.voca.adapter.VocaListAdapter;
import com.axioms.voca.databinding.ActivityMainBinding;
import com.axioms.voca.databinding.ActivityVocaManageBinding;
import com.axioms.voca.vo.VoVocaList;
import com.axioms.voca.vo.VoVocaListArray;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by kiel1 on 2018-10-31.
 */

public class VocaListManageActivity extends ToolbarBaseActivity<ActivityVocaManageBinding> {

    public interface KeyboardListener {
        void onShowKeyboard(boolean state);
    }

//    @BindView(R.id.recycler_view) RecyclerView recyclerView;
//    @BindView(R.id.ll_bottom) LinearLayout ll_bottom;
//    @BindView(R.id.btn_advertise) LinearLayout btn_advertise;
//    @BindView(R.id.btn_addwords) LinearLayout btn_addwords;

    private VocaListAdapter adapter;
    private Animation animationUp;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_voca_manage;
    }

    @Override
    protected void UIInit() {
        binding.setActivity(this);
        setToolbar();
    }

    @Override
    protected void init() {
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setDataList();

        //btn_addwords.setOnClickListener(this);

        animationUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        animationUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.llBottom.setVisibility(View.VISIBLE);
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


    /**
     * 단어 추가
     */
    public void addNewWords(View view) {
        adapter.addNewWord();
        binding.recyclerView.smoothScrollToPosition(0);
    }


//    @Override
//    public void onClick(View view) {
//        if(view.getId() == R.id.btn_addwords) {
//            adapter.addNewWord();
//            recyclerView.smoothScrollToPosition(0);
//        }
//    }

    private void setDataList() {
        if(adapter == null) {
            adapter = new VocaListAdapter(this, VoVocaListArray.getInstance().getVOCALIST_LIST());
            adapter.setOnKeyboardState(keyboardListener);
            binding.recyclerView.setAdapter(adapter);
        }
    }

    /**
     * 키보드 Listener
     */
    KeyboardListener keyboardListener = new KeyboardListener() {
        @Override
        public void onShowKeyboard(boolean state) {
            if(state) {
                binding.llBottom.setVisibility(View.GONE);
            }else{
                binding.llBottom.startAnimation(animationUp);
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
