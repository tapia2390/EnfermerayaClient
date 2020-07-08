package com.enfermeraya.enfermerayaclient.comandos;


import android.util.Log;

import androidx.annotation.NonNull;

import com.enfermeraya.enfermerayaclient.app.Modelo;
import com.enfermeraya.enfermerayaclient.clases.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tacto on 4/10/17.
 */

public class ComandoValidarCorreoFirebase {

    Modelo modelo = Modelo.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference referencia = database.getReference();



    //interface del listener de la actividad interesada
    private OnValidarCorreoFirebaseChangeListener mListener;

    /**
     * Interfaz para avisar de eventos a los interesados
     */
    public interface OnValidarCorreoFirebaseChangeListener {

        void cargoValidarCorreoFirebase();
        void cargoValidarCorreoFirebaseEroor();

        void setUsuarioListener();
        void errorSetUsuario();
        void errorCreacionUsuario();

    }

    public ComandoValidarCorreoFirebase(OnValidarCorreoFirebaseChangeListener mListener){

        this.mListener = mListener;

    }

    public  void registroUsuario(Usuario u){
               //creating a new user
        //creating a new user


        mAuth.createUserWithEmailAndPassword(u.getCorreo(), u.getPasString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            String uid = mAuth.getCurrentUser().getUid();

                            final DatabaseReference ref = database.getReference("cliente/"+uid+"/");//ruta path

                            Map<String, Object> enviarRegistroUsuario = new HashMap<String, Object>();

                            enviarRegistroUsuario.put("nombre", u.getNombre());
                            enviarRegistroUsuario.put("apellido", u.getApellido());
                            enviarRegistroUsuario.put("celular", u.getCelular());
                            enviarRegistroUsuario.put("correo", u.getCorreo());
                            enviarRegistroUsuario.put("foto", u.getFoto());
                            enviarRegistroUsuario.put("tokem", u.getToken());
                            enviarRegistroUsuario.put("direccion", "");
                            enviarRegistroUsuario.put("lat", u.getLatitud());
                            enviarRegistroUsuario.put("long", u.getLongitud());

                            ref.setValue(enviarRegistroUsuario, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (databaseError == null) {

                                        mListener.setUsuarioListener();
                                        return;
                                    } else {
                                        mListener.errorSetUsuario();
                                    }
                                }
                            });


                        }else{
                            mListener.errorCreacionUsuario();

                        }

                    }
                });

    }

    public void checkAccountEmailExistInFirebase(String email) {
               mAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if (task.isSuccessful()) {
                            SignInMethodQueryResult result = task.getResult();
                            List<String> signInMethods = result.getSignInMethods();

                            //if (task.getResult().getProviders().size() > 0)
                            if (signInMethods.size() > 0) {
                                mListener.cargoValidarCorreoFirebaseEroor();
                            }else{
                                mListener.cargoValidarCorreoFirebase();
                            }

                        } else {
                            Log.e("Error", "Error getting sign in methods for user", task.getException());
                        }
                    }
                });
    }


    /**
     * Para evitar nullpointerExeptions
     */
    private static OnValidarCorreoFirebaseChangeListener sDummyCallbacks = new OnValidarCorreoFirebaseChangeListener()
    {
        @Override
        public void cargoValidarCorreoFirebase()
        {

        }

        @Override
        public void cargoValidarCorreoFirebaseEroor()
        {}

        @Override
        public void setUsuarioListener()
        {}

        @Override
        public void errorSetUsuario()
        {}

        @Override
        public void errorCreacionUsuario()
        {}

    };
}
