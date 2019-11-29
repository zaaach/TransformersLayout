package com.zaaach.transformerslayout.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zaaach.transformerslayout.holder.Holder;
import com.zaaach.transformerslayout.holder.TransformersHolderCreator;
import com.zaaach.transformerslayout.listener.OnTransformersItemClickListener;

import java.util.List;

/**
 * @Author: Zaaach
 * @Date: 2019/11/22
 * @Email: zaaach@aliyun.com
 * @Description:
 */
public class TransformersAdapter<T> extends RecyclerView.Adapter<Holder<T>> {
    private Context mContext;
    private List<T> mData;
    private TransformersHolderCreator<T> holderCreator;
    private RecyclerView mRecyclerView;
    private int spanCount;
    private OnTransformersItemClickListener onTransformersItemClickListener;

    public void setOnTransformersItemClickListener(OnTransformersItemClickListener listener) {
        this.onTransformersItemClickListener = listener;
    }

    public TransformersAdapter(Context context, RecyclerView recyclerView){
        mContext = context;
        mRecyclerView = recyclerView;
    }

    public void setData(List<T> data){
        mData = data;
        notifyDataSetChanged();
    }

    public void setSpanCount(int spanCount){
        this.spanCount = spanCount;
    }

    public void setHolderCreator(TransformersHolderCreator<T> creator){
        holderCreator = creator;
    }

    @NonNull
    @Override
    public Holder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = holderCreator.getLayoutId();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        //每个item平分整个屏幕的宽度
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
        params.width = mRecyclerView.getMeasuredWidth() / spanCount;
        return holderCreator.createHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder<T> holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onTransformersItemClickListener != null){
                    onTransformersItemClickListener.onItemClick(position);
                }
            }
        });
        holder.bindData(mContext, mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }
}
