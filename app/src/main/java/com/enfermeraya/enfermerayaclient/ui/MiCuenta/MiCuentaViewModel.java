package com.enfermeraya.enfermerayaclient.ui.MiCuenta;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MiCuentaViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MiCuentaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Mi Cuenta");
    }

    public LiveData<String> getText() {
        return mText;
    }
}