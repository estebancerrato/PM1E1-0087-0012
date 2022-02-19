package com.example.pm1e1_0087_0012;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.pm1e1_0087_0012.Clases.Contactos;
import com.example.pm1e1_0087_0012.configuraciones.SQLiteConexion;
import com.example.pm1e1_0087_0012.configuraciones.Transacciones;

import java.util.ArrayList;

public class ActivityListadoContacto extends AppCompatActivity {

    /* Variables globales */
    SQLiteConexion conexion;
    ListView lista;
    ArrayList<Contactos> listaContactos;
    ArrayList <String> ArregloContactos;

    Button alcbtnAtras,btnactualizarContacto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_contacto);

        lista = (ListView) findViewById(R.id.LisViewContactos);

        conexion = new SQLiteConexion(this, Transacciones.NameDatabase,null,1);

        obtenerlistaContactos();

        //llenar grip con datos empleado
        ArrayAdapter adp = new ArrayAdapter(this, android.R.layout.simple_list_item_1,ArregloContactos);
        lista.setAdapter(adp);





        alcbtnAtras = (Button) findViewById(R.id.btnAtras);
        btnactualizarContacto = (Button) findViewById(R.id.btnActualizarContacto);

        alcbtnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        btnactualizarContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ActivityActualizarContacto.class);
                startActivity(intent);
            }
        });
    }

    private void obtenerlistaContactos() {
        //conexion a la BD modo lectura
        SQLiteDatabase db = conexion.getReadableDatabase();

        //clase empleados
        Contactos list_contact = null;

        //inicializar array empleados con la clase
        listaContactos = new ArrayList<Contactos>();

        //consulta BD directa
        Cursor cursor = db.rawQuery("SELECT * FROM "+ Transacciones.tablacontactos, null);

        //RECORRER LA TABLA MOVIENDONOS SOBRE EL CURSOR
        while (cursor.moveToNext())
        {
            list_contact = new Contactos();
            list_contact.setCodigo(cursor.getInt(0));
            list_contact.setNombreContacto(cursor.getString(1));
            list_contact.setNumeroContacto(cursor.getInt(2));
            list_contact.setNota(cursor.getString(3));
            list_contact.setCodigoPais(cursor.getString(5));
            listaContactos.add(list_contact);
        }
        cursor.close();
        //metodo para llenar lista
        llenarlista();

    }

    private void llenarlista()
    {
        ArregloContactos = new ArrayList<String>();

        for (int i=0; i<listaContactos.size();i++)
        {
            ArregloContactos.add(listaContactos.get(i).getNombreContacto()+"|"+
                    listaContactos.get(i).getCodigoPais()+
                    listaContactos.get(i).getNumeroContacto());

        }
    }

}