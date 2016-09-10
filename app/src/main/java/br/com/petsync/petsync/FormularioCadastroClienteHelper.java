package br.com.petsync.petsync;

import android.widget.EditText;

import br.com.petsync.petsync.model.Cliente;
import br.com.petsync.petsync.model.Usuario;

/**
 * Created by Virgínia on 14/08/2016.
 */
public class FormularioCadastroClienteHelper {

    private final EditText campoNome;
    private final EditText campoEndereco;
    private final EditText campoCep;
    private final EditText campoCidade;
    private final EditText campoEstado;
    private final EditText campoTelefone;
    private final EditText campoEmail;
    private final EditText campoSenha;

    private Cliente cliente;
    private Usuario usuario;

    public FormularioCadastroClienteHelper(FormularioCadastroUsuarioActivity activity) {
        campoNome = (EditText) activity.findViewById(R.id.cadastro_usuario_nome);
        campoEndereco = (EditText) activity.findViewById(R.id.cadastro_usuario_endereco);
        campoCep = (EditText) activity.findViewById(R.id.cadastro_usuario_cep);
        campoCidade = (EditText) activity.findViewById(R.id.cadastro_usuario_cidade);
        campoEstado = (EditText) activity.findViewById(R.id.cadastro_usuario_estado);
        campoTelefone = (EditText) activity.findViewById(R.id.cadastro_usuario_telefone);
        campoEmail = (EditText) activity.findViewById(R.id.cadastro_usuario_email);
        campoSenha = (EditText) activity.findViewById(R.id.cadastro_usuario_senha);

        cliente = new Cliente();
        usuario = new Usuario();
    }

    public Cliente getCliente() {
        cliente.setNome(campoNome.getText().toString());
        cliente.setEndereco(campoEndereco.getText().toString());
        cliente.setCep(campoCep.getText().toString());
        cliente.setCidade(campoCidade.getText().toString());
        cliente.setEstado(campoEstado.getText().toString());
        cliente.setTelefone(campoTelefone.getText().toString());
        cliente.setStatus(true);

        return cliente;
    }

    public Usuario getUsuario() {
        usuario.setEmail(campoEmail.getText().toString());
        usuario.setSenha(campoSenha.getText().toString());

        return usuario;
    }
}
