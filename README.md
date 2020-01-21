[![Platform](https://img.shields.io/badge/platform-android-green.svg)](https://developer.android.google.cn) [![Licence](https://img.shields.io/badge/Licence-Apache2-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0) [![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16) [![jitpack](https://jitpack.io/v/zaaach/TransformersLayout.svg)](https://jitpack.io/#zaaach/TransformersLayout)

# TransformersLayout

> :fire: APP金刚区导航布局，下方带横向滚动条，很多APP都有使用这种，效果还不错就封装了一下:smile:
>
> 整体结构是Recyclerview + 滚动条

### Features

- 每页行数、列数可配置
- 滚动状态自动恢复
- 支持数据重新排序，类似viewpager的分页模式
- item布局样式自定义
- scrollbar样式可配置

# Preview
![gif](https://github.com/zaaach/TransformersLayout/raw/master/arts/preview2.gif)

[下载APK体验](https://github.com/zaaach/TransformersLayout/raw/master/arts/app-debug.apk)

# Install

:mega:项目基于AndroidX构建，参考迁移指南：[AndroidX迁移](https://developer.android.google.cn/jetpack/androidx/migrate)

**Step 1：** 项目根目录的build.gradle添加如下配置：

```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

**Step 2：** app添加依赖：

```groovy
dependencies {
	 implementation 'com.github.zaaach:TransformersLayout:x.y.z'
}
```

记得把`x.y.z`替换为[![jitpack](https://jitpack.io/v/zaaach/TransformersLayout.svg)](https://jitpack.io/#zaaach/TransformersLayout)中的数字

# How to use

**Step 1：** xml布局文件

```xml
<com.zaaach.transformerslayout.TransformersLayout                                         
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:background="#ffffff"
    app:tl_spanCount="5"
    app:tl_lines="2"
    app:tl_pagingMode="true"
    app:tl_scrollbarWidth="72dp"
    app:tl_scrollbarHeight="4dp"
    app:tl_scrollbarRadius="2dp"
    app:tl_scrollbarMarginTop="6dp"
    app:tl_scrollbarTrackColor="#f0f0f0"
    app:tl_scrollbarThumbColor="#FFC107"/>
```

**Step 2：** 自定义ViewHolder，第三步需要用到

```java
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
    public void onBind(Context context, List<T> list, @Nullable Nav data, int position) {
        text.setText(data.getText());
        Glide.with(context)
                .asBitmap()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.default_place_holder)
                .load(data.getUrl())
                .into(icon);
    }
}
```

**Step 3：** java代码中调用

```java
List<Nav> navList = DataFactory.loadData();
TransformersLayout<Nav> header = findViewById();
//options可选配置，会覆盖xml里的属性
TransformersOptions options = new TransformersOptions.Builder()
        .lines(2)
        .spanCount(5)
    	.pagingMode(true)
        .scrollBarWidth(Util.dp2px(this, 40))
        .scrollBarHeight(Util.dp2px(this, 3))
        .scrollBarRadius(Util.dp2px(this, 3) / 2f)
        .scrollBarTopMargin(Util.dp2px(this, 6))
    	.scrollBarTrackColor(Color.parseColor("#e5e5e5"))
        .scrollBarThumbColor(Color.parseColor("#658421"))
        .build();
header.apply(options)//options可为null
        .addOnTransformersItemClickListener(new OnTransformersItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showToast();
            }
        })
        .load(navList, new TransformersHolderCreator<Nav>() {
            @Override
            public Holder<Nav> createHolder(View itemView) {
                return new NavAdapterViewHolder(itemView);
            }
            @Override
            public int getLayoutId() {
                return R.layout.item_nav_list;//第二步使用的布局
            }
        });
```

:smirk:Good luck！！！

### 支持的attrs属性：

| Attributes | Format | Description |
| -------- | ---- | ---- |
| tl_spanCount | integer | 每页列数，默认5 |
| tl_lines | integer | 每页行数，默认2 |
| tl_pagingMode | boolean | 分页模式，数据会重新排序，默认false |
| tl_scrollbarWidth | dimension \| reference | scrollbar宽度，默认48dp |
| tl_scrollbarHeight | dimension \| reference | scrollbar高度，默认3dp |
| tl_scrollbarMarginTop | dimension \| reference | scrollbar上间距 |
| tl_scrollbarRadius | dimension \| reference | scrollbar圆角，默认高度的一半 |
| tl_scrollbarTrackColor | color \| reference | scrollbar轨道颜色 |
| tl_scrollbarThumbColor | color \| reference | scrollbar高亮颜色 |

# Change log

2020-1-21

- 新方法修复滚动条变长变短问题（很完美）
- 支持数据重新排序
- 回调方法变动

2020-1-5

- 修复滚动条突然变长变短的问题
- 优化默认圆角大小显示效果

2019-12-13 

- 修复滚动条颜色配置无效的问题

# About me

掘金主页：[ https://juejin.im/user/56f3dfe8efa6310055ac719f ](https://juejin.im/user/56f3dfe8efa6310055ac719f)

简书主页：[ https://www.jianshu.com/u/913a8bb93d12 ](https://www.jianshu.com/u/913a8bb93d12)

我的淘宝店：[ https://shop238932691.taobao.com ]( https://shop238932691.taobao.com)

:smile:是时候来一波三连了~

# License

```
Copyright (c) 2019 zaaach

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```