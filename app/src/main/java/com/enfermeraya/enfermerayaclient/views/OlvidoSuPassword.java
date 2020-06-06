package com.enfermeraya.enfermerayaclient.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.enfermeraya.enfermerayaclient.R;
import com.enfermeraya.enfermerayaclient.Splash;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class OlvidoSuPassword extends Activity {

    EditText corrreo;
    ProgressBar progressBar4;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_olvido_su_password);

        if (savedInstanceState != null){
            Intent i = new Intent(getApplicationContext(), Splash.class);
            startActivity(i);
            finish();
            return;
        }

        corrreo =(EditText)findViewById(R.id.corrreo1);
        progressBar4 =(ProgressBar)findViewById(R.id.progressBar4);

    }




    public void  atras(View v){
        onBackPressed();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void resetPassword(View v){
        if(corrreo.getText().toString().equals("")){
            Toast.makeText(OlvidoSuPassword.this, "Ingrese su correo electrónico", Toast.LENGTH_SHORT).show();
        }else {
            progressBar4.setVisibility(View.VISIBLE);
            String emailId = corrreo.getText().toString();
            mAuth.sendPasswordResetEmail(emailId)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(OlvidoSuPassword.this, "Las instrucciones han sido enviados a su correo electrónico", Toast.LENGTH_SHORT).show();
                                progressBar4.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(OlvidoSuPassword.this, "Datos incorrectos.", Toast.LENGTH_SHORT).show();
                                progressBar4.setVisibility(View.GONE);
                            }

                        }
                    });
        }
    }




}