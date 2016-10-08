package br.com.petsync.petsync;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import br.com.petsync.petsync.adapter.EstabelecimentoAdapter;
import br.com.petsync.petsync.converter.ClienteConverter;
import br.com.petsync.petsync.converter.EstabelecimentoConverter;
import br.com.petsync.petsync.model.Cliente;
import br.com.petsync.petsync.model.Estabelecimento;
import br.com.petsync.petsync.webservices.WebClient;

public class ListEstabelecimentosActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView listEstabelecimentos;

    //User session manager class
    UserSessionManager session;

    private Long clienteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_estabelecimentos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Cria a sessão
        createSession();

        //get user data from session
        HashMap<String, String> user = session.getUserDetails();
        String name = user.get(UserSessionManager.KEY_NAME);
        String email = user.get(UserSessionManager.KEY_EMAIL);
        this.clienteId = Long.valueOf(user.get(UserSessionManager.KEY_ID));

        //lista estabelecimentos.
        listEstabelecimentos = (ListView) findViewById(R.id.lista_estabelcimentos);

        listEstabelecimentos.setOnItemClickListener(new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Estabelecimento estabelecimento = (Estabelecimento) listEstabelecimentos.getItemAtPosition(position);
                Intent intentVaiParaDetalhe = new Intent(ListEstabelecimentosActivity.this, DetalheEstabelecimentoActivity.class);
                intentVaiParaDetalhe.putExtra("estabelecimento", estabelecimento);
                startActivity(intentVaiParaDetalhe);
            }
        });

        //Menu DrawerLayout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        TextView lblName = (TextView)header.findViewById(R.id.nav_header_nome_cliente);
        TextView lblEmail = (TextView)header.findViewById(R.id.nav_header_email);
        lblName.setText(name);
        lblEmail.setText(email);
    }



    /**
     * Metodo que cria a sessão.
     */
    private void createSession() {
        //Session class instance
        session = new UserSessionManager(getApplicationContext());

        Toast.makeText(getApplicationContext(),
                "User Login Status: " + session.isUserLoggedIn(),
                Toast.LENGTH_LONG).show();

        //Check user login (this is the important point)
        //If user is not logged in, this will redirect user to LoginActivity
        //and finish current activity from activity stack.
        if(session.checkLogin())
            finish();

    }

    /**
     * Método que chama a Classe de task para buscar os estabelecimentos.
     */
    private void loadEstablishments() {
        new SearchEstablishmentsTask(this, this.clienteId).execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadEstablishments();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_estabelecimentos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_list_estab_inicio) {
            // Handle the camera action
        } else if (id == R.id.nav_list_estab_meus_pets) {
            Intent listaAnimais = new Intent(this, ListaAnimaisActivity.class);
            startActivity(listaAnimais);
        } else if (id == R.id.nav_list_estab_avaliacoes) {
            //Avaliações que o cliente fez aos estabelecimentos
        } else if (id == R.id.nav_list_estab_logout) {
            session.logoutUser();
        } else if (id == R.id.nav_list_estab_cadastrar) {
            Intent cadastroUsuario = new Intent(this, FormularioCadastroUsuarioActivity.class);
            startActivity(cadastroUsuario);
        } /*else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Class AsyncTask
    public class SearchEstablishmentsTask extends AsyncTask {

        private Context context;
        private ProgressDialog dialog;
        private List<Estabelecimento> estabelecimentoList;
        private Long clienteId;
        private Cliente cliente;

        public SearchEstablishmentsTask(Context context, Long clienteId) {
            this.context = context;
            this.clienteId = clienteId;
        }

        @Override
        protected void onPreExecute() {
            this.dialog = ProgressDialog.show(context, "Aguarde", "Buscando estabelecimentos...", true, true);
        }

        @Override
        protected Object doInBackground(Object[] params) {

            WebClient client = new WebClient();
            String resposta = client.getJsonFromUrl("http://www.petsync.com.br/api/estabelecimentos");

            EstabelecimentoConverter conversor = new EstabelecimentoConverter();
            this.estabelecimentoList = conversor.ParseJSON(resposta);

            WebClient webClient2 = new WebClient();
            String jsonCliente = webClient2.getJsonFromUrl("http://www.petsync.com.br/api/clientes/" + this.clienteId);
            ClienteConverter clienteConverter = new ClienteConverter();
            this.cliente = clienteConverter.parseJsonForOneCliente(jsonCliente);

            return this.estabelecimentoList;
        }

        @Override
        protected void onPostExecute(Object result) {
            this.dialog.dismiss();
            EstabelecimentoAdapter adapter = new EstabelecimentoAdapter(this.context, this.estabelecimentoList, this.cliente);
            listEstabelecimentos.setAdapter(adapter);
        }
    } //fim classe AsyncTask
}
