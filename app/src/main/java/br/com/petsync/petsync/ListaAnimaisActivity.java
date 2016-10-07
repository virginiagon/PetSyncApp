package br.com.petsync.petsync;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import br.com.petsync.petsync.adapter.AnimalAdapter;
import br.com.petsync.petsync.converter.AnimalConverter;
import br.com.petsync.petsync.model.Animal;
import br.com.petsync.petsync.webservices.WebClient;

public class ListaAnimaisActivity extends AppCompatActivity {

    private ListView listaAnimais;

    //User session manager class
    UserSessionManager session;

    Long clienteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_animais);

        //Session class instance
        session = new UserSessionManager(getApplicationContext());

        if(session.checkLogin())
            finish();

        //get user data from session
        HashMap<String, String> user = session.getUserDetails();
        this.clienteId = Long.valueOf(user.get(UserSessionManager.KEY_ID));

        listaAnimais = (ListView) findViewById(R.id.lista_anaimais);

        Button novoAnimal = (Button) findViewById(R.id.novo_animal);
        novoAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ListaAnimaisActivity.this, "Clicou em novo animal", Toast.LENGTH_SHORT).show();
                Intent intentVaiProForm = new Intent(ListaAnimaisActivity.this, FormularioCadastroAnimalActivity.class);
                startActivity(intentVaiProForm);
            }
        });

        listaAnimais.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Animal animal = (Animal) listaAnimais.getItemAtPosition(position);
                Intent formularioCadastroAnimal = new Intent(ListaAnimaisActivity.this, FormularioCadastroAnimalActivity.class);
                formularioCadastroAnimal.putExtra("animal", animal);
                startActivity(formularioCadastroAnimal);
            }
        });
    }

    /**
     * Método que chama a classe de task
     */
    private void loadAnimais() {
        new SearchAnimaisTask(this, this.clienteId).execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAnimais();
    }

    //Classe de tarefa da listagem de Animais
    public class SearchAnimaisTask extends AsyncTask {

        private Context context;
        private ProgressDialog dialog;
        private List<Animal> animaisList;
        private Long clienteId;

        public SearchAnimaisTask(Context context, Long clienteId) {
            this.context = context;
            this.clienteId = clienteId;
        }

        @Override
        protected void onPreExecute() {
            this.dialog = ProgressDialog.show(context, "Aguarde", "Buscando seus Pets ;)", true, true);
        }

        @Override
        protected Object doInBackground(Object[] params) {

            WebClient cliente = new WebClient();
            String resposta = cliente.getJsonFromUrl("http://www.petsync.com.br/api/animais");

            AnimalConverter conversor = new AnimalConverter();
            this.animaisList = conversor.ParseJSON(resposta, this.clienteId);

            return this.animaisList;
        }

        @Override
        protected void onPostExecute(Object o) {
            this.dialog.dismiss();
            AnimalAdapter adapter = new AnimalAdapter(context, this.animaisList);
            listaAnimais.setAdapter(adapter);
        }
    }//fim da classe AsyncTask
}
