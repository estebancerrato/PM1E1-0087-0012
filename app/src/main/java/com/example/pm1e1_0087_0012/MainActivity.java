package com.example.pm1e1_0087_0012;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pm1e1_0087_0012.Clases.Pais;
import com.example.pm1e1_0087_0012.configuraciones.SQLiteConexion;
import com.example.pm1e1_0087_0012.configuraciones.Transacciones;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    SQLiteConexion conexion = new SQLiteConexion(this,Transacciones.NameDatabase,null,1);
    SQLiteDatabase db;

    EditText nombreCompleto, telefono, nota;
    Spinner spPais;
    ImageView foto;
    Button btnSlcFoto,btnTomarFoto;

    static final int PETICION_ACCESO_CAM = 100;
    static final int TAKE_PIC_REQUEST = 101;
    Bitmap imagen;

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

        Button btnGuardarContacto= (Button) findViewById(R.id.btnGuardar);
        btnTomarFoto = (Button) findViewById(R.id.btnTomarFoto);
        btnSlcFoto = (Button) findViewById(R.id.btnSeleccionarFoto);
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

        btnTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permisos();
            }
        });

        ObtenerListaPaises();

        ArrayAdapter<CharSequence> adp = new ArrayAdapter(this, android.R.layout.simple_spinner_item,lista_paises);
        spPais.setAdapter(adp);

        spPais.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                Toast.makeText(getApplicationContext(),adapterView.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                //spPais.setAdapter(adapterView.getSelectedItem().toString());
                //item = adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void permisos() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},PETICION_ACCESO_CAM);
        }else{
            tomarFoto();
        }
    }
    private void tomarFoto() {
        Intent takepic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(takepic.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(takepic,TAKE_PIC_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requescode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requescode, resultCode, data);

        if(requescode == TAKE_PIC_REQUEST && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            imagen = (Bitmap) extras.get("data");
            foto.setImageBitmap(imagen);
        }

    }


    private void validarDatos() {
        if (nombreCompleto.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Debe de escribir un nombre" ,Toast.LENGTH_LONG).show();
        }else if (telefono.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Debe de escribir un telefono" ,Toast.LENGTH_LONG).show();
        }else if (nota.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Debe de escribir una nota" ,Toast.LENGTH_LONG).show();
        }else{
            guardarContacto(imagen);
        }
    }

    private void guardarContacto(Bitmap bitmap) {
        //SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);

        db = conexion.getWritableDatabase();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] ArrayFoto  = stream.toByteArray();


        ContentValues valores = new ContentValues();

        valores.put(Transacciones.nombreCompleto, nombreCompleto.getText().toString());
        valores.put(Transacciones.telefono, telefono.getText().toString());
        valores.put(Transacciones.nota, nota.getText().toString());
        //valores.put(Transacciones.pais, spPais.getSelectedItem().toString());
        valores.put(Transacciones.foto, String.valueOf(foto));

        Long resultado = db.insert(Transacciones.tablacontactos, Transacciones.id, valores);

        Toast.makeText(getApplicationContext(), "Registro ingreso con exito, Codigo " + resultado.toString()
                ,Toast.LENGTH_LONG).show();

        db.close();
    }

    private void ObtenerListaPaises() {
        Pais pais = null;
        lista = new ArrayList<Pais>();
        //conexion = new SQLiteConexion(this, Transacciones.NameDatabase,null,1);
        db = conexion.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Transacciones.tblPaises,null);

        while (cursor.moveToNext())
        {
            pais = new Pais();

            pais.setCodigo(cursor.getString(0));
            pais.setNombrePais(cursor.getString(1));

            lista.add(pais);
        }

        cursor.close();

        fillCombo();

    }

    private void fillCombo() {
        lista_paises = new ArrayList<String>();

        for (int i=0; i<lista.size();i++)
        {
            lista_paises.add(lista.get(i).getNombrePais()+" ( "+lista.get(i).getCodigo()+" )");
        }
    }
}