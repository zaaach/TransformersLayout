package com.zaaach.transformerslayout;

import androidx.annotation.ColorInt;
import androidx.annotation.Px;

/**
 * @Author: Zaaach
 * @Date: 2019/11/23
 * @Email: zaaach@aliyun.com
 * @Description:
 */
public class TransformersOptions {
    public final int spanCount;
    public final int lines;
    public final int scrollBarWidth;
    public final int scrollBarHeight;
    public final int scrollBarTopMargin;
    public final int scrollBarTrackColor;
    public final int scrollBarThumbColor;
    public final float scrollBarRadius;

    private TransformersOptions(Builder builder) {
        spanCount = builder.spanCount;
        lines = builder.lines;
        scrollBarWidth = builder.scrollBarWidth;
        scrollBarHeight = builder.scrollBarHeight;
        scrollBarTopMargin = builder.scrollBarTopMargin;
        scrollBarTrackColor = builder.scrollBarTrackColor;
        scrollBarThumbColor = builder.scrollBarThumbColor;
        scrollBarRadius = builder.scrollBarRadius;
    }

    public static class Builder{
        private int spanCount;
        private int lines;
        private int scrollBarWidth;
        private int scrollBarHeight;
        private int scrollBarTopMargin;
        private int scrollBarTrackColor;
        private int scrollBarThumbColor;
        private float scrollBarRadius = -1;

        public Builder scrollBarRadius(@Px float radius) {
            this.scrollBarRadius = radius;
            return this;
        }

        public Builder scrollBarTrackColor(@ColorInt int color) {
            this.scrollBarTrackColor = color;
            return this;
        }

        public Builder scrollBarThumbColor(@ColorInt int color) {
            this.scrollBarThumbColor = color;
            return this;
        }

        public Builder scrollBarTopMargin(@Px int topMargin){
            scrollBarTopMargin = topMargin;
            return this;
        }

        public Builder scrollBarWidth(@Px int width) {
            this.scrollBarWidth = width;
            return this;
        }

        public Builder scrollBarHeight(@Px int height) {
            this.scrollBarHeight = height;
            return this;
        }

        public Builder spanCount(int spanCount) {
            this.spanCount = spanCount;
            return this;
        }

        public Builder lines(int lines) {
            this.lines = lines;
            return this;
        }

        public TransformersOptions build(){
            return new TransformersOptions(this);
        }
    }
}
