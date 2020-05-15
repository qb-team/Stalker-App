package it.qbteam.stalkerapp.ui.view;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import it.qbteam.stalkerapp.R;
import it.qbteam.stalkerapp.model.backend.dataBackend.Organization;
import it.qbteam.stalkerapp.tools.BackPressImplementation;
import it.qbteam.stalkerapp.tools.OnBackPressListener;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import org.json.JSONException;
import java.io.IOException;

public class StandardOrganizationFragment extends Fragment implements OnBackPressListener {

    public final static String TAG="Home_Fragment";
    private TextView title, description ;
    private ImageView image;

    //Creation of the fragment as a component.
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        return view;
    }

    //Makes the 'add to favorites' option visible to the application's action tab menu and hides the search command.
    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        menu.findItem(R.id.favoriteID).setVisible(true);
        menu.findItem(R.id.searchID).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    //Add the organization to the favorites list.
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id= item.getItemId();

        if(id==R.id.preferitiID){
            Bundle bundle=this.getArguments();

            if(bundle!=null){

                try {
                    Organization o=new Organization();
                    o.setName(bundle.getString("name"));
                    MyStalkersListFragment myStalkersListFragment = new MyStalkersListFragment();
                    myStalkersListFragment.addOrganization(o);

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        return super.onOptionsItemSelected(item);
    }

    //Manages the back button.
    @Override
    public boolean onBackPressed() {
        return new BackPressImplementation(this).onBackPressed();
    }


}