package br.com.petsync.petsync.converter;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.List;

import br.com.petsync.petsync.model.Cliente;
import br.com.petsync.petsync.model.Usuario;

/**
 * Created by Virgínia on 22/08/2016.
 */
public class UsuarioConverter {

    /**
     * Converte o objeto usuário para json para ser gravado no banco de dados.
     * @param usuario Usuario
     * @return String json
     */
    public String converteParaJson(Usuario usuario) {

        JSONStringer js = new JSONStringer();

        try {
            js.object();
            js.key("cliente_id").value(usuario.getCliente());
            js.key("email").value(usuario.getEmail());
            js.key("senha").value(usuario.getSenha());
            js.key("status").value(usuario.getStatus());
            js.key("tipo").value(usuario.getTipo());
            js.endObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return js.toString();

    }

    /**
     * Metódo utilizado no cadastro do cliente/usuário.
     * Faz o parse do retorno do cadastro do cliente e retorna somente o id do cliente.
     * @param json String
     * @return Long id
     */
    public Long parseJsonOneObject(String json) {
        Long id = null;
        try {
            JSONObject jsonObj = new JSONObject(json);
            id = jsonObj.getLong("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return id;
    }

    /**
     * Método utilizado no login. Busca no arquivo json o usuário que tenha o tipo "C" e tenha o
     * e-mail igual passado no parametro
     * @param json String
     * @return Object Usuario.
     */
    public Usuario parseJsonForOneUsuario(String json) {
        Usuario usuario = new Usuario();
        if (json != null) {
            try {
                JSONObject jsonObj = new JSONObject(json);

                usuario.setId(jsonObj.getLong("id"));
                usuario.setCliente(jsonObj.getLong("cliente_id"));
                usuario.setEmail(jsonObj.getString("email"));
                usuario.setSenha(jsonObj.getString("senha"), false);
                usuario.setTipo(jsonObj.getString("tipo"));
                boolean status = jsonObj.getLong("status") == 1 ? true : false;
                usuario.setStatus(status);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            Log.e("ServiceHandler", "No data received from HTTP request");
            return null;
        }
        return usuario;
    }

}
