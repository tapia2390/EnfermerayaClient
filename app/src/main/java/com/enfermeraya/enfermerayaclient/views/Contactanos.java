package com.enfermeraya.enfermerayaclient.views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.enfermeraya.enfermerayaclient.R;
import com.enfermeraya.enfermerayaclient.app.Modelo;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Contactanos extends   Activity {

    private EditText asunto, mensaje;
    Modelo modelo = Modelo.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_contactanos);

        asunto = (EditText)findViewById(R.id.asunto);
        mensaje = (EditText)findViewById(R.id.mensaje);
        //enviarEmail(asunto.getText().toString(),mensaje.getText().toString());
    }


    public void correo(View v){
        if(asunto.getText().toString().equals("")){
            asunto.setError("Ingrese el  asunto.");
            return;
        }

        if(mensaje.getText().toString().equals("")){
            asunto.setError("Ingrese el  mensaje.");
            return;
        }

        enviarEmail(asunto.getText().toString(), mensaje.getText().toString());
    }

    private void enviarEmail(String asunto, String mensaje){
        //Instanciamos un Intent del tipo ACTION_SEND
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        //Aqui definimos la tipologia de datos del contenido dle Email en este caso text/html
        emailIntent.setType("text/html");
        // Indicamos con un Array de tipo String las direcciones de correo a las cuales enviar
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"yhonatanasweb@gmail.com"});
        // Aqui definimos un titulo para el Email
        emailIntent.putExtra(android.content.Intent.EXTRA_TITLE, "Enfermeraya");
        // Aqui definimos un Asunto para el Email
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, asunto);
        // Aqui obtenemos la referencia al texto y lo pasamos al Email Intent
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, mensaje);
        try {
            //Enviamos el Correo iniciando una nueva Activity con el emailIntent.
            startActivity(Intent.createChooser(emailIntent, "Enviar Correo..."));
            alertaOK("Correo","Correo eviado con éxito.");
        } catch (android.content.ActivityNotFoundException ex) {
            alerta("Correo","No hay ningún cliente de correo instalado...");
            Toast.makeText(getApplicationContext(), "No hay ningún cliente de correo instalado.", Toast.LENGTH_SHORT).show();
        }
    }


    //alerta swit alert
    //alerta
    public void alerta(String titulo,String decripcion){

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(titulo)
                .setContentText(decripcion)
                .setConfirmText("Aceptar")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {


                        sDialog.dismissWithAnimation();

                    }
                })

                .show();
    }

    //alerta
    public void alertaOK(String titulo,String decripcion){

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(titulo)
                .setContentText(decripcion)
                .setConfirmText("Aceptar")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        asunto.setText("");
                        mensaje.setText("");

                    }
                })

                .show();
    }

    public  void  facebook(View v){

        openWebPage(modelo.setting.getUrl_face());

    }

    public  void  twitter(View v){

        openWebPage(modelo.setting.getUrl_twitter());

    }


    public  void  linkedln(View v){
        openWebPage(modelo.setting.getUrl_in());

    }


    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

}