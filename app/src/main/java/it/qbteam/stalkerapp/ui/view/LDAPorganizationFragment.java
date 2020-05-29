package it.qbteam.stalkerapp.ui.view;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.unboundid.ldap.sdk.LDAPException;
import org.json.JSONException;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutionException;

import it.qbteam.stalkerapp.HomePageActivity;
import it.qbteam.stalkerapp.R;
import it.qbteam.stalkerapp.model.backend.dataBackend.Organization;
import it.qbteam.stalkerapp.contract.LDAPorganizationContract;
import it.qbteam.stalkerapp.presenter.LDAPorganizationPresenter;
import it.qbteam.stalkerapp.tools.BackPressImplementation;
import it.qbteam.stalkerapp.tools.OnBackPressListener;

public class LDAPorganizationFragment extends Fragment implements View.OnClickListener, OnBackPressListener , LDAPorganizationContract.View {
    private TextView title;
    private Button authentication;
    private Long orgID;
    private OffsetDateTime creationDate;
    private String serverURL;
    private LDAPorganizationPresenter ldapOrganizationPresenter;
    private Dialog myDialog;

    LDAPorganizationFragmentListener iLDAPorganizationFragmentListener;

    //Interfate to communicate with MyStalkerListFragment through the HomePageActivity.
    public interface LDAPorganizationFragmentListener {

        void disableScroll(boolean enable);
    }

    // This method insures that the Activity has actually implemented our
    // listener and that it isn't null.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LDAPorganizationFragmentListener) {
            iLDAPorganizationFragmentListener = (LDAPorganizationFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " LDAPorganizationFragmentListener");
        }
    }

    //Creation of the fragment as a component.
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ldapOrganizationPresenter = new LDAPorganizationPresenter(this);
        setHasOptionsMenu(true);
    }

    //Creation of the graphic part displayed by the user.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ldap_organization, container, false);
        HomePageActivity.getTabLayout().setVisibility(View.GONE);
        Bundle bundle = this.getArguments();
        title = view.findViewById(R.id.titleID);
        title.setText(bundle.getString("name"));
        TextView description = view.findViewById(R.id.descriptionID);
        description.setText(bundle.getString("description"));
        authentication = view.findViewById(R.id.LDAPaccessID);
        ImageView mImageView = view.findViewById(R.id.imageID);
        orgID = bundle.getLong("orgID");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        creationDate = OffsetDateTime.parse(bundle.getString("creationDate"), dateTimeFormatter);
        serverURL = bundle.getString("serverURL");
        authentication.setOnClickListener(this);
        UrlImageViewHelper.setUrlDrawable(mImageView, bundle.getString("image"));
        return view;
    }

    //Set invisible menu item.
    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        menu.findItem(R.id.searchID).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    //Management of the back button.
    @Override
    public boolean onBackPressed() {
        HomePageActivity.getTabLayout().setVisibility(View.VISIBLE);
        iLDAPorganizationFragmentListener.disableScroll(true);
        return new BackPressImplementation(this).onBackPressed();
    }

    //Authentication Button in the LDAP Organization.
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.LDAPaccessID:
                myDialog = new Dialog(view.getContext());
                LDAPAuthentication();
                break;
        }

    }

    private void LDAPAuthentication(){
        myDialog.setContentView(R.layout.dialog_ldap_access);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button annul= myDialog.findViewById(R.id.annulID);
        Button access= myDialog.findViewById(R.id.accessID);
        myDialog.show();

        //This is the annul button of the pop-up.
        annul.setOnClickListener(v12 -> myDialog.dismiss());

        //This is the access button of the pop-up.
        access.setOnClickListener(v1 -> {
            EditText userNameLDAP = myDialog.findViewById(R.id.userNameID);
            EditText passwordLDAP = myDialog.findViewById(R.id.passwordID);
            //Try to connect of the LDAP server and it sends the credentials to the model (to the presenter).
            ldapOrganizationPresenter.setLDAP("2.234.128.81",389, userNameLDAP.getText().toString(), passwordLDAP.getText().toString());
            try {
                ldapOrganizationPresenter.bind();
                ldapOrganizationPresenter.search();
            }
            catch(ExecutionException e) {
                Toast.makeText(getActivity(), R.string.ldap_login_failed_check_credentials, Toast.LENGTH_SHORT).show();
            }
            catch(InterruptedException e) {
                Toast.makeText(getActivity(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
            }
            catch(LDAPException e) {
                e.printStackTrace();
            }
            myDialog.dismiss();
        });
    }

    //Success of LDAP authentication
    @Override
    public void onSuccessLdap(String message) {

        authentication.setVisibility(View.INVISIBLE);
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();

        //Try to add of the organization on MyStalkersList
        try {
            Organization o = new Organization();
            o.setName(title.getText().toString());
            o.setId(orgID);
            o.setAuthenticationServerURL(serverURL);
            o.setCreationDate(creationDate);
            MyStalkersListFragment mMyStalkersListFragment = (MyStalkersListFragment)ActionTabFragment.getMyStalkerFragment();
            mMyStalkersListFragment.addOrganization(o);
        }
        catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    //Failure of LDAP authentication
    @Override
    public void onFailureLdap(String message) {
        authentication.setVisibility(View.VISIBLE);
        authentication.setVisibility(View.INVISIBLE);
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

}
