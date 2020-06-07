package com.enfermeraya.enfermerayaclient.ui.MisServicios;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MisServiciosViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MisServiciosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Mis Servicios");
    }

    public LiveData<String> getText() {
        return mText;
    }
}