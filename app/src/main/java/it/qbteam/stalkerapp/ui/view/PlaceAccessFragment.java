package it.qbteam.stalkerapp.ui.view;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.io.IOException;
import java.util.List;

import it.qbteam.stalkerapp.HomePageActivity;
import it.qbteam.stalkerapp.R;
import it.qbteam.stalkerapp.contract.PlaceAccessContract;
import it.qbteam.stalkerapp.model.backend.dataBackend.PlaceAccess;
import it.qbteam.stalkerapp.presenter.PlaceAccessPresenter;
import it.qbteam.stalkerapp.tools.BackPressImplementation;
import it.qbteam.stalkerapp.tools.OnBackPressListener;
import lombok.SneakyThrows;

public class PlaceAccessFragment extends Fragment implements OnBackPressListener, PlaceAccessContract.View {
    private Bundle bundle;
    private PlaceAccessPresenter placeAccessPresenter;
    private TableRow.LayoutParams  params1;
    private TableRow.LayoutParams  params2;
    private TableLayout tbl;
    private TextView timeStay;
    private TextView accessType;
    private FloatingActionButton buttonDelete;
    PlaceAccessFragmentListener placeAccessFragmentListener;

    //Interfate to communicate with MyStalkerListFragment through the HomePageActivity.
    public interface PlaceAccessFragmentListener {
        void disableScroll(boolean enable);
    }

    // This method insures that the Activity has actually implemented our
    // listener and that it isn't null.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PlaceAccessFragmentListener) {
            placeAccessFragmentListener = (PlaceAccessFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " PlaceAccessFragmentListener");
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @SneakyThrows
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_place_access, container, false);
        bundle = this.getArguments();
        getActivity().setTitle("Luoghi di: "+bundle.getString("name"));
        placeAccessPresenter = new PlaceAccessPresenter(this);
        timeStay= view.findViewById(R.id.timeStayID);
        Long time=bundle.getLong("timeID");
        accessType=view.findViewById(R.id.accessTypeID);
        accessType.setText(bundle.getString("accessType"));
        timeStay.setText(Integer.toString(time.intValue()/1000/60/60)+":"+Integer.toString(time.intValue()/1000/60)+":"+Integer.toString(time.intValue()/1000));
        params1 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f);
        params2 = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        tbl=(TableLayout) view.findViewById(R.id.accessTableID);
        try {
            placeAccessPresenter.getPlaceAccessList();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        buttonDelete= view.findViewById(R.id.deleteAccessID);
        buttonDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    placeAccessPresenter.deletePlaceAccess(bundle.getLong("orgID"));
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });
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

    @Override
    public boolean onBackPressed() {
        HomePageActivity.getTabLayout().setVisibility(View.VISIBLE);
        placeAccessFragmentListener.disableScroll(true);
        getActivity().setTitle("Storico accessi");
        return new BackPressImplementation(this).onBackPressed();

    }

    @Override
    public void onSuccessGetPlaceAccessInLocal(List<PlaceAccess> placeAccessList) {

        if(placeAccessList!=null&&placeAccessList.size()!=0){
            for(int i=0;i<placeAccessList.size();i++){
                    if((placeAccessList.get(i).getOrgId()).equals(bundle.getLong("orgID"))){
                        //Creating new tablerows and textviews
                        TableRow row = new TableRow(getContext());
                        TextView txt1 = new TextView(getContext());
                        txt1.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                        TextView txt2 = new TextView(getContext());
                        txt2.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                        TextView txt3 = new TextView(getContext());
                        txt3.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                        TextView txt4 = new TextView(getContext());
                        txt4.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                        //setting the textViews
                        txt1.setText(placeAccessList.get(i).getPlaceName());
                        txt2.setText(placeAccessList.get(i).getEntranceTimestamp().getYear() + "/" + placeAccessList.get(i).getEntranceTimestamp().getMonthValue() + "/" + placeAccessList.get(i).getEntranceTimestamp().getDayOfMonth());
                        txt3.setText(placeAccessList.get(i).getEntranceTimestamp().getHour()+":"+placeAccessList.get(i).getEntranceTimestamp().getMinute()+":"+placeAccessList.get(i).getEntranceTimestamp().getSecond());
                        txt4.setText(placeAccessList.get(i).getExitTimestamp().getHour()+":"+placeAccessList.get(i).getExitTimestamp().getMinute()+":"+placeAccessList.get(i).getExitTimestamp().getSecond());
                        txt1.setLayoutParams(params1);
                        txt2.setLayoutParams(params1);
                        txt3.setLayoutParams(params1);
                        txt4.setLayoutParams(params1);
                        //the textViews have to be added to the row created
                        row.addView(txt1);
                        row.addView(txt2);
                        row.addView(txt3);
                        row.addView(txt4);
                        row.setLayoutParams(params2);
                        tbl.addView(row);
                    }
            }
        }
    }

    @Override
    public void onSuccessDeletePlaceAccess() {
            tbl.removeAllViews();
    }
}
