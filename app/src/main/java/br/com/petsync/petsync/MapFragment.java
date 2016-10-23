package br.com.petsync.petsync;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import br.com.petsync.petsync.map.DirectionFinder;
import br.com.petsync.petsync.map.DirectionFinderListener;
import br.com.petsync.petsync.map.Route;
import br.com.petsync.petsync.model.Cliente;
import br.com.petsync.petsync.model.Estabelecimento;

/**
 * Created by Mult-e on 16/10/2016.
 */
public class MapFragment extends SupportMapFragment implements OnMapReadyCallback, DirectionFinderListener {

    private Estabelecimento estabelecimento;
    private String enderecoCliente;
    private GoogleMap mMap;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinesPaths = new ArrayList<>();
    private ProgressDialog progressDialog;

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

        mMap = googleMap;

        MapsUtils mapUtil = new MapsUtils(getContext());

        String enderecoEstabelecimento = this.estabelecimento.getAddress() + ", " + this.estabelecimento.getCity() + ", " + this.estabelecimento.getState();

        try {
            new DirectionFinder(this, enderecoCliente, enderecoEstabelecimento).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /*ArrayList<LatLng> points = new ArrayList<>();
        PolylineOptions polylineOptions = new PolylineOptions();

        MapsUtils mapUtil = new MapsUtils(getContext());

        LatLng posicaoEstabelecimento = mapUtil.pegaCoordenadaDoEndereco(this.estabelecimento.getAddress() + ", " + this.estabelecimento.getCity() + ", " + this.estabelecimento.getState());
        String enderecoEstabelecimento = this.estabelecimento.getAddress() + ", " + this.estabelecimento.getCity() + ", " + this.estabelecimento.getState();

        LatLng posicaoCliente = mapUtil.pegaCoordenadaDoEndereco(enderecoCliente);

        /*MarkerOptions marcadorEstabelecimento = new MarkerOptions();
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

        googleMap.addPolyline(polylineOptions);*/

        /*String url = RoadProvider
                .getUrl(fromLat, fromLon, toLat, toLon);
        InputStream is = getConnection(url);
        mRoad = RoadProvider.getRoute(is);
        mHandler.sendEmptyMessage(0);*/

    }

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(getContext(), "Por favor, espere.", "Traçando a rota", true);

        if(originMarkers != null) {
            for(Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if(destinationMarkers != null) {
            for(Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if(polylinesPaths != null) {
            for(Polyline polyline : polylinesPaths) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {

        progressDialog.dismiss();
        polylinesPaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for(Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));

            originMarkers.add(mMap.addMarker(new MarkerOptions().position(route.startLocation)));
            originMarkers.add(mMap.addMarker(new MarkerOptions().position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().geodesic(true).color(Color.GREEN).width(10);

            for(int i = 0; i < route.points.size(); i++) {
                polylineOptions.add(route.points.get(i));
            }

            polylinesPaths.add(mMap.addPolyline(polylineOptions));
        }

    }
}
