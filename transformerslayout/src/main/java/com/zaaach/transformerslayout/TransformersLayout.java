package com.zaaach.transformerslayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zaaach.transformerslayout.adapter.TransformersAdapter;
import com.zaaach.transformerslayout.holder.TransformersHolderCreator;
import com.zaaach.transformerslayout.listener.OnTransformersItemClickListener;
import com.zaaach.transformerslayout.listener.OnTransformersScrollListener;
import com.zaaach.transformerslayout.view.RecyclerViewScrollBar;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Zaaach
 * @Date: 2019/11/22
 * @Email: zaaach@aliyun.com
 * @Description:
 */
public class TransformersLayout<T> extends LinearLayout {
    private static final String TAG = TransformersLayout.class.getSimpleName();

    /** 默认每页5列 */
    private static final int DEFAULT_SPAN_COUNT = 5;
    /** 默认每页2行 */
    private static final int DEFAULT_LINES      = 2;
    /** 滚动条默认宽度 */
    private static final int DEFAULT_SCROLL_BAR_WIDTH  = 48;//dp
    /** 滚动条默认高度 */
    private static final int DEFAULT_SCROLL_BAR_HEIGHT = 3;//dp
    private static final int DEFAULT_TRACK_COLOR = Color.parseColor("#f0f0f0");
    private static final int DEFAULT_THUMB_COLOR = Color.parseColor("#ffc107");

    private int spanCount;
    private int lines;
    private float scrollBarRadius;
    private int scrollBarTrackColor;
    private int scrollBarThumbColor;
    private int scrollBarTopMargin;
    private int scrollBarBottomMargin;
    private int scrollBarWidth;
    private int scrollBarHeight;
    private boolean pagingMode;
    private OnTransformersItemClickListener onTransformersItemClickListener;

    private RecyclerView recyclerView;
    private RecyclerViewScrollBar scrollBar;
    private OnTransformersScrollListener onScrollListener;

    private List<T> mDataList;
    private TransformersAdapter<T> transformersAdapter;
    private GridLayoutManager layoutManager;
    private Parcelable savedState;//保存的滚动状态
    private TransformersOptions transformersOptions;

    public TransformersLayout(Context context) {
        this(context, null);
    }

