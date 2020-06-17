package it.qbteam.stalkerapp.ui.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import it.qbteam.stalkerapp.model.backend.dataBackend.OrganizationMovement;
import it.qbteam.stalkerapp.model.service.StalkerLDAP;
import it.qbteam.stalkerapp.presenter.LDAPorganizationPresenter;
import it.qbteam.stalkerapp.tools.BackPressImplementation;
import it.qbteam.stalkerapp.tools.FragmentListenerFeatures;
import it.qbteam.stalkerapp.tools.OnBackPressListener;

public class LDAPorganizationFragment extends Fragment implements View.OnClickListener, OnBackPressListener , LDAPorganizationContract.View {
    private TextView title;
    private Button authentication;
    private Long orgID;
    private OffsetDateTime creationDate;
    private String serverURL;
    private LDAPorganizationPresenter ldapOrganizationPresenter;
    private Dialog myDialog;
    private String trackingMode;
    private Bundle bundle;
    private FragmentListenerFeatures fragmentListenerFeatures;

    // This method insures that the Activity has actually implemented our
    // listener and that it isn't null.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentListenerFeatures) {
            fragmentListenerFeatures = (FragmentListenerFeatures) context;
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
         bundle = this.getArguments();
        HomePageActivity.getTabLayout().setVisibility(View.GONE);
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
        trackingMode = bundle.getString("trackingMode");
        authentication.setOnClickListener(this);
        FloatingActionButton access= view.findViewById(R.id.accessID);
        access.setOnClickListener(this);
        UrlImageViewHelper.setUrlDrawable(mImageView, bundle.getString("image"));
        if(fragmentListenerFeatures.deleteAuthButton(bundle.getString("name"))){
            authentication.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    //Set invisible menu item.
    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        menu.findItem(R.id.searchID).setVisible(false);
        menu.findItem(R.id.search_forID).setVisible(false);
        menu.findItem(R.id.order_forID).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    //Management of the back button.
    @Override
    public boolean onBackPressed() {
        HomePageActivity.getTabLayout().setVisibility(View.VISIBLE);
        fragmentListenerFeatures.disableScroll(true);
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
            case R.id.accessID:
                Dialog accessDialog = new Dialog(getContext());
                accessDialog.setContentView(R.layout.dialog_last_access);
                TableLayout tableLayout= accessDialog.findViewById(R.id.scroll_table);
                accessDialog.show();
                Button exit=accessDialog.findViewById(R.id.exitID);
                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        accessDialog.dismiss();
                    }
                });

                OrganizationMovement om= null;
                try {
                    om = ldapOrganizationPresenter.getLastAccess(bundle.getLong("orgID"));
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                Long orgId=bundle.getLong("orgID");

                if(om!=null&&orgId.equals(om.getOrganizationId()))
                {
                    TableRow tr=new TableRow(getContext());
                    TextView tv= new TextView(getContext());
                    tv.setText("           "+om.getTimestamp().getYear()+"-"+om.getTimestamp().getMonthValue()+"-"+om.getTimestamp().getDayOfMonth()+
                            "                       "+om.getTimestamp().getHour()+":"+om.getTimestamp().getMinute()+":"+om.getTimestamp().getSecond());
                    tv.setGravity(Gravity.CENTER);
                    tr.addView(tv);
                    tableLayout.addView(tr);
                }
                accessDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                break;
        }

    }

    private String[] parseUrl(String serverUrl) {
        String[] parsedUrl;
        if(serverUrl.contains(":")) {
            parsedUrl = serverUrl.split(":");
        } else {
            parsedUrl = new String[]{serverUrl};
        }

        return parsedUrl;
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

            String ldapServer;
            int ldapPort = 389;

            String[] ldapUrl = parseUrl(serverURL);
            ldapServer = ldapUrl[0];

            if(ldapUrl.length == 2) {
                try {
                    ldapPort = Integer.parseInt(ldapUrl[1]);
                } catch(NumberFormatException exc) {
                    ldapPort = 389;
                }
            }

            //Try to connect of the LDAP server and it sends the credentials to the model (to the presenter).
            ldapOrganizationPresenter.setLDAP(ldapServer,ldapPort, userNameLDAP.getText().toString(), passwordLDAP.getText().toString());
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
            catch(LDAPException | JSONException | IOException e) {
                e.printStackTrace();
            }
            myDialog.dismiss();
        });
    }

    //Success of LDAP authentication
    @Override
    public void onSuccessLdap(String message) throws IOException, JSONException {

        authentication.setVisibility(View.INVISIBLE);
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();

        //Try to add of the organization on MyStalkersList
        Organization o = new Organization();
        o.setName(title.getText().toString());
        o.setId(orgID);
        o.setAuthenticationServerURL(serverURL);
        o.setCreationDate(creationDate);
        o.setTrackingMode(trackingMode);
        o.setOrgAuthServerId(StalkerLDAP.getOrgAuthServerId());
        System.out.print("AUTH   "+StalkerLDAP.getOrgAuthServerId());
        fragmentListenerFeatures.addOrganization(o);
    }

    //Failure of LDAP authentication
    @Override
    public void onFailureLdap(String message) {
        authentication.setVisibility(View.VISIBLE);
        authentication.setVisibility(View.INVISIBLE);
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

}
