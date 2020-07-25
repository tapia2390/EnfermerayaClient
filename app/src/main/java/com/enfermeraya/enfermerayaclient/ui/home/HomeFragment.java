package com.enfermeraya.enfermerayaclient.ui.home;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.enfermeraya.enfermerayaclient.Alarm.AlarmNotificationReceiver;
import com.enfermeraya.enfermerayaclient.Alarm.AlarmToastReceiver;
import com.enfermeraya.enfermerayaclient.R;
import com.enfermeraya.enfermerayaclient.Service.Tiempo;
import com.enfermeraya.enfermerayaclient.adapter.ServicioAdapter2;
import com.enfermeraya.enfermerayaclient.app.Modelo;
import com.enfermeraya.enfermerayaclient.clases.Servicios;
import com.enfermeraya.enfermerayaclient.comandos.ComandoSercicio;
import com.enfermeraya.enfermerayaclient.models.utility.Utility;
import com.enfermeraya.enfermerayaclient.notificacion.Token;
import com.enfermeraya.enfermerayaclient.views.MainActivity;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;


import com.google.android.gms.maps.model.VisibleRegion;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import static com.facebook.FacebookSdk.getApplicationContext;


public class HomeFragment extends Fragment implements ComandoSercicio.OnSercicioChangeListener,OnMapReadyCallback,TimePickerDialog.OnTimeSetListener, android.app.TimePickerDialog.OnTimeSetListener {

    private HomeViewModel homeViewModel;
    Modelo modelo = Modelo.getInstance();
    Geocoder geocoder = null;
    private GoogleMap mMap;
    MarkerOptions markerOptions;
    LatLng startingPoint;
    Button btn_estado;
    LinearLayout containermap;
    SweetAlertDialog pDialog;
    LinearLayout layoutvacio, layoutmap;
    RelativeLayout listservice;
    RelativeLayout layoutcontent;
    RelativeLayout relativeopaciti;
    Date date;
    DateFormat hourFormat;
    ImageView imgopaciti;
    int hr = 0;
    int mn = 0;
    protected Handler handler;
    Tiempo tiemoService;
    Utility utility;
    ComandoSercicio comandoSercicio;
    List<Servicios> serList = new ArrayList<>();
    ServicioAdapter2 servicioAdapter;
    RecyclerView recyclerView;


    @SuppressLint("WrongViewCast")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        markerOptions = new MarkerOptions();
        mapFragment.getMapAsync(this::goolemapa);


        btn_estado = (Button) root.findViewById(R.id.btn_estado);
        containermap = (LinearLayout) root.findViewById(R.id.containermap);
        layoutvacio = (LinearLayout) root.findViewById(R.id.layoutvacio);
        layoutmap = (LinearLayout) root.findViewById(R.id.layoutmap);
        listservice = (RelativeLayout) root.findViewById(R.id.listservice);
        layoutcontent = (RelativeLayout) root.findViewById(R.id.layoutcontent);
        relativeopaciti = (RelativeLayout) root.findViewById(R.id.relativeopaciti);
        imgopaciti = (ImageView) root.findViewById(R.id.imgopaciti);
        recyclerView = root.findViewById(R.id.recycler_view2);


        modelo.vistaservice = 0;
        utility = new Utility();
        comandoSercicio = new ComandoSercicio(this);

        if (utility.estado(getActivity())) {
            comandoSercicio.getListServicio();
            mapa();
            stado();
            UpdateToken();

        } else {
            alerta("Sin Internet", "Valide la conexión a internet");
        }


