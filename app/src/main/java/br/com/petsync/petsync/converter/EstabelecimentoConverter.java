package br.com.petsync.petsync.converter;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.petsync.petsync.model.Estabelecimento;

/**
 * Created by Virgínia on 11/07/2016.
 */
public class EstabelecimentoConverter {

    public List<Estabelecimento> ParseJSON(String json) {
        if (json != null) {
            try {
                // Hashmap for ListView
                //ArrayList<HashMap<String, String>> studentList = new ArrayList<HashMap<String, String>>();

                List<Estabelecimento> estabelecimentoList = new ArrayList<Estabelecimento>();

                JSONObject jsonObj = new JSONObject(json);

                // Getting JSON Object embedded
                JSONObject embedded = jsonObj.getJSONObject("_embedded");

                //Get JSON Array estabelecimentos
                JSONArray establishments = embedded.getJSONArray("estabelecimentos");

                // looping through All Students
                for (int i = 0; i < establishments.length(); i++) {
                    JSONObject object = establishments.getJSONObject(i);

                    Estabelecimento estabelecimento = new Estabelecimento();
                    estabelecimento.setId(object.getLong("id"));
                    estabelecimento.setName(object.getString("nome"));
                    estabelecimento.setCompanyName(object.getString("razao_social"));
                    estabelecimento.setCnpj(object.getString("cnpj"));
                    estabelecimento.setNameResponsible(object.getString("nome_responsavel"));
                    estabelecimento.setAddress(object.getString("endereco"));
                    estabelecimento.setZipCode(object.getString("cep"));
                    estabelecimento.setCity(object.getString("cidade"));
                    estabelecimento.setState(object.getString("estado"));
                    estabelecimento.setPhone(object.getString("telefone"));
                    estabelecimento.setSite(object.getString("site"));
                    //estabelecimento.setDate(object.getString("data_cadastro"));
                    //estabelecimento.setStatus(object.getBoolean("status"));

                    // Phone node is JSON Object
                    /*JSONObject phone = c.getJSONObject(TAG_STUDENT_PHONE);
                    String mobile = phone.getString(TAG_STUDENT_PHONE_MOBILE);
                    String home = phone.getString(TAG_STUDENT_PHONE_HOME);*/

                    // adding estabelecimento to estabelecimento list
                    estabelecimentoList.add(estabelecimento);
                }

                return estabelecimentoList;

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
