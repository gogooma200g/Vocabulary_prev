package com.axioms.voca.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.axioms.voca.R;
import com.axioms.voca.activity.MainActivity;
import com.axioms.voca.util.LogUtil;
import com.axioms.voca.vo.VoVoca;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kiel1 on 2018-10-19.
 */

public class VocaFragment extends Fragment implements View.OnClickListener {

    private final String INFO_VOCA = "voca_info";

    @BindView(R.id.tv_word) TextView tv_word;
    @BindView(R.id.tv_mean) TextView tv_mean;
    @BindView(R.id.btn_sound) ImageButton btn_sound;
    @BindView(R.id.btn_more) ImageButton btn_more;
    @BindView(R.id.btn_visible1) ImageButton btn_visible1;
    @BindView(R.id.btn_visible2) ImageButton btn_visible2;
    @BindView(R.id.btn_memorize) Button btn_memorize;

    //private VoVoca mVovoca = null;
    private MainVocaFragment mainVocaFragment = null;

    public VocaFragment() {

    }

    public VocaFragment newInstance(VoVoca info) {
        VocaFragment vocaFragment = new VocaFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(INFO_VOCA, info);
        vocaFragment.setArguments(bundle);
        return vocaFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mVovoca = getArguments() != null ? (VoVoca) getArguments().getSerializable(INFO_VOCA) : null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainVocaFragment = (MainVocaFragment) getParentFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_voca, container, false);

        ButterKnife.bind(this, rootView);

        VoVoca voVoca = (VoVoca) getArguments().getSerializable(INFO_VOCA);


        tv_word.setText(voVoca.getWORD());
        tv_mean.setText(voVoca.getMEAN());

        btn_memorize.setSelected("N".equals(voVoca.getMEMO_YN()));

        if(MainVocaFragment.isVisibleWords) {
            btn_visible1.setSelected(false);
            tv_word.setVisibility(View.VISIBLE);
        }else{
            btn_visible1.setSelected(true);
            tv_word.setVisibility(View.INVISIBLE);
        }

        if(MainVocaFragment.isVisibleMeans) {
            btn_visible2.setSelected(false);
            tv_mean.setVisibility(View.VISIBLE);
        }else{
            btn_visible2.setSelected(true);
            tv_mean.setVisibility(View.INVISIBLE);
        }

        btn_sound.setOnClickListener(this);
        btn_more.setOnClickListener(this);
        btn_visible1.setOnClickListener(this);
        btn_visible2.setOnClickListener(this);
        btn_memorize.setOnClickListener(this);
        btn_memorize.setTag(voVoca);
        return rootView;
    }

    @Override
    public void onClick(View view) {

        VoVoca voVoca;

        switch (view.getId()) {
            case R.id.btn_sound :
                break;

            case R.id.btn_more :
                break;

            case R.id.btn_visible1 :
                if(MainVocaFragment.isVisibleWords) {
                    MainVocaFragment.isVisibleWords = false;
                }else{
                    MainVocaFragment.isVisibleWords = true;
                }
                mainVocaFragment.refreshFragment();
                break;

            case R.id.btn_visible2 :
                if(MainVocaFragment.isVisibleMeans) {
                    MainVocaFragment.isVisibleMeans = false;
                }else{
                    MainVocaFragment.isVisibleMeans = true;
                }
                mainVocaFragment.refreshFragment();
                break;

            case R.id.btn_memorize :
                voVoca = (VoVoca) view.getTag();
                Button btn = (Button) view;

                if(btn.isSelected()) {
                    voVoca.setMEMO_YN("N");
                    btn.setSelected(false);
                }else{
                    voVoca.setMEMO_YN("Y");
                    btn.setSelected(true);
                }
                mainVocaFragment.refreshFragment();
                break;
        }
    }

    private void refresh() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }
}








