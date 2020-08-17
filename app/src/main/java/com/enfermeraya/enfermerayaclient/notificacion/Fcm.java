package com.enfermeraya.enfermerayaclient.notificacion;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.enfermeraya.enfermerayaclient.R;
import com.enfermeraya.enfermerayaclient.app.Modelo;
import com.enfermeraya.enfermerayaclient.views.MainActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;



public class Fcm extends FirebaseMessagingService {

    Modelo modelo = Modelo.getInstance();
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.v("Token",s);
        guardarToken(s);
    }

    private void  guardarToken(String s) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("usuario");
        ref.child(modelo.uid+"/token/").setValue(s);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String from = remoteMessage.getFrom();
        Log.v("TAG","El mensaje recivido es:"+ from);
        if (remoteMessage.getNotification() != null){
            Log.v("TAG","El titulo es:"+ remoteMessage.getNotification().getTitle());
            Log.v("TAG","El titulo es:"+ remoteMessage.getNotification().getBody());
        }

        if (remoteMessage.getData().size() > 0){


            String datamsms = remoteMessage.getFrom()+"\n"+
                    remoteMessage.getTo()+"\n"+
                    remoteMessage.getData()+"\n";

            Log.v("TAG","El titulo es:"+ remoteMessage.getData().get("Title"));
            Log.v("TAG","El Detalle es:"+ remoteMessage.getData().get("Message"));
            Log.v("TAG","El Color es:"+ remoteMessage.getData().get("color"));

            String titulo = remoteMessage.getData().get("Title");
            String detail = remoteMessage.getData().get("Message");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                notificacion(titulo,detail);
            }

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
                // notificacion2();
                notificacion(titulo,detail);
            }

        }
    }

    private void notificacion2() {
        // notificacion2();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private  void notificacion(String titulo, String detail){
        String id ="mensaje";
        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,id);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel nc = new NotificationChannel(id,"nuevo", NotificationManager.IMPORTANCE_HIGH);
            nc.setShowBadge(true);
            assert  nm != null;
            nm.createNotificationChannel(nc);
        }

        builder.setAutoCancel(true).
                setWhen(System.currentTimeMillis()).
                setContentTitle(titulo).
                setSmallIcon(R.mipmap.ic_launcher).
                setContentText(detail).
                setContentIntent(clicknoti()).
                setContentInfo("Nuevo");
        Random random = new Random();
        int idNoty = random.nextInt(8000);
        assert  nm != null;
        nm.notify(idNoty,builder.build());
    }

    public PendingIntent clicknoti(){
        Intent  nf = new Intent(getApplicationContext(), MainActivity.class);
        nf.putExtra("color", "rojo");
        nf.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(this,0, nf,0);
    }
}
