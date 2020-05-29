package it.qbteam.stalkerapp.ui.view;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.io.IOException;
import java.util.List;
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
    private FloatingActionButton buttonDelete;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SneakyThrows
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_place_access, container, false);
        placeAccessPresenter = new PlaceAccessPresenter(this);
        bundle = this.getArguments();
        TextView nameOrg = view.findViewById(R.id.nomeOrgID);
        nameOrg.setText(bundle.getString("name"));
        params1 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f);
        params2 = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        tbl=(TableLayout) view.findViewById(R.id.accessTableID);
        placeAccessPresenter.getPlaceAccessList();
        buttonDelete= view.findViewById(R.id.deleteAccessID);
        buttonDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    placeAccessPresenter.deletePlaceAccess();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        return view;
    }
    @Override
    public boolean onBackPressed() {
        return new BackPressImplementation(this).onBackPressed();
    }

    @Override
    public void onSuccessGetPlaceAccessInLocal(List<PlaceAccess> PlaceAccessList) {

        if(PlaceAccessList!=null){
            System.out.print(PlaceAccessList+"numero elementi  "+PlaceAccessList.size());
            for(int i=0;i<PlaceAccessList.size();i++){
                    if((PlaceAccessList.get(i).getOrgId()).equals(bundle.getLong("orgID"))){
                        //Creating new tablerows and textviews
                        TableRow row = new TableRow(getContext());
                        TextView txt1 = new TextView(getContext());
                        txt1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        TextView txt2 = new TextView(getContext());
                        txt2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        TextView txt3 = new TextView(getContext());
                        txt3.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        TextView txt4 = new TextView(getContext());
                        txt4.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        //setting the textViews
                        txt1.setText(PlaceAccessList.get(i).getPlaceName());
                        txt2.setText(PlaceAccessList.get(i).getEntranceTimestamp().getYear() + "/" + PlaceAccessList.get(i).getEntranceTimestamp().getMonthValue() + "/" + PlaceAccessList.get(i).getEntranceTimestamp().getDayOfMonth());
                        txt3.setText(PlaceAccessList.get(i).getEntranceTimestamp().getHour()+":"+PlaceAccessList.get(i).getEntranceTimestamp().getMinute()+":"+PlaceAccessList.get(i).getEntranceTimestamp().getSecond());
                        txt4.setText(PlaceAccessList.get(i).getExitTimestamp().getHour()+":"+PlaceAccessList.get(i).getExitTimestamp().getMinute()+":"+PlaceAccessList.get(i).getExitTimestamp().getSecond());
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
