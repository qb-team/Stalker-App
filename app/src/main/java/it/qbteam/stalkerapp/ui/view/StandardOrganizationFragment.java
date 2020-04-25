package it.qbteam.stalkerapp.ui.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import it.qbteam.stalkerapp.R;
import it.qbteam.stalkerapp.tools.BackPressImplementation;
import it.qbteam.stalkerapp.tools.OnBackPressListener;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.PolyUtil;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class StandardOrganizationFragment extends Fragment implements OnBackPressListener {
    private static StandardOrganizationFragment instance = null;
    final ArrayList<LatLng> poligono = new ArrayList<>();
    private LocationManager locationManager;
    private LocationListener listener;
    LatLngBounds.Builder builder = new LatLngBounds.Builder();
    ///////////////////////////////////////////////////////
    TextView titolo;
    TextView risultati;
    Button mostra;
    //////////////////////////////////////////////////////

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        instance=this;
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        menu.clear();
        inflater.inflate(R.menu.aggiungipreferiti, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id= item.getItemId();
        if(id==R.id.preferitiID){
            Bundle bundle1=this.getArguments();

            if(bundle1!=null){

                try {
                   MyStalkersListFragment.getInstance().aggiungiOrganizzazione(bundle1.getString("nomeOrganizzazione"));

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("Creata organizzazione");
        View view=inflater.inflate(R.layout.fragment_organization, container, false);

        titolo=view.findViewById(R.id.titleID);
        risultati=view.findViewById(R.id.coordinateID);



        MostraNome();
        checkPermission();
        InserisciCoordinate();



        /////////// FINE LISTENER ////////////////

        return view;
    }

    public void posizione() {
        System.out.println("poligono");
        try {
            locationManager.requestLocationUpdates("gps", 15000, 0, listener);
        } catch (SecurityException e) {
            e.getMessage();
        }
    }

    public void localizzazione(){
        System.out.println("è entrato dentro localizzazione");
        locationManager = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE); // Ottenimento servizi di localizzazione
        listener = new LocationListener() { // Inizio Inizializzazione listener
            @Override
            public void onLocationChanged(Location location) { // Viene invocato ogni volta che c'è un cambio della posizione o ogni tot millisecondi
                System.out.println("il poligono sta facendo cose");
                risultati.setText(" ");
                risultati.append("\n " + location.getLongitude() + " " + location.getLatitude());   //Stampa le tue coordinate attuali
                LatLng test = new LatLng(location.getLatitude(), location.getLongitude());
                System.out.println(poligono);
                boolean isInsideBoundary = builder.build().contains(test); // true se il test point è all'interno del confine
                boolean isInside = PolyUtil.containsLocation(test, poligono, true); // false se il punto è all'esterno del poligono
                if (isInsideBoundary && isInside == true )
                {
                    risultati.append("\n" + "Sei dentro");
                    System.out.println("Sei dentro");

                }
                else {
                    risultati.append("\n" + "Sei fuori");
                    System.out.println("Sei fuori");
                }
            }

            // Metodi utili a listener
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }

        };
    }

    public void InserisciCoordinate(){  // Aggiunge le coordinate dei vertici del poligono
        System.out.println("è entrato qui dentro");
        poligono.add(new LatLng(45.4139244815,11.8809040336));
        poligono.add(new LatLng(45.4137732038,11.8812763624));
        poligono.add(new LatLng(45.4134925404,11.8810503718));
        poligono.add(new LatLng(45.4136378199,11.8806753327));


        for (LatLng point : poligono) {
            builder.include(point);
        }
        System.out.println("creo builder:  " + builder);
    }

    void checkPermission() {//DA CAPIRE GLI IF!!!!
        // Gestisce i permessi (come prima cosa chiede il permesso)
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }}

    public void MostraNome(){
        Bundle bundle=this.getArguments();
        if(bundle!=null){
            titolo.setText(bundle.getString("nomeOrganizzazione"));
        }
    }

    public static StandardOrganizationFragment getInstance() {
        return instance;
    }

    @Override
    public boolean onBackPressed() {
        return new BackPressImplementation(this).onBackPressed();
    }


}