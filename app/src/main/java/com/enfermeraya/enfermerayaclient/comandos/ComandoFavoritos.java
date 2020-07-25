package com.enfermeraya.enfermerayaclient.comandos;

import android.util.Log;

import com.enfermeraya.enfermerayaclient.app.Modelo;
import com.enfermeraya.enfermerayaclient.clases.Favoritos;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import java.util.HashMap;
import java.util.Map;

public class ComandoFavoritos {


    Modelo modelo = Modelo.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference referencia = database.getReference();
    DataSnapshot dataSnapshot;



    //interface del listener de la actividad interesada
    private OnFavoritosChangeListener mListener;

    /**
     * Interfaz para avisar de eventos a los interesados
     */
    public interface OnFavoritosChangeListener {


        void getFavorito();
        void cargoFavorito();
        void errorFavorito();

    }

    public ComandoFavoritos(OnFavoritosChangeListener mListener){

        this.mListener = mListener;

    }

    public  void registarFavotito(String direccion, double lat , double longi){
        //creating a new user
        //creating a new user


        //String uid = mAuth.getCurrentUser().getUid();
        DatabaseReference memoReference = FirebaseDatabase.getInstance().getReference();
        String key = memoReference.push().getKey();
        final DatabaseReference ref = database.getReference("cliente/"+modelo.uid+"/favoritos/"+key);//ruta path


        Map<String, Object> favorito = new HashMap<String, Object>();


        favorito.put("direccion", direccion);
        favorito.put("latitud", lat);
        favorito.put("longitud", longi);
        favorito.put("timestamp", ServerValue.TIMESTAMP);
        favorito.put("estado", "true");




        ref.setValue(favorito, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {

                    mListener.cargoFavorito();
                    return;
                } else {
                    mListener.errorFavorito();
                }
            }
        });



    }



    public void  getListFavorito(){
        //preguntasFrecuentes
        modelo.listFavoritos.clear();
        DatabaseReference ref = database.getReference("cliente/"+modelo.uid+"/servicios/");//ruta path

        ChildEventListener listener = new ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot snFav, String s) {



                if(snFav.child("estado").getValue().toString().equals("true")){
                    Favoritos fav = new Favoritos();
                    Long timestamp =  (Long) snFav.child("timestamp").getValue();
                    fav.setKey(snFav.getKey());

                    double lattitud = (double)snFav.child("latitud").getValue();
                    double longitud = (double)snFav.child("longitud").getValue();

                    fav.setLatitud(lattitud);
                    fav.setLongitud(longitud);
                    fav.setTipoServicio(snFav.child("tipoServicio").getValue().toString());
                    fav.setFecha(snFav.child("fecha").getValue().toString());
                    fav.setHoraServicio(snFav.child("horaServicio").getValue().toString());
                    fav.setDireccion(snFav.child("direccion").getValue().toString());
                    fav.setInformacion(snFav.child("informacion").getValue().toString());
                    fav.setObsciones(snFav.child("obsciones").getValue().toString());
                    fav.setTitulo(snFav.child("titulo").getValue().toString());
                    fav.setEstado(snFav.child("estado").getValue().toString());
                    fav.setTimestamp(timestamp);


                    modelo.listFavoritos.add(fav);

                }

                mListener.getFavorito();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        ref.addChildEventListener(listener);


    }







    /**
     * Para evitar nullpointerExeptions
     */
    private static OnFavoritosChangeListener sDummyCallbacks = new OnFavoritosChangeListener()
    {

        @Override
        public void cargoFavorito()
        {}


        @Override
        public void errorFavorito()
        {}

        @Override
        public void getFavorito()
        {}

    };
}
