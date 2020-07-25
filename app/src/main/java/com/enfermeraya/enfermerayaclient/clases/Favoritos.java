package com.enfermeraya.enfermerayaclient.clases;

public class Favoritos {
    String key;
    String nombre;
    String tipoServicio;
    String fecha;
    String horaInicio;
    String horaFin;
    String informacion;
    String obsciones;
    String direccion;
    double latitud;
    double longitud;
    String titulo;
    String estado;
    long timestamp;
    String horaServicio;

    public Favoritos(){}


    public Favoritos(String key, String nombre, String tipoServicio, String fecha, String horaInicio, String horaFin, String informacion, String obsciones, String direccion, double latitud, double longitud, String titulo, String estado, long timestamp, String horaServicio) {
        this.key = key;
        this.nombre = nombre;
        this.tipoServicio = tipoServicio;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.informacion = informacion;
        this.obsciones = obsciones;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.titulo = titulo;
        this.estado = estado;
        this.timestamp = timestamp;
        this.horaServicio = horaServicio;

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public String getInformacion() {
        return informacion;
    }

    public void setInformacion(String informacion) {
        this.informacion = informacion;
    }

    public String getObsciones() {
        return obsciones;
    }

    public void setObsciones(String obsciones) {
        this.obsciones = obsciones;
    }

    public String getHoraServicio() {
        return horaServicio;
    }

    public void setHoraServicio(String horaServicio) {
        this.horaServicio = horaServicio;
    }
}
