package it.qbteam.stalkerapp.ui.view;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
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

import java.io.File;
import java.io.FileWriter;
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
    private TextView title,description,positionTextView;
    private Button authentication;
    private ImageView mImageView;
    private TextView trackingTextView;
    private Switch anonimousSwitch;
    private EditText userNameLDAP, passwordLDAP;
    private LDAPorganizationPresenter ldaPorganizationPresenter;
    Dialog myDialog;


    public LDAPorganizationFragment() {
        // Required empty public constructor
    }
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        menu.findItem(R.id.searchID).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        System.out.println("Creata organizzazione");
        View view=inflater.inflate(R.layout.fragment_ldap_organization, container, false);
        Bundle bundle=this.getArguments();
        title=view.findViewById(R.id.titleID);
        title.setText(bundle.getString("name"));
        description=view.findViewById(R.id.descriptionID);
        description.setText(bundle.getString("description"));
        positionTextView=view.findViewById(R.id.positionID);
        authentication=view.findViewById(R.id.LDAPaccessID);
        mImageView=view.findViewById(R.id.imageID);
        anonimousSwitch=view.findViewById(R.id.switchAnonimousID);
        anonimousSwitch.setVisibility(View.INVISIBLE);
        trackingTextView=view.findViewById(R.id.trackingTextID);
        trackingTextView.setVisibility(View.INVISIBLE);
        ldaPorganizationPresenter=new LDAPorganizationPresenter();
        authentication.setOnClickListener(this);

        FileWriter w;
        try {
            w = new FileWriter(getContext().getFilesDir() + "/image.txt");
            System.out.println(bundle.getString("image"));
            w.write(bundle.getString("image"));
            w.flush();
            w.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Bitmap bitmap = BitmapFactory.decodeFile(new File((getContext().getFilesDir() + "/image.txt")).getPath());
        mImageView.setImageBitmap(bitmap);
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
                StalkerLDAP stalkerLDAP=new StalkerLDAP("2.234.128.81",8080,userNameLDAP.getText().toString(),passwordLDAP.getText().toString());
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
                anonimousSwitch.setVisibility(View.VISIBLE);
                anonimousSwitch.setChecked(true);
                trackingTextView.setVisibility(View.VISIBLE);
                myDialog.dismiss();
            }
        });
    }


}
