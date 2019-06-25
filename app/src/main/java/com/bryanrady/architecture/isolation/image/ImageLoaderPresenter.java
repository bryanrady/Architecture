package com.bryanrady.architecture.isolation.image;

import android.content.Context;
import android.widget.ImageView;

import com.bryanrady.architecture.isolation.image.glide.GlideImageLoader;

/**
 * Created by Administrator on 2019/6/25.
 */

public class ImageLoaderPresenter implements ImageLoader {

    private ImageLoader mImageLoader;
    private static volatile ImageLoaderPresenter instance;

    private ImageLoaderPresenter(){
        //给个默认的
        mImageLoader = new GlideImageLoader();
    }

    public static ImageLoaderPresenter getInstance(){
        if (instance == null){
            synchronized (ImageLoaderPresenter.class){
                if (instance == null){
                    instance = new ImageLoaderPresenter();
                }
            }
        }
        return instance;
    }

    public ImageLoaderPresenter with(ImageLoader imageLoader){
        this.mImageLoader = imageLoader;
        return instance;
    }

    @Override
    public void displayImage(Context context, String imageUrl, ImageView imageView) {
        if(mImageLoader == null){
            //你必须通过with方法先制定请求类型
            throw new IllegalStateException("You must first formulate the request type (ImageLoader) through the with method!");
        }
        mImageLoader.displayImage(context,imageUrl,imageView);
    }
}
