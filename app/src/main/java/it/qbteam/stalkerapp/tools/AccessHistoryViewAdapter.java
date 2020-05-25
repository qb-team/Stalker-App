package it.qbteam.stalkerapp.tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.qbteam.stalkerapp.R;
import it.qbteam.stalkerapp.model.backend.dataBackend.OrganizationAccess;

public class AccessHistoryViewAdapter extends RecyclerView.Adapter<AccessHistoryViewAdapter.ViewHolder> {

    private List<OrganizationAccess> organizationAccessList;
    private Context context;
    private OrganizationAccessListener organizationAccessListener;
    private String orgaName;


    //Interface to manage the type of click.
    public interface OrganizationAccessListener{
        void organizationClick(int position);
        void organizationLongClick(int position);
    }


    //OrganizationViewAdapter's constructor.
    public AccessHistoryViewAdapter(List<OrganizationAccess> organizationAccessList,String orgName,Context context, OrganizationAccessListener organizationAccessListener){
        this.organizationAccessList = organizationAccessList;
        this.orgaName=orgName;
        this.context=context;
        this.organizationAccessListener=organizationAccessListener;
    }


    //Returns the layout of the organizations' list.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.access_history_row,parent,false);

        return new ViewHolder(v,organizationAccessListener);
    }

    //Sets to the holder the organization's name.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        OrganizationAccess oa = organizationAccessList.get(position);
        System.out.print(oa);
        holder.nameOrg.setText(orgaName);
        holder.date.setText(oa.getEntranceTimestamp().getYear()+"/"+oa.getEntranceTimestamp().getMonthValue()+"/"+oa.getEntranceTimestamp().getDayOfMonth());
        holder.access.setText(oa.getEntranceTimestamp().getHour()+":"+oa.getEntranceTimestamp().getMinute()+":"+oa.getEntranceTimestamp().getSecond());
        holder.exit.setText("");

    }

    //Returns the organizations' list size.
    @Override
    public int getItemCount() {
        return organizationAccessList.size();
    }

    //Inner class to manage the organizations' access layout with its actions.
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public TextView nameOrg;
        public TextView date;
        public TextView access;
        public TextView exit;

        OrganizationAccessListener organizationAccessListenerViewHolder;
        //Interface to manage the type of click.

        public ViewHolder(@NonNull View itemView, OrganizationAccessListener organizationAccessListenerViewHolder) {
            super(itemView);
            this.organizationAccessListenerViewHolder=organizationAccessListenerViewHolder;

            nameOrg = itemView.findViewById(R.id.orgNameID);
            date = itemView.findViewById(R.id.dateID);
            access = itemView.findViewById(R.id.accessID);
            exit = itemView.findViewById(R.id.exitID);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            organizationAccessListenerViewHolder.organizationClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            organizationAccessListenerViewHolder.organizationLongClick(getAdapterPosition());
            return true;
        }
    }

}
