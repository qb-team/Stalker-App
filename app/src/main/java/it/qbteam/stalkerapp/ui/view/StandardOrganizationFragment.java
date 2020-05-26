package it.qbteam.stalkerapp.ui.view;

import android.app.Dialog;
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
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import it.qbteam.stalkerapp.R;
import it.qbteam.stalkerapp.contract.StandardOrganizationContract;
import it.qbteam.stalkerapp.model.backend.dataBackend.OrganizationMovement;
import it.qbteam.stalkerapp.presenter.StandardOrganizationPresenter;
import it.qbteam.stalkerapp.tools.BackPressImplementation;
import it.qbteam.stalkerapp.tools.OnBackPressListener;
import lombok.SneakyThrows;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class StandardOrganizationFragment extends Fragment implements OnBackPressListener, StandardOrganizationContract.View {

    public final static String TAG="StandardOrganizationFragment";
    private TextView title, description ;
    private ImageView image;
    private FloatingActionButton access;
    private Dialog accessDialog;
    private StandardOrganizationPresenter standardOrganizationPresenter;


    //Creation of the fragment as a component.
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        standardOrganizationPresenter= new StandardOrganizationPresenter(this);
        setHasOptionsMenu(true);
    }

    //Creation of the graphic part displayed by the user.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_organization, container, false);
        Bundle bundle=this.getArguments();
        title=view.findViewById(R.id.titleID);
        image=view.findViewById(R.id.imageID);
        description=view.findViewById(R.id.descriptionID);
        title.setText(bundle.getString("name"));
        UrlImageViewHelper.setUrlDrawable(image, bundle.getString("image"));
        description.setText(bundle.getString("description"));
        access= view.findViewById(R.id.accessID);
        access.setOnClickListener(new View.OnClickListener() {
            @SneakyThrows
            @Override
            public void onClick(View v) {
                accessDialog = new Dialog(getContext());
                accessDialog.setContentView(R.layout.dialog_access_history);
                TableLayout tableLayout= accessDialog.findViewById(R.id.scroll_table);
                accessDialog.show();
                Button exit=accessDialog.findViewById(R.id.exitID);
                exit.setOnClickListener(view -> {
                    accessDialog.dismiss();
                });

                OrganizationMovement om= standardOrganizationPresenter.getOrganizationMovement();
                Long orgId=bundle.getLong("orgID");

                if(om!=null&&orgId.equals(om.getOrganizationId()))
                {
                    TableRow tr=new TableRow(getContext());
                    TextView tv= new TextView(getContext());
                    tv.setText("       "+om.getTimestamp().getYear()+"-"+om.getTimestamp().getMonthValue()+"-"+om.getTimestamp().getDayOfMonth()+
                            "                  "+om.getTimestamp().getHour()+":"+om.getTimestamp().getMinute()+":"+om.getTimestamp().getSecond());
                    tv.setGravity(Gravity.CENTER);
                    tr.addView(tv);
                    tableLayout.addView(tr);
                }
                accessDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


            }
        });
        return view;
    }

    //Makes the 'add to favorites' option visible to the application's action tab menu and hides the search command.
    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {

        menu.findItem(R.id.searchID).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }


    //Manages the back button.
    @Override
    public boolean onBackPressed() {
        return new BackPressImplementation(this).onBackPressed();
    }


}