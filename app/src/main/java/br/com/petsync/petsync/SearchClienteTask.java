package br.com.petsync.petsync;

import android.os.AsyncTask;

import br.com.petsync.petsync.converter.ClienteConverter;
import br.com.petsync.petsync.model.Cliente;
import br.com.petsync.petsync.webservices.WebClient;

/**
 * Created by Mult-e on 20/10/2016.
 */
public class SearchClienteTask  extends AsyncTask {

    private Cliente cliente;
    private Long clienteId;

    public SearchClienteTask(Long clienteId) {
        this.clienteId = clienteId;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        WebClient webClient2 = new WebClient();
        String jsonCliente = webClient2.getJsonFromUrl("http://www.petsync.com.br/api/clientes/" + this.clienteId);
        ClienteConverter clienteConverter = new ClienteConverter();
        this.cliente = clienteConverter.parseJsonForOneCliente(jsonCliente);

        return this.cliente;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }
}
