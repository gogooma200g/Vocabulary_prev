package com.axioms.voca.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.axioms.voca.R;
import com.axioms.voca.dao.VocaListDAO;
import com.axioms.voca.dialog.CommDialog;
import com.axioms.voca.util.LogUtil;
import com.axioms.voca.vo.VoVocaList;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VocaListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements  View.OnClickListener {

    private Context context;
    private ArrayList<VoVocaList> dataList;

    public VocaListAdapter(ArrayList<VoVocaList> dataList) {
        this.dataList = dataList;
    }

    public void setDataList(ArrayList<VoVocaList> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.listview_voca_manage, parent, false);
        return new VocaListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        VoVocaList voVocaList = dataList.get(position);
        VocaListViewHolder viewHolder = (VocaListViewHolder) holder;

        viewHolder.et_conts.setText(voVocaList.getTITLE());
        viewHolder.btn_write.setOnClickListener(this);
        viewHolder.btn_write.setTag(voVocaList);
        viewHolder.btn_write.setTag(R.id.IDX, position);

        viewHolder.btn_delete.setOnClickListener(this);
        viewHolder.btn_delete.setTag(voVocaList);
        viewHolder.btn_delete.setTag(R.id.IDX, position);

        viewHolder.btn_confirm.setOnClickListener(this);
        viewHolder.btn_confirm.setTag(voVocaList);
        viewHolder.btn_confirm.setTag(R.id.IDX, position);

        if(voVocaList.isModifying()) {
            viewHolder.ll_itme.setSelected(true);
            viewHolder.et_conts.setEnabled(true);
            viewHolder.et_conts.requestFocus();
            viewHolder.btn_write.setVisibility(View.GONE);
            viewHolder.btn_confirm.setVisibility(View.VISIBLE);
        }else{
            viewHolder.ll_itme.setSelected(false);
            viewHolder.et_conts.setEnabled(false);
            viewHolder.btn_write.setVisibility(View.VISIBLE);
            viewHolder.btn_confirm.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    VoVocaList modifyingInfo = null;
    int prevPos = 0;

    @Override
    public void onClick(View view) {

        VoVocaList info = (VoVocaList) view.getTag();
        int pos = (int) view.getTag(R.id.IDX);

        if(R.id.btn_write == view.getId()) {

            if(modifyingInfo != null) {
                modifyingInfo.setModifying(false);
                notifyItemChanged(prevPos, modifyingInfo);
            }
            info.setModifying(true);
            notifyItemChanged(pos, info);

            modifyingInfo = info;
            prevPos = pos;

            //showKeyboard();

        }else if(R.id.btn_confirm == view.getId()) {

        }
    }


    //    View prevView = null;
//
//    @Override
//    public void onClick(View view) {
//
//        VoVocaList voVocaList = (VoVocaList) view.getTag();
//
//        if(R.id.btn_write == view.getId()) {
//
//            View parent = (View) view.getParent();
//            parent.setSelected(true);
//            EditText now_editText = parent.findViewById(R.id.et_conts);
//            now_editText.setEnabled(true);
//            now_editText.requestFocus();
//            //showKeyboard();
//            parent.findViewById(R.id.btn_write).setVisibility(View.GONE);
//            parent.findViewById(R.id.btn_confirm).setVisibility(View.VISIBLE);
//
//            if(prevView != null){
//                prevView.setSelected(false);
//                EditText prev_editText = prevView.findViewById(R.id.et_conts);
//                prev_editText.setEnabled(false);
//                prevView.findViewById(R.id.btn_write).setVisibility(View.VISIBLE);
//                prevView.findViewById(R.id.btn_confirm).setVisibility(View.GONE);
//            }
//
//            prevView = parent;
//
//        }else if(R.id.btn_confirm == view.getId()) {
//            if(prevView != null){
//                prevView.setSelected(false);
//                EditText editText = prevView.findViewById(R.id.et_conts);
//                editText.setEnabled(false);
//                prevView.findViewById(R.id.btn_write).setVisibility(View.VISIBLE);
//                prevView.findViewById(R.id.btn_confirm).setVisibility(View.GONE);
//
//                String title = editText.getText().toString();
//                LogUtil.i("title : " + title);
//
//                prevView = null;
//                closeKeyboard();
//            }
//        }else if(R.id.btn_delete == view.getId()) {
//            showDialogDelete(voVocaList);
//            closeKeyboard();
//        }
//    }

    boolean isShowKeyboard = false;
    private void showKeyboard(){
        if(isShowKeyboard) return;
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        isShowKeyboard = true;
        LogUtil.i("showKeyboard : " + isShowKeyboard);
    }

    private void closeKeyboard(){
        LogUtil.i("closeKeyboard : " + isShowKeyboard);
        if(!isShowKeyboard) return;
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        isShowKeyboard = false;
    }

    private void showDialogDelete(final VoVocaList info) {
        new CommDialog.Builder(context)
                .setConts(R.string.dialog_voca_list_delete)
                .setContsGravity(Gravity.CENTER)
                .setBtns(R.string.dialog_no, R.string.dialog_yes)
                .setListener(new CommDialog.ListenerDialog() {
                    @Override
                    public void onClick(Dialog dialog, int result, Object object) {

                        if(CommDialog.ListenerDialog.DIALOG_BTN_SECOND == result) {
                            VocaListDAO.getInstance(context).delete(info);
                            dataList.remove(info);
                            notifyDataSetChanged();
                        }
                        dialog.dismiss();
                    }
                }).build().show();
    }


    public class VocaListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ll_itme) LinearLayout ll_itme;
        @BindView(R.id.et_conts) EditText et_conts;
        @BindView(R.id.btn_write) ImageButton btn_write;
        @BindView(R.id.btn_confirm) ImageButton btn_confirm;
        @BindView(R.id.btn_delete) ImageButton btn_delete;

        public VocaListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}














