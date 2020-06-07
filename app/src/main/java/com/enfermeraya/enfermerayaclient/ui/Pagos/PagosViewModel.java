package com.enfermeraya.enfermerayaclient.ui.Pagos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PagosViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PagosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Pagos");
    }

    public LiveData<String> getText() {
        return mText;
    }
}