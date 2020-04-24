package qbteam.stalkerapp.ui.main;

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

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import qbteam.stalkerapp.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class Organizzazione extends RootFragment {
    private static Organizzazione instance = null;
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
        super.onPrepareOptionsMenu(menu);
        MenuItem menuItem = menu.findItem(R.id.cercaID);
        MenuItem menuItem2 = menu.findItem(R.id.ordina);
        if(menuItem!=null)
            menuItem.setVisible(false);
        if(menuItem2!=null)
            menuItem2.setVisible(false);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        menu.clear();
        inflater.inflate(R.menu.cerca_organizzazione, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id= item.getItemId();
        if(id==R.id.preferitiID){
            Bundle bundle1=this.getArguments();
            boolean aggiunto=true;
            if(bundle1!=null){

                try {
                   ListaPreferiti.getInstance().aggiungiOrganizzazione(bundle1.getString("nomeOrganizzazione"));
                    if(aggiunto==true)

                        Toast.makeText(getActivity(),"Aggiunta organizzazione ai preferiti",Toast.LENGTH_SHORT).show();
                    else

                        Toast.makeText(getActivity(),"Hai già aggiunto questa organizzazione ai preferiti", Toast.LENGTH_SHORT).show();
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
        View view=inflater.inflate(R.layout.fragment_organizzazione, container, false);
        /////////////////////////////////////////////////////////////////////////////////////
        titolo=view.findViewById(R.id.titleID);
        risultati=view.findViewById(R.id.coordinateID);
        mostra=view.findViewById(R.id.mostraID);
        ////////////////////////////////////////////////////////////////////////

        MostraNome();
        checkPermission();
        InserisciCoordinate();


        /////////// LISTENER ////////////////
        mostra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                localizzazione();
                posizione();
            }
        });
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

    public static Organizzazione getInstance() {
        return instance;
    }

    /*
    public void Parse() {
        Cache cache = new DiskBasedCache(getContext().getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());

        mQueue = new RequestQueue(cache, network);
        mQueue.start(); // Inizia la Queue (Coda)

        String url = "https://api.myjson.com/bins/17t4ai"; // Indirizzo dove è contenuto il file Json

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        risposta = response;
                        //  Parsing e creazione del poligono
                        try {

                            JSONObject jObject = new JSONObject(risposta);
                            JSONArray jsonArray = jObject.getJSONArray("Organizzazioni");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject org = jsonArray.getJSONObject(i);
                                String organizzazione1 = org.getString("lat");
                                String organizzazione2 = org.getString("long");
                                double o1 = Double.parseDouble(organizzazione1);
                                double o2 = Double.parseDouble(organizzazione2);
                                setCoordinate(o1, o2);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        for (LatLng point : poligono) {
                            builder.include(point);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                    }
                });
        mQueue.add(stringRequest);


    }
    public void setCoordinate(double lat,double lon){  // Aggiunge le coordinate dei vertici del poligono
        poligono.add(new LatLng(lat, lon));
        System.out.println(lat+" "+lon);
    }
    */


}