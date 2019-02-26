package com.axioms.voca.adapter;

import android.app.Dialog;
import android.content.Context;
import android.databinding.BindingBuildInfo;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.axioms.voca.R;
import com.axioms.voca.activity.VocaListManageActivity;
import com.axioms.voca.customview.CustomEditeText;
import com.axioms.voca.databinding.ListviewVocaManageBinding;
import com.axioms.voca.dialog.CommDialog;
import com.axioms.voca.util.LogUtil;
import com.axioms.voca.util.StringUtil;
import com.axioms.voca.vo.VoVocaList;

import java.util.List;

public class VocaListAdapter extends BaseRecyclerViewAdapter<VoVocaList, VocaListAdapter.VocaListViewHolder> implements  View.OnClickListener {

    private VocaListManageActivity.KeyboardListener keyboardListener = new VocaListManageActivity.KeyboardListener() {
        @Override
        public void onShowKeyboard(boolean state) {}
    };

    public void setOnKeyboardState(VocaListManageActivity.KeyboardListener listener) {
        this.keyboardListener = listener;
    }

    public VocaListAdapter(Context context) {
        super(context);
    }

    public VocaListAdapter(Context context, List<VoVocaList> arrayList) {
        super(context, arrayList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_voca_manage, parent, false);
        return new VocaListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        VoVocaList voVocaList = getItem(position);
        VocaListViewHolder viewHolder = (VocaListViewHolder) holder;

        LogUtil.i("voVocaList : " + position);

        viewHolder.binding.setVoVocaList(voVocaList);
        viewHolder.binding.setPos(position);
        viewHolder.binding.setAdapter(this);

        if(voVocaList.isModifying()) {
            viewHolder.binding.etConts.requestFocus();
            viewHolder.binding.etConts.setSelection(voVocaList.getTITLE().length());
        }
        LogUtil.i("position : " + position);
        LogUtil.i("position : " + position);
        LogUtil.i("isModifying : " + voVocaList.isModifying());

        //((VocaListViewHolder) holder).binding.etConts.setTag();
    }

//    public void onBindViewHolder(VocaListViewHolder holder, int position) {


//        VoVocaList voVocaList = arrayList.get(position);
//        VocaListViewHolder viewHolder = (VocaListViewHolder) holder;
//
//        viewHolder.et_conts.setText(voVocaList.getTITLE());
//        viewHolder.et_conts.setOnClickListener(this);
//        viewHolder.et_conts.setTag(voVocaList);
//        viewHolder.et_conts.setTag(R.id.IDX, position);
//        viewHolder.et_conts.setOnBackPressListener(onBackPressListener);
//
//        viewHolder.btn_write.setOnClickListener(this);
//        viewHolder.btn_write.setTag(voVocaList);
//        viewHolder.btn_write.setTag(R.id.IDX, position);
//
//        viewHolder.btn_delete.setOnClickListener(this);
//        viewHolder.btn_delete.setTag(voVocaList);
//        viewHolder.btn_delete.setTag(R.id.IDX, position);
//
//        viewHolder.btn_confirm.setOnClickListener(this);
//        viewHolder.btn_confirm.setTag(voVocaList);
//        viewHolder.btn_confirm.setTag(R.id.IDX, position);
//        viewHolder.btn_confirm.setTag(R.id.VIEW, viewHolder.et_conts);
//
//        viewHolder.btn_cancel.setOnClickListener(this);
//        viewHolder.btn_cancel.setTag(voVocaList);
//        viewHolder.btn_cancel.setTag(R.id.IDX, position);
//
//        if(voVocaList.isModifying()) {
//            viewHolder.ll_item.setSelected(true);
//            viewHolder.et_conts.setFocusable(true);
//            viewHolder.et_conts.setFocusableInTouchMode(true);
//            viewHolder.et_conts.requestFocus();
//            viewHolder.et_conts.setSelection(viewHolder.et_conts.length());
//            viewHolder.btn_write.setVisibility(View.GONE);
//            viewHolder.btn_delete.setVisibility(View.GONE);
//            viewHolder.btn_confirm.setVisibility(View.VISIBLE);
//            viewHolder.btn_cancel.setVisibility(View.VISIBLE);
//        }else{
//            viewHolder.ll_item.setSelected(false);
//            viewHolder.et_conts.setFocusable(false);
//            viewHolder.et_conts.setFocusableInTouchMode(false);
//            viewHolder.btn_write.setVisibility(View.VISIBLE);
//            viewHolder.btn_delete.setVisibility(View.VISIBLE);
//            viewHolder.btn_confirm.setVisibility(View.GONE);
//            viewHolder.btn_cancel.setVisibility(View.GONE);
//        }

//    }

    VoVocaList modifyingInfo = null;
    boolean isShowKeyboard = false;
    int prevPos = 0;                // 이전 선택 저장

    public void onClickList(VoVocaList info, int pos) {
        LogUtil.i("click list");
        LogUtil.i("pos : " + pos);
        LogUtil.i(info.getID());

    }

