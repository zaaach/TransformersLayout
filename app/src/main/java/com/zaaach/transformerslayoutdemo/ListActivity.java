package com.zaaach.transformerslayoutdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.zaaach.transformerslayout.TransformersLayout;
import com.zaaach.transformerslayout.TransformersOptions;
import com.zaaach.transformerslayout.holder.Holder;
import com.zaaach.transformerslayout.holder.TransformersHolderCreator;
import com.zaaach.transformerslayout.listener.OnTransformersItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Zaaach
 * @Date: 2019/11/25
 * @Email: zaaach@aliyun.com
 * @Description:
 */
public class ListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final List<Nav> navList = DataFactory.loadData();
        final TransformersLayout<Nav> header = new TransformersLayout<>(this);
        //使用options配置会覆盖xml的属性
        TransformersOptions options = new TransformersOptions.Builder()
                .lines(2)
                .spanCount(5)
                .scrollBarWidth(Util.dp2px(this, 40))
                .scrollBarHeight(Util.dp2px(this, 4))
                .scrollBarRadius(Util.dp2px(this, 4) / 2)
                .scrollBarTopMargin(Util.dp2px(this, 6))
                .scrollBarTrackColor(Color.parseColor("#e5e5e5"))
//                .scrollBarThumbColor(Color.parseColor("#658421"))
                .build();
        header.apply(options)
                .addOnTransformersItemClickListener(new OnTransformersItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Toast.makeText(getApplication(), "点击：" + navList.get(position).getText() + "，位置" + position, Toast.LENGTH_SHORT).show();
                    }
                })
                .load(navList, new TransformersHolderCreator<Nav>() {
                    @Override
                    public Holder<Nav> createHolder(View itemView) {
                        return new NavAdapterViewHolder(itemView);
                    }

                    @Override
                    public int getLayoutId() {
                        return R.layout.item_nav_list;
                    }
                });

        RecyclerView rv = findViewById(R.id.rv_main);
        rv.setLayoutManager(new LinearLayoutManager(this));
        List<String> beans = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            beans.add("");
        }
        final ListAdapter adapter = new ListAdapter(beans);
        adapter.addHeaderView(header);
        rv.setAdapter(adapter);

        final SwipeRefreshLayout refreshLayout = findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        navList.remove(0);
                        header.notifyDataChanged(navList);
                        refreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
    }
}
