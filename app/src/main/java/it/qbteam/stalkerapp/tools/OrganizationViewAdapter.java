package it.qbteam.stalkerapp.tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import it.qbteam.stalkerapp.model.data.Organization;
import it.qbteam.stalkerapp.R;

public class OrganizationViewAdapter extends RecyclerView.Adapter<OrganizationViewAdapter.ViewHolder> {

    private  ArrayList<Organization> listOrganization;
    private Context context;
    private OrganizationListener OrganizationListener;

    public interface OrganizationListener{
        void organizationClick(int position);
        void organizationLongClick(int position);
    }


    public OrganizationViewAdapter(ArrayList<Organization> listOrganization, Context context, OrganizationListener OrganizationListener){
        this.listOrganization=listOrganization;
        this.context=context;
        this.OrganizationListener=OrganizationListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.organization_row,parent,false);


        return new ViewHolder(v,OrganizationListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Organization organization= listOrganization.get(position);
        holder.textNome.setText(organization.getNome());
    }

    @Override
    public int getItemCount() {
        return listOrganization.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public TextView textNome;

        OrganizationListener OrganizationListener;

        public ViewHolder(@NonNull View itemView, OrganizationListener OrganizationListener) {
            super(itemView);
            this.OrganizationListener=OrganizationListener;
            textNome = itemView.findViewById(R.id.textView2);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            OrganizationListener.organizationClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            OrganizationListener.organizationLongClick(getAdapterPosition());
            return true;
        }
    }

}