    /**
     * 쓰기
     */
    public void onClickWrite(View view, VoVocaList info, int pos) {

        LogUtil.i("pos : " + pos);

        removeNewWord();

        if(modifyingInfo != null) {
            modifyingInfo.setModifying(false);
            notifyItemChanged(prevPos, modifyingInfo);
        }
        info.setModifying(true);
        notifyItemChanged(pos, info);

        modifyingInfo = info;
        prevPos = pos;

        showKeyboardState();
    }

    public void onClickDelete(VoVocaList info) {

//        int pos = info.getPos();
//
//        AppCompatEditText editText = (AppCompatEditText) view.getTag(R.id.VIEW);
//        String editStr = editText.getText().toString();
//
//        if(StringUtil.isNull(editStr)) {
//            editStr = getContext().getResources().getString(R.string.app_name) + " " + (arrayList.size() + 1);
//        }
//
//        info.setTITLE(editStr);
//        info.setModifying(false);
//        notifyItemChanged(pos, info);
//        closeKeyboard();
//
//        if(isAddNew) {
//            isAddNew = false;
//            //VocaListDAO.getInstance(context).insert(info);
//        }else{
//            //VocaListDAO.getInstance(context).update(info);
//        }
    }

    @Override
    public void onClick(View view) {

        VoVocaList info = (VoVocaList) view.getTag();
        int pos = (int) view.getTag(R.id.IDX);

        if(R.id.et_conts == view.getId()) {
            LogUtil.i(info.getTITLE());

        }else if(R.id.btn_write == view.getId()) {

            removeNewWord();

            if(modifyingInfo != null) {
                modifyingInfo.setModifying(false);
                notifyItemChanged(prevPos, modifyingInfo);
            }
            info.setModifying(true);
            notifyItemChanged(pos, info);

            modifyingInfo = info;
            prevPos = pos;

            showKeyboardState();

        }else if(R.id.btn_confirm == view.getId()) {

            AppCompatEditText editText = (AppCompatEditText) view.getTag(R.id.VIEW);
            String editStr = editText.getText().toString();

            if(StringUtil.isNull(editStr)) {
                editStr = getContext().getResources().getString(R.string.app_name) + " " + (arrayList.size() + 1);
            }

            info.setTITLE(editStr);
            info.setModifying(false);
            notifyItemChanged(pos, info);
            closeKeyboard();

            if(isAddNew) {
                isAddNew = false;
                //VocaListDAO.getInstance(context).insert(info);
            }else{
                //VocaListDAO.getInstance(context).update(info);
            }


        }else if(R.id.btn_delete == view.getId()) {

            removeNewWord();

            closeKeyboard();
            showDialogDelete(pos);

            //VocaListDAO.getInstance(context).delete(info);
        }else if(R.id.btn_cancel == view.getId()) {

            removeNewWord();

            info.setModifying(false);
            notifyItemChanged(pos);
            closeKeyboard();
        }
    }

    /** add NewWord */
    boolean isAddNew = false;
    public void addNewWord() {

        if(isAddNew) {
            removeNewWord();
            return;
        }

        this.isAddNew = true;
        VoVocaList info = new VoVocaList();
        info.setModifying(true);

        modifyingInfo = info;
        prevPos = 0;

        arrayList.add(0, info);
        notifyItemInserted(0);
        showKeyboardState();
    }

    /** 새로운 단어 삭제 */
    public void removeNewWord() {
        if(!isAddNew) return;

        isAddNew = false;
        arrayList.remove(0);
        notifyItemRemoved(0);
        notifyItemRangeChanged(0, arrayList.size());
    }

    private CustomEditeText.onEditTextListener onBackPressListener = new CustomEditeText.onEditTextListener() {
        @Override
        public void onBackPress() {
            closeKeyboard();
        }

        @Override
        public void onFocusMoved() {

        }
    };

    /**
     * 키보드 보이기
     */
    private void showKeyboardState() {
        if(!isShowKeyboard) {
            isShowKeyboard = true;
            keyboardListener.onShowKeyboard(true);
        }
    }

    /**
     * 키보드 닫기
     */
    private void closeKeyboard(){
        if(!isShowKeyboard) return;
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        isShowKeyboard = false;
        keyboardListener.onShowKeyboard(false);
    }

    /**
     * 단어장 삭제
     * @param position
     */
    private void showDialogDelete(final int position) {
        new CommDialog.Builder(getContext())
                .setConts(R.string.dialog_voca_list_delete)
                .setContsGravity(Gravity.CENTER)
                .setBtns(R.string.dialog_no, R.string.dialog_yes)
                .setListener(new CommDialog.ListenerDialog() {
                    @Override
                    public void onClick(Dialog dialog, int result, Object object) {

                        if(CommDialog.ListenerDialog.DIALOG_BTN_SECOND == result) {
                            //VocaListDAO.getInstance(context).delete(dataList.get(position));
                            arrayList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, arrayList.size());
                        }
                        dialog.dismiss();
                    }
                }).build().show();
    }


    /**
     * VocaList View Holder
     */
    public class VocaListViewHolder extends RecyclerView.ViewHolder {

        ListviewVocaManageBinding binding;

        public VocaListViewHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }
}














