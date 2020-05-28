package com.bryanrady.architecture.databind;

import android.widget.ImageView;

import com.bryanrady.architecture.BR;
import com.bumptech.glide.Glide;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

public class User extends BaseObservable {

    private String name;
    private String sex;
    private int age;
    private String headerPath;

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
        notifyPropertyChanged(BR.sex);
    }

    @Bindable
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
        notifyPropertyChanged(BR.age);
    }

    public String getHeaderPath() {
        return headerPath;
    }

    public void setHeaderPath(String headerPath) {
        this.headerPath = headerPath;
    }

    //自定义属性:提供一个静态方法来加载image
    @BindingAdapter("bind:headerPath")
    public static void loadImage(ImageView view, String url){
        Glide.with(view.getContext()).load(url).into(view);
    }

}
