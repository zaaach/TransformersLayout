package com.zaaach.transformerslayoutdemo;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zaaach.transformerslayout.holder.Holder;

/**
 * @Author: Zaaach
 * @Date: 2019/11/25
 * @Email: zaaach@aliyun.com
 * @Description:
 */
public class NavAdapterViewHolder extends Holder<Nav> {
    private ImageView icon;
    private TextView text;

    NavAdapterViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    protected void initView(View itemView) {
        icon = itemView.findViewById(R.id.iv_menu_icon);
        text = itemView.findViewById(R.id.tv_menu_text);
    }

    @Override
    public void bindData(Context context, Nav data) {
        text.setText(data.getText());
//        icon.setImageResource(data.getIcon());
        Glide.with(context)
                .asBitmap()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.default_place_holder)
                .load(data.getUrl())
                .into(icon);
    }
}
