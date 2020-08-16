package com.enfermeraya.enfermerayaclient.comandos;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.enfermeraya.enfermerayaclient.app.Modelo;
import com.enfermeraya.enfermerayaclient.clases.Historial;
import com.enfermeraya.enfermerayaclient.clases.Servicios;
import com.enfermeraya.enfermerayaclient.clases.TipoServicio;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ComandoHistorial {


    Modelo modelo = Modelo.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference referencia = database.getReference();
    DataSnapshot dataSnapshot;



    //interface del listener de la actividad interesada
    private OnHistorialChangeListener mListener;


    /**
     * Interfaz para avisar de eventos a los interesados
     */
    public interface OnHistorialChangeListener {


        void errorHistorial();
        void okHistorial();

    }

    public ComandoHistorial(OnHistorialChangeListener mListener){

        this.mListener = mListener;

    }



    public void updateFavorito(String estado, String key,String titulo){

        final DatabaseReference ref = database.getReference("cliente/"+modelo.uid+"/servicios/"+key+ "/estado/");//ruta path
        ref.setValue(estado);


        final DatabaseReference ref2 = database.getReference("cliente/"+modelo.uid+"/servicios/"+key+ "/titulo/");//ruta path
        ref2.setValue(titulo);

        mListener.okHistorial();


    }

    public void servicoAceptadouidCliente(String uidCliente, String idServicio, String stado){

        final DatabaseReference ref = database.getReference("servicioclientes/"+idServicio+ "/uidCliente/");//ruta path
        ref.setValue(uidCliente);

        final DatabaseReference ref2 = database.getReference("servicioclientes/"+idServicio+ "/estado/");//ruta path
        ref2.setValue(stado);



        mListener.okHistorial();


    }



    public void updateServico(String observcionEnfermero, String medicamentos,String key,String foto){

        final DatabaseReference ref = database.getReference("servicioclientes/"+key+ "/observacionesEnfermero/");//ruta path
        ref.setValue(observcionEnfermero);


        final DatabaseReference ref2 = database.getReference("servicioclientes/"+key+ "/medicamentosAsignados/");//ruta path
        ref2.setValue(medicamentos);

        final DatabaseReference ref3 = database.getReference("servicioclientes/"+key+ "/estado/");//ruta path
        ref3.setValue("Finalizado");

        final DatabaseReference ref4 = database.getReference("servicioclientes/"+key+ "/foto/");//ruta path
        ref4.setValue(foto);

        moverHistorial(key);

    }

    private void moverHistorial(String key) {

        DatabaseReference ref = database.getReference("servicioclientes/"+key);//ruta path
        DatabaseReference refHistorico = database.getReference("historial/" + key);//ruta path

        moverRegistro(ref, refHistorico, key);

    }


    public void moverRegistro(final DatabaseReference fromPath, final DatabaseReference toPath, final String key) {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                modelo = Modelo.getInstance();
                toPath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError error, DatabaseReference ref) {
                        if (error != null) {
                            mListener.errorHistorial();
                        } else {
                            fromPath.removeValue();
                            mListener.okHistorial();

                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mListener.errorHistorial();

                Log.d("firebaseError", "error intentando copiar datos historico");
            }
        });
    }



    public void  getListaHistorico(){
        //preguntasFrecuentes
        modelo.listHistorial.clear();
        DatabaseReference ref = database.getReference("historial");//ruta path
        Query query = ref.orderByChild("uidCliente").equalTo(modelo.uid);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snFav, @Nullable String s) {
                Historial ser = new Historial();
                Long timestamp =  (Long) snFav.child("timestamp").getValue();
                ser.setKey(snFav.getKey());

                ser.setTimestamp(timestamp);

                double lattitud = (double)snFav.child("latitud").getValue();
                double longitud = (double)snFav.child("longitud").getValue();

                if(snFav.child("calificacion").exists()){
                    double calificacion = (double)snFav.child("calificacion").getValue();
                    ser.setCalificaion(calificacion);

                }else{
                    ser.setCalificaion(0.0);

                }

                ser.setLatitud(lattitud);
                ser.setLongitud(longitud);

                ser.setTipoServicio(snFav.child("tipoServicio").getValue().toString());
                ser.setFecha(snFav.child("fecha").getValue().toString());
                ser.setHoraServicio(snFav.child("horaServicio").getValue().toString());

                ser.setDireccion(snFav.child("direccion").getValue().toString());
                ser.setInformacion(snFav.child("informacion").getValue().toString());
                ser.setObsciones(snFav.child("obsciones").getValue().toString());


                ser.setTitulo(snFav.child("titulo").getValue().toString());
                ser.setEstado(snFav.child("estado").getValue().toString());
                ser.setUid(snFav.child("uid").getValue().toString());
                ser.setToken(snFav.child("token").getValue().toString());
                ser.setObservacionesEnfermero(snFav.child("observacionesEnfermero").getValue().toString());
                ser.setMedicamentosAsignados(snFav.child("medicamentosAsignados").getValue().toString());
                ser.setFoto(snFav.child("foto").getValue().toString());
                modelo.listHistorial.add(ser);

                mListener.okHistorial();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    /**
     * Para evitar nullpointerExeptions
     */
    private static OnHistorialChangeListener sDummyCallbacks = new OnHistorialChangeListener()
    {

        @Override
        public void okHistorial() {}

        @Override
        public void errorHistorial() {}

    };
}
