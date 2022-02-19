package com.example.pm1e1_0087_0012.configuraciones;

import java.sql.Blob;

public class Transacciones {
    //Nombre de la base de datos
    public static final String NameDatabase = "PM01DB";

    //Creacion de las tablas de la base de datos
    public static final String tablacontactos = "contactos";
       //Creacion de los atributos
        public static final String id = "id";
        public static final String nombreCompleto = "nombreCompleto";
        public static final String telefono = "telefono";
        public static final String nota = "nota";
        public static final Blob foto = null;
        public static final String pais = "pais";




        //Transacciones DDL
        public static final String createTableContact = "CREATE TABLE " + tablacontactos +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombreCompleto TEXT, telefono INTEGER, nota TEXT, foto BLOB, pais TEXT)";

        public static final String dropTableContact = "DROP TABLE IF EXIST" + tablacontactos;
}
