package br.com.petsync.petsync.converter;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.petsync.petsync.model.Animal;

/**
 * Created by Virgínia on 11/07/2016.
 */
public class AnimalConverter {

    private SimpleDateFormat dateFormatter;

    public List<Animal> ParseJSON(String json, Long clienteId) {
        if (json != null) {
            try {
                List<Animal> animalList = new ArrayList<Animal>();

                JSONObject jsonObj = new JSONObject(json);

                // Getting JSON Object embedded
                JSONObject embedded = jsonObj.getJSONObject("_embedded");

                //Get JSON Array animais
                JSONArray animais = embedded.getJSONArray("animais");

                // looping through All Students
                for (int i = 0; i < animais.length(); i++) {
                    JSONObject object = animais.getJSONObject(i);

                    if(clienteId.equals(object.getLong("cliente_id"))) {
                        Animal animal = new Animal();
                        animal.setId(object.getLong("id"));
                        animal.setNome(object.getString("nome"));
                        animal.setRaca(object.getString("raca"));
                        // adding estabelecimento to estabelecimento list
                        animalList.add(animal);
                    }
                }

                return animalList;

            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            Log.e("ServiceHandler", "No data received from HTTP request");
            return null;
        }
    }

    public String converteParaJson(Animal animal) {

        JSONStringer js = new JSONStringer();

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String dataFormatada = dateFormatter.format(animal.getDataNascimento());

        try {
            js.object();
            js.key("cliente_id").value(animal.getCliente());
            js.key("nome").value(animal.getNome());
            js.key("raca").value(animal.getRaca());
            js.key("data_nascimento").value(dataFormatada);
            js.key("peso").value(animal.getPeso());
            js.key("doencas").value(animal.getDoencas());
            js.key("status").value(animal.getStatus());
            js.endObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return js.toString();

    }
}
