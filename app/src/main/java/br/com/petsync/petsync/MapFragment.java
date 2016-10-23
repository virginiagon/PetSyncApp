package br.com.petsync.petsync;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.petsync.petsync.map.DirectionFinder;
import br.com.petsync.petsync.map.DirectionFinderListener;
import br.com.petsync.petsync.map.Route;
import br.com.petsync.petsync.model.Estabelecimento;

public class MapFragment extends SupportMapFragment implements OnMapReadyCallback, DirectionFinderListener {

    private Estabelecimento estabelecimento;
    private String enderecoCliente;
    private GoogleMap mMap;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinesPaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    //User session manager class
    UserSessionManager session;
    private String cep;

    public MapFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Prepara uma instancia do Google Maps
        getMapAsync(this); //this é o proprio fragment

        //Session class instance
        session = new UserSessionManager(getContext());

        HashMap<String, String> user = session.getUserDetails();
        this.cep = session.getCep();
        Toast.makeText(getContext(), "Session = " + session.getCep(), Toast.LENGTH_LONG).show();

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
            if(this.cep == null) {
                new DirectionFinder(this, enderecoCliente, enderecoEstabelecimento).execute();
            }else {
                new DirectionFinder(this, this.cep, enderecoEstabelecimento).execute();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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
