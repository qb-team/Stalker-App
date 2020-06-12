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
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import it.qbteam.stalkerapp.HomePageActivity;
import it.qbteam.stalkerapp.R;
import it.qbteam.stalkerapp.contract.StandardOrganizationContract;
import it.qbteam.stalkerapp.model.backend.dataBackend.OrganizationMovement;
import it.qbteam.stalkerapp.presenter.StandardOrganizationPresenter;
import it.qbteam.stalkerapp.tools.BackPressImplementation;
import it.qbteam.stalkerapp.tools.OnBackPressListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import java.io.IOException;

public class StandardOrganizationFragment extends Fragment implements View.OnClickListener, OnBackPressListener, StandardOrganizationContract.View {
    private Bundle bundle;
    private StandardOrganizationPresenter standardOrganizationPresenter;
    StandardOrganizationFragmentListener standardOrganizationFragmentListener;

    //Interfate to communicate with MyStalkerListFragment through the HomePageActivity.
    public interface StandardOrganizationFragmentListener {

        void disableScroll(boolean enable);
    }

    // This method insures that the Activity has actually implemented our
    // listener and that it isn't null.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StandardOrganizationFragmentListener) {
            standardOrganizationFragmentListener = (StandardOrganizationFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " StandardOrganizationFragmentListener");
        }
    }

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
        bundle=this.getArguments();
        HomePageActivity.getTabLayout().setVisibility(View.GONE);
        TextView title=view.findViewById(R.id.titleID);
        ImageView image=view.findViewById(R.id.imageID);
        TextView description=view.findViewById(R.id.descriptionID);
        title.setText(bundle.getString("name"));
        UrlImageViewHelper.setUrlDrawable(image, bundle.getString("image"));
        description.setText(bundle.getString("description"));
        FloatingActionButton access= view.findViewById(R.id.accessID);
        access.setOnClickListener(this);
        return view;
    }

    //Makes the 'add to favorites' option visible to the application's action tab menu and hides the search command.
    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        menu.findItem(R.id.searchID).setVisible(false);
        menu.findItem(R.id.search_forID).setVisible(false);
        menu.findItem(R.id.order_forID).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }


    //Manages the back button.
    @Override
    public boolean onBackPressed() {
        HomePageActivity.getTabLayout().setVisibility(View.VISIBLE);
        standardOrganizationFragmentListener.disableScroll(true);
        return new BackPressImplementation(this).onBackPressed();
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
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
                    om = standardOrganizationPresenter.getLastAccess(bundle.getLong("orgID"));
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
}