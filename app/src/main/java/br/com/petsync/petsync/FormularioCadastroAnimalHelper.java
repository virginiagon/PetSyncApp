package br.com.petsync.petsync;

import android.widget.EditText;

import br.com.petsync.petsync.R;
import br.com.petsync.petsync.model.Animal;

/**
 * Created by Virgínia on 14/08/2016.
 */
public class FormularioCadastroAnimalHelper {

    private final EditText campoNome;
    private final EditText campoRaca;
    private final EditText campoDataNascimento;
    private final EditText campoDoencas;

    private Animal animal;
    
    public FormularioCadastroAnimalHelper(FormularioCadastroAnimalActivity activity) {
        campoNome = (EditText) activity.findViewById(R.id.cadastro_animal_nome);
        campoRaca = (EditText) activity.findViewById(R.id.cadastro_animal_raca);
        campoDataNascimento = (EditText) activity.findViewById(R.id.cadastro_animal_data_nascimento);
        campoDoencas = (EditText) activity.findViewById(R.id.cadastro_animal_doencas);


        animal = new Animal();
    }

    public Animal getAnimal() {
        animal.setNome(campoNome.getText().toString());
        animal.setRaca(campoRaca.getText().toString());
        animal.setDoencas(campoDoencas.getText().toString());
        animal.setStatus(true);

        return animal;
    }

    public String getDataNascimento() {
        return campoDataNascimento.getText().toString();
    }
}
