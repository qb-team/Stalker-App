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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import it.qbteam.stalkerapp.R;
import it.qbteam.stalkerapp.model.backend.dataBackend.Organization;
import it.qbteam.stalkerapp.model.service.Storage;
import it.qbteam.stalkerapp.tools.BackPressImplementation;
import it.qbteam.stalkerapp.tools.OnBackPressListener;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import org.json.JSONException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StandardOrganizationFragment extends Fragment implements OnBackPressListener {

    public final static String TAG="StandardOrganizationFragment";
    private TextView title, description ;
    private ImageView image;
    private FloatingActionButton access;
    private Dialog accessDialog;


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
        access= view.findViewById(R.id.accessID);
        access.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accessDialog = new Dialog(getContext());
                accessDialog.setContentView(R.layout.dialog_access_history);
                TableLayout tableLayout= accessDialog.findViewById(R.id.scroll_table);
                accessDialog.show();
                Button exit=accessDialog.findViewById(R.id.exitID);
                Button reset= accessDialog.findViewById(R.id.resetID);
                exit.setOnClickListener(view -> {
                    accessDialog.dismiss();
                });
                reset.setOnClickListener(view -> {
                    tableLayout.removeAllViews();
                });


                HashMap<String, String> map= Storage.deserializeAccessExitInLocal();
                Set set =map.entrySet();
                Iterator iterator = set.iterator();
                while(iterator.hasNext()) {
                    Map.Entry entry = (Map.Entry)iterator.next();
                    TableRow tr=new TableRow(getContext());
                    TextView tv= new TextView(getContext());
                    tv.setText(entry.getValue().toString());
                    tv.setGravity(Gravity.CENTER);
                    tr.addView(tv);
                    tableLayout.addView(tr);
                    System.out.print("key: "+ entry.getKey() + " & Value: ");
                    System.out.println(entry.getValue());
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