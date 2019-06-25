package com.bryanrady.architecture.isolation.image.picasso;

import android.content.Context;
import android.widget.ImageView;

import com.bryanrady.architecture.isolation.image.ImageLoader;
import com.squareup.picasso.Picasso;

/**
 * Created by Administrator on 2019/6/25.
 */

public class PicassoImageLoader implements ImageLoader {

    public PicassoImageLoader(){
        //可以在构造函数做一些配置等信息
    }

    @Override
    public void displayImage(Context context, String imageUrl, ImageView imageView) {
        Picasso.get().load(imageUrl).into(imageView);
    }
}
