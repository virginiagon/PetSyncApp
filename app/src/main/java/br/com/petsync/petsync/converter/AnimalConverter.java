package br.com.petsync.petsync.converter;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.petsync.petsync.model.Animal;

/**
 * Created by Virgínia on 11/07/2016.
 */
public class AnimalConverter {

    public List<Animal> ParseJSON(String json) {
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

                    Animal animal = new Animal();
                    animal.setId(object.getLong("id"));
                    animal.setNome(object.getString("nome"));
                    animal.setRaca(object.getString("raca"));

                    // adding estabelecimento to estabelecimento list
                    animalList.add(animal);
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


}
