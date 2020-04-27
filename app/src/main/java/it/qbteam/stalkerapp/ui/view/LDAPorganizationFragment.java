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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.unboundid.ldap.sdk.LDAPException;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import it.qbteam.stalkerapp.R;
import it.qbteam.stalkerapp.model.service.StalkerLDAP;
import it.qbteam.stalkerapp.presenter.LDAPorganizationPresenter;
import it.qbteam.stalkerapp.tools.BackPressImplementation;
import it.qbteam.stalkerapp.tools.OnBackPressListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class LDAPorganizationFragment extends Fragment implements OnBackPressListener , View.OnClickListener{
    private TextView title,description,position;
    private Button authentication;
    private ImageView image;
    private TextView trackingText;
    private Switch anonimous;
    private EditText userNameLDAP, passwordLDAP;
    private LDAPorganizationPresenter ldaPorganizationPresenter;
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
        authentication=view.findViewById(R.id.LDAPaccessID);
        image=view.findViewById(R.id.imageID);
        anonimous=view.findViewById(R.id.switchAnonimousID);
        anonimous.setVisibility(View.INVISIBLE);
        trackingText=view.findViewById(R.id.trackingTextID);
        trackingText.setVisibility(View.INVISIBLE);
        ldaPorganizationPresenter=new LDAPorganizationPresenter();
        authentication.setOnClickListener(this);

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
                userNameLDAP=myDialog.findViewById(R.id.userNameID);
                passwordLDAP=myDialog.findViewById(R.id.passwordID);
                StalkerLDAP stalkerLDAP=new StalkerLDAP("ldap.forumsys.com",389,userNameLDAP.getText().toString(),passwordLDAP.getText().toString());
                try {
                    stalkerLDAP.execute();
                    stalkerLDAP.performSearch();

                    //ldaPorganizationPresenter.setLDAP("ldap.forumsys.com",389,userNameLDAP.getText().toString(),passwordLDAP.getText().toString());
                    //System.out.println(ldaPorganizationPresenter.getLDAP().getHost()+ldaPorganizationPresenter.getLDAP().getPort()+ldaPorganizationPresenter.getLDAP().getBindDN()+ldaPorganizationPresenter.getLDAP().getPassword());

                    //ldaPorganizationPresenter.bind();
                    //ldaPorganizationPresenter.search();
                } catch (ExecutionException e) {
                    Toast.makeText(getActivity(), R.string.ldap_login_failed_check_credentials, Toast.LENGTH_SHORT).show();
                } catch (InterruptedException e) {
                    Toast.makeText(getActivity(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                }
                authentication.setVisibility(View.INVISIBLE);
                anonimous.setVisibility(View.VISIBLE);
                anonimous.setChecked(true);
                trackingText.setVisibility(View.VISIBLE);
                myDialog.dismiss();
            }
        });
    }
}
