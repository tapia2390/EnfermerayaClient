package com.enfermeraya.enfermerayaclient.app;


import com.enfermeraya.enfermerayaclient.clases.ClassTerminosYCondiciones;
import com.enfermeraya.enfermerayaclient.clases.Favoritos;
import com.enfermeraya.enfermerayaclient.clases.Servicios;
import com.enfermeraya.enfermerayaclient.clases.TipoServicio;
import com.enfermeraya.enfermerayaclient.clases.Usuario;
import com.google.android.gms.maps.GoogleMap;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Modelo {
    public static final Modelo ourInstance = new Modelo();
    public String uid = "";
    public String tipo ="";
    public String terminosycondiciones = "";
    public double latitud;
    public double longitud;
    public GoogleMap mMap;

    public static Modelo getInstance() {
        return ourInstance;
    }

    public Modelo() {
    }

    public ClassTerminosYCondiciones classTerminosYCondiciones = new ClassTerminosYCondiciones();

    public ArrayList<Favoritos> listFavoritos = new ArrayList<Favoritos>();
    public ArrayList<Servicios> listServicios = new ArrayList<Servicios>();
    public ArrayList<TipoServicio> listTipoServicios = new ArrayList<TipoServicio>();
    public  Favoritos favoritos = new Favoritos();
    public Servicios servicios =  new Servicios();

    public Usuario usuario = new Usuario();
    public String tipoLogin = "";
    public String modal = "servicios";



    //version app
    public String versionapp ="";
    public DecimalFormat decimales = new DecimalFormat("#.##");

    //datos date

    //Date date = new Date();
    //Caso 1: obtener la hora y salida por pantalla con formato:
    public DateFormat hourFormat = new SimpleDateFormat("hh:mm a");
    //System.out.println("Hora: "+hourFormat.format(date));
    //Caso 2: obtener la fecha y salida por pantalla con formato:
    public DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    //System.out.println("Fecha: "+dateFormat.format(date));
    //Caso 3: obtenerhora y fecha y salida por pantalla con formato:
    public DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
    //System.out.println("Hora y fecha: "+hourdateFormat.format(date));



    //Devuelve true si la cadena que llega es un numero decimal, false en caso contrario
    public boolean esDecimal(String cad)
    {
        try
        {
            Double.parseDouble(cad);
            return true;
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
    }



}
