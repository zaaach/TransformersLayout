package com.zaaach.transformerslayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zaaach.transformerslayout.adapter.TransformersAdapter;
import com.zaaach.transformerslayout.holder.TransformersHolderCreator;
import com.zaaach.transformerslayout.listener.OnTransformersItemClickListener;
import com.zaaach.transformerslayout.listener.OnTransformersScrollListener;
import com.zaaach.transformerslayout.view.RecyclerViewScrollBar;

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
    private static final int DEFAULT_SCROLL_BAR_WIDTH  = 36;//dp
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
    private int scrollBarWidth;
    private int scrollBarHeight;
    private OnTransformersItemClickListener onTransformersItemClickListener;

    private RecyclerView recyclerView;
    private RecyclerViewScrollBar scrollBar;
    private OnTransformersScrollListener onScrollListener;

    private TransformersAdapter<T> transformersAdapter;
    private GridLayoutManager layoutManager;
    private Parcelable savedState;//保存的滚动状态
//    private boolean attached;

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
        scrollBarRadius = array.getDimensionPixelSize(R.styleable.TransformersLayout_tl_scrollbarRadius, dp2px(DEFAULT_SCROLL_BAR_HEIGHT / 2f));
        scrollBarTrackColor = array.getColor(R.styleable.TransformersLayout_tl_scrollbarTrackColor, DEFAULT_TRACK_COLOR);
        scrollBarThumbColor = array.getColor(R.styleable.TransformersLayout_tl_scrollbarThumbColor, DEFAULT_THUMB_COLOR);
        scrollBarTopMargin = array.getDimensionPixelSize(R.styleable.TransformersLayout_tl_scrollbarMarginTop, 0);
        scrollBarWidth = array.getDimensionPixelSize(R.styleable.TransformersLayout_tl_scrollbarWidth, dp2px(DEFAULT_SCROLL_BAR_WIDTH));
        scrollBarHeight = array.getDimensionPixelSize(R.styleable.TransformersLayout_tl_scrollbarHeight, dp2px(DEFAULT_SCROLL_BAR_HEIGHT));
        array.recycle();

        if (spanCount <= 0){
            spanCount = DEFAULT_SPAN_COUNT;
        }
        if (lines <= 0){
            lines = DEFAULT_LINES;
        }
    }

    private void init(Context context) {
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
        transformersAdapter = new TransformersAdapter<>(context, recyclerView);
        recyclerView.setAdapter(transformersAdapter);
        setupRecyclerView();

        scrollBar = new RecyclerViewScrollBar(context);
        setupScrollBar();

        addView(recyclerView);
        addView(scrollBar);
    }

    private void setupRecyclerView() {
        layoutManager = new GridLayoutManager(getContext(), lines, GridLayoutManager.HORIZONTAL, false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(layoutManager);
        transformersAdapter.setSpanCount(spanCount);
    }

    private void setupScrollBar() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(scrollBarWidth, scrollBarHeight);
        params.topMargin = scrollBarTopMargin;
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

    public void load(List<T> data, TransformersHolderCreator<T> creator){
        transformersAdapter.setOnTransformersItemClickListener(onTransformersItemClickListener);
        transformersAdapter.setHolderCreator(creator);
        transformersAdapter.setSpanCount(spanCount);
        transformersAdapter.setData(data);
        toggleScrollBar(data);
        if (scrollBar.getVisibility() == VISIBLE) {
            scrollBar.attachRecyclerView(recyclerView);
        }
//        Log.e(TAG, "----------load()");
    }

    public TransformersLayout<T> apply(@Nullable TransformersOptions options){
        if (options != null){
            spanCount = options.spanCount <= 0 ? spanCount : options.spanCount;
            int newLines = options.lines <= 0 ? lines : options.lines;
            scrollBarWidth = options.scrollBarWidth <= 0 ? scrollBarWidth : options.scrollBarWidth;
            scrollBarHeight = options.scrollBarHeight <= 0 ? scrollBarHeight : options.scrollBarHeight;
            scrollBarRadius = options.scrollBarRadius < 0 ? scrollBarHeight/2f : options.scrollBarRadius;
            scrollBarTopMargin = options.scrollBarTopMargin <= 0 ? scrollBarTopMargin : options.scrollBarTopMargin;
            scrollBarTrackColor = options.scrollBarTrackColor <= 0 ? DEFAULT_TRACK_COLOR : options.scrollBarTrackColor;
            scrollBarThumbColor = options.scrollBarThumbColor <= 0 ? DEFAULT_THUMB_COLOR : options.scrollBarThumbColor;

            if (newLines != lines){
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), lines, GridLayoutManager.HORIZONTAL, false));
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
        Log.e(TAG, "----------onAttachedToWindow()");
        super.onAttachedToWindow();
//        attached = true;
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
        Log.e(TAG, "----------onDetachedFromWindow()");
        super.onDetachedFromWindow();
//        attached = false;
        savedState = layoutManager.onSaveInstanceState();
    }

    public void notifyDataChanged(List<T> data){
        if (transformersAdapter != null){
            transformersAdapter.setData(data);
            scrollToStart();
        }
        toggleScrollBar(data);
        //数据发生改变时重新计算滚动比例
        if (scrollBar.getVisibility() == VISIBLE) {
            scrollBar.computeScrollScale();
        }
    }

    public void scrollToStart(){
        scrollToStart(true);
    }

    public void scrollToStart(boolean smooth){
        scrollBar.setScrollBySelf(true);
        if (recyclerView != null) {
            if (recyclerView.computeHorizontalScrollOffset() > 0) {
                Log.e(TAG, "----------scrollToStart()");
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
