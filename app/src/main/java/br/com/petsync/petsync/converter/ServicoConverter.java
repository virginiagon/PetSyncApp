package br.com.petsync.petsync.converter;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.petsync.petsync.model.Estabelecimento;
import br.com.petsync.petsync.model.Servico;

/**
 * Created by Virgínia on 07/09/2016.
 */
public class ServicoConverter {

    public List<Servico> ParseJSON(String json, Estabelecimento estabelecimento) {
        if (json != null) {
            try {
                // Hashmap for ListView
                //ArrayList<HashMap<String, String>> studentList = new ArrayList<HashMap<String, String>>();

                List<Servico> servicoList = new ArrayList<Servico>();

                JSONObject jsonObj = new JSONObject(json);

                // Getting JSON Object embedded
                JSONObject embedded = jsonObj.getJSONObject("_embedded");

                //Get JSON Array estabelecimentos
                JSONArray servicos = embedded.getJSONArray("servicos");

                // looping through All Students
                for (int i = 0; i < servicos.length(); i++) {
                    JSONObject object = servicos.getJSONObject(i);

                    Long idEstabelecimento = object.getLong("estabelecimento_id");

                    if(idEstabelecimento.longValue() == estabelecimento.getId().longValue()) {
                        Servico servico = new Servico();
                        servico.setId(object.getLong("id"));
                        servico.setNome(object.getString("nome"));
                        servico.setDescricao(object.getString("descricao"));
                        servico.setValor(object.getDouble("valor"));

                        // adding estabelecimento to estabelecimento list
                        servicoList.add(servico);
                    }
                }

                return servicoList;

            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            Log.e("ServiceHandler", "No data received from HTTP request");
            return null;
        }
    }

}
