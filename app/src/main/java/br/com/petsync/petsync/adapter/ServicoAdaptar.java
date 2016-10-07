package br.com.petsync.petsync.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;

import br.com.petsync.petsync.R;
import br.com.petsync.petsync.model.Servico;

/**
 * Created by Virgínia on 05/09/2016.
 */
public class ServicoAdaptar extends BaseAdapter {

    private final List<Servico> servicos;
    private final Context context;

    public ServicoAdaptar(Context context, List<Servico> servicos) {
        this.servicos = servicos;
        this.context = context;
    }


    @Override
    public int getCount() {
        return this.servicos.size();
    }

    @Override
    public Object getItem(int position) {
        return this.servicos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.servicos.get(position).getId();
    }

    //Método invocado para montar a lista.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Servico servico = this.servicos.get(position);

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = convertView;
        if(view == null) {
            //layout da lista de serviços
            view = inflater.inflate(R.layout.lista_servico_item, parent, false);
        }

        TextView nome = (TextView) view.findViewById(R.id.servico_item_nome);
        nome.setText(servico.getNome());

        TextView valor = (TextView) view.findViewById(R.id.servico_item_valor);
        valor.setText("R$ " + servico.getValor());
        /*valor.setText((int) servico.getValor());*/

        return view;
    }
}
