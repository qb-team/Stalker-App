package android.StalkerAppPoCTracciamento;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ListaOrganizzazioni extends AppCompatActivity {
    // Dichiarazione
    ListView listaOrg;
    FirebaseAuth fAuth;
    private RequestQueue mQueue;
    private String risposta;
    Button aggiorna;
    static String Organizzazione[]=new String[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) { // Inizio onCreate
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_organizzazioni);

        // Inizializzazione
        fAuth = FirebaseAuth.getInstance();
        aggiorna=findViewById(R.id.Aggiorna);
        listaOrg =  findViewById(R.id.ListaOrg);

        MyAdapter adapter = new MyAdapter(this, Organizzazione);  // Dichiara e inizializza un oggetto MyAdapter (definito dall'utente)
        Parse(); // Invocazione metodo per lettura file Json

        aggiorna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listaOrg.setAdapter(adapter);   // Inserisce nella ListView un adapter (l'adapter contiene più oggetti da visualizzare a schermo)
                listaOrg.setOnItemClickListener(new AdapterView.OnItemClickListener() {    // Inizio Indirizzamento layout dell'organizzazione scelta
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        for(int i=0; i< Organizzazione.length ;i++){
                            if(position==i){
                                Intent intent = new Intent(getApplicationContext(), AnotherActivity.class);
                                Bundle bundle = new Bundle();
                                intent.putExtras(bundle);
                                intent.putExtra("title", Organizzazione[i]);
                                System.out.println(Organizzazione[i]);
                                intent.putExtra("position", ""+i);

                                startActivity(intent);
                            }
                        }
                    }
                }); // Fine Indirizzamento layout dell'organizzazione scelta
            }
        });

    }// Fine onCreate



    class MyAdapter extends ArrayAdapter<String> {  // Implementazione classe Adapter
        Context context;
        String Organizzazione[];
        MyAdapter (Context c, String title[]) {
            super(c, R.layout.row, R.id.textView2, title);
            this.context = c;
            this.Organizzazione = title;
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            TextView myTitle = row.findViewById(R.id.textView2);
            myTitle.setText(Organizzazione[position]);
            return row;
        }
    }

    // Legge il contenuto del file Json e crea una lista di organizzazioni
    public void Parse() {
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        mQueue = new RequestQueue(cache, network);
        mQueue.start();
        String url = "https://api.myjson.com/bins/skt66";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        risposta = response;
                        try {
                            JSONObject jObject = new JSONObject(risposta);
                            JSONArray jsonArray = jObject.getJSONArray("listaOrganizzazioni");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String organizzazione = jsonArray.getJSONObject(i).getString("nome");

                                aggiungiOrganizzazione(organizzazione,i);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
    // FINE PARSE()

    public void logout(View view) { // Implementazione del pulsante di Logout
        FirebaseAuth.getInstance().signOut();   //logout
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    // Metodo per inserire un'organizzazione in una lista
    public void aggiungiOrganizzazione(String o,int i){
        Organizzazione[i]=o;
    }

    // Funzionalità per il backbutton (tasto per andare indietro)
    @Override
    public void onBackPressed() {
        AlertDialog alertDialog = new AlertDialog.Builder(ListaOrganizzazioni.this).
        setTitle("Attenzione").setMessage("Sei sicuro di uscire ?").setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                }).
        setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        }).show();
    }

    public static String[] getArray(){
        return Organizzazione;
    }

}

