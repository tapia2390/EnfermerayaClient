package com.enfermeraya.enfermerayaclient.comandos;


import androidx.annotation.NonNull;

import com.enfermeraya.enfermerayaclient.app.Modelo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by tacto on 2/10/17.
 */

public class ComandoService {

    Modelo modelo = Modelo.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    //interface del listener de la actividad interesada
    private OnValidarUsuarioChangeListener mListener;

    public ComandoService(OnValidarUsuarioChangeListener mListener){

        this.mListener = mListener;

    }

    /**
     * Interfaz para avisar de eventos a los interesados
     */
    public interface OnValidarUsuarioChangeListener {

        void ValindandoOk();

    }


    public void validandServicio(){
        mListener.ValindandoOk();

    }




    /**
     * Para evitar nullpointerExeptions
     */
    private static OnValidarUsuarioChangeListener sDummyCallbacks = new OnValidarUsuarioChangeListener()
    {
        @Override
        public void ValindandoOk()
        {}


    };

}