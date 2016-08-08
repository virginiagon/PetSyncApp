package br.com.petsync.petsync;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class MetodosBuscaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metodos_busca);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Botão Informar CEP
        Button informarCep = (Button) findViewById(R.id.metodos_busca_cep);
        informarCep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentInformarCepForm = new Intent(MetodosBuscaActivity.this, FormularioInformarCepActivity.class);
                startActivity(intentInformarCepForm);
            }
        });
    }

}