        btn_estado.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"ResourceAsColor", "Range"})
            @Override
            public void onClick(View v) {


                if (btn_estado.getText().toString().equals("DISPONIBLE")) {


                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                    //mBuilder.setCancelable(false);
                    View mView = getLayoutInflater().inflate(R.layout.dialogactivacion, null);

                    Button btnahora = (Button) mView.findViewById(R.id.btnahora);
                    Button btnmastarde = (Button) mView.findViewById(R.id.btnmastarde);
                    Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
                    //layoutmap.startAnimation(animation);

                    mBuilder.setView(mView);
                    final AlertDialog dialog = mBuilder.create();
                    dialog.show();


                    btnahora.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            layoutvacio.setVisibility(View.GONE);
                            layoutmap.setVisibility(View.VISIBLE);
                            listservice.setVisibility(View.VISIBLE);
                            btn_estado.setText("DESCONECTAR");
                            hideDialog();
                            dialog.dismiss();
                            modelo.estado = 1;
                        }
                    });


                    btnmastarde.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            customTimePickerDialog();
                            hideDialog();
                            dialog.dismiss();


                            /*AlertDialog.Builder mBuildervacio = new AlertDialog.Builder(getActivity());
                            mBuildervacio.setCancelable(false);
                            View mViewvacio = getLayoutInflater().inflate(R.layout.dialovacio, null);
                            final AlertDialog dialogvacio = mBuildervacio.create();
                            dialogvacio.show();*/
                        }
                    });

                } else {

                    layoutvacio.setVisibility(View.VISIBLE);
                    layoutmap.setVisibility(View.GONE);
                    listservice.setVisibility(View.GONE);
                    btn_estado.setText("DISPONIBLE");
                    modelo.estado = 0;

                }

            }
        });


        date = new Date();
        hourFormat = new SimpleDateFormat("hh:mm aa");

        return root;
    }


    public void alertaEstado() {

       /* new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("")
                .setContentText("Esta en Inactivo")
                .setConfirmText("Pasar a activo")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        if(!btn_estado.getText().equals("Activo")){
                            btn_estado.setText("Activo");
                            btn_estado.setBackgroundResource(R.drawable.edittext_style);
                            btn_estado.setTextColor(R.color.colorNegro);
                           // btn_estado.setVisibility(View.VISIBLE);
                            modelo.activo = true;
                           // sDialog.dismissWithAnimation();
                        }else{

                            btn_estado.setText("Inactivo");
                            btn_estado.setBackgroundResource(R.drawable.gris_post_border_style);
                            btn_estado.setTextColor(R.color.colorBlanco);
                            // btn_estado.setVisibility(View.VISIBLE);
                            modelo.activo = true;
                            // sDialog.dismissWithAnimation();
                        }


                    }
                })

                .show();*/
    }


    private void hideDialog() {
        if (pDialog != null)
            pDialog.dismiss();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        modelo.mMap = googleMap;
        goolemapa(modelo.mMap);


    }


    public void goolemapa(GoogleMap mMap) {
        modelo.mMap = mMap;
        //5.059288, -75.497652
        LatLng ctg = new LatLng(modelo.latitud, modelo.longitud);// colombia
        CameraPosition possiCameraPosition = new CameraPosition.Builder().target(ctg).zoom(15).bearing(0).tilt(0).build();
        CameraUpdate cam3 =
                CameraUpdateFactory.newCameraPosition(possiCameraPosition);
        mMap.animateCamera(cam3);
        // mMap.addMarker(new MarkerOptions().position(ctg).title("Mi ubicación"));

        // float verde = BitmapDescriptorFactory.HUE_GREEN;
        //marcadorColor(modelo.latitud, modelo.longitud,"Pais Colombia", verde,mMap);
        marcadorImg(modelo.latitud, modelo.longitud, "Pais Colombia", mMap);
        //setLocation();

    }

    private void marcadorColor(double lat, double lng, String pais, float color, GoogleMap mMap) {
        mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(pais).icon(BitmapDescriptorFactory.defaultMarker(color)));
    }

    private void marcadorImg(double lat, double lng, String pais, GoogleMap mMap) {
        //modelo.mMap = mMap;
        //   MarkerOptions marker = new MarkerOptions().position(new LatLng(lat, lng)).title("Enfermera");

        LatLng latLng = new LatLng(lat, lng);

        modelo.mMap.clear();
        modelo.mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lng))
                .title("Enfermeraya")
                .snippet(getCity(latLng))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pingmapa))
                .draggable(true)
        );


        if (modelo.mMap != null) {
            modelo.mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {
                    Log.v("1", "1");
                }

                @Override
                public void onMarkerDrag(Marker marker) {
                    Log.v("2", "2");
                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    Log.v("3", "3");
                    getCity(marker.getPosition());
                }
            });

            modelo.mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    //Toast.makeText(getContext(),"click", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });


            modelo.mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    // Toast.makeText(getActivity(), "datos"+latLng, Toast.LENGTH_SHORT).show();
                    modelo.latitud = latLng.latitude;
                    modelo.longitud = latLng.longitude;
                    goolemapa(modelo.mMap);
                }
            });
        }

    }

    //GPS
    public void mapa() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }
    }

    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        HomeFragment.Localizacion Local = new HomeFragment.Localizacion();
        Local.setMainActivity(HomeFragment.this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);
        Toast.makeText(getActivity(), "Localización agregada", Toast.LENGTH_SHORT).show();

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }

    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {

                try {
                    if (geocoder == null) {
                        geocoder = new Geocoder(getActivity(), Locale.getDefault());
                    }


                } catch (Exception e) {
                    String ex = e.getMessage();
                }

                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);


                    String dir = DirCalle.getAddressLine(0);

                    // Toast.makeText(getApplicationContext(),"Mi direccion es "+ dir, Toast.LENGTH_SHORT).show();
                    String[] parts = dir.split(",");
                    String direc = parts[0];

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public String getCity(LatLng posicion) {

        Geocoder geocoder;
        List<Address> addresses;
        String dir = "";

        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        try {
            List<Address> list = geocoder.getFromLocation(
                    posicion.latitude, posicion.longitude, 1);
            if (!list.isEmpty()) {
                Address DirCalle = list.get(0);


                dir = DirCalle.getAddressLine(0);

                // Toast.makeText(getApplicationContext(),"Mi direccion es "+ dir, Toast.LENGTH_SHORT).show();
                String[] parts = dir.split(",");
                String direc = parts[0];


            }
        } catch (Exception e) {
            String ex = e.getMessage();
            e.printStackTrace();
        }
        return dir;
    }

    @Override
    public void getTipoServicio() {

    }

    @Override
    public void getServicio() {
        serList = modelo.listServicios;
        servicioAdapter = new ServicioAdapter2(getActivity(), serList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(servicioAdapter);
    }

    @Override
    public void cargoServicio() {

    }

    @Override
    public void errorServicio() {

    }

    @Override
    public void actualizarFavorito() {

    }


    /* Aqui empieza la Clase Localizacion */
    public class Localizacion implements LocationListener {
        HomeFragment mainActivity;

        public HomeFragment getMainActivity() {
            return mainActivity;
        }

        public void setMainActivity(HomeFragment mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion
            loc.getLatitude();
            loc.getLongitude();


            modelo.latitud = loc.getLatitude();
            modelo.longitud = loc.getLongitude();

            // this.mainActivity.setLocation(loc);
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado

            Toast.makeText(getActivity().getApplicationContext(), "GPS Desactivado", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado

            Toast.makeText(getActivity().getApplicationContext(), "GPS Activado", Toast.LENGTH_SHORT).show();
            goolemapa(modelo.mMap);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }


    //fin gps


    public void alerta(String titulo, String descripcion) {
        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(titulo)
                .setContentText(descripcion)
                .setConfirmText("Aceptar")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        // reuse previous dialog instance

                        sDialog.hide();
                    }
                })
                .show();
    }


    //__methode will be call when we click on "Custom Date Picker Dialog" and will be show the custom date selection dilog.
    public void customTimePickerDialog() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog dpd = TimePickerDialog.newInstance(this, now.get(Calendar.HOUR), now.get(Calendar.MINUTE), false);

        dpd.setAccentColor(getResources().getColor(R.color.colorVerdeOscuro));
        dpd.setMinTime(now.get(Calendar.HOUR), now.get(Calendar.MINUTE), now.get(Calendar.SECOND));
        dpd.show(getActivity().getFragmentManager(), "Timepickerdialog");


        hr = now.get(Calendar.HOUR);
        mn = now.get(Calendar.MINUTE);


        dpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                //Toast.makeText(getActivity(),"hola", Toast.LENGTH_LONG).show();
            }
        });


    }


    //___this is the listener callback method will be call on time selection by default date picker.
    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        //Toast.makeText(this, "Selected by default time picker : " + hourOfDay + ":" + minute, Toast.LENGTH_SHORT).show();

        Calendar datetime = Calendar.getInstance();
        datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        datetime.set(Calendar.MINUTE, minute);
        date = datetime.getTime();

        //Toast.makeText(this, "Selected by custom time picker : " + hourOfDay + ":" + minute, Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(),""+hourFormat.format(date), Toast.LENGTH_LONG).show();

        //txtfecha.setText(""+hourFormat.format(date));

    }


    //___this is the listener callback method will be call on time selection by custom date picker.
    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

        Calendar datetime = Calendar.getInstance();
        datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        datetime.set(Calendar.MINUTE, minute);
        date = datetime.getTime();

        String string = "" + hourFormat.format(date);
        String[] parts = string.split(" ");
        String part1 = parts[0];
        String part2 = parts[1];

        if (part2.equals("a.") || part2.equals("a.m.") || part2.equals("a. m.") || part2.equals("A.")) {
            part2 = "AM";
        }
        if (part2.equals("p.") || part2.equals("p.m.") || part2.equals("p. m.") || part2.equals("P.")) {
            part2 = "PM";
        }

        int totalmin = 0;
        int hora = datetime.get(Calendar.HOUR);
        int min = datetime.get(Calendar.MINUTE);

        int year = datetime.get(Calendar.YEAR);
        int mes = datetime.get(Calendar.MONTH) + 1;
        int dia = datetime.get(Calendar.DAY_OF_MONTH);


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");
        Date fechaI = null, fechaF = null;
        try {
            String f1 = dia + "/" + mes + "/" + year + " " + hora + ":" + min + ":00";
            String f2 = dia + "/" + mes + "/" + year + " " + hr + ":" + mn + ":00";
            fechaI = simpleDateFormat.parse(f1);
            //fechaF puede ser la fecha actual o tu puedes asignarala,
            //por ejemplo: fechaF = simpleDateFormat.parse("2/6/2016 15:40:42");
            // fechaF = new Date(System.currentTimeMillis());
            fechaF = simpleDateFormat.parse(f2);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        String tiempo = getDiferencia(fechaF, fechaI);
        int time = Integer.parseInt(tiempo);
        if (time <= 0) {

            Toast.makeText(getActivity(), "La diferencia debe ser minimo 1 minuto", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getActivity(), part1 + " " + part2, Toast.LENGTH_LONG).show();
            relativeopaciti.setVisibility(View.VISIBLE);
            layoutmap.setVisibility(View.VISIBLE);
            listservice.setVisibility(View.VISIBLE);
            layoutvacio.setVisibility(View.GONE);
            btn_estado.setVisibility(View.GONE);

            modelo.tiempo = time;



               /* Log.v("Diferencia", horaresta+"-"+minresta);
                if(totalmin > 1){
                    Toast.makeText(getActivity(),"se activara en: " + totalmin+ " minutos", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getActivity(),"se activara en: " + totalmin+ " minuto", Toast.LENGTH_LONG).show();
                }*/


            tiemoService = new Tiempo(getActivity(), time);
            tiemoService.timerTiempo();
            modelo.estado = 2;

               /* new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //visible();
                    }
                },(time *1000)*100);*/


            // startAlarm(false, false, time);
        }


        //modelo.mMap.setOnMarkerDragListener(null);
        // modelo.mMap.setOnMapClickListener(null);


    }


    private void startAlarm(boolean isNotification, boolean isRepeat, int tiempo) {
        AlarmManager manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent myIntent;
        PendingIntent pendingIntent;

        if (!isNotification) {
            myIntent = new Intent(getActivity(), AlarmToastReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, myIntent, 0);
        } else {
            myIntent = new Intent(getActivity(), AlarmNotificationReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, myIntent, 0);
        }

        if (!isRepeat) {
            manager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + tiempo * (60 * 1000), pendingIntent);

        } else {
            manager.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + 3000, 60 * 1000, pendingIntent);
        }
    }


    public String getDiferencia(Date fechaInicial, Date fechaFinal) {

        long diferencia = fechaFinal.getTime() - fechaInicial.getTime();

        Log.i("MainActivity", "fechaInicial : " + fechaInicial);
        Log.i("MainActivity", "fechaFinal : " + fechaFinal);

        long segsMilli = 1000;
        long minsMilli = segsMilli * 60;
        long horasMilli = minsMilli * 60;
        long diasMilli = horasMilli * 24;

        long diasTranscurridos = diferencia / diasMilli;
        diferencia = diferencia % diasMilli;

        long horasTranscurridos = diferencia / horasMilli;
        diferencia = diferencia % horasMilli;

        long minutosTranscurridos = diferencia / minsMilli;
        diferencia = diferencia % minsMilli;

        long segsTranscurridos = diferencia / segsMilli;

       /* return "diasTranscurridos: " + diasTranscurridos + " , horasTranscurridos: " + horasTranscurridos +
                " , minutosTranscurridos: " + minutosTranscurridos + " , segsTranscurridos: " + segsTranscurridos;*/

        return minutosTranscurridos + "";


    }


    public void visible() {
        Toast.makeText(getActivity(), "Activado", Toast.LENGTH_LONG).show();
        relativeopaciti.setVisibility(View.GONE);
        btn_estado.setText("DESCONECTAR");
        btn_estado.setVisibility(View.VISIBLE);

    }


    public void stado() {

        if (modelo.estado == 1) {
            layoutvacio.setVisibility(View.GONE);
            layoutmap.setVisibility(View.VISIBLE);
            listservice.setVisibility(View.VISIBLE);
            btn_estado.setText("DESCONECTAR");
        } else if (modelo.estado == 2) {
            relativeopaciti.setVisibility(View.VISIBLE);
            layoutmap.setVisibility(View.VISIBLE);
            listservice.setVisibility(View.VISIBLE);
            layoutvacio.setVisibility(View.GONE);
            btn_estado.setVisibility(View.GONE);
        } else {
            Toast.makeText(getActivity(), "Estado inactivo", Toast.LENGTH_LONG).show();
        }

    }

    private void UpdateToken(){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        Token token= new Token(refreshToken);
        comandoSercicio.updateToken(refreshToken);
        //FirebaseDatabase.getInstance().getReference("cliente/"+modelo.uid+"/tokem").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);
    }

}
