package com.zaaach.transformerslayout.listener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @Author: Zaaach
 * @Date: 2019/11/27
 * @Email: zaaach@aliyun.com
 * @Description:
 */
public interface OnTransformersScrollListener {
    void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState);

    void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy);
}
