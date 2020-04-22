package qbteam.stalkerapp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

   private  ArrayList<Organizzazioni> listaOrganizzazioni;
   private Context context;
   private OnOrganizzazioneListener onOrganizzazioneListener;
   Dialog myDialog;

   public MyAdapter(ArrayList<Organizzazioni> listaOrganizzazioni, Context context,OnOrganizzazioneListener onOrganizzazioneListener){
       this.listaOrganizzazioni=listaOrganizzazioni;
       this.context=context;
       this.onOrganizzazioneListener=onOrganizzazioneListener;
   }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v= LayoutInflater.from(parent.getContext())
               .inflate(R.layout.organizzazioni,parent,false);


       return new ViewHolder(v,onOrganizzazioneListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

       Organizzazioni organizzazione= listaOrganizzazioni.get(position);
       holder.textNome.setText(organizzazione.getNome());
    }

    @Override
    public int getItemCount() {
        return listaOrganizzazioni.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public TextView textNome;

        OnOrganizzazioneListener onOrganizzazioneListener;
        public ViewHolder(@NonNull View itemView, OnOrganizzazioneListener onOrganizzazioneListener) {
            super(itemView);
            this.onOrganizzazioneListener=onOrganizzazioneListener;
            textNome = itemView.findViewById(R.id.textView2);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onOrganizzazioneListener.organizzazioneClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            onOrganizzazioneListener.organizzazioneLongClick(getAdapterPosition());
            return true;
        }
    }
    public  interface OnOrganizzazioneListener{
       void organizzazioneClick(int position);
       void organizzazioneLongClick(int position);
    }
}
