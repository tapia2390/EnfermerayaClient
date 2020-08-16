package com.enfermeraya.enfermerayaclient.ui.Historial;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HistorialViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HistorialViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Historial");
    }

    public LiveData<String> getText() {
        return mText;
    }
}