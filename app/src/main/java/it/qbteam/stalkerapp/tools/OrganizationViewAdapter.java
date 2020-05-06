package it.qbteam.stalkerapp.tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import it.qbteam.stalkerapp.model.backend.dataBackend.Organization;
import it.qbteam.stalkerapp.R;

public class OrganizationViewAdapter extends RecyclerView.Adapter<OrganizationViewAdapter.ViewHolder> {

    private  ArrayList<Organization> organizationList;
    private Context context;
    private OrganizationListener organizationListener;

    public interface OrganizationListener{
        void organizationClick(int position);
        void organizationLongClick(int position);
    }


    public OrganizationViewAdapter(ArrayList<Organization> organizationList, Context context, OrganizationListener organizationListener){
        this.organizationList = organizationList;
        this.context=context;
        this.organizationListener=organizationListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.organization_row,parent,false);


        return new ViewHolder(v,organizationListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Organization organization = organizationList.get(position);
        holder.textNome.setText(organization.getName());
    }

    @Override
    public int getItemCount() {
        return organizationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public TextView textNome;

        OrganizationListener organizationListenerViewHolder;

        public ViewHolder(@NonNull View itemView, OrganizationListener organizationListenerViewHolder) {
            super(itemView);
            this.organizationListenerViewHolder=organizationListenerViewHolder;
            textNome = itemView.findViewById(R.id.textView2);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            organizationListenerViewHolder.organizationClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            organizationListenerViewHolder.organizationLongClick(getAdapterPosition());
            return true;
        }
    }

}
