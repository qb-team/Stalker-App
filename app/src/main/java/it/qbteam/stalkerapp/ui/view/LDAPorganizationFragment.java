package it.qbteam.stalkerapp.ui.view;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;

import it.qbteam.stalkerapp.R;
import it.qbteam.stalkerapp.tools.BackPressImplementation;
import it.qbteam.stalkerapp.tools.OnBackPressListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class LDAPorganizationFragment extends Fragment implements OnBackPressListener , View.OnClickListener{
    private TextView title,description,position;
    private Button access;
    private ImageView image;
    Dialog myDialog;
    public LDAPorganizationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ldap_organization, container, false);

        title=view.findViewById(R.id.titleID);
        description=view.findViewById(R.id.descriptionID);
        position=view.findViewById(R.id.positionID);
        access=view.findViewById(R.id.LDAPaccessID);
        image=view.findViewById(R.id.imageID);
        access.setOnClickListener(this);
        return view;

    }

    @Override
    public boolean onBackPressed() {
        return new BackPressImplementation(this).onBackPressed();
    }

    @Override
    public void onClick(View v) {
        myDialog=new Dialog(getContext());
        myDialog.setContentView(R.layout.dialog_ldap_access);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        EditText userName= myDialog.findViewById(R.id.userNameID);
        EditText password= myDialog.findViewById(R.id.passwordID);
        Button annull= myDialog.findViewById(R.id.annulID);
        Button access= myDialog.findViewById(R.id.accessID);
        myDialog.show();

        annull.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                myDialog.dismiss();
            }
        });
        access.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Hai fatto l'accesso", Toast.LENGTH_SHORT).show();
                myDialog.dismiss();
            }
        });
    }
}
