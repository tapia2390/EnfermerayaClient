package com.enfermeraya.enfermerayaclient.clases;

public class Usuario {

    private  String key;
    private  String nombre;
    private  String apellido;
    private  String celular;
    private  String correo;
    private  String pasString;
    private  String foto;
    private  String token;
    private  String direccion;
    private  double latitud;
    private  double longitud;
    private double distancia;
    private double tiempo;
    private  boolean estado;
    private  String idculqi;
    private String codigo;

    public Usuario(){}

    public Usuario(String key, String nombre, String apellido, String celular, String correo, String pasString, String foto, String token, String direccion, double latitud, double longitud, double distancia, double tiempo, boolean estado,String idculqi, String codigo) {
        this.key = key;
        this.nombre = nombre;
        this.apellido = apellido;
        this.celular = celular;
        this.correo = correo;
        this.pasString = pasString;
        this.foto = foto;
        this.token = token;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.distancia = distancia;
        this.tiempo = tiempo;
        this.estado = estado;
        this.idculqi =  idculqi;
        this.codigo= codigo;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPasString() {
        return pasString;
    }

    public void setPasString(String pasString) {
        this.pasString = pasString;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public double getTiempo() {
        return tiempo;
    }

    public void setTiempo(double tiempo) {
        this.tiempo = tiempo;
    }



    public boolean getEstado() {
        return estado;
    }

    public void setTEstado(boolean tiempo) {
        this.estado = estado;
    }


    public String getIdculqi() {
        return idculqi;
    }

    public void setIdculqi(String idculqi) {
        this.idculqi = idculqi;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
