package com.zaaach.transformerslayout.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.zaaach.transformerslayout.listener.OnTransformersScrollListener;

/**
 * @Author: Zaaach
 * @Date: 2019/11/22
 * @Email: zaaach@aliyun.com
 * @Description:
 */
public class RecyclerViewScrollBar extends View {
    private static final String TAG = RecyclerViewScrollBar.class.getSimpleName();
    private static final int SCROLL_LOCATION_START  = 1;
    private static final int SCROLL_LOCATION_MIDDLE = 2;
    private static final int SCROLL_LOCATION_END    = 3;

    private RecyclerView mRecyclerView;
    private OnTransformersScrollListener onTransformersScrollListener;
    private int mWidth;
    private int mHeight;

    private Paint mPaint = new Paint();
    private RectF mTrackRectF = new RectF();
    private RectF mThumbRectF = new RectF();
    private float radius;
    private int mTrackColor;
    private int mThumbColor;

    private float mThumbScale = 0f;
    private float mScrollScale = 0f;
    //当前滚动条位置：起点、滚动中、终点
    private int mScrollLocation = SCROLL_LOCATION_START;
    private boolean scrollBySelf;//是否是调用scrollToPosition或smoothScrollToPosition方法来滚动的

    public void setScrollBySelf(boolean bySelf) {
        this.scrollBySelf = bySelf;
    }

    public void setOnTransformersScrollListener(OnTransformersScrollListener listener) {
        this.onTransformersScrollListener = listener;
    }

    private final RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            Log.d(TAG, "---------onScrollStateChanged()");
            if (onTransformersScrollListener != null){
                onTransformersScrollListener.onScrollStateChanged(recyclerView, newState);
            }
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            Log.d(TAG, "---------onScrolled()");
            computeScrollScale();
            //如果调用scrollToPosition或smoothScrollToPosition来滚动，不会触发onScrollStateChanged，所以这里再次判断下然后手动回调
            if (scrollBySelf && mRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE){
                onScrollStateChanged(recyclerView, RecyclerView.SCROLL_STATE_IDLE);
                scrollBySelf = false;
            }
            if (onTransformersScrollListener != null){
                onTransformersScrollListener.onScrolled(recyclerView, dx, dy);
            }
        }
    };

    public RecyclerViewScrollBar(Context context) {
        this(context, null);
    }

    public RecyclerViewScrollBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerViewScrollBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RecyclerViewScrollBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        initPaint();
    }

    private void initPaint() {
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);
    }

    public void attachRecyclerView(RecyclerView recyclerView){
        if (mRecyclerView == recyclerView){
            return;
        }
        mRecyclerView = recyclerView;
        if (mRecyclerView != null){
            mRecyclerView.removeOnScrollListener(mScrollListener);
            mRecyclerView.addOnScrollListener(mScrollListener);
            //监听View的视图树变化,防止初始化未获取到滚动条比例
            mRecyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mRecyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                    computeScrollScale();
                    return true;
                }
            });
        }
    }

    public RecyclerViewScrollBar setRadius(float radius){
        this.radius = radius;
        return this;
    }

    public RecyclerViewScrollBar setTrackColor(@ColorInt int color) {
        this.mTrackColor = color;
        return this;
    }

    public RecyclerViewScrollBar setThumbColor(@ColorInt int color) {
        this.mThumbColor = color;
        return this;
    }

    public void applyChange(){
        postInvalidate();
    }

    public void computeScrollScale() {
        if (mRecyclerView == null) return;
        //RecyclerView已显示宽度
        float mScrollExtent = mRecyclerView.computeHorizontalScrollExtent();
        //RecyclerView实际宽度
        float mScrollRange = mRecyclerView.computeHorizontalScrollRange();
        if (mScrollRange != 0){
            mThumbScale = mScrollExtent / mScrollRange;
        }

        //RecyclerView已经滚动的距离
        float mScrollOffset = mRecyclerView.computeHorizontalScrollOffset();
        if (mScrollRange != 0){
            mScrollScale = mScrollOffset / mScrollRange;
        }

        //RecyclerView可以滚动的距离
        float canScrollDistance = mScrollRange - mScrollExtent;
//        Log.d(TAG, "---------mScrollExtent = " + mScrollExtent);
//        Log.d(TAG, "---------mScrollRange = " + mScrollRange);
//        Log.d(TAG, "---------mScrollOffset = " + mScrollOffset);
//        Log.d(TAG, "---------canScrollDistance = " + canScrollDistance);
//        Log.d(TAG, "---------mThumbScale = " + mThumbScale);
//        Log.d(TAG, "---------mScrollScale = " + mScrollScale);
//        Log.d(TAG, "*****************************************");
        if (mScrollOffset == 0){
            mScrollLocation = SCROLL_LOCATION_START;
        }else if (canScrollDistance == mScrollOffset){
            mScrollLocation = SCROLL_LOCATION_END;
        }else{
            mScrollLocation = SCROLL_LOCATION_MIDDLE;
        }
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTrack(canvas);

        drawThumb(canvas);
    }

    private void drawThumb(Canvas canvas) {
        initPaint();
        mPaint.setColor(mThumbColor);
        float left = mScrollScale * mWidth;
        float right = left + mWidth * mThumbScale;
        switch (mScrollLocation){
            case SCROLL_LOCATION_START:
                mThumbRectF.set(0, 0, right, mHeight);
                break;
            case SCROLL_LOCATION_MIDDLE:
                mThumbRectF.set(left, 0, right, mHeight);
                break;
            case SCROLL_LOCATION_END:
                mThumbRectF.set(left, 0, mWidth, mHeight);
                break;
        }
        canvas.drawRoundRect(mThumbRectF, radius, radius, mPaint);
    }

    private void drawTrack(Canvas canvas) {
        initPaint();
        mPaint.setColor(mTrackColor);
        mTrackRectF.set(0, 0, mWidth, mHeight);
        canvas.drawRoundRect(mTrackRectF, radius, radius, mPaint);
    }
}
