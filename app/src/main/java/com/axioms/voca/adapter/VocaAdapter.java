package com.axioms.voca.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.axioms.voca.R;
import com.axioms.voca.listener.ItemMoveCallback;
import com.axioms.voca.util.ToastUtil;
import com.axioms.voca.vo.VoVoca;
import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kiel1 on 2018-11-20.
 */

public class VocaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener, ItemMoveCallback.ItemTouchHelperContract {

    private Context context;
    private ArrayList<VoVoca> dataList;

    public VocaAdapter(ArrayList<VoVoca> dataList) {
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
        return new VocaVIewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        VoVoca voData = dataList.get(position);
        VocaVIewHolder vIewHolder = (VocaVIewHolder) holder;

//        vIewHolder.constraintLayout.setOnTouchListener(this);
//        vIewHolder.constraintLayout.setTag();


        vIewHolder.tv_english.setText(voData.getWORD());
        vIewHolder.tv_mean.setText(voData.getMEAN());

        vIewHolder.btn_delete.setTag(voData);
        vIewHolder.btn_delete.setTag(R.id.IDX, position);

        vIewHolder.ll_list.setOnClickListener(this);
        vIewHolder.ll_list.setTag(voData);
        vIewHolder.ll_list.setTag(R.id.IDX, position);

        vIewHolder.btn_delete.setOnClickListener(this);
        vIewHolder.btn_play.setOnClickListener(this);
        vIewHolder.btn_memorize.setOnClickListener(this);
        vIewHolder.btn_memorize.setTag(voData);
        vIewHolder.btn_memorize.setTag(R.id.IDX, position);

        if("Y".equals(voData.getMEMO_YN())) {
            vIewHolder.btn_memorize.setSelected(true);
        }else{
            vIewHolder.btn_memorize.setSelected(false);
        }
    }

    int selectedRow = -1;
    VoVoca voVocaInfo = null;

    @Override
    public void onClick(View view) {

        VoVoca voData = (VoVoca) view.getTag();
        int pos = (int) view.getTag(R.id.IDX);

        switch (view.getId()) {
            case R.id.ll_list:
                ToastUtil.show(context, "단어로 이동");
                break;

            case R.id.btn_delete:
                dataList.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, dataList.size());
                break;

            case R.id.btn_memorize:
                if("Y".equals(voData.getMEMO_YN())) {
                    voData.setMEMO_YN("N");
                }else{
                    voData.setMEMO_YN("Y");
                }
                notifyItemChanged(pos, voData);
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

    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(dataList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(dataList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onRowSelected(RecyclerView.ViewHolder myViewHolder) {
        selectedRow = myViewHolder.getAdapterPosition();
    }

    @Override
    public void onRowClear(RecyclerView.ViewHolder myViewHolder) {
        ToastUtil.show(context, "onRowClear // fromPos : " + selectedRow + "/ toPos : " + myViewHolder.getAdapterPosition());
        //VoVoca fromVoData = dataList.get(selectedRow);
        //VoVoca toVoData = dataList.get(myViewHolder.getAdapterPosition());
        //fromVoData.setVOCA_ID();
    }

    class VocaVIewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.constraintLayout) LinearLayout constraintLayout;
        @BindView(R.id.swipeLayout) SwipeLayout swipeLayout;
        @BindView(R.id.ll_list) LinearLayout ll_list;
        @BindView(R.id.ll_delete) LinearLayout ll_delete;
        @BindView(R.id.tv_english) TextView tv_english;
        @BindView(R.id.tv_mean) TextView tv_mean;
        @BindView(R.id.btn_memorize) AppCompatButton btn_memorize;
        @BindView(R.id.btn_delete) AppCompatButton btn_delete;
        @BindView(R.id.btn_play) AppCompatImageButton btn_play;

        public VocaVIewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }
}
