package br.com.petsync.petsync.converter;

import org.json.JSONException;
import org.json.JSONStringer;

import br.com.petsync.petsync.model.Cliente;

/**
 * Created by Virgínia on 14/08/2016.
 */
public class ClienteConverter {

    public String converteParaJson(Cliente cliente) {

        JSONStringer js = new JSONStringer();

        try {
            js.object();
            js.key("nome").value(cliente.getNome());
            js.key("endereco").value(cliente.getEndereco());
            js.key("cep").value(cliente.getCep());
            js.key("cidade").value(cliente.getCidade());
            js.key("estado").value(cliente.getEstado());
            js.key("telefone").value(cliente.getTelefone());
            js.key("status").value(cliente.getStatus());
            js.endObject();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return js.toString();

    }

}
