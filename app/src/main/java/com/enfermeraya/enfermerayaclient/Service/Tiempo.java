package com.enfermeraya.enfermerayaclient.Service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.enfermeraya.enfermerayaclient.R;
import com.enfermeraya.enfermerayaclient.app.Modelo;
import com.enfermeraya.enfermerayaclient.comandos.ComandoSercicio;
import com.enfermeraya.enfermerayaclient.comandos.ComandoService;
import com.enfermeraya.enfermerayaclient.ui.home.HomeFragment;
import com.enfermeraya.enfermerayaclient.views.MainActivity;
import com.enfermeraya.enfermerayaclient.views.MenuLateral;

public class Tiempo extends Service {

    FragmentActivity activity;
    int time;

    Modelo modelo = Modelo.getInstance();
    public Tiempo(FragmentActivity activity, int time) {
        this.activity =  activity;
        this.time = time;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(activity,"Servicio iniciado",Toast.LENGTH_LONG).show();



    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void timerTiempo() {
        Toast.makeText(activity," timer",Toast.LENGTH_LONG).show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // acciones que se ejecutan tras los milisegundos
                Toast.makeText(activity," visible",Toast.LENGTH_LONG).show();
                modelo.estado = 1;
                setPreference(modelo.estado);
            }
        }, (time*60)*100);
    }


    public void setPreference(int estadoActivo){
        SharedPreferences.Editor editor = activity.getSharedPreferences("stadoactivo", MODE_PRIVATE).edit();
        editor.putInt("estadoActivo", estadoActivo);
        editor.apply();
    }


}
