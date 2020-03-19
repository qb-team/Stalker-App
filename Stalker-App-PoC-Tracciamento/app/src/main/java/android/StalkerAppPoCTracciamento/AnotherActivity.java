package android.StalkerAppPoCTracciamento;
import android.StalkerAppPoCTracciamento.ListaOrganizzazioni;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.PolyUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class AnotherActivity extends AppCompatActivity {
    // Dichiarazione
    TextView Organizzazione;
    FirebaseAuth fAuth;
    int position;
    private String risposta;
    private TextView t;
    private TextView titolo;
    private TextView s;
    private Button bottoneAccessi;
    private LocationManager locationManager;
    private LocationListener listener;

    private Button b;
    private RequestQueue mQueue;
    final ArrayList<LatLng> poligono = new ArrayList<>();
    final LatLngBounds.Builder builder = new LatLngBounds.Builder();
    public  String [] listOrg= new String[3];
    public ArrayList <String> Accessi=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Inizio onCreate
        Intent intent = getIntent();
        Bundle bundle = this.getIntent().getExtras();
        //int pic = bundle.getInt("image");
        String aTitle = intent.getStringExtra("title");

        super.onCreate(savedInstanceState);
        if(aTitle.equals("Torre Archimede"))
        {
        setContentView(R.layout.activity_another_act);

        // Inizializzazione
        fAuth = FirebaseAuth.getInstance();
        t = (TextView) findViewById(R.id.text_view_result);
        b = (Button) findViewById(R.id.coordinate);
        s = (TextView) findViewById(R.id.TextAccessi);
        s.setMovementMethod(new ScrollingMovementMethod());

        bottoneAccessi=(Button) findViewById(R.id.storico);
        Organizzazione = findViewById(R.id.titleText);
        setArray();  // Invocazione
        Parse();    // Invocazione metodo per la lettura dei dati da file Json

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE); // Ottenimento servizi di localizzazione
        listener = new LocationListener() { // Inizio Inizializzazione listener
            @Override
            public void onLocationChanged(Location location) { // Viene invocato ogni volta che c'è un cambio della posizione o ogni tot millisecondi
                t.setText(" ");
                t.append("\n " + location.getLongitude() + " " + location.getLatitude());   //Stampa le tue coordinate attuali
                LatLng test = new LatLng(location.getLatitude(), location.getLongitude());
                boolean isInsideBoundary = builder.build().contains(test); // true se il test point è all'interno del confine
                boolean isInside = PolyUtil.containsLocation(test, poligono, true); // false se il punto è all'esterno del poligono
                if (isInsideBoundary == true && isInside == true )
                {   Date date = Calendar.getInstance().getTime();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                    String strDate = dateFormat.format(date);
                    Accessi.add(strDate);
                    for(Iterator<String> i = Accessi.iterator(); i.hasNext();) {
                       
                        if(Accessi.size()>1) i.remove();
                    }
                    t.append("\n" + "Sei dentro");

                }
                else
                    t.append("\n" + "Sei fuori");


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

        }; // Fine Inizializzazione listener

        configure_button(); // Chiama alla funzione configure_button

        //  Inzio Funzionalità per ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }


        for(int i=0;i<listOrg.length;i++)
        {
            if(position==i) {


                //imageView.setImageResource(pic);
                System.out.println(aTitle);
                Organizzazione.setText(aTitle);
                actionBar.setTitle(aTitle);
            }
        }
        //  Fine Funzionalità per ActionBar
        bottoneAccessi.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View view) {

                                                  String[] array = new String[Accessi.size()];
                                                  array = Accessi.toArray(array);
                                                  for(int i=0;i<array.length;i++){

                                                      s.append(array[i]+" "+"\n");
                                                  }



                                              }
                                          }
        );
    }

    }   // Fine onCreate

    // Metodo
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    // Metodo che richiama i vari permessi da far accettare all'utente
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }

    // Chiede il permesso per la localizzazione e gestisci la funzionalità del bottone "Scarica coordinate"
    void configure_button() {
        // Gestisce i permessi (come prima cosa chiede il permesso)
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }
        // questo codice non sarà eseguito se i permessi non sono accettati, perchè nella riga precedente c'è un return.
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // Funzionalità del bottone "Scarica coordinate" (quando viene cliccato)
                //noinspection MissingPermission
                try {
                    locationManager.requestLocationUpdates("gps", 15000, 0, listener);
                } catch (SecurityException e) {
                    e.getMessage();
                }
            }
        });
    }

    // implementazione metodo logout
    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();//logout
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    // Legge il contenuto del file Json e crea un poligono
    public void Parse() {
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());

        mQueue = new RequestQueue(cache, network);
        mQueue.start(); // Inizia la Queue (Coda)

        String url = "https://api.myjson.com/bins/17t4ai"; // Indirizzo dove è contenuto il file Json

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    risposta=response;
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
                },
                error -> {
                    System.out.println("Errore");
                });
        mQueue.add(stringRequest);


    }

    public void setCoordinate(double lat,double lon){  // Aggiunge le coordinate dei vertici del poligono
        poligono.add(new LatLng(lat, lon));
        System.out.println(lat+" "+lon);
    }
public  ArrayList<String> getData(){
System.out.println(Accessi.size());
        return Accessi;

}
    //Metodo
    public void setArray(){
        this.listOrg=ListaOrganizzazioni.getArray();
    }

}