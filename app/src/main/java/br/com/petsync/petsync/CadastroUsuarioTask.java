package br.com.petsync.petsync;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import br.com.petsync.petsync.converter.ClienteConverter;
import br.com.petsync.petsync.converter.UsuarioConverter;
import br.com.petsync.petsync.model.Cliente;
import br.com.petsync.petsync.model.Usuario;
import br.com.petsync.petsync.webservices.WebClient;

/**
 * Created by Virgínia on 14/08/2016.
 */
public class CadastroUsuarioTask extends AsyncTask {

    private Context context;
    private ProgressDialog dialog;
    private Cliente cliente;
    private Usuario usuario;

    public CadastroUsuarioTask(Context context, Cliente cliente, Usuario usuario) {
        this.context = context;
        this.cliente = cliente;
        this.usuario = usuario;
    }

    @Override
    protected void onPreExecute() {
        this.dialog = ProgressDialog.show(context, "Aguarde", "Enviados dados para cadastro", true, true);
    }

    @Override
    protected Object doInBackground(Object[] params) {

        ClienteConverter conversor = new ClienteConverter();
        String json = conversor.converteParaJson(this.cliente);

        WebClient client = new WebClient();
        //cadastra os clientes
        String resposta = client.post(json, "http://www.petsync.com.br/api/clientes");

        if(resposta != null) {
            //Após cadastrar o cliente, pega o id do cliente para cadastrar o usuário
            UsuarioConverter converteUsuario = new UsuarioConverter();
            Long idCliente = converteUsuario.parseJsonOneObject(resposta);

            this.usuario.setCliente(idCliente);
            this.usuario.setStatus(true);
            this.usuario.setTipo("C");

            String jsonUser = converteUsuario.converteParaJson(this.usuario);

            //cadastra o user
            WebClient user = new WebClient();
            String response = user.post(jsonUser, "http://www.petsync.com.br/api/usuarios");
        }
        return resposta;
    }

    @Override
    protected void onPostExecute(Object resposta) {
        if(resposta != null) {
            dialog.dismiss();
            Toast.makeText(context, "Cadastro Realizado com Sucesso!", Toast.LENGTH_LONG).show();
        } else {
            dialog.dismiss();
            Toast.makeText(context, "Ocorreu um erro ao cadastrar o usuário!", Toast.LENGTH_LONG).show();
        }

    }
}
