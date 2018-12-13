package com.axioms.voca.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.axioms.voca.R;
import com.axioms.voca.activity.VocaManageActivity;
import com.axioms.voca.customview.CustomEditeText;
import com.axioms.voca.dao.VocaListDAO;
import com.axioms.voca.dialog.CommDialog;
import com.axioms.voca.util.StringUtil;
import com.axioms.voca.vo.VoVocaList;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VocaListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements  View.OnClickListener {

    private Context context;
    private ArrayList<VoVocaList> dataList;


    private VocaManageActivity.KeyboardListener keyboardListener = new VocaManageActivity.KeyboardListener() {
        @Override
        public void onShowKeyboard(boolean state) {

        }
    };

    public VocaListAdapter(ArrayList<VoVocaList> dataList) {
        this.dataList = dataList;
    }

    public void setDataList(ArrayList<VoVocaList> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public void setOnKeyboardState(VocaManageActivity.KeyboardListener listener) {
        this.keyboardListener = listener;
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
        viewHolder.et_conts.setOnBackPressListener(onBackPressListener);
        viewHolder.btn_write.setOnClickListener(this);
        viewHolder.btn_write.setTag(voVocaList);
        viewHolder.btn_write.setTag(R.id.IDX, position);

        viewHolder.btn_delete.setOnClickListener(this);
        viewHolder.btn_delete.setTag(voVocaList);
        viewHolder.btn_delete.setTag(R.id.IDX, position);

        viewHolder.btn_confirm.setOnClickListener(this);
        viewHolder.btn_confirm.setTag(voVocaList);
        viewHolder.btn_confirm.setTag(R.id.IDX, position);
        viewHolder.btn_confirm.setTag(R.id.VIEW, viewHolder.et_conts);

        viewHolder.btn_cancel.setOnClickListener(this);
        viewHolder.btn_cancel.setTag(voVocaList);
        viewHolder.btn_cancel.setTag(R.id.IDX, position);

        if(voVocaList.isModifying()) {
            viewHolder.ll_itme.setSelected(true);
            viewHolder.et_conts.setFocusable(true);
            viewHolder.et_conts.setFocusableInTouchMode(true);
            viewHolder.et_conts.requestFocus();
            viewHolder.et_conts.setSelection(viewHolder.et_conts.length());
            viewHolder.btn_write.setVisibility(View.GONE);
            viewHolder.btn_delete.setVisibility(View.GONE);
            viewHolder.btn_confirm.setVisibility(View.VISIBLE);
            viewHolder.btn_cancel.setVisibility(View.VISIBLE);
        }else{
            viewHolder.ll_itme.setSelected(false);
            viewHolder.et_conts.setFocusable(false);
            viewHolder.et_conts.setFocusableInTouchMode(false);
            viewHolder.btn_write.setVisibility(View.VISIBLE);
            viewHolder.btn_delete.setVisibility(View.VISIBLE);
            viewHolder.btn_confirm.setVisibility(View.GONE);
            viewHolder.btn_cancel.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    VoVocaList modifyingInfo = null;
    int prevPos = 0;
    boolean isShowKeyboard = false;

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

            showKeyboardState();

        }else if(R.id.btn_confirm == view.getId()) {

            AppCompatEditText editText = (AppCompatEditText) view.getTag(R.id.VIEW);
            String editStr = editText.getText().toString();

            if(StringUtil.isNull(editStr)) {
                editStr = context.getResources().getString(R.string.app_name) + " " + (dataList.size() + 1);
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
            closeKeyboard();
            showDialogDelete(pos);
        }else if(R.id.btn_cancel == view.getId()) {

            if(isAddNew) {
                isAddNew = false;
                dataList.remove(0);
                notifyItemRemoved(0);
                notifyItemRangeChanged(0, dataList.size());
                closeKeyboard();
                return;
            }

            info.setModifying(false);
            notifyItemChanged(pos);
            closeKeyboard();
        }
    }

    boolean isAddNew = false;
    public void addNewWord() {

        if(isAddNew) return;

        this.isAddNew = true;
        VoVocaList info = new VoVocaList();
        info.setModifying(true);

        modifyingInfo = info;
        prevPos = 0;

        dataList.add(0, info);
        notifyItemInserted(0);
        showKeyboardState();

    }

    private CustomEditeText.OnBackPressListener onBackPressListener = new CustomEditeText.OnBackPressListener() {
        @Override
        public void onBackPress() {
            closeKeyboard();
        }
    };

    private void showKeyboardState() {
        if(!isShowKeyboard) {
            isShowKeyboard = true;
            keyboardListener.onShowKeyboard(true);
        }
    }

//    boolean isShowKeyboard = false;
//    private void showKeyboard(){
//        if(isShowKeyboard) return;
//        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//        isShowKeyboard = true;
//        LogUtil.i("showKeyboard : " + isShowKeyboard);
//    }
//
    private void closeKeyboard(){
        if(!isShowKeyboard) return;
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        isShowKeyboard = false;
        keyboardListener.onShowKeyboard(false);
    }

    private void showDialogDelete(final int position) {
        new CommDialog.Builder(context)
                .setConts(R.string.dialog_voca_list_delete)
                .setContsGravity(Gravity.CENTER)
                .setBtns(R.string.dialog_no, R.string.dialog_yes)
                .setListener(new CommDialog.ListenerDialog() {
                    @Override
                    public void onClick(Dialog dialog, int result, Object object) {

                        if(CommDialog.ListenerDialog.DIALOG_BTN_SECOND == result) {
                            //VocaListDAO.getInstance(context).delete(dataList.get(position));
                            dataList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, dataList.size());
                        }
                        dialog.dismiss();
                    }
                }).build().show();
    }


    public class VocaListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ll_itme) LinearLayout ll_itme;
        //@BindView(R.id.et_conts) EditText et_conts;
        @BindView(R.id.btn_write) ImageButton btn_write;
        @BindView(R.id.btn_confirm) ImageButton btn_confirm;
        @BindView(R.id.btn_delete) ImageButton btn_delete;
        @BindView(R.id.btn_cancel) ImageButton btn_cancel;
        CustomEditeText et_conts;

        public VocaListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            et_conts = view.findViewById(R.id.et_conts);
        }
    }
}














