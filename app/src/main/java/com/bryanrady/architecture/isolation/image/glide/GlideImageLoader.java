package com.bryanrady.architecture.isolation.image.glide;

import android.content.Context;
import android.widget.ImageView;

import com.bryanrady.architecture.isolation.image.ImageLoader;
import com.bumptech.glide.Glide;

/**
 * Created by Administrator on 2019/6/25.
 */

public class GlideImageLoader implements ImageLoader {

    public GlideImageLoader(){
        //可以在构造函数做一些配置等信息
    }

    @Override
    public void displayImage(Context context, String imageUrl, ImageView imageView) {
        Glide.with(context).load(imageUrl).into(imageView);
    }
}
