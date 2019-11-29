package com.zaaach.transformerslayoutdemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @Author: Zaaach
 * @Date: 2019/11/25
 * @Email: zaaach@aliyun.com
 * @Description:
 */
public class ListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public ListAdapter(@Nullable List<String> data) {
        super(android.R.layout.simple_list_item_1, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
        helper.setText(android.R.id.text1, "item " + helper.getAdapterPosition());
    }
}
