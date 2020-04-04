package com.example.stalkerapp.Presenter;
import androidx.fragment.app.Fragment;
import com.example.stalkerapp.Model.ListaPreferitiModel;

import java.util.ArrayList;

public class ListaPreferitiPresenter implements ListaPreferitiContract.Presenter{

private ListaPreferitiModel listaPreferitiModel;


public ListaPreferitiPresenter(){

    listaPreferitiModel=new ListaPreferitiModel();
}

    @Override
    public ArrayList<String> controlla(Fragment fragment) {
       return listaPreferitiModel.performControllaLista(fragment);

    }



}
