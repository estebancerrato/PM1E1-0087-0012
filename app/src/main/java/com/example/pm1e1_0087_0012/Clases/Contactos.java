package com.example.pm1e1_0087_0012.Clases;

import java.sql.Blob;

public class Contactos {
    private int codigo;
    private String nombrePais;
    private String nombreContacto;
    private int numeroContacto;
    private String nota;
    private Blob image;

    public Contactos() {
    }

    public Contactos(int codigo, String nombrePais, String nombreContacto, int numeroContacto, String nota, Blob image) {
        this.codigo = codigo;
        this.nombrePais = nombrePais;
        this.nombreContacto = nombreContacto;
        this.numeroContacto = numeroContacto;
        this.nota = nota;
        this.image = image;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombrePais() {
        return nombrePais;
    }

    public void setNombrePais(String nombrePais) {
        this.nombrePais = nombrePais;
    }

    public String getNombreContacto() {
        return nombreContacto;
    }

    public void setNombreContacto(String nombreContacto) {
        this.nombreContacto = nombreContacto;
    }

    public int getNumeroContacto() {
        return numeroContacto;
    }

    public void setNumeroContacto(int numeroContacto) {
        this.numeroContacto = numeroContacto;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }



    public void setImage(Blob image) {
        this.image = image;
    }


}
