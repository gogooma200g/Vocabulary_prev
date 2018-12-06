package com.axioms.voca.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.axioms.voca.R;
import com.axioms.voca.util.ToastUtil;
import com.axioms.voca.vo.VoVoca;
import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;

/**
 * Created by kiel1 on 2018-11-20.
 */

public class VocaListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context context;
    private ArrayList<VoVoca> dataList;

    public VocaListAdapter(ArrayList<VoVoca> dataList) {
        this.dataList = dataList;
    }

    public void setDataList(ArrayList<VoVoca> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.listview_voca_word, parent, false);
        VocaListVIewHolder holder = new VocaListVIewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        VoVoca voData = dataList.get(position);
        VocaListVIewHolder vIewHolder = (VocaListVIewHolder) holder;

        vIewHolder.tv_mean.setText(voData.getMEAN());

        vIewHolder.btn_delete.setTag(voData);
        vIewHolder.btn_delete.setTag(R.id.IDX, position);

        vIewHolder.ll_list.setOnClickListener(this);
        vIewHolder.btn_delete.setOnClickListener(this);
        vIewHolder.btn_play.setOnClickListener(this);
        vIewHolder.btn_memorize.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_list:


                break;

            case R.id.btn_delete:
                VoVoca voData = (VoVoca) view.getTag();
                int pos = (int) view.getTag(R.id.IDX);

                ToastUtil.show(context, R.string.toast_delete_voca);
                dataList.remove(pos);
                notifyDataSetChanged();
                break;
            case R.id.btn_memorize:
                ToastUtil.show(context, R.string.toast_memorize);
                break;

            case R.id.btn_play:
                ToastUtil.show(context, R.string.toast_play);
                break;

            default:
                break;

        }
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public class VocaListVIewHolder extends RecyclerView.ViewHolder {

        private SwipeLayout swipeLayout;
        private LinearLayout ll_list, ll_delete;
        private TextView tv_english, tv_mean;
        private Button btn_memorize, btn_delete;
        private ImageButton btn_play;

        public VocaListVIewHolder(View view) {
            super(view);
            swipeLayout = view.findViewById(R.id.swipeLayout);
            ll_list = view.findViewById(R.id.ll_list);
            ll_delete = view.findViewById(R.id.ll_delete);
            tv_english = view.findViewById(R.id.tv_english);
            tv_mean = view.findViewById(R.id.tv_mean);
            btn_play = view.findViewById(R.id.btn_play);
            btn_memorize = view.findViewById(R.id.btn_memorize);
            btn_delete = view.findViewById(R.id.btn_delete);
        }

    }
}
