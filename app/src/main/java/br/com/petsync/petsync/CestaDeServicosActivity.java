package br.com.petsync.petsync;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

import br.com.petsync.petsync.model.Servico;

public class CestaDeServicosActivity extends AppCompatActivity {

    private List<Servico> itensCesta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cesta_de_servicos);

        //Intent intent = getIntent();
        //Servico servico = (Servico) intent.getSerializableExtra("servico");

        //itensCesta.add(servico);
    }
}
