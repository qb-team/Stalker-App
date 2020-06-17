package it.qbteam.stalkerapp.ui.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.webianks.library.scroll_choice.ScrollChoice;
import it.qbteam.stalkerapp.model.backend.dataBackend.Organization;
import it.qbteam.stalkerapp.model.backend.dataBackend.OrganizationMovement;
import it.qbteam.stalkerapp.tools.BackPressImplementation;
import it.qbteam.stalkerapp.tools.FragmentListenerFeatures;
import it.qbteam.stalkerapp.tools.OnBackPressListener;
import it.qbteam.stalkerapp.contract.MyStalkersListContract;
import it.qbteam.stalkerapp.presenter.MyStalkersListPresenter;
import it.qbteam.stalkerapp.R;
import it.qbteam.stalkerapp.tools.OrganizationViewAdapter;
import it.qbteam.stalkerapp.tools.SearchViewCustom;
import org.json.JSONException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyStalkersListFragment extends Fragment implements MyStalkersListContract.View, OrganizationViewAdapter.OrganizationListener, SearchView.OnQueryTextListener, OnBackPressListener {

    private MyStalkersListPresenter myStalkersListPresenter;
    private List<Organization> organizationList;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private static String path;
    private FragmentListenerFeatures fragmentListenerFeatures;
    private TextView errorText;
    private SwipeRefreshLayout refresh;
    private String countrySelected="";
    private MenuItem searchForName;
    private MenuItem searchForCity;
    private Dialog dialogNation;
    private List<String> nationList;
    private Button selectCountry;
    private SearchView searchView;
    private List<Organization> auxList;
    private static final String SHARED_PREFS = "sharedPrefs";
    private SharedPreferences mPrefs2;
    private Gson gson;
    private String userToken;
    private String userID;

    // This method insures that the Activity has actually implemented our
    // listener and that it isn't null.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentListenerFeatures) {
            fragmentListenerFeatures = (FragmentListenerFeatures) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " MyStalkersListFragmentListener");
        }
    }

    //Creation of the fragment as a component.
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        myStalkersListPresenter = new MyStalkersListPresenter(this);
        organizationList = new ArrayList<>();
        path = getContext().getFilesDir() + "/Preferiti.txt";

    }

    //Creation of the graphic part displayed by the user.
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mystalker_list, container, false);
        refresh = view.findViewById(R.id.swiperefreshID);
        refresh.setColorSchemeResources(R.color.colorAccent);
        gson = Converters.registerOffsetDateTime(new GsonBuilder()).create();
        auxList= new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        errorText = view.findViewById(R.id.errorTextID);
        //Refresh to load MyStalkerList's organizations (swipe down).
        refresh.setOnRefreshListener(this::loadMyStalkerList);
        nationList= new ArrayList<>();
        new Handler().postDelayed(() -> {

            mPrefs2 = this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
            userToken = mPrefs2.getString("userToken", "");
            userID = mPrefs2.getString("userID","");
            loadMyStalkerList();
            }, 3000);

        String[] locales = Locale.getISOCountries();
        for(String countryCode : locales) {

            Locale obj = new Locale("", countryCode);
            nationList.add(obj.getDisplayCountry());
        }

        return view;
    }

    //Initializes and allows the user to view the fragment of the organization following a quick click.
    @Override
    public void organizationClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("name", organizationList.get(position).getName());
        bundle.putString("description", organizationList.get(position).getDescription());
        bundle.putLong("orgID", organizationList.get(position).getId());
        bundle.putString("image", organizationList.get(position).getImage());
        bundle.putString("serverURL", organizationList.get(position).getAuthenticationServerURL());
        bundle.putString("creationDate", organizationList.get(position).getCreationDate().toString());

        if (organizationList.get(position).getTrackingMode().getValue().equals("anonymous")) {
            StandardOrganizationFragment stdOrgFragment = new StandardOrganizationFragment();
            stdOrgFragment.setArguments(bundle);
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.addToBackStack(null);
            transaction.replace(R.id.ListaPreferitiID, stdOrgFragment).commit();
            fragmentListenerFeatures.disableScroll(false);
        } else {
            LDAPorganizationFragment LDAPFragment = new LDAPorganizationFragment();
            LDAPFragment.setArguments(bundle);
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.addToBackStack(null);
            transaction.replace(R.id.ListaPreferitiID, LDAPFragment).commit();
            fragmentListenerFeatures.disableScroll(false);
        }
    }
    //Notifies the user through a dialog box, the possibility of deleting the organization selected by the user after a long click.
    @Override
    public void organizationLongClick(int position) {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(getContext())
                // set message, title, and icon
                .setTitle("Elimina organizzazione")
                .setMessage("Sei sicuro di voler eliminare l'organizzazione?")
                .setPositiveButton("Elimina", (dialog, whichButton) -> {
                    try {
                         String organizationMovementJson = mPrefs2.getString("organizationMovement",null);
                         OrganizationMovement organizationMovement = gson.fromJson(organizationMovementJson,OrganizationMovement.class);
                        if (organizationMovement !=null && organizationList.get(position).getId().equals(organizationMovement.getOrganizationId())){
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Condizioni di eliminazione organizzazione")
                                    .setMessage("Attualmente sei tracciato in questa organizzazione, prima di eliminarla devi uscire dall'organizzazione")
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                        }
                                    });
                            builder.show();
                        }

                        else removeOrganization(position);

                    }
                    catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                })
                .setNegativeButton("Annulla", (dialog, which) -> dialog.dismiss())
                .create();
        myQuittingDialogBox.show();
    }

    //It hides to menu actionTab the option "Aggiungi a MyStalkers".
    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        MenuItem item= menu.findItem(R.id.searchID);
        searchView= (SearchView) item.getActionView();
        MenuItem countryItem = menu.findItem(R.id.search_countryID);
        searchForName = menu.findItem(R.id.search_nameID);
        searchForCity = menu.findItem(R.id.search_cityID);
        menu.setGroupVisible(R.id.filterID,true);
        menu.findItem(R.id.order_forID).setVisible(false);

        if(!countrySelected.equals("")) {
            countryItem.setTitle("Nazione scelta: " + countrySelected);
        } else {
            countryItem.setTitle("Scegli una nazione");
        }

        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        searchView.setMaxWidth(width*2/3);

        if (!searchForCity.isChecked())
            new SearchViewCustom()
                    .setSearchBackGroundResource(R.drawable.custom_border)
                    .setSearchIconResource(R.drawable.ic_search_black_24dp, true, false) //true to icon inside edittext, false to outside
                    .setSearchHintText("Cerca per nome..")
                    .setSearchTextColorResource(R.color.colorPrimary)
                    .setSearchHintColorResource(R.color.colorPrimary)
                    .setSearchCloseIconResource(R.drawable.ic_close_black_24dp)
                    .setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS)
                    .format(searchView);

        searchView.setOnQueryTextListener(this);

        super.onPrepareOptionsMenu(menu);
    }

    private void resetAdapter(){
        new SearchViewCustom()
                .setSearchIconResource(R.drawable.ic_search_black_24dp, true, false) //true to icon inside edittext, false to outside
                .setSearchHintText("Cerca per nome..")
                .format(searchView);
        countrySelected = "";
        auxList.clear();
        adapter = new OrganizationViewAdapter(organizationList, this.getContext(),this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()){

            case R.id.search_nameID:
                resetAdapter();
                item.setChecked(true);

                break;

            case R.id.search_cityID:
                item.setChecked(true);
                new SearchViewCustom()
                        .setSearchIconResource(R.drawable.ic_search_black_24dp, true, false) //true to icon inside edittext, false to outside
                        .setSearchHintText("Cerca per città..")
                        .format(searchView);
                break;

            case R.id.search_countryID:
                countryDialog(item);
                break;

            case R.id.search_anonymousID:
                resetAdapter();
                item.setChecked(true);
                for(int i = 0; i< organizationList.size(); i++){
                    if(organizationList.get(i).getTrackingMode().toString().equals("anonymous"))
                        auxList.add(organizationList.get(i));
                }
                adapter=new OrganizationViewAdapter(auxList,this.getContext(),this);
                recyclerView.setAdapter(adapter);
                break;

            case R.id.search_authenticateID:
                resetAdapter();
                item.setChecked(true);
                for(int i = 0; i< organizationList.size(); i++){
                    if(organizationList.get(i).getTrackingMode().toString().equals("authenticated"))
                        auxList.add(organizationList.get(i));
                }
                adapter=new OrganizationViewAdapter(auxList,this.getContext(),this);
                recyclerView.setAdapter(adapter);
                break;

        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    //Display the list of organizations on the screen following user inputs in the search menu.
    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText.toLowerCase();
        List<Organization> newList = new ArrayList<>();

        if (searchForName.isChecked() || searchForCity.isChecked()){
            auxList= new ArrayList<>(organizationList);
        }

        if (searchForCity.isChecked()){
            for (int i = 0; i < auxList.size(); i++) {
                if (auxList.get(i).getCity().toLowerCase().contains(userInput))
                    newList.add(auxList.get(i));
            }
        }
        else if (organizationList.size() != 0) {
            for (int i = 0; i < auxList.size(); i++) {
                if (auxList.get(i).getName().toLowerCase().contains(userInput))
                    newList.add(auxList.get(i));
            }
        }

        adapter = new OrganizationViewAdapter(newList, this.getContext(), this);
        recyclerView.setAdapter(adapter);
        return false;
    }

    private void countryDialog(MenuItem item){
        dialogNation = new Dialog(getContext());
        dialogNation.setContentView(R.layout.dialog_scroll_choice_nation);
        dialogNation.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogNation.show();
        ScrollChoice scrollChoiceNation = dialogNation.findViewById(R.id.scroll_choiceID);
        scrollChoiceNation.addItems(nationList,nationList.size()/2);
        selectCountry=dialogNation.findViewById(R.id.selectID);
        Button annulCountry = dialogNation.findViewById(R.id.annulID);
        scrollChoiceNation.setOnItemSelectedListener(new ScrollChoice.OnItemSelectedListener() {
            @Override
            public void onItemSelected(ScrollChoice scrollChoice, int position, String name) {

                selectCountry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        resetAdapter();
                        countrySelected=name;
                        item.setChecked(true);
                        printCountrySelected();
                        dialogNation.dismiss();
                    }
                });
            }
        });
        annulCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogNation.dismiss();

            }
        });
    }

    private void printCountrySelected(){
        for(int i = 0; i< organizationList.size(); i++){
            if(organizationList.get(i).getCountry().equals(countrySelected))
                auxList.add(organizationList.get(i));
        }
        adapter=new OrganizationViewAdapter(auxList,this.getContext(),this);
        recyclerView.setAdapter(adapter);
    }

    //Add the organization received as input to both the FileSystem and the Server.
    public void addOrganization(Organization organization) throws IOException, JSONException {
        myStalkersListPresenter.addOrganizationLocal(organization, organizationList, path);
        myStalkersListPresenter.addOrganizationServer(organization, userID, userToken);
        new Handler().postDelayed(() -> {
            loadMyStalkerList();
        }, 2000);
    }

    //Notifies the user of the success of the organization's add operation.
    @Override
    public void onSuccessAddOrganization(List<Organization> list, String message) throws IOException, JSONException {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        adapter = new OrganizationViewAdapter(list, this.getContext(), this);
        recyclerView.setAdapter(adapter);
        myStalkersListPresenter.updateFile(list, path);
        errorText.setVisibility(View.INVISIBLE);

    }

    //Notifies the user that the organization's addition operation has failed.
    @Override
    public void onFailureAddOrganization(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    //Removes an organization from both the FileSystem and the Server.
    private void removeOrganization(int position) throws IOException, JSONException {
        myStalkersListPresenter.removeOrganizationServer(organizationList.get(position), userID, userToken);
        myStalkersListPresenter.removeOrganizationLocal(organizationList.get(position), organizationList, path);
    }

    //Notifies the user of the success of an organization's removal operation.
    @Override
    public void onSuccessRemoveOrganization(List<Organization> list) throws IOException, JSONException {
        adapter = new OrganizationViewAdapter(list, this.getContext(), this);
        recyclerView.setAdapter(adapter);
        myStalkersListPresenter.updateFile(list, path);
        if(list.size()==0)
            errorText.setVisibility(View.VISIBLE);
    }

    //Downloads from the Server the list of organizations previously added by the user.
    public void loadMyStalkerList() {

        if(userToken!=null && userID!=null) {
            myStalkersListPresenter.loadFavoriteListServer(userID, userToken);

        }

    }

    //It notifies the user of the successful download of his list of organizations included in `MyStalkersList` and shows them on the screen.
    @Override
    public void onSuccessLoadMyStalkerList(List<Organization> list) throws IOException, JSONException {

        if (list != null)
            //Assegno la lista appena scaricata dal server a organizationList.
            organizationList = list;
            adapter = new OrganizationViewAdapter(organizationList, this.getContext(), this);
            recyclerView.setAdapter(adapter);
            myStalkersListPresenter.updateFile(organizationList, path);
            errorText.setVisibility(View.INVISIBLE);
            Toast.makeText(getActivity(), "Lista MyStalker aggiornata", Toast.LENGTH_SHORT).show();
            refresh.setRefreshing(false);
    }

    @Override
    public void onFailureLoadMyStalkerList() {
        errorText.setVisibility(View.VISIBLE);
        Toast.makeText(getContext(), "Lista MyStalker ancora vuota", Toast.LENGTH_SHORT).show();
        refresh.setRefreshing(false);
    }

    public boolean organizationIsPresentInList(String orgName){
        boolean found = false;
        for(int i=0; i<organizationList.size();i++){
            if(organizationList.get(i).getName()==orgName)
                found = true;
        }
        return found;

    }

    //Returns the user to the previous Activity or Fragment.
    @Override
    public boolean onBackPressed() {
        return new BackPressImplementation(this).onBackPressed();
    }

}