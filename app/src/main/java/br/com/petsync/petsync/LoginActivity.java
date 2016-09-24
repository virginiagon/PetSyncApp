package br.com.petsync.petsync;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.com.petsync.petsync.converter.ClienteConverter;
import br.com.petsync.petsync.converter.UsuarioConverter;
import br.com.petsync.petsync.model.Cliente;
import br.com.petsync.petsync.model.Usuario;
import br.com.petsync.petsync.webservices.WebClient;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;

    EditText txtEmail, txtPassword;

    //User session manager class
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //User Session Manager
        session = new UserSessionManager(getApplicationContext());

        //get Email and Password input text
        txtEmail = (EditText) findViewById(R.id.login_email);
        txtPassword = (EditText) findViewById(R.id.login_senha);

        Toast.makeText(getApplicationContext(), "User Login Status: " + session.isUserLoggedIn(), Toast.LENGTH_LONG).show();

        //User Login button
        btnLogin = (Button) findViewById(R.id.login_btn);

        //Login button click event
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get email, password from editText
                String userEmail = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();

                //Validate if email and password is filled
                if(userEmail.trim().length() > 0 && password.trim().length() > 0) {
                    new SearchUsuarioTask(LoginActivity.this, userEmail, password).execute();
                } else {
                    //user didn't entered username or password
                    Toast.makeText(getApplicationContext(), "Please enter username and password", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public class SearchUsuarioTask extends AsyncTask{

        private Context context;
        private String email;
        private String password;
        private Usuario usuario;
        private Cliente cliente;

        public SearchUsuarioTask(Context context, String email, String password) {
            this.context = context;
            this.email = email;
            this.password = password;
        }

        @Override
        protected Object doInBackground(Object[] params) {

            WebClient webClient = new WebClient();
            String json = webClient.getJsonFromUrl("http://www.petsync.com.br/api/usuarios/" + this.email);

            UsuarioConverter converter = new UsuarioConverter();
            this.usuario = converter.parseJsonForOneUsuario(json);

            WebClient webClient2 = new WebClient();
            String jsonCliente = webClient2.getJsonFromUrl("http://www.petsync.com.br/api/clientes/" + this.usuario.getCliente());
            ClienteConverter clienteConverter = new ClienteConverter();
            this.cliente = clienteConverter.parseJsonForOneCliente(jsonCliente);

            return usuario;
        }

        @Override
        protected void onPostExecute(Object o) {

            String passwordCriptografada = usuario.criptografaSenhaMD5(this.password);

            //For testing purpose email, password is checked with static data
            if(this.email.equals(this.usuario.getEmail()) && passwordCriptografada.equals(this.usuario.getSenha()) && this.usuario.getStatus()) {
                //Creating user login session
                session.createUserLoginSession(String.valueOf(this.usuario.getCliente()), this.cliente.getNome(), this.usuario.getEmail());

                //starting ListEstabelecimentosActivity
                Intent i = new Intent(getApplicationContext(), ListEstabelecimentosActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                //Add new Flag to start new Activity
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

                finish();
            } else {
                //user name/password doesn't match
                Toast.makeText(getApplicationContext(), "Email/password is incorrect", Toast.LENGTH_LONG).show();
            }

        }
    }


}
