package com.tc5u.vehiclemanger.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.tc5u.vehiclemanger.R;
import com.tc5u.vehiclemanger.model.SpinnerModel;
import com.tc5u.vehiclemanger.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * checkBox 适配器
 */
public class CheckBoxAdapter extends RecyclerView.Adapter<CheckBoxAdapter.ViewHolder> {
    private List<SpinnerModel> mList;

    private Context context;

    private Integer maxCount;

    public CheckBoxAdapter(List<SpinnerModel> mList, Context context, Integer maxCount) {
        this.mList = mList;
        this.context = context;
        this.maxCount = maxCount;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.checkbox_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final SpinnerModel bean = mList.get(position);
        holder.cb.setText(bean.getName());
        holder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canSelect()) {
                    boolean checked = ObjectUtil.getBoolean(bean.getValue());
                    bean.setValue(!checked);
                } else {
                    holder.cb.setChecked(false);
                    bean.setValue(false);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public List<String> getSelectName() {
        List<String> strings = new ArrayList<>();
        for (SpinnerModel models : mList) {
            if (ObjectUtil.getBoolean(models.getValue())) strings.add(models.getName());
        }
        return strings;
    }

    private Boolean canSelect() {
        if (ObjectUtil.getInteger(maxCount) < 1) return true;
        int i = 0;
        for (SpinnerModel models : mList) {
            if (ObjectUtil.getBoolean(models.getValue())) i++;
        }
        return maxCount > i;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox cb;

        public ViewHolder(View view) {
            super(view);
            cb = view.findViewById(R.id.scb);
        }
    }
}
