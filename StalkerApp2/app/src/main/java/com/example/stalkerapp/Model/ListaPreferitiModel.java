package com.example.stalkerapp.Model;

import androidx.fragment.app.Fragment;

import com.example.stalkerapp.Presenter.ListaPreferitiContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class ListaPreferitiModel implements ListaPreferitiContract.Intractor {


    public ListaPreferitiModel(){

}
    @Override
    public ArrayList<String> performControllaLista(Fragment fragment) {
//CONTROLLO ESISTENZA DEL FILE
        ArrayList<String>aux=new ArrayList<>();
        File organizzazioniFile = new File(fragment.getContext().getFilesDir()+"/Preferiti.txt");
        if(organizzazioniFile.length()==0 || !organizzazioniFile.exists()){
            System.out.println("File is empty ...");

            return aux;
        }
        else {
            try {
                FileInputStream fin=new FileInputStream(organizzazioniFile);
                byte[] buffer= new byte[(int)organizzazioniFile.length()];
                new DataInputStream(fin).readFully(buffer);
                fin.close();
                String s=new String(buffer,"UTF-8");
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = (JSONArray) jsonObject.get("listaOrganizzazioni");
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject organizzazione= jsonArray.getJSONObject(i);
                    String nomeOrganizzazione= organizzazione.getString("nome");
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
