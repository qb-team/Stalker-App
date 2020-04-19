package qbteam.stalkerapp.Model;

import androidx.fragment.app.Fragment;
import qbteam.stalkerapp.Presenter.ListaPreferitiContract;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

//Modello di Lista Preferiti
public class ListaPreferitiModel implements ListaPreferitiContract.Intractor {

    @Override
    public ArrayList<String> performControllaLista(Fragment fragment) {
        //Controllo esistenza file dove vengono salvate le organizzazioni preferite in formato string mantenendo la grammatica Json
        ArrayList<String>aux = new ArrayList<>();
        File organizzazioniFile = new File(fragment.getContext().getFilesDir()+"/Preferiti.txt");
        if(organizzazioniFile.length()==0 || !organizzazioniFile.exists()){
            System.out.println("File is empty ...");
            return aux;
        }
        else {//Viene ritornato un array di stinghe contenete il nome di ciascuna organizzazione preferita presente nel file
            try {
                //Conversione del file in stream e creazione di un oggetto Json contenete i nomi delle organizzazioni preferite
                FileInputStream fin = new FileInputStream(organizzazioniFile);
                byte[] buffer= new byte[(int)organizzazioniFile.length()];
                new DataInputStream(fin).readFully(buffer);
                fin.close();
                String s = new String(buffer,"UTF-8");
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = (JSONArray) jsonObject.get("listaOrganizzazioni");
                //Ogni nome delle organizzazioni presente nel JSONarray viene inserito in un array "aux" di stringhe
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject organizzazione = jsonArray.getJSONObject(i);
                    String nomeOrganizzazione = organizzazione.getString("nome");
                    aux.add(nomeOrganizzazione);
                }
            } catch (JSONException | FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return aux;
        }
    }
}
