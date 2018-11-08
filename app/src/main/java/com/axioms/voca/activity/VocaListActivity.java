package com.axioms.voca.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.axioms.voca.R;
import com.daimajia.swipe.SwipeLayout;


/**
 * Created by kiel1 on 2018-10-31.
 */

public class VocaListActivity extends BaseActivity {

    private SwipeLayout swipeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voca_list);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_main_search);
        mToolbar.inflateMenu(R.menu.menu_comm);

        findViewById(R.id.ll_add_voca).setSelected(true);

        swipeLayout = (SwipeLayout) findViewById(R.id.swipeLayout);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, swipeLayout.findViewById(R.id.ll_list));
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.no_change, R.anim.slide_down);
    }
}
