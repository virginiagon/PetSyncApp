package br.com.petsync.petsync;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;

import br.com.petsync.petsync.model.Cliente;
import br.com.petsync.petsync.model.Estabelecimento;

public class MapsActivity extends AppCompatActivity {

    private GoogleMap mMap;
    private Estabelecimento estabelecimento;
    private String enderecoCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent intent = getIntent();
        this.estabelecimento = (Estabelecimento) intent.getSerializableExtra("estabelecimento");
        this.enderecoCliente = (String) intent.getSerializableExtra("enderecoCliente");

        Bundle argument = new Bundle();
        argument.putSerializable("estabelecimento", this.estabelecimento);
        argument.putSerializable("enderecoCliente", this.enderecoCliente);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction tx = manager.beginTransaction();
        MapFragment mapFragment = new MapFragment();
        mapFragment.setArguments(argument);
        tx.replace(R.id.frame_mapa, new MapFragment());
        tx.commit();

        /*// Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    /*@Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        MapsUtils util = new MapsUtils(MapsActivity.this);
        LatLng posicao = util.pegaCoordenadaDoEndereco(this.estabelecimento.getAddress() + "," + this.estabelecimento.getCity() + "," + this.estabelecimento.getState());
        mMap.addMarker(new MarkerOptions().position(posicao)); //.title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posicao, 17));
    }*/




}
