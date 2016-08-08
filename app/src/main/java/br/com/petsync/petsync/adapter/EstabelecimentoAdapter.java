package br.com.petsync.petsync.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.petsync.petsync.R;
import br.com.petsync.petsync.model.Estabelecimento;

/**
 * Created by Virgínia on 12/07/2016.
 */
public class EstabelecimentoAdapter extends BaseAdapter {

    private final List<Estabelecimento> estabelecimentos;
    private final Context context;


    public EstabelecimentoAdapter(Context context, List<Estabelecimento> estabelecimentos) {
        this.context = context;
        this.estabelecimentos = estabelecimentos;
    }

    @Override
    public int getCount() {
        return this.estabelecimentos.size();
    }

    @Override
    public Object getItem(int position) {
        return this.estabelecimentos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.estabelecimentos.get(position).getId();
    }

    //Método invocado para montar a lista.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Estabelecimento estabelecimento = this.estabelecimentos.get(position);

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = convertView;
        if(view == null) {
            view = inflater.inflate(R.layout.lista_estabelecimento_item, parent, false);
        }

        TextView nome = (TextView) view.findViewById(R.id.item_nome);
        nome.setText(estabelecimento.getName());

        TextView telefone = (TextView) view.findViewById(R.id.item_telefone);
        telefone.setText(estabelecimento.getPhone());

        return view;
    }
}
