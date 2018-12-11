package com.axioms.voca.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.axioms.voca.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class ToolbarBaseActivity extends BaseActivity {

    /*----------------------------- abstract method S------------------------------------- */
    /** Contents 영역의 Layout Resource Id  */
    protected abstract int getContentViewId();

    /** 기본 기능 대해서 초기설정  */
    protected abstract void init();

    /*----------------------------- abstract method E------------------------------------- */

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.toolbar_title) TextView toolbar_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        ButterKnife.bind(this);
        initUI();
        init();
    }

    private void initUI() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_title.setText(setTitle());
    }

    /**
     * toolbar title
     */
    public int setTitle() {
        return R.string.app_name;
    }

    /**
     * Menu 설정
     */
    public void onSettingMenu(Menu menu) {
        menu.findItem(R.id.action_close).setVisible(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_comm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_close) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
