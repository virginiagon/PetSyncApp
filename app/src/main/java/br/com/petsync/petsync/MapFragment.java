package br.com.petsync.petsync;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.Serializable;
import java.util.ArrayList;

import br.com.petsync.petsync.model.Cliente;
import br.com.petsync.petsync.model.Estabelecimento;

/**
 * Created by Mult-e on 16/10/2016.
 */
public class MapFragment extends SupportMapFragment implements OnMapReadyCallback {

    private Estabelecimento estabelecimento;
    private String enderecoCliente;

    public MapFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Prepara uma instancia do Google Maps
        getMapAsync(this); //this é o proprio fragment

        Bundle extras = getActivity().getIntent().getExtras();
        this.estabelecimento = (Estabelecimento) extras.getSerializable("estabelecimento");
        this.enderecoCliente = (String) extras.getSerializable("enderecoCliente");

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        ArrayList<LatLng> points = new ArrayList<>();
        PolylineOptions polylineOptions = new PolylineOptions();

        MapsUtils mapUtil = new MapsUtils(getContext());

        LatLng posicao = mapUtil.pegaCoordenadaDoEndereco(this.estabelecimento.getAddress() + ", " + this.estabelecimento.getCity() + ", " + this.estabelecimento.getState());

        MarkerOptions marcadorEstabelecimento = new MarkerOptions();
        marcadorEstabelecimento.position(posicao);

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(posicao, 17);
        googleMap.moveCamera(update);

        LatLng posicaoCliente = mapUtil.pegaCoordenadaDoEndereco(enderecoCliente);

        MarkerOptions marcador = new MarkerOptions();
        marcador.position(posicaoCliente);

        //adiciona o marcador no mapa
        googleMap.addMarker(marcador);
        googleMap.addMarker(marcadorEstabelecimento);

        points.add(posicao);
        points.add(posicaoCliente);

        polylineOptions.addAll(points);
        polylineOptions.width(10);
        polylineOptions.color(Color.GREEN);

        googleMap.addPolyline(polylineOptions);

        /*String url = RoadProvider
                .getUrl(fromLat, fromLon, toLat, toLon);
        InputStream is = getConnection(url);
        mRoad = RoadProvider.getRoute(is);
        mHandler.sendEmptyMessage(0);*/

    }
}
