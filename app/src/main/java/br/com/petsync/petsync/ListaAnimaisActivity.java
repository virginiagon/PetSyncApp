package br.com.petsync.petsync;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

import br.com.petsync.petsync.adapter.AnimalAdapter;
import br.com.petsync.petsync.converter.AnimalConverter;
import br.com.petsync.petsync.model.Animal;
import br.com.petsync.petsync.webservices.WebClient;

public class ListaAnimaisActivity extends AppCompatActivity {

    private ListView listaAnimais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_animais);

        listaAnimais = (ListView) findViewById(R.id.lista_anaimais);
    }


    private void loadAnimais() {
        new SearchAnimaisTask(this).execute();
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

        public SearchAnimaisTask(Context context) {
            this.context = context;
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
            this.animaisList = conversor.ParseJSON(resposta);

            return this.animaisList;
        }

        @Override
        protected void onPostExecute(Object o) {
            this.dialog.dismiss();
            AnimalAdapter adapter = new AnimalAdapter(context, this.animaisList);
            listaAnimais.setAdapter(adapter);
        }
    }
}
