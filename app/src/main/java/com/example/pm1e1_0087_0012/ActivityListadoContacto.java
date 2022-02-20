package com.example.pm1e1_0087_0012;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pm1e1_0087_0012.Clases.Contactos;
import com.example.pm1e1_0087_0012.configuraciones.SQLiteConexion;
import com.example.pm1e1_0087_0012.configuraciones.Transacciones;

import java.util.ArrayList;
import java.util.Objects;

public class ActivityListadoContacto extends AppCompatActivity {

    /* Variables globales */
    SQLiteConexion conexion;
    ListView lista;
    ArrayList<Contactos> listaContactos;
    ArrayList <String> ArregloContactos;
    EditText alctxtnombre;
    Button alcbtnAtras,btnactualizarContacto, btnEliminar, btnCompartir;

    Intent intent;
    Contactos contacto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_contacto);

        lista = (ListView) findViewById(R.id.LisViewContactos);
        intent = new Intent(getApplicationContext(),ActivityActualizarContacto.class);

        conexion = new SQLiteConexion(this, Transacciones.NameDatabase,null,1);

        obtenerlistaContactos();

        //llenar grip con datos contactos
        ArrayAdapter adp = new ArrayAdapter(this, android.R.layout.simple_list_item_checked,ArregloContactos);
        lista.setAdapter(adp);

        /*BUSCAR CONTACTOS EN LISTA*/
        alctxtnombre = (EditText) findViewById(R.id.alctxtnombre);

        alctxtnombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                adp.getFilter().filter(charSequence);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //--------------------------------------LISTA------------------------------------------
        //seteo el contacto seleccionado para luego iniciar la actividad en el boton actualizar
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                contacto = listaContactos.get(i);//lleno la lista de contacto
                setContactoSeleccionado();
            }
        });




        alcbtnAtras = (Button) findViewById(R.id.btnAtras);
        btnactualizarContacto = (Button) findViewById(R.id.btnActualizarContacto);
        btnEliminar = (Button) findViewById(R.id.btnaclEliminarContacto);
        btnCompartir = (Button) findViewById(R.id.btnCompartir);

    //------------------------------------------BOTONES------------------------------------------
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

                startActivity(intent);
            }
        });

        btnCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    enviarContacto();
            }
        });


        final Context context = this;

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                // set title

                alertDialogBuilder.setTitle("Eliminar Contacto");


                // set dialog message
                alertDialogBuilder
                        .setMessage("¿Está seguro de eliminar el contacto?")
                        .setCancelable(false)
                        .setPositiveButton("SI",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // si el usuario da click en si procede a llamar el metodo de eliminar
                                eliminarContacto();
                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }
        });
    }

    //-------------------------------------------METODOS-----------------------------------------

    private void eliminarContacto() {
        SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
        SQLiteDatabase db = conexion.getWritableDatabase();
        int obtenerCodigo = contacto.getCodigo();

        db.delete(Transacciones.tablacontactos,Transacciones.id +" = "+ obtenerCodigo, null);

        Toast.makeText(getApplicationContext(), "Registro eliminado con exito, Codigo " + obtenerCodigo
                ,Toast.LENGTH_LONG).show();
        db.close();

        //despues de eliminar vuelve a abrir la activida, limpiando los menu anteriores
        Intent intent = new Intent(this, ActivityListadoContacto.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();


    }

    private void enviarContacto(){
        String contactoEnviado = "El numero de "+contacto.getNombreContacto().toString()+
                " es +"+contacto.getCodigoPais()+contacto.getNumeroContacto() ;
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, contactoEnviado);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }


    private void setContactoSeleccionado() {

        //contacto = listaContactos.get(id);
        //intent = new Intent(getApplicationContext(),ActivityActualizarContacto.class);
        intent.putExtra("codigo", contacto.getCodigo()+"");
        intent.putExtra("nombreContacto", contacto.getNombreContacto());
        intent.putExtra("numeroContacto", contacto.getNumeroContacto()+"");
        intent.putExtra("codigoPais", contacto.getCodigoPais()+"");
        intent.putExtra("notaContacto", contacto.getNota());
        //startActivity(intent);
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
            ArregloContactos.add(listaContactos.get(i).getNombreContacto()+" | "+
                    listaContactos.get(i).getCodigoPais()+
                    listaContactos.get(i).getNumeroContacto());

        }
    }

}