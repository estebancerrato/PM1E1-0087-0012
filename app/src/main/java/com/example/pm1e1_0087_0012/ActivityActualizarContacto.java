package com.example.pm1e1_0087_0012;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class ActivityActualizarContacto extends AppCompatActivity {
    Button btnatras;
    EditText codigo, nombrecompleto, telefono, nota;
    Spinner codigoPais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_contacto);
        btnatras = (Button) findViewById(R.id.acbtnatras);
        codigo = (EditText)findViewById(R.id.txtActCodigo);
        nombrecompleto = (EditText)findViewById(R.id.txtActNombreCompleto);
        telefono = (EditText)findViewById(R.id.txtActTelefono);
        nota = (EditText)findViewById(R.id.txtActNota);
        codigoPais = (Spinner)findViewById(R.id.cmbActSeleccionarPais);


        //setear la informacion a actualizar

        codigo.setText(getIntent().getStringExtra("codigo"));
        nombrecompleto.setText(getIntent().getStringExtra("nombreContacto"));
        telefono.setText(getIntent().getStringExtra("numeroContacto"));
        nota.setText(getIntent().getStringExtra("notaContacto"));


        btnatras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ActivityListadoContacto.class);
                startActivity(intent);
            }
        });
    }
}