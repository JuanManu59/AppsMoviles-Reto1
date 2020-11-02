package com.example.mapsicesi.modelo;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class Hueco {

    private String id;
    private String direccion;
    private String username;
    private double latitud;
    private double longitud;
    private boolean confirmado;

    public Hueco() {
    }

    public Hueco(String id, String username, double latitud, double longitud, String direccion, boolean confirmado) {
        this.id = id;
        this.username = username;
        this.latitud = latitud;
        this.longitud = longitud;
        this.direccion = direccion;
        this.confirmado = confirmado;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public void setConfirmado(boolean confirmado) {
        this.confirmado = confirmado;
    }
}
