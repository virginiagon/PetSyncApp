package br.com.petsync.petsync;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import br.com.petsync.petsync.model.Servico;

public class DetalheServicoActivity extends AppCompatActivity {

    private Servico servico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_servico);

        Intent intent = getIntent();
        servico = (Servico) intent.getSerializableExtra("servico");

        TextView nome = (TextView) findViewById(R.id.detalhe_servico_nome);
        nome.setText(servico.getNome());

        TextView descricao = (TextView) findViewById(R.id.detalhe_servico_descricao);
        descricao.setText(servico.getDescricao());

        TextView valor = (TextView) findViewById(R.id.detalhe_servico_valor);
        valor.setText("R$ " + servico.getValor());

        Button btnAdicionarCesta = (Button) findViewById(R.id.detalhe_servico_btn_add_to_cart);
        btnAdicionarCesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cestaIntent = new Intent(DetalheServicoActivity.this, CestaDeServicosActivity.class);
                //cestaIntent.putExtra("servico", servico);
                startActivity(cestaIntent);
            }
        });

    }
}
