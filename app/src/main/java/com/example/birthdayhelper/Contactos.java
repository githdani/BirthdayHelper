package com.example.birthdayhelper_danielavila;

import android.graphics.Bitmap;

import java.util.Date;
import java.util.*;

public class Contactos {
    private Bitmap foto;
    private String Id;
    private String nombre;
    private List<String> número;
    private String fecha;
    private String notif;
    private String cumple;



    public Contactos() {
    }

    public Contactos(String nombre, List<String> número, String fecha, String Id, Bitmap foto, String notif, String cumple) {
        this.nombre = nombre;
        this.número = número;
        this.fecha = fecha;
        this.Id = Id;
        this.foto = foto;
        this.notif= notif;
        this.cumple = cumple;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<String> getNúmero() {
        return número;
    }

    public void setNúmero(List<String> número) {
        this.número = número;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Bitmap getFoto() {return foto;}

    public void setFoto(Bitmap foto) {this.foto = foto;}

    public String getId() {return Id;}

    public void setId(String id) {Id = id;}

    public String getNotif() {return notif;}

    public void setNotif(String notif) {this.notif = notif;}

    public String getCumple() {return cumple;}

    public void setCumple(String cumple) {this.cumple = cumple;}


}
