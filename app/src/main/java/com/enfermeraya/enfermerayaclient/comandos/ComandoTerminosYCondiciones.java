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

public class ComandoTerminosYCondiciones {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference referencia = database.getReference();
    Modelo modelo  = Modelo.getInstance();

    public interface OnComandoTerminosYCondicionesChangeListener {

        void terminos_y_Condiciones();
    }

    //interface del listener de la actividad interesada
    private OnComandoTerminosYCondicionesChangeListener mListener;

    public ComandoTerminosYCondiciones(OnComandoTerminosYCondicionesChangeListener mListener){

        this.mListener = mListener;
    }


    public void getTerminos_y_Condiciones(){
        DatabaseReference ref = database.getReference("TerminosYCondiciones/");//ruta path
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snap) {

                for (DataSnapshot snterminos : snap.getChildren()) {
                    modelo.classTerminosYCondiciones.setId(snterminos.getKey());
                    modelo.classTerminosYCondiciones.setTexto(snterminos.child("texto").getValue().toString());
                    modelo.classTerminosYCondiciones.setTitulo(snterminos.child("titulo").getValue().toString());
                }

                mListener.terminos_y_Condiciones();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /**
     * Para evitar nullpointerExeptions
     */
    private static OnComandoTerminosYCondicionesChangeListener sDummyCallbacks = new OnComandoTerminosYCondicionesChangeListener()
    {


        @Override
        public void terminos_y_Condiciones()
        {}

    };
}