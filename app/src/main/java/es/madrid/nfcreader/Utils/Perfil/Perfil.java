package es.madrid.nfcreader.Utils.Perfil;

import java.io.Serializable;
import java.util.ArrayList;

public class Perfil implements Serializable {
    String nombre;
    String FechaNac;
    String FechaExp;
    String FechaCad;
    ArrayList<String> Aficiones= new ArrayList<>();
    ArrayList<String> Aptitudes= new ArrayList<>();
    ArrayList<String> Historial= new ArrayList<String>();


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaNac() {
        return FechaNac;
    }

    public void setFechaNac(String fechaNac) {
        FechaNac = fechaNac;
    }

    public String getFechaExp() {
        return FechaExp;
    }

    public void setFechaExp(String fechaExp) {
        FechaExp = fechaExp;
    }

    public String getFechaCad() {
        return FechaCad;
    }

    public void setFechaCad(String fechaCad) {
        FechaCad = fechaCad;
    }

    public ArrayList<String> getAficiones() {
        return Aficiones;
    }

    public void setAficiones(ArrayList<String> aficiones) {
        Aficiones = aficiones;
    }

    public ArrayList<String> getAptitudes() {
        return Aptitudes;
    }
    public void addAficiones(String s){
        Aficiones.add(s);
    }
    public void setAptitudes(ArrayList<String> aptitudes) {
        Aptitudes = aptitudes;
    }

    public ArrayList<String> getHistorial() {
        return Historial;
    }
    public void addAptitudes(String s){
        Aptitudes.add(s);
    }
    public void addHistorial(String s){
        Historial.add(s);
    }


    public void setHistorial(ArrayList<String> historial) {
        Historial = historial;
    }
}
