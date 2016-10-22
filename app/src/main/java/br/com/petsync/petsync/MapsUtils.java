package br.com.petsync.petsync;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.util.List;

import br.com.petsync.petsync.model.Cliente;
import br.com.petsync.petsync.model.Estabelecimento;

public class MapsUtils {

    private final Context context;

    public MapsUtils(Context context) {
        this.context = context;
    }

    public LatLng pegaCoordenadaDoEndereco(String endereco) {
        LatLng posicao = null;
        try {
            Geocoder geocoder = new Geocoder(this.context);
            List<Address> resultados = geocoder.getFromLocationName(endereco, 1);

            if(!resultados.isEmpty()) {
                posicao = new LatLng(resultados.get(0).getLatitude(), resultados.get(0).getLongitude());
                //return posicao;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return posicao;
    }

    public double calculateKM(Cliente cliente, Estabelecimento estabelecimento) {
        double distance = 0;

        LatLng posicaoInicial = pegaCoordenadaDoEndereco(cliente.getEndereco() + ", " + cliente.getCidade() );
        LatLng posicaoFinal = pegaCoordenadaDoEndereco(estabelecimento.getAddress() + ", " + estabelecimento.getCity());

        distance = SphericalUtil.computeDistanceBetween(posicaoInicial, posicaoFinal) /1000.0f;

        return distance;
    }

}
