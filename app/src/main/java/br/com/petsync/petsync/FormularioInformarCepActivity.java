package br.com.petsync.petsync;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;

public class FormularioInformarCepActivity extends AppCompatActivity {

    //User session manager class
    UserSessionManager session;

    private EditText cepText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_informar_cep);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Session class instance
        session = new UserSessionManager(getApplicationContext());

        Button btnConfirmar = (Button) findViewById(R.id.button_confirmar_cep);
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cepText = (EditText) findViewById(R.id.busca_por_cep);
                String cep = cepText.getText().toString();
                session.setCep(cep);
            }
        });

    }

}
