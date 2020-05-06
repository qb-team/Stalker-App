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
import it.qbteam.stalkerapp.model.backend.modelBackend.Organization;
import it.qbteam.stalkerapp.tools.BackPressImplementation;
import it.qbteam.stalkerapp.tools.OnBackPressListener;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import org.json.JSONException;
import java.io.IOException;

public class StandardOrganizationFragment extends Fragment implements OnBackPressListener {

    private static StandardOrganizationFragment instance = null;
    public final static String TAG="Home_Fragment";
    private TextView title, risultati, description ;
    private ImageView image;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_organization, container, false);
        Bundle bundle=this.getArguments();
        title=view.findViewById(R.id.titleID);
        image=view.findViewById(R.id.imageID);
        description=view.findViewById(R.id.descriptionID);
        risultati=view.findViewById(R.id.coordinateID);
        title.setText(bundle.getString("name"));
        UrlImageViewHelper.setUrlDrawable(image, bundle.getString("image"));
        description.setText(bundle.getString("description"));

        return view;

    }


    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        menu.findItem(R.id.favoriteID).setVisible(true);
        menu.findItem(R.id.searchID).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id= item.getItemId();
        if(id==R.id.preferitiID){
            Bundle bundle=this.getArguments();

            if(bundle!=null){

                try {
                    Organization o=new Organization();
                    o.setName(bundle.getString("name"));
                    MyStalkersListFragment.getInstance().addOrganization(o);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        return super.onOptionsItemSelected(item);
    }



    public static StandardOrganizationFragment getInstance() {
        return instance;
    }

    @Override
    public boolean onBackPressed() {
        return new BackPressImplementation(this).onBackPressed();
    }


}