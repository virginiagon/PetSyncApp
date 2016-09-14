package br.com.petsync.petsync.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.petsync.petsync.R;
import br.com.petsync.petsync.model.Animal;

/**
 * Created by Virgínia on 12/09/2016.
 */
public class AnimalAdapter extends BaseAdapter {

    private final List<Animal> animais;
    private final Context context;

    public AnimalAdapter(Context context, List<Animal> animais) {
        this.animais = animais;
        this.context = context;
    }


    @Override
    public int getCount() {
        return this.animais.size();
    }

    @Override
    public Object getItem(int position) {
        return this.animais.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.animais.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Animal animal = this.animais.get(position);

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = convertView;
        if(view == null) {
            view = inflater.inflate(R.layout.lista_animais_item, parent, false);
        }

        TextView nome = (TextView) view.findViewById(R.id.item_animal_nome);
        nome.setText(animal.getNome());

        return view;
    }
}
