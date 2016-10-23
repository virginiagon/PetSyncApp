package br.com.petsync.petsync;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MetodosBuscaActivity extends AppCompatActivity {

    //User session manager class
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metodos_busca);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Session class instance
        session = new UserSessionManager(getApplicationContext());

        //Botão Informar CEP
        Button informarCep = (Button) findViewById(R.id.metodos_busca_cep);
        informarCep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentInformarCepForm = new Intent(MetodosBuscaActivity.this, FormularioInformarCepActivity.class);
                startActivity(intentInformarCepForm);
            }
        });

        TextView enderecoCadastro = (TextView) findViewById(R.id.metodos_busca_end_cadastro);
        enderecoCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.setCep(null);
            }
        });
    }

}
