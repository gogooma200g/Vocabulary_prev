package com.axioms.voca.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.axioms.voca.R;
import com.axioms.voca.adapter.VocaListAdapter;
import com.axioms.voca.util.LogUtil;
import com.axioms.voca.vo.VoVoca;
import com.axioms.voca.vo.VoVocaList;
import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by kiel1 on 2018-10-31.
 */

public class VocaListActivity extends BaseActivity {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;


    private VocaListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voca_list);
        ButterKnife.bind(this);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_main_search);
        mToolbar.inflateMenu(R.menu.menu_comm);

        setDataInfo();
    }

    private void setDataInfo() {

        ArrayList<VoVoca> dataList = new ArrayList<>();

        for(int i = 0; i < 10; i++) {
            VoVoca voVoca = new VoVoca();
            voVoca.setCONTS("Let's push through this week harder as we always have been doing.");
            voVoca.setMEAN("그것은 당연하다 " + i);
            dataList.add(voVoca);
        }

        if(adapter == null) {
            adapter = new VocaListAdapter(dataList);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.no_change, R.anim.slide_down);
    }
}
