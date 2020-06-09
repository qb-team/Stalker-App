package it.qbteam.stalkerapp.ui.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.scrounger.countrycurrencypicker.library.Country;
import com.scrounger.countrycurrencypicker.library.CountryCurrencyPicker;
import com.scrounger.countrycurrencypicker.library.Currency;
import com.scrounger.countrycurrencypicker.library.Listener.CountryCurrencyPickerListener;
import com.scrounger.countrycurrencypicker.library.PickerType;
import com.webianks.library.scroll_choice.ScrollChoice;

import org.json.JSONException;
import it.qbteam.stalkerapp.HomePageActivity;
import it.qbteam.stalkerapp.model.backend.dataBackend.Organization;
import it.qbteam.stalkerapp.tools.BackPressImplementation;
import it.qbteam.stalkerapp.tools.OnBackPressListener;
import it.qbteam.stalkerapp.contract.HomeContract;
import it.qbteam.stalkerapp.presenter.HomePresenter;
import it.qbteam.stalkerapp.R;
import it.qbteam.stalkerapp.tools.OrganizationViewAdapter;
import it.qbteam.stalkerapp.tools.SearchViewCustom;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment implements HomeContract.View, OrganizationViewAdapter.OrganizationListener, SearchView.OnQueryTextListener, OnBackPressListener {

    private HomePresenter OrganizationListPresenter;
    private List<Organization> organizationList;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private String path;
    private FragmentListener fragmentListener;
    private SwipeRefreshLayout refresh;
    private List<Organization> auxList;
    private String countrySelected="";
    private MenuItem searchForName;
    private MenuItem searchForCity;
    private Dialog dialogNation;
    private List<String> nationList;
    private Button selectCountry;
    private SearchView searchView;
    private TextView errorTextView;
    private static final String SHARED_PREF = "sharedPref";
    private SharedPreferences mPrefs2;
    private String userToken;
    private String userID;


    //Interfate to communicate with MyStalkerListFragment through the HomePageActivity.
    public interface FragmentListener {
        void sendOrganization(Organization organization) throws IOException, JSONException;
        void disableScroll(boolean enable);
    }

    // This method insures that the Activity has actually implemented our
    // listener and that it isn't null.
   @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentListener) {
            fragmentListener = (FragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " FragmentListener");
        }
    }


    //Creation of the fragment as a component and instantiation of the path of the file "/Organizzazioni.txt".
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        OrganizationListPresenter = new HomePresenter(this);


        try {
            OrganizationListPresenter.createAllFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        path = getContext().getFilesDir() + "/Organizzazioni.txt";
    }

    //Creation of the graphic part displayed by the user.
    @SneakyThrows
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organizations_list, container, false);
        refresh = view.findViewById(R.id.swiperefreshID);
        recyclerView = view.findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        errorTextView = view.findViewById(R.id.errorTextID);

        //Refresh to upload the organization list (swipe down).
        refresh.setOnRefreshListener(this::downloadList);
        auxList= new ArrayList<>();
        //Controlla se la lista è vuota, in caso positivo la scarica
        new Handler().postDelayed(() -> {
            mPrefs2 = this.getActivity().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
            userToken = mPrefs2.getString("userToken", "");
            userID = mPrefs2.getString("userID","");
                    checkFile();
        }, 3000);
         nationList= new ArrayList<>();
         String[] locales = Locale.getISOCountries();
         for(String countryCode : locales) {

            Locale obj = new Locale("", countryCode);
            nationList.add(obj.getDisplayCountry());
         }

        return view;

    }


    //It takes care of loading the list of organizations and uploading them directly from FileSystem.
    public void checkFile()  {
        organizationList=OrganizationListPresenter.checkLocalFile(path);

        if(organizationList != null){
            adapter = new OrganizationViewAdapter(organizationList, this.getContext(),this);
            recyclerView.setAdapter(adapter);
        }
        else
            Toast.makeText(getActivity(),"Devi ancora scaricare la lista", Toast.LENGTH_SHORT).show();
    }

    //It takes care of managing a possible error while reading from FileSystem and it makes the user see the error during loading.
    @Override
    public void onFailureCheckFile(String message) {


            AlertDialog download = new AlertDialog.Builder(getContext())
                    .setTitle("Scarica lista delle organizzazioni")
                    .setMessage("La tua lista delle organizzazioni è ancora vuota, vuoi scaricarla?")
                    .setPositiveButton("Scarica", (dialog, which) -> {
                        downloadList();
                        dialog.dismiss();
                    })
                    .setNegativeButton("Annulla", (dialog, which) -> {
                    })
                    .create();
            download.show();
            errorTextView.setVisibility(View.VISIBLE);

    }

    //It takes care of downloading the list from the Server and it saves it on FileSystem.
    private void downloadList() {

        if(userToken!=null)
            OrganizationListPresenter.downloadHomeListServer(path,userToken);
        else {
            refresh.setRefreshing(false);
            Toast.makeText(getActivity(), "Errore durante lo scaricamento della lista", Toast.LENGTH_SHORT).show();
        }
    }

    //It notifies the user of the correct download of the list from the Server.
    @Override
    public void onSuccessDownloadList(String message) {
        refresh.setRefreshing(false);
        checkFile();
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        errorTextView.setVisibility(View.INVISIBLE);
    }

    //It notifies the user of the false download of the list from the Server.
    @Override
    public void onFailureDownloadList(String message) {
        refresh.setRefreshing(false);
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    //Initializing and displaying of the fragment of the organization to the user, this method is invoked following a user click.
    @Override
    public void organizationClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("name", organizationList.get(position).getName());
        bundle.putString("description", organizationList.get(position).getDescription());
        bundle.putLong("orgID",organizationList.get(position).getId());
        bundle.putString("image", organizationList.get(position).getImage());
        bundle.putString("serverURL", organizationList.get(position).getAuthenticationServerURL());
        bundle.putString("creationDate",organizationList.get(position).getCreationDate().toString());

        if(organizationList.get(position).getTrackingMode().toString().equals("anonymous")){
            StandardOrganizationFragment stdOrgFragment= new StandardOrganizationFragment();
            stdOrgFragment.setArguments(bundle);
            FragmentTransaction transaction= getChildFragmentManager().beginTransaction();
            transaction.addToBackStack(null);
            transaction.replace(R.id.HomeFragmentID, stdOrgFragment).commit();
            fragmentListener.disableScroll(false);
        }
        else {
            bundle.putString("trackingMode",organizationList.get(position).getTrackingMode().getValue());
            LDAPorganizationFragment LDAPFragment= new LDAPorganizationFragment();
            LDAPFragment.setArguments(bundle);
            FragmentTransaction transaction= getChildFragmentManager().beginTransaction();
            transaction.addToBackStack(null);
            transaction.replace(R.id.HomeFragmentID, LDAPFragment).commit();
            fragmentListener.disableScroll(false);
        }
    }

    //Notification received through a dialog, additional information of the organization selected by the user afterwards and a long click.
    @Override
    public void organizationLongClick(int position) {
        Dialog myDialog = new Dialog(getContext());
        myDialog.setContentView(R.layout.dialog_organizzazione);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView dialog_nomeOrganizzazione = myDialog.findViewById(R.id.dialog_nomeOrganizzazione);
        ImageView image = myDialog.findViewById(R.id.imageID);
        UrlImageViewHelper.setUrlDrawable(image,organizationList.get(position).getImage());
        dialog_nomeOrganizzazione.setText(organizationList.get(position).getName());
        myDialog.show();
        Button moreInfo=myDialog.findViewById(R.id.Button_moreInfo);
        Button aggPref=myDialog.findViewById(R.id.Button_aggiungiPreferiti);

        if(!organizationList.get(position).getTrackingMode().toString().equals("anonymous")) {
            aggPref.setVisibility(View.INVISIBLE);
        }

        moreInfo.setOnClickListener(v -> {
            organizationClick(position);
            myDialog.dismiss();
        });

        //Try to add the organization locally and on the server.
        aggPref.setOnClickListener(v -> {
            try {
                fragmentListener.sendOrganization(organizationList.get(position));
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            myDialog.dismiss();
        });
    }

    //It sorts the list in the HomeFragment view in alphabetical order.
    public void alphabeticalOrder(){

        auxList= new ArrayList<>(organizationList);
        Collections.sort(organizationList);
        try {
            adapter = new OrganizationViewAdapter(organizationList,this.getContext(),this);
            recyclerView.setAdapter(adapter);
            OrganizationListPresenter.updateFile(organizationList,path);
        }
        catch (IOException | JSONException e) {
            e.printStackTrace();
        }
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

    public void resetAdapter(){
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

           case R.id.alphabeticalOrderID:
               alphabeticalOrder();
               item.setChecked(true);
               break;

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
                //da finire.
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

    public void printCountrySelected(){
        for(int i = 0; i< organizationList.size(); i++){
            if(organizationList.get(i).getCountry().equals(countrySelected))
                auxList.add(organizationList.get(i));
        }
        adapter=new OrganizationViewAdapter(auxList,this.getContext(),this);
        recyclerView.setAdapter(adapter);
    }

    //Management of the back button.
    @Override
    public boolean onBackPressed() {
        return new BackPressImplementation(this).onBackPressed();
    }

}



