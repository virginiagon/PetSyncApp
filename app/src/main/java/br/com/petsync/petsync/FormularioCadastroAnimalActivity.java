package br.com.petsync.petsync;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import br.com.petsync.petsync.converter.AnimalConverter;
import br.com.petsync.petsync.model.Animal;
import br.com.petsync.petsync.webservices.WebClient;

public class FormularioCadastroAnimalActivity extends AppCompatActivity {

    private FormularioCadastroAnimalHelper helper;

    private SimpleDateFormat dateFormatter;

    //User session manager class
    UserSessionManager session;

    Long clienteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_cadastro_animal);

        //Session class instance
        session = new UserSessionManager(getApplicationContext());

        if(session.checkLogin())
            finish();

        //get user data from session
        HashMap<String, String> user = session.getUserDetails();
        this.clienteId = Long.valueOf(user.get(UserSessionManager.KEY_ID));

        //Assim que criar o formulario.
        helper = new FormularioCadastroAnimalHelper(this);

        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cadastro_animal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_formulario_animal_ok:
                Animal animal = helper.getAnimal();
                try {
                    String data = helper.getDataNascimento();
                    Date dateParse = dateFormatter.parse(data);
                    animal.setDataNascimento(dateParse);
                    animal.setCliente(this.clienteId);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new CadsastroAnimalTask(this, animal).execute();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public class CadsastroAnimalTask extends AsyncTask {

        private ProgressDialog dialog;
        private Context context;
        private Animal animal;

        public CadsastroAnimalTask(Context context, Animal animal) {
            this.context = context;
            this.animal = animal;
        }

        @Override
        protected void onPreExecute() {
            this.dialog = ProgressDialog.show(this.context, "Aguarde", "Enviados dados para cadastro", true, true);
        }

        @Override
        protected Object doInBackground(Object[] params) {

            AnimalConverter converteAnimal = new AnimalConverter();
            String json = converteAnimal.converteParaJson(this.animal);

            WebClient client = new WebClient();
            String resposta = client.post(json, "http://www.petsync.com.br/api/animais");

            return resposta;
        }

        @Override
        protected void onPostExecute(Object resposta) {
            if(resposta != null) {
                dialog.dismiss();
                Toast.makeText(context, "Cadastro Realizado com Sucesso!", Toast.LENGTH_LONG).show();
            } else {
                dialog.dismiss();
                Toast.makeText(context, "Ocorreu um erro ao cadastrar o seu pet!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
