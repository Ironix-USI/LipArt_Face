package com.mobile.lipart.ui.circle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CircleViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CircleViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is circle fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}