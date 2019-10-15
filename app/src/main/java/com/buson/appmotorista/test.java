package com.buson.appmotorista;

import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class test extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;

    private void incializarFireBase() {

        FirebaseApp.initializeApp(test.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        final Button botao = (Button) findViewById(R.id.btn_iniciar);
        final Button botao_finalizar = (Button) findViewById(R.id.btn_finalizar);

        final TextInputEditText etNome = findViewById(R.id.nome_motorista);
        final TextInputEditText etNumeroOnibus = findViewById(R.id.numero_onibus);
        final Spinner etLinha = (Spinner) findViewById(R.id.linha);

        final String nome = etNome.getText().toString().trim();
        final String numerobus = etNumeroOnibus.getText().toString().trim();

        setTitle("Motorista");

        // Spinner element
        final Spinner spinner = (Spinner) findViewById(R.id.linha);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        final List<String> categories = new ArrayList<String>();
        categories.add("Blumenau - Ilhota");
        categories.add("Ilhota - Blumenau");
        categories.add("Blumenau - Gaspar");
        categories.add("Gaspar - Blumenau");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        final Mot motorista = new Mot();
        final Dados dados = new Dados();
        dados.buildGoogleApiClient();
        incializarFireBase();
        int id = 1;


        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String linha = spinner.getSelectedItem().toString();


                motorista.setNomeMotorista(etNome.getText().toString());
                motorista.setNumMotorista(etNumeroOnibus.getText().toString());
                motorista.setLinha(linha);
                motorista.setLatitude(dados.pontoBlumenau.latitude);
                motorista.setLongitude(dados.pontoBlumenau.longitude);
                Toast.makeText(test.this,"o" +dados.pontoBlumenau.latitude + dados.pontoBlumenau.longitude,Toast.LENGTH_SHORT).show();

                ref.child("motorista").child(motorista.getNumMotorista()).setValue(motorista);

                botao.setVisibility(View.INVISIBLE);
                botao_finalizar.setVisibility(View.VISIBLE);

                etNome.setEnabled(false);
                etNumeroOnibus.setEnabled(false);
                etLinha.setEnabled(false);

//                Toast.makeText(test.this,"Iniciando",Toast.LENGTH_SHORT).show();

            }

        });

        botao_finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ref.child("motorista").child(motorista.getNumMotorista()).removeValue();

                botao_finalizar.setVisibility(View.INVISIBLE);
                botao.setVisibility(View.VISIBLE);

                etNome.setEnabled(true);
                etNumeroOnibus.setEnabled(true);
                etLinha.setEnabled(true);

                etNome.setText("");
                etNumeroOnibus.setText("");


                Toast.makeText(test.this,"Finalizando",Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub


    }
}

