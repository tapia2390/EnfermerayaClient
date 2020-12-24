package com.enfermeraya.enfermerayaclient.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.enfermeraya.enfermerayaclient.R;
import com.enfermeraya.enfermerayaclient.Splash;
import com.enfermeraya.enfermerayaclient.app.Modelo;
import com.enfermeraya.enfermerayaclient.clases.Usuario;
import com.enfermeraya.enfermerayaclient.comandos.ComandoPerfil;
import com.enfermeraya.enfermerayaclient.models.utility.Utility;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.enfermeraya.enfermerayaclient.views.Registrarse.decodeBase64;

public class MenuLateral extends AppCompatActivity  implements ComandoPerfil.OnPerfilChangeListener{

    private AppBarConfiguration mAppBarConfiguration;
    ImageView camara1;
    TextView txtnombre;
    TextView txt_calificacion;
    Utility utility;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ComandoPerfil comandoPerfil;
    Usuario usuario;
    Modelo modelo = Modelo.getInstance();
    SweetAlertDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_menu_lateral);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState != null){
            Intent i = new Intent(getApplicationContext(), Splash.class);
            startActivity(i);
            finish();
            return;
        }

        //instanciamos la clase utility
        utility = new Utility();
        usuario =  null;

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_mi_cuenta, R.id.nav_mis_servicios,  R.id.nav_historial, R.id.nav_recomienda, R.id.nav_ayuda)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);



        // Obtiene una referencia al TextView del otro layout, other.xml
        camara1 = (ImageView)findViewById(R.id.imageView);
        txtnombre =  (TextView)findViewById(R.id.txtnombreperfil);
        txt_calificacion = (TextView)findViewById(R.id.txt_calificacion);

        comandoPerfil =  new ComandoPerfil(this);

        if (utility.estado(getApplicationContext())) {


            Log.v("User" ,  ""+user.getDisplayName());

            if(modelo.tipoLogin.equals("normal")){
                loadswet("Cargando la información...");
                comandoPerfil.getUsuario();
            }else{
                String nombre  = ""+user.getDisplayName();
                txtnombre.setText(""+nombre);
                txt_calificacion.setText("3.7");

                //Bitmap bitmap = BitmapFactory.decodeFile(user.getPhotoUrl());

                try {

                    Glide.with(getApplicationContext())
                            .load(user.getPhotoUrl())
                            .apply(RequestOptions.circleCropTransform())
                            .into(camara1);


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }else {
            alerta("Sin Internet","Valide la conexión a internet");
        }

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lateral, menu);
        return true;
    }*/

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    //posgres dialos sweetalert

    public void loadswet(String text){

        try {
            pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText(text);
            pDialog.setCancelable(false);
            pDialog.show();

        }catch (Exception e){

        }

    }


    //oculatomos el dialog
    private void hideDialog() {
        if (pDialog != null)
            pDialog.dismiss();
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


    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void cargoUSuario() {

        hideDialog();
        txtnombre.setText("" + modelo.usuario.getNombre());
        if(modelo.usuario.getEstado()){
            modelo.estado = 1;
        }else{
            modelo.estado = 3;
        }
        txt_calificacion.setText("3.5");
        if (!modelo.usuario.getFoto().equals("")) {
            camara1.setImageBitmap(getCircularBitmap(decodeBase64(modelo.usuario.getFoto())));
        }

    }

    @Override
    public void setUsuarioListener() {
        hideDialog();

        alerta("Actualización","Datos Actualizados");
    }

    @Override
    public void errorSetUsuario() {
        hideDialog();

        alerta("Eror","Error con el regitro");
    }


    //imagen circular
    public static Bitmap getCircularBitmap(Bitmap bitmap) {
        Bitmap output;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            output = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        } else {
            output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        float r = 0;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            r = bitmap.getHeight() / 2;
        } else {
            r = bitmap.getWidth() / 2;
        }

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(r, r, r, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    //atras hadware
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {
            if(!modelo.activo){
                return false;
            }
            onBackPressed();
        }
        return true;
    }


}
