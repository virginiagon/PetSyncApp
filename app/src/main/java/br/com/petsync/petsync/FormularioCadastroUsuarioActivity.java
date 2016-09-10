package br.com.petsync.petsync;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import br.com.petsync.petsync.model.Cliente;
import br.com.petsync.petsync.model.Usuario;

public class FormularioCadastroUsuarioActivity extends AppCompatActivity {

    private FormularioCadastroClienteHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_cadastro_usuario);

        //Assim que criar o formulario.
        helper = new FormularioCadastroClienteHelper(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cadastro_usuario, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_formulario_ok:
                Cliente cliente = helper.getCliente();
                Usuario usuario = helper.getUsuario();
                new CadastroUsuarioTask(this, cliente, usuario).execute();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
