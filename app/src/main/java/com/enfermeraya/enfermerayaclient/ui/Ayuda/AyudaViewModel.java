package com.enfermeraya.enfermerayaclient.ui.Ayuda;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AyudaViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AyudaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Ayuda");
    }

    public LiveData<String> getText() {
        return mText;
    }
}