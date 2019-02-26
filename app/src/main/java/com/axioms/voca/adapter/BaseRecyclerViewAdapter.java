package com.axioms.voca.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerViewAdapter<T, H extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    protected List<T> arrayList;

    public BaseRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public BaseRecyclerViewAdapter(Context context, List<T> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public int getItemCount() {
        return  null == arrayList ? 0 : arrayList.size();
    }

    public T getItem(int position) {
        return null== arrayList ? null : arrayList.get(position);
    }

    public void setItems(List<T> items) {
        if(null == arrayList) arrayList = new ArrayList<>();
        arrayList = items;
        notifyDataSetChanged();
    }

    public void clearItems() {
        if(null != arrayList) arrayList.clear();
    }

    public void addItem(T item) {
        if(null != arrayList) arrayList.add(item);
    }

}
