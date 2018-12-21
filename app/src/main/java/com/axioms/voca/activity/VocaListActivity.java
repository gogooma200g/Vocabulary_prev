package com.axioms.voca.activity;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.axioms.voca.R;
import com.axioms.voca.adapter.VocaAdapter;
import com.axioms.voca.listener.ItemMoveCallback;
import com.axioms.voca.util.LogUtil;
import com.axioms.voca.vo.VoVocaListArray;

import butterknife.BindView;


/**
 * Created by kiel1 on 2018-10-31.
 */

public class VocaListActivity extends ToolbarBaseActivity implements View.OnClickListener{

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private static final String PARAM_WORDLIST_NUM = "wordList_num";
    private VocaAdapter adapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_voca_list;
    }

    @Override
    protected void init() {

        setDataInfo();
    }

    @Override
    public void onClick(View view) {

    }

//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_voca_list);
//        ButterKnife.bind(this);
//
//        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        mToolbar.setNavigationIcon(R.drawable.ic_main_search);
//        mToolbar.inflateMenu(R.menu.menu_comm);
//
//        setDataInfo();
//    }

    private void setDataInfo() {
        int wordListNum = getIntent().getIntExtra(PARAM_WORDLIST_NUM, 0);
        LogUtil.i("wordListNum : " + wordListNum);

        if(adapter == null) {
            adapter = new VocaAdapter(VoVocaListArray.getInstance().getVOCALIST_LIST().get(wordListNum).getVOCA_LIST());
            ItemTouchHelper.Callback callback = new ItemMoveCallback(adapter);
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(recyclerView);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.no_change, R.anim.slide_down);
    }

}












