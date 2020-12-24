package com.enfermeraya.enfermerayaclient.comandos;

import com.enfermeraya.enfermerayaclient.app.Modelo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Created by tacto on 24/11/17.
 */

public class ComandoSetting {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference referencia = database.getReference();
    Modelo modelo  = Modelo.getInstance();

    public interface OnComandoSettingChangeListener {

        void setting();
    }

    //interface del listener de la actividad interesada
    private OnComandoSettingChangeListener mListener;

    public ComandoSetting(OnComandoSettingChangeListener mListener){

        this.mListener = mListener;
    }


    public void getsetting(){
        DatabaseReference ref = database.getReference("setting/");//ruta path
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snap) {

                modelo.setting.setMsmcompartir(snap.child("msmcompartir").getValue().toString());
                modelo.setting.setUrl_face(snap.child("url_face").getValue().toString());
                modelo.setting.setUrl_in(snap.child("url_in").getValue().toString());
                modelo.setting.setUrl_instagra(snap.child("url_instagra").getValue().toString());
                modelo.setting.setUrl_twitter(snap.child("url_twitter").getValue().toString());
                modelo.setting.setUrlcompartir(snap.child("urlcompartir").getValue().toString());




                mListener.setting();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /**
     * Para evitar nullpointerExeptions
     */
    private static OnComandoSettingChangeListener sDummyCallbacks = new OnComandoSettingChangeListener()
    {


        @Override
        public void setting()
        {}

    };
}