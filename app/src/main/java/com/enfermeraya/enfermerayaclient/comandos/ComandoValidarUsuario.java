package com.enfermeraya.enfermerayaclient.comandos;


import androidx.annotation.NonNull;

import com.enfermeraya.enfermerayaclient.app.Modelo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Created by tacto on 2/10/17.
 */

public class ComandoValidarUsuario {

    Modelo modelo = Modelo.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    //interface del listener de la actividad interesada
    private OnValidarUsuarioChangeListener mListener;

    public ComandoValidarUsuario(OnValidarUsuarioChangeListener mListener){

        this.mListener = mListener;

    }

    /**
     * Interfaz para avisar de eventos a los interesados
     */
    public interface OnValidarUsuarioChangeListener {

        void validandoConductorOK();
        void validandoConductorError();


    }


    public void validandoUsuario(){
        DatabaseReference ref = database.getReference("cliente/"+modelo.uid+"/");//ruta path
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snap) {


                if(snap.exists()){
                    mListener.validandoConductorOK();
                }else{
                    mListener.validandoConductorError();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }




    /**
     * Para evitar nullpointerExeptions
     */
    private static OnValidarUsuarioChangeListener sDummyCallbacks = new OnValidarUsuarioChangeListener()
    {
        @Override
        public void validandoConductorOK()
        {}


        @Override
        public void validandoConductorError()
        {}





    };

}