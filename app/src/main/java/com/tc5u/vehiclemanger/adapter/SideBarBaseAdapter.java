package com.tc5u.vehiclemanger.adapter;

import android.support.v7.widget.RecyclerView;

import com.tc5u.vehiclemanger.customerstyle.SideBarItemEntity;

import java.util.HashSet;
import java.util.List;

public abstract class SideBarBaseAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected List<SideBarItemEntity<T>> mDataList;

    public SideBarBaseAdapter(List<SideBarItemEntity<T>> dataList) {
        mDataList = dataList;
    }

    public List<SideBarItemEntity<T>> getDataList() {
        return mDataList;
    }

    public void setDataList(List<SideBarItemEntity<T>> dataList) {
        mDataList = dataList;
        notifyDataSetChanged();
    }

    public String getSortLetters(int position) {
        if (mDataList == null || mDataList.isEmpty()) {
            return null;
        }
        return mDataList.get(position).getSortLetters();
    }

    public int getSortLettersFirstPosition(String letters) {
        if (mDataList == null || mDataList.isEmpty()) {
            return -1;
        }
        int position = -1;
        for (int index = 0; index < mDataList.size(); index++) {
            if (mDataList.get(index).getSortLetters().equals(letters)) {
                position = index;
                break;
            }
        }
        return position;
    }

    public int getNextSortLetterPosition(int position) {
        if (mDataList == null || mDataList.isEmpty() || mDataList.size() <= position + 1) {
            return -1;
        }
        int resultPosition = -1;
        for (int index = position + 1; index < mDataList.size(); index++) {
            if (!mDataList.get(position).getSortLetters().equals(mDataList.get(index).getSortLetters())) {
                resultPosition = index;
                break;
            }
        }
        return resultPosition;
    }

    public int getLastSortLetterPosition(int position) {
        if (mDataList == null || mDataList.isEmpty() || mDataList.size() <= position + 1) {
            return -1;
        }
        return getSortLettersFirstPosition(mDataList.get(position - 1).getSortLetters());
    }

    public int getSortLetterSize() {
        HashSet<String> strings = new HashSet<>();
        for (int index = 0; index < mDataList.size(); index++) {
            if (strings.contains(mDataList.get(index).getSortLetters())) continue;
            strings.add(mDataList.get(index).getSortLetters());
        }
        return strings.size();
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }
}
