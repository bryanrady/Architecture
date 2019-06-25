package com.bryanrady.architecture.isolation.image;
import android.content.Context;
import android.widget.ImageView;

/**
 * 加载器
 */
public interface ImageLoader {

    /**
     *
     * 加载图片
     * @param imageUrl
     * @param imageView
     */
    void displayImage(Context context, String imageUrl, ImageView imageView);

}
