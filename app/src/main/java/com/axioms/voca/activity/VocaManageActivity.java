package com.axioms.voca.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.axioms.voca.R;
import com.axioms.voca.adapter.VocaAdapter;
import com.axioms.voca.adapter.VocaListAdapter;
import com.axioms.voca.util.LogUtil;
import com.axioms.voca.vo.VoVocaList;
import com.axioms.voca.vo.VoVocaListArray;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by kiel1 on 2018-10-31.
 */

public class VocaManageActivity extends ToolbarBaseActivity {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.btn_advertise) LinearLayout btn_advertise;
    @BindView(R.id.btn_confirm) LinearLayout btn_confirm;

    private VocaListAdapter adapter;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_voca_manage;
    }

    @Override
    protected void init() {
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setDataList();
    }

    @Override
    public int setTitle() {
        return R.string.title_word_manage;
    }

    private void setDataList() {
        if(adapter == null) {
            adapter = new VocaListAdapter(VoVocaListArray.getInstance().getVOCALIST_LIST());
            recyclerView.setAdapter(adapter);
        }
    }

    private void clearSelectedAdapter() {
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
