package br.com.petsync.petsync;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TabHost;

import java.util.List;

import br.com.petsync.petsync.adapter.ServicoAdaptar;
import br.com.petsync.petsync.converter.ServicoConverter;
import br.com.petsync.petsync.model.Estabelecimento;
import br.com.petsync.petsync.model.Servico;
import br.com.petsync.petsync.webservices.WebClientEstablishments;

public class DetalheEstabelecimentoActivity extends AppCompatActivity {

    private ListView listServicos;
    private Estabelecimento estabelecimento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_estabelecimento_old);

        Intent intent = getIntent();
        estabelecimento = (Estabelecimento) intent.getSerializableExtra("estabelecimento");

        listServicos = (ListView) findViewById(R.id.detalhe_lista_servicos);

        TabHost host = (TabHost) findViewById(R.id.tabHostDetalheEstabelecimento);
        host.setup();

        //Tab1
        TabHost.TabSpec spec = host.newTabSpec("Serviços");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Serviços");
        host.addTab(spec);

        //Tab2
        spec = host.newTabSpec("Avaliações");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Avaliações");
        host.addTab(spec);

        spec = host.newTabSpec("Info.");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Info.");
        host.addTab(spec);
    }

    private void loadServicos() {
        new SearchServicosTask(this, estabelecimento).execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadServicos();
    }

    public class SearchServicosTask extends AsyncTask {

        private Context context;
        private ProgressDialog dialog;
        private List<Servico> servicosList;
        private Estabelecimento estabelecimento;

        public SearchServicosTask(Context context, Estabelecimento estabelecimento) {
            this.context = context;
            this.estabelecimento = estabelecimento;
        }

        @Override
        protected void onPreExecute() {
            this.dialog = ProgressDialog.show(context, "Aguarde", "Buscando Serviços...", true, true);
        }

        @Override
        protected Object doInBackground(Object[] params) {

            WebClientEstablishments cliente = new WebClientEstablishments();
            String resposta = cliente.getJsonFromUrl("http://www.petsync.com.br/api/servicos");

            ServicoConverter conversor = new ServicoConverter();
            this.servicosList = conversor.ParseJSON(resposta, estabelecimento);

            return this.servicosList;
        }

        @Override
        protected void onPostExecute(Object o) {
            this.dialog.dismiss();
            ServicoAdaptar adapter = new ServicoAdaptar(this.context, this.servicosList);
            listServicos.setAdapter(adapter);
        }
    } // fim da classe do asyncTask
}
