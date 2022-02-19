package com.example.pm1e1_0087_0012;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pm1e1_0087_0012.Clases.Pais;
import com.example.pm1e1_0087_0012.configuraciones.SQLiteConexion;
import com.example.pm1e1_0087_0012.configuraciones.Transacciones;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText nombreCompleto, telefono, nota;
    Spinner spPais;
    ImageView foto;


    ArrayList<String> lista_paises;
    ArrayList<Pais> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nombreCompleto = (EditText) findViewById(R.id.txtNombreCompleto);
        telefono = (EditText) findViewById(R.id.txtTelefono);
        nota = (EditText) findViewById(R.id.txtNota);

        spPais = (Spinner)findViewById(R.id.cmbPais);
        foto = (ImageView) findViewById(R.id.imageView);

        Button btnGuardarContacto = (Button) findViewById(R.id.btnGuardar);
        Button btnContactoSalvados = (Button)findViewById(R.id.btnListar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatAgregarPais);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Ir a la ventaja de ingresar nuevo pais
                Intent intent = new Intent(getApplicationContext(),ActivityPais.class);
                startActivity(intent);

            }
        });

        btnContactoSalvados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ActivityListadoContacto.class);
                startActivity(intent);
            }
        });

        btnGuardarContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //valida que los datos esten ingresados, antes de guardar
                validarDatos();


            }
        });

    }

    private void validarDatos() {
        if (nombreCompleto.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Debe de escribir un nombre" ,Toast.LENGTH_LONG).show();
        }else if (telefono.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Debe de escribir un telefono" ,Toast.LENGTH_LONG).show();
        }else if (nota.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Debe de escribir una nota" ,Toast.LENGTH_LONG).show();
        }else{
            guardarContacto();
        }
    }

    private void guardarContacto() {
        SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);

        SQLiteDatabase db = conexion.getWritableDatabase();


        ContentValues valores = new ContentValues();

        valores.put(Transacciones.nombreCompleto, nombreCompleto.getText().toString());
        valores.put(Transacciones.telefono, telefono.getText().toString());
        valores.put(Transacciones.nota, nota.getText().toString());
        valores.put(Transacciones.pais, spPais.getSelectedItem().toString());

        Long resultado = db.insert(Transacciones.tablacontactos, Transacciones.id, valores);

        Toast.makeText(getApplicationContext(), "Registro ingreso con exito, Codigo " + resultado.toString()
                ,Toast.LENGTH_LONG).show();

        db.close();
    }
}