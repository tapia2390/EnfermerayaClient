package com.enfermeraya.enfermerayaclient.comandos;


import android.util.Log;

import com.enfermeraya.enfermerayaclient.app.Modelo;
import com.enfermeraya.enfermerayaclient.clases.Servicios;
import com.enfermeraya.enfermerayaclient.clases.TipoServicio;
import com.enfermeraya.enfermerayaclient.notificacion.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import java.util.HashMap;
import java.util.Map;

public class ComandoSercicio {


    Modelo modelo = Modelo.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference referencia = database.getReference();
    DataSnapshot dataSnapshot;



    //interface del listener de la actividad interesada
    private OnSercicioChangeListener mListener;

    public void updateToken(String token){

        final DatabaseReference ref = database.getReference("cliente/"+modelo.uid+"/tokem/");//ruta path
        ref.setValue(token);
    }

    /**
     * Interfaz para avisar de eventos a los interesados
     */
    public interface OnSercicioChangeListener {


        void getTipoServicio();
        void getServicio();
        void cargoServicio();
        void errorServicio();
        void actualizarFavorito();

    }

    public ComandoSercicio(OnSercicioChangeListener mListener){

        this.mListener = mListener;

    }

    public  void registarServicio(String tipoServicio,String fecha,String horaInicio,String horaFin,
                                  String direccion, String informacion, String obsciones, double lat , double longi, String titulo){
        //creating a new user
        //creating a new user


        //String uid = mAuth.getCurrentUser().getUid();
        DatabaseReference memoReference = FirebaseDatabase.getInstance().getReference();
        String key = memoReference.push().getKey();
        final DatabaseReference ref = database.getReference("cliente/"+modelo.uid+"/servicios/"+key);//ruta path


        Map<String, Object> favorito = new HashMap<String, Object>();

        favorito.put("tipoServicio", tipoServicio);
        favorito.put("fecha", fecha);
        favorito.put("horaInicio", horaInicio);
        favorito.put("horaFin", horaFin);
        favorito.put("direccion", direccion);
        favorito.put("informacion", informacion);
        favorito.put("obsciones", obsciones);
        favorito.put("latitud", lat);
        favorito.put("longitud", longi);
        favorito.put("titulo", titulo);
        favorito.put("estado", "false");
        favorito.put("timestamp", ServerValue.TIMESTAMP);




        ref.setValue(favorito, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {

                    mListener.cargoServicio();
                    return;
                } else {
                    mListener.errorServicio();
                }
            }
        });



    }




    public void getServicio() {
        modelo = Modelo.getInstance();

        DatabaseReference ref = database.getReference("servicioclientes");
        Query query = ref.orderByChild("uid").equalTo(modelo.uid);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snFav, String s) {


                Servicios ser = new Servicios();
                Long timestamp =  (Long) snFav.child("timestamp").getValue();
                ser.setKey(snFav.getKey());

                ser.setTimestamp(timestamp);

                double lattitud = (double)snFav.child("latitud").getValue();
                double longitud = (double)snFav.child("longitud").getValue();

                ser.setLatitud(lattitud);
                ser.setLongitud(longitud);

                ser.setTipoServicio(snFav.child("tipoServicio").getValue().toString());
                ser.setFecha(snFav.child("fecha").getValue().toString());
                ser.setHoraInicio(snFav.child("horaInicio").getValue().toString());
                ser.setHoraFin(snFav.child("horaFin").getValue().toString());
                ser.setDireccion(snFav.child("direccion").getValue().toString());
                ser.setInformacion(snFav.child("informacion").getValue().toString());
                ser.setObsciones(snFav.child("obsciones").getValue().toString());


                ser.setTitulo(snFav.child("titulo").getValue().toString());
                ser.setEstado(snFav.child("estado").getValue().toString());
                ser.setUid(snFav.child("uid").getValue().toString());
                ser.setToken(snFav.child("token").getValue().toString());
                modelo.listServicios.add(ser);

                mListener.getServicio();


            }

            @Override
            public void onChildChanged(DataSnapshot snap, String s) {
                Log.v("texto:",s);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                Log.v("dataSnapshot",dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.v("dataSnapshot",dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("dataSnapshot",databaseError.getMessage());
            }
        });
        //mListener.cargoUnaOrdenesConductor();
    }

    public void  getListServicio(){
        //preguntasFrecuentes
        modelo.listServicios.clear();
        DatabaseReference ref = database.getReference("servicioclientes");//ruta path

        ChildEventListener listener = new ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot snFav, String s) {

                Servicios ser = new Servicios();
                Long timestamp =  (Long) snFav.child("timestamp").getValue();
                ser.setKey(snFav.getKey());

                ser.setTimestamp(timestamp);

                double lattitud = (double)snFav.child("latitud").getValue();
                double longitud = (double)snFav.child("longitud").getValue();

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
               if(snFav.child("foto").exists()){
                   ser.setFoto(snFav.child("foto").getValue().toString());
               }

                modelo.listServicios.add(ser);

                mListener.getServicio();

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


    public void  getTipoServicio(){
        //preguntasFrecuentes
        modelo.listTipoServicios.clear();
        DatabaseReference ref = database.getReference("Servicios/");//ruta path

        ChildEventListener listener = new ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot snFav, String s) {

                TipoServicio ser = new TipoServicio();
                ser.setKey(snFav.getKey());
                ser.setNombre(snFav.child("Nombre").getValue().toString());
                modelo.listTipoServicios.add(ser);
                mListener.getTipoServicio();

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




    public void updateFavorito(String estado, String key,String titulo){

        final DatabaseReference ref = database.getReference("cliente/"+modelo.uid+"/servicios/"+key+ "/estado/");//ruta path
        ref.setValue(estado);


        final DatabaseReference ref2 = database.getReference("cliente/"+modelo.uid+"/servicios/"+key+ "/titulo/");//ruta path
        ref2.setValue(titulo);

        mListener.actualizarFavorito();


    }

    public void updateStado(boolean estado){

        final DatabaseReference ref = database.getReference("cliente/"+modelo.uid+"/estado/");//ruta path
        ref.setValue(estado);

        mListener.actualizarFavorito();


    }


    public void servicoAceptadouidCliente(String uidCliente, String idServicio, String stado){

        final DatabaseReference ref = database.getReference("servicioclientes/"+idServicio+ "/uidCliente/");//ruta path
        ref.setValue(uidCliente);

        final DatabaseReference ref2 = database.getReference("servicioclientes/"+idServicio+ "/estado/");//ruta path
        ref2.setValue(stado);



        mListener.actualizarFavorito();


    }





    /**
     * Para evitar nullpointerExeptions
     */
    private static OnSercicioChangeListener sDummyCallbacks = new OnSercicioChangeListener()
    {

        @Override
        public void cargoServicio() {}

        @Override
        public void errorServicio() {}

        @Override
        public void actualizarFavorito() {}

        @Override
        public void getServicio() {}

        @Override
        public void getTipoServicio() {}

    };
}
