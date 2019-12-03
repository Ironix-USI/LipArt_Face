package com.mobile.lipart.ui.snap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SnapViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public SnapViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is try fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