    public TransformersLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TransformersLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttrs(context, attrs);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TransformersLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        parseAttrs(context, attrs);
        init(context);
    }

    private void parseAttrs(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TransformersLayout);
        spanCount = array.getInteger(R.styleable.TransformersLayout_tl_spanCount, DEFAULT_SPAN_COUNT);
        lines = array.getInteger(R.styleable.TransformersLayout_tl_lines, DEFAULT_LINES);
        pagingMode = array.getBoolean(R.styleable.TransformersLayout_tl_pagingMode, false);
        scrollBarRadius = array.getDimensionPixelSize(R.styleable.TransformersLayout_tl_scrollbarRadius, -1);
        scrollBarTrackColor = array.getColor(R.styleable.TransformersLayout_tl_scrollbarTrackColor, DEFAULT_TRACK_COLOR);
        scrollBarThumbColor = array.getColor(R.styleable.TransformersLayout_tl_scrollbarThumbColor, DEFAULT_THUMB_COLOR);
        scrollBarTopMargin = array.getDimensionPixelSize(R.styleable.TransformersLayout_tl_scrollbarMarginTop, 0);
        scrollBarBottomMargin = array.getDimensionPixelSize(R.styleable.TransformersLayout_tl_scrollBarMarginBottom, 0);
        scrollBarWidth = array.getDimensionPixelSize(R.styleable.TransformersLayout_tl_scrollbarWidth, dp2px(DEFAULT_SCROLL_BAR_WIDTH));
        scrollBarHeight = array.getDimensionPixelSize(R.styleable.TransformersLayout_tl_scrollbarHeight, dp2px(DEFAULT_SCROLL_BAR_HEIGHT));
        array.recycle();

        if (scrollBarRadius < 0){
            scrollBarRadius = dp2px(DEFAULT_SCROLL_BAR_HEIGHT) / 2f;
        }
        if (spanCount <= 0){
            spanCount = DEFAULT_SPAN_COUNT;
        }
        if (lines <= 0){
            lines = DEFAULT_LINES;
        }
    }

    private void init(final Context context) {
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
        setLayoutParams(new LinearLayout.LayoutParams(-1, -2));

        recyclerView = new RecyclerView(context);
        recyclerView.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        RecyclerView.ItemAnimator itemAnimator = recyclerView.getItemAnimator();
        if (itemAnimator != null){
            itemAnimator.setChangeDuration(0);
        }

        layoutManager = new GridLayoutManager(getContext(), lines, GridLayoutManager.HORIZONTAL, false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(layoutManager);
        transformersAdapter = new TransformersAdapter<>(context, recyclerView);
        recyclerView.setAdapter(transformersAdapter);

        scrollBar = new RecyclerViewScrollBar(context);
        setupScrollBar();

        addView(recyclerView);
        addView(scrollBar);
    }

    private void setupScrollBar() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(scrollBarWidth, scrollBarHeight);
        params.topMargin = scrollBarTopMargin;
        params.bottomMargin = scrollBarBottomMargin;
        scrollBar.setLayoutParams(params);
        scrollBar.setTrackColor(scrollBarTrackColor)
                .setThumbColor(scrollBarThumbColor)
                .setRadius(scrollBarRadius)
                .applyChange();
    }

    public TransformersLayout<T> addOnTransformersItemClickListener(OnTransformersItemClickListener listener){
        this.onTransformersItemClickListener = listener;
        return this;
    }

    public void load(@NonNull List<T> data, TransformersHolderCreator<T> creator){
        fixData(data);
        transformersAdapter.setOnTransformersItemClickListener(onTransformersItemClickListener);
        transformersAdapter.setHolderCreator(creator);
        transformersAdapter.setSpanCount(spanCount);
        //如果数据少于一页的列数
        resetOneLine();
        transformersAdapter.setData(mDataList);
        toggleScrollBar(data);
        if (scrollBar.getVisibility() == VISIBLE) {
            scrollBar.attachRecyclerView(recyclerView);
        }
    }

    /**
     * 重新排列数据，使数据转换成分页模式
     * 原始数据：
     * 1 3 5 7 9   11 13 15
     * 2 4 6 8 10  12 14 16
     * ==============================
     * 转换之后：（数据会增加null值）
     * 1 2 3 4 5   11 12 13 14 15
     * 6 7 8 9 10  16 null...
     */
    private List<T> rearrange(List<T> data) {
        if (lines <= 1) return data;
        if (data == null || data.isEmpty()) return data;
        int pageSize = lines * spanCount;
        int size = data.size();
        //如果数据少于一行
        if (size <= spanCount){
            return new ArrayList<>(data);
        }
        List<T> destList = new ArrayList<>();
        //转换后的总数量，包括空数据
        int sizeAfterTransform;
        if (size < pageSize) {
//            sizeAfterTransform = pageSize;
            sizeAfterTransform = size < spanCount ? size * lines : pageSize;
        } else if (size % pageSize == 0) {
            sizeAfterTransform = size;
        } else {
//            sizeAfterTransform = (size / pageSize + 1) * pageSize;
            sizeAfterTransform = size % pageSize < spanCount
                    ? (size / pageSize) * pageSize + size % pageSize * lines
                    : (size / pageSize + 1) * pageSize;
        }
        //类似置换矩阵
        for (int i = 0; i < sizeAfterTransform; i++) {
            int pageIndex = i / pageSize;
            int columnIndex = (i - pageSize * pageIndex) / lines;
            int rowIndex = (i - pageSize * pageIndex) % lines;
            int destIndex = (rowIndex * spanCount + columnIndex) + pageIndex * pageSize;

            if (destIndex >= 0 && destIndex < size) {
                destList.add(data.get(destIndex));
            } else {
                destList.add(null);
            }
        }

        //自己瞎捣鼓的方法...和上面的一比惨不忍睹
        /*List<List<T>> splitList = new ArrayList<>();
        int size = data.size();
        int toIndex = spanCount;
        for (int i = 0; i < size; i += spanCount) {
            if (i + spanCount > size){
                toIndex = size - i;
            }
            List<T> split = data.subList(i, i + toIndex);
            splitList.add(split);
        }

        List<List<T>> rowList = new ArrayList<>();
        for (int i = 0; i < lines; i++) {
            List<T> row = new ArrayList<>();
            for (int j = i; j < splitList.size(); j += lines) {
                row.addAll(splitList.get(j));
            }
            rowList.add(row);
        }

        List<T> destList = new ArrayList<>();
        for (int i = 0; i < rowList.get(0).size(); i++) {
            destList.add(rowList.get(0).get(i));
            for (int j = 0; j < rowList.size() - 1; j++) {
                if (i > rowList.get(j+1).size() - 1){
                    destList.add(null);
                }else {
                    destList.add(rowList.get(j+1).get(i));
                }
            }
        }*/

        return destList;
    }

    /**
     * 默认排序时如果数据大于一页，使用空数据填满最后一列，用于修复滚动条滑动时变长变短的问题
     * @param data
     */
    private void fillData(@NonNull List<T> data) {
        if (lines <= 1) return;
        mDataList = new ArrayList<>(data);
        if (mDataList.size() > lines * spanCount && mDataList.size() % lines > 0){
            int rest = lines - mDataList.size() % lines;
            for (int i = 0; i < rest; i++) {
                mDataList.add(null);
            }
        }
    }

    /**
     * 获取列表数据
     * @return
     */
    public List<T> getDataList(){
        return mDataList;
    }

    public TransformersOptions getOptions(){
        return transformersOptions;
    }

    public TransformersLayout<T> apply(@Nullable TransformersOptions options){
        if (options != null){
            transformersOptions = options;
            spanCount = options.spanCount <= 0 ? spanCount : options.spanCount;
            int newLines = options.lines <= 0 ? lines : options.lines;
            scrollBarWidth = options.scrollBarWidth <= 0 ? scrollBarWidth : options.scrollBarWidth;
            scrollBarHeight = options.scrollBarHeight <= 0 ? scrollBarHeight : options.scrollBarHeight;
            scrollBarRadius = options.scrollBarRadius < 0 ? scrollBarHeight/2f : options.scrollBarRadius;
            scrollBarTopMargin = options.scrollBarTopMargin <= 0 ? scrollBarTopMargin : options.scrollBarTopMargin;
            pagingMode = options.pagingMode;

//            Log.e(TAG, "trackColor = " + options.scrollBarTrackColor);
//            Log.e(TAG, "thumbColor = " + options.scrollBarThumbColor);
//            Log.e(TAG, "radius = " + options.scrollBarRadius);
            scrollBarTrackColor = options.scrollBarTrackColor == 0 ? scrollBarTrackColor : options.scrollBarTrackColor;
            scrollBarThumbColor = options.scrollBarThumbColor == 0 ? scrollBarThumbColor : options.scrollBarThumbColor;

            if (newLines != lines){
                lines = newLines;
                layoutManager.setSpanCount(newLines);
            }
            setupScrollBar();
        }
        return this;
    }

    /**
     * 恢复滚动状态
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (savedState != null) {
            layoutManager.onRestoreInstanceState(savedState);
        }
        savedState = null;
    }

    /**
     * 保存滚动状态
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        savedState = layoutManager.onSaveInstanceState();
    }

    public void notifyDataChanged(List<T> data){
        if (transformersAdapter != null){
            fixData(data);
            resetOneLine();
            transformersAdapter.setData(mDataList);
            scrollToStart();
        }
        toggleScrollBar(data);
        //数据发生改变时重新计算滚动比例
        if (scrollBar.getVisibility() == VISIBLE) {
            scrollBar.computeScrollScale();
        }
    }

    private void resetOneLine() {
        //如果数据少于一页的列数
        if (pagingMode && mDataList.size() <= spanCount){
            lines = 1;
            layoutManager.setSpanCount(1);
        }
    }

    private void fixData(List<T> data) {
        if (pagingMode) {
            mDataList = rearrange(data);
        } else {
            fillData(data);
        }
    }

    public void scrollToStart(){
        scrollToStart(true);
    }

    public void scrollToStart(boolean smooth){
        scrollBar.setScrollBySelf(true);
        if (recyclerView != null) {
            if (recyclerView.computeHorizontalScrollOffset() > 0) {
                if (smooth) {
                    recyclerView.smoothScrollToPosition(0);
                } else {
                    recyclerView.scrollToPosition(0);
                }
            }
        }
    }

    public TransformersLayout<T> addOnTransformersScrollListener(OnTransformersScrollListener listener){
        this.onScrollListener = listener;
        if (scrollBar != null) {
            scrollBar.setOnTransformersScrollListener(onScrollListener);
        }
        return this;
    }

    /**
     * 不足一页时隐藏滚动条
     */
    private void toggleScrollBar(List<T> data) {
        if (spanCount * lines >= data.size()){
            scrollBar.setVisibility(GONE);
        }else {
            scrollBar.setVisibility(VISIBLE);
        }
    }

    private int dp2px(float dp){
        return (int) (getContext().getResources().getDisplayMetrics().density * dp + 0.5f);
    }
}
