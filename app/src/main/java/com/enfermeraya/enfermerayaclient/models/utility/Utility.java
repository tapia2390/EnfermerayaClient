package com.enfermeraya.enfermerayaclient.models.utility;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Patterns;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;

public class Utility {


    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final int MY_PERMISSIONS_REQUEST_CallPhone= 0;

   /*validar conexión a inernet*/

    //validacion conecion

   public Boolean estado(Context context){
       boolean res = false;
       if (conectadoWifi(context) == true) {
           res = true;
       }else if(conectadoRedMovil(context)){
           res =true;
       }
       return res;

    }


    //conexion wifi
    protected Boolean conectadoWifi(Context context){

        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    //conexion movil
    protected Boolean conectadoRedMovil(Context context){
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    //validacion conecion a internet





    //validar correo
    public boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    public static String convertToMoney(String valor) {

        String temp = "$";

        try {
            Locale colombia = new Locale("es", "CO");
            NumberFormat defaultFormat = NumberFormat.getCurrencyInstance(colombia);
            defaultFormat.setMaximumFractionDigits(0);
            temp = defaultFormat.format(Integer.valueOf(valor));
            temp = temp.replace(',', '.');


        } catch (RuntimeException r) {
            Log.i("MONEY", r.getLocalizedMessage());
            Log.i("MONEY", r.getMessage());
            return "$";


        }
        return temp;

    }


    public static Date convertStringConHoraToDate(String fecha) {
        SimpleDateFormat sdfr = new SimpleDateFormat("dd/MM/yyyy h:mm a", Locale.ENGLISH);
        try {
            Date f = (Date) sdfr.parse(fecha);
            return f;
        } catch (Exception e) {
            return null;

        }
    }


    public static String convertToDistancia(float distancia) {

        int metros = (int) distancia;

        if (metros < 1000) {
            return metros + " m";
        } else {
            String res = String.format("%.1f kms", distancia / 1000);
            return res;
        }

    }


    public static String convertToMoney(int valor) {

        String temp = "$";
        try {
            Locale colombia = new Locale("es", "CO");
            NumberFormat defaultFormat = NumberFormat.getCurrencyInstance(colombia);
            defaultFormat.setMaximumFractionDigits(0);
            temp = defaultFormat.format(valor);
            temp = temp.replace(',', '.');


        } catch (RuntimeException r) {
            Log.i("MONEY", r.getLocalizedMessage());
            Log.i("MONEY", r.getMessage());
            return "$";

        }
        return temp;

    }


    public static Date convertStringToDate(String fecha) {
        Date f = null;
        SimpleDateFormat sdfr = new SimpleDateFormat("dd/MM/yyyy");
        try {
             f = (Date) sdfr.parse(fecha);
            
        } catch (Exception e) {

        }
        return f;
    }





    public static String convertDateToFechayHora(Date indate) {
        String fecha = null;
        DateFormat sdfr = new SimpleDateFormat("dd/MM/yyyy h:mm a");

        try {
            fecha = sdfr.format(indate);
            fecha = fecha.replace("a. m.", "AM");
            fecha = fecha.replace("p. m.", "PM");
            fecha = fecha.replace("a.m.", "AM");
            fecha = fecha.replace("p.m.", "PM");
            fecha = fecha.replace("am", "AM");
            fecha = fecha.replace("pm", "PM");

        } catch (Exception ex) {
            System.out.println(ex);
        }
        return fecha;
    }


    public static String convertDateToString(Date indate) {
        String dateString = null;
        SimpleDateFormat sdfr = new SimpleDateFormat("dd/MM/yyyy");

        try {
            dateString = sdfr.format(indate);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return dateString;
    }


    public static Calendar convertDateToCalendar(Date fecha) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        return cal;
    }





    public static String llenarCeros(String inicial, int numero) {

        String snum = numero + "";
        while (snum.length() < 6) {
            snum = "0" + snum;
        }

        return inicial + snum;
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public static boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }


    public static boolean LunCheck(String ccNumber) {
        int sum = 0;
        boolean alternate = false;
        for (int i = ccNumber.length() - 1; i >= 0; i--) {
            int n = 0;
            try {
                n = Integer.parseInt(ccNumber.substring(i, i + 1));
            } catch (Exception e) {
                return false;
            }
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }


    public static int calcularMesesEntresFechas(Date fechaInicio, Date fechaFin) {
        try {
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(fechaInicio);
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(fechaFin);
            int startMes = (startCalendar.get(Calendar.YEAR) * 12) + startCalendar.get(Calendar.MONTH);
            int endMes = (endCalendar.get(Calendar.YEAR) * 12) + endCalendar.get(Calendar.MONTH);
            int diffMonth = endMes - startMes;
            return diffMonth;
        } catch (Exception e) {
            return 0;
        }
    }



    public static String getVersionParaUsuario(Context context) {
        PackageInfo pInfo;

        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return "Versión " + pInfo.versionName + " (" + pInfo.versionCode + ")";
        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();
        }

        return "";


    }


    public static int calcularMinutosEntresFechas(Date fechaInicio, Date fechaFin) {

        long fin = fechaFin.getTime();
        long ini = fechaInicio.getTime();


        int  diff  = (int) Math.abs((( fin -  ini) / 1000 ) / 60 );

        return diff;

    }



    //permiso para hacer llamadas
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermissionCall(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CALL_PHONE)) {

                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Call phone permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CallPhone);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();


                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CallPhone);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }





}
