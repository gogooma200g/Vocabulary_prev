package com.axioms.voca.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.axioms.voca.R;


/**
 * Created by kiel1 on 2018-10-31.
 */

public class VocaManageActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voca_manage);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_main_search);
        mToolbar.inflateMenu(R.menu.menu_comm);

        findViewById(R.id.ll_add_voca).setSelected(true);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.no_change, R.anim.slide_down);
    }
}
