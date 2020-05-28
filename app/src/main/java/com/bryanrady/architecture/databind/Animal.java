package com.bryanrady.architecture.databind;

import com.bryanrady.architecture.BR;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class Animal extends BaseObservable {

    private String name;
    private float weight;

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
        notifyPropertyChanged(BR.weight);
    }
}
