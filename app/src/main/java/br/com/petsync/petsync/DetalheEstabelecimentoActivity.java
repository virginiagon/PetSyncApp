package br.com.petsync.petsync;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

import br.com.petsync.petsync.adapter.ServicoAdaptar;
import br.com.petsync.petsync.converter.ServicoConverter;
import br.com.petsync.petsync.model.Estabelecimento;
import br.com.petsync.petsync.model.Servico;
import br.com.petsync.petsync.webservices.WebClient;

public class DetalheEstabelecimentoActivity extends AppCompatActivity {

    private ListView listServicos;
    private Estabelecimento estabelecimento;
    //User session manager class
    UserSessionManager session;
    private String endereco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_estabelecimento);

        Intent intent = getIntent();
        estabelecimento = (Estabelecimento) intent.getSerializableExtra("estabelecimento");
        endereco = (String) intent.getSerializableExtra("enderecoCliente");

        listServicos = (ListView) findViewById(R.id.detalhe_lista_servicos);

        listServicos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Servico servico = (Servico) listServicos.getItemAtPosition(position);
                Intent detalheServico = new Intent(DetalheEstabelecimentoActivity.this, DetalheServicoActivity.class);
                detalheServico.putExtra("servico", servico);
                startActivity(detalheServico);
            }
        });

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
        carregaInfo(estabelecimento);
    }

    public void carregaInfo(Estabelecimento estabelecimento) {

        TextView nome = (TextView) findViewById(R.id.tab_estab_info_nome);
        nome.setText(estabelecimento.getName());

        TextView responsavel = (TextView) findViewById(R.id.tab_estab_info_responsavel);
        responsavel.setText(estabelecimento.getNameResponsible());

        TextView endereco = (TextView) findViewById(R.id.tab_estab_info_endereco);
        endereco.setText(estabelecimento.getAddress());

        TextView cep = (TextView) findViewById(R.id.tab_estab_info_cep);
        cep.setText(estabelecimento.getZipCode());

        TextView cidade = (TextView) findViewById(R.id.tab_estab_info_cidade);
        cidade.setText(estabelecimento.getCity());

        TextView telefone = (TextView) findViewById(R.id.tab_estab_info_telefone);
        telefone.setText(estabelecimento.getPhone());

        TextView site = (TextView) findViewById(R.id.tab_estab_info_site);
        site.setText(estabelecimento.getSite());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalhe_estabelecimento, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_detalhe_estab_map:
                Intent intentMapa = new Intent(this, MapsActivity.class);
                intentMapa.putExtra("estabelecimento", estabelecimento);
                intentMapa.putExtra("enderecoCliente", endereco);
                startActivity(intentMapa);
                //Toast.makeText(DetalheEstabelecimentoActivity.this, "Teste MAPA", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_detalhe_estab_navigation:
                Intent intentGoogleMapa = new Intent(Intent.ACTION_VIEW);
                intentGoogleMapa.setData(Uri.parse("geo:0,0?q=" + this.estabelecimento.getAddress() + ", " + this.estabelecimento.getCity() + ", " + this.estabelecimento.getState()));
                startActivity(intentGoogleMapa);
                //Toast.makeText(DetalheEstabelecimentoActivity.this, "teste NAVIGATION", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    //Classe que busca os servicos
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

            WebClient cliente = new WebClient();
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
