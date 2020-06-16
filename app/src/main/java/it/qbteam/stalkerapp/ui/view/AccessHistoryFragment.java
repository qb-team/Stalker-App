package it.qbteam.stalkerapp.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.InputType;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import it.qbteam.stalkerapp.HomePageActivity;
import it.qbteam.stalkerapp.R;
import it.qbteam.stalkerapp.model.backend.dataBackend.OrganizationAccess;
import it.qbteam.stalkerapp.tools.AccessHistoryViewAdapter;
import it.qbteam.stalkerapp.tools.BackPressImplementation;
import it.qbteam.stalkerapp.tools.FragmentListenerFeatures;
import it.qbteam.stalkerapp.tools.OnBackPressListener;
import it.qbteam.stalkerapp.tools.SearchViewCustom;

public class AccessHistoryFragment extends Fragment implements SearchView.OnQueryTextListener, AccessHistoryViewAdapter.OrganizationAccessListener, OnBackPressListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private FloatingActionButton buttonDelete;
    private List<OrganizationAccess> accessList;
    private TextView errorText;
    private FragmentListenerFeatures fragmentListenerFeatures;
    private static final String SHARED_PREFS = "sharedPrefs";
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor prefsEditor;
    private Gson gson;

    // This method insures that the Activity has actually implemented our
    // listener and that it isn't null.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentListenerFeatures) {
            fragmentListenerFeatures = (FragmentListenerFeatures) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " AccessHistoryFragmentListener");
        }
    }

    public AccessHistoryFragment() {
        // Required empty public constructor
    }

    //Creation of the fragment as a component.
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_access_history, container, false);
        recyclerView = view.findViewById(R.id.recyclerAccessViewID);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        accessList = new ArrayList<>();
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        errorText = view.findViewById(R.id.errorTextID);
        mPrefs = this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        prefsEditor =  mPrefs.edit();
        gson = new Gson();
        printAccess();

        buttonDelete = view.findViewById(R.id.accessID);
        buttonDelete.setOnClickListener(v -> {

            if(accessList!=null){
                prefsEditor.putString("organizationAccessList",null);
                prefsEditor.commit();
                adapter = new AccessHistoryViewAdapter(null, getActivity(), this);
                recyclerView.setAdapter(adapter);
                errorText.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    public void printAccess() {
        Type type = new TypeToken<List<OrganizationAccess>>(){}.getType();
        String organizationAccessListJson = mPrefs.getString("organizationAccessList",null);
        accessList = gson.fromJson(organizationAccessListJson, type);
        if (accessList != null && accessList.size() != 0) {
            adapter = new AccessHistoryViewAdapter(accessList, getActivity(), this);
            recyclerView.setAdapter(adapter);
            errorText.setVisibility(View.INVISIBLE);
        } else {
            errorText.setVisibility(View.VISIBLE);
        }

    }
    public void saveAccess(){

        String organizationAccessJson =mPrefs.getString("organizationAccess",null);
        OrganizationAccess organizationAccess = gson.fromJson(organizationAccessJson,OrganizationAccess.class);

        Type type = new TypeToken<List<OrganizationAccess>>(){}.getType();
        String organizationAccessListJson = mPrefs.getString("organizationAccessList",null);
        accessList = gson.fromJson(organizationAccessListJson, type);
        if(accessList!=null){
            accessList.add(organizationAccess);
            String organizationAccessListFileJson = gson.toJson(accessList);
            prefsEditor.putString("organizationAccessList",organizationAccessListFileJson);
            prefsEditor.commit();
        }
        else{
            accessList = new ArrayList<>();
            accessList.add(organizationAccess);
            String placeAccessListFileJson = gson.toJson(accessList);
            prefsEditor.putString("organizationAccessList",placeAccessListFileJson);
            prefsEditor.commit();

        }
        printAccess();

    }

    //It hides to menu actionTab the option "Aggiungi a MyStalkers".
    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        MenuItem item = menu.findItem(R.id.searchID);
        item.setVisible(true);
        menu.findItem(R.id.search_forID).setVisible(false);
        SearchView searchView = (SearchView) item.getActionView();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        searchView.setMaxWidth(width * 2 / 3);

        new SearchViewCustom()
                .setSearchBackGroundResource(R.drawable.custom_border)
                .setSearchIconResource(R.drawable.ic_search_black_24dp, true, false) //true to icon inside edittext, false to outside
                .setSearchHintText("cerca qui..")
                .setSearchTextColorResource(R.color.colorPrimary)
                .setSearchHintColorResource(R.color.colorPrimary)
                .setSearchCloseIconResource(R.drawable.ic_close_black_24dp)
                .setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS)
                .format(searchView);

        searchView.setOnQueryTextListener(this);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.DateDecreasingOrderID:
                DateDecreasingOrder(accessList);
                item.setChecked(true);
                break;

            case R.id.DateIncreasingOrderID:
                DateCreasingOrder(accessList);
                item.setChecked(true);
                break;

            case R.id.speacificDateID:

                break;


        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText.toLowerCase();
        List<OrganizationAccess> newList = new ArrayList<>();
        if (accessList != null) {
            for (int i = 0; i < accessList.size(); i++) {
                if (accessList.get(i).getOrgName().toLowerCase().contains(userInput))
                    newList.add(accessList.get(i));
            }
            adapter = new AccessHistoryViewAdapter(newList, getActivity(), this);
            recyclerView.setAdapter(adapter);
        }
        return false;
    }

    @Override
    public void organizationClick(int position) {

        Bundle bundle = new Bundle();
        bundle.putString("name", accessList.get(position).getOrgName());
        bundle.putLong("orgID", accessList.get(position).getOrganizationId());

        PlaceAccessFragment placeAccessFragment = new PlaceAccessFragment();
        placeAccessFragment.setArguments(bundle);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.AccessHistoryID, placeAccessFragment).commit();

        HomePageActivity.getTabLayout().setVisibility(View.GONE);
        fragmentListenerFeatures.disableScroll(false);
    }


    @Override
    public void organizationLongClick(int position) {
        Dialog myDialog = new Dialog(getContext());
        myDialog.setContentView(R.layout.dialog_more_info_access);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView timeStay = myDialog.findViewById(R.id.timeStayID);
        TextView accessType = myDialog.findViewById(R.id.accessTypeID);
        timeStay.setText(Integer.toString( accessList.get(position).getTimeStay().intValue()/1000/60/60)+":"+Integer.toString( accessList.get(position).getTimeStay().intValue()/1000/60)+":"+Integer.toString( accessList.get(position).getTimeStay().intValue()/1000));
        accessType.setText(accessList.get(position).getAccessType());
        myDialog.show();


    }

    @Override
    public boolean onBackPressed() {
        return new BackPressImplementation(this).onBackPressed();
    }

    public void DateDecreasingOrder(List<OrganizationAccess> list){
        if(list!=null&&list.size()>0){
            Collections.sort(list, byDateCreasing);
            List<OrganizationAccess>aux= new ArrayList<>();
            int flag=list.size();
            for (int i=0; i<flag; i++){
                aux.add(list.get(flag-1-i));
            }
            adapter = new AccessHistoryViewAdapter(aux, getActivity(), this);
            recyclerView.setAdapter(adapter);
        }


    }

    public void DateCreasingOrder(List<OrganizationAccess>list){

        if(list!=null&&list.size()>0) {
            Collections.sort(list, byDateCreasing);
            adapter = new AccessHistoryViewAdapter(list, getActivity(), this);
            recyclerView.setAdapter(adapter);
        }
    }

    public void searchForDay(List<OrganizationAccess>list){

    }

    static final Comparator<OrganizationAccess> byDateCreasing = (o1, o2) -> o1.getExitTimestamp().compareTo(o2.getExitTimestamp());

}