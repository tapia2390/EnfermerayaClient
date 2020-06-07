package com.enfermeraya.enfermerayaclient.ui.Recomienda;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RecomiendaViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RecomiendaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Recomienda");
    }

    public LiveData<String> getText() {
        return mText;
    }
}