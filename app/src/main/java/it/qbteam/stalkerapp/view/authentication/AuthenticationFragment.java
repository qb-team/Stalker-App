package it.qbteam.stalkerapp.view.authentication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import it.qbteam.stalkerapp.R;

public class AuthenticationFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.authentication_fragment_layout,container,false);

        Button login = view.findViewById(R.id.buttonLogin);
        Button signUp = view.findViewById(R.id.buttonSignUp);
        login.setOnClickListener(this);
        signUp.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLogin:
                goToLogin();
                break;
            case R.id.buttonSignUp:
                goToSignUp();
                break;
        }

    }

    private void goToLogin(){
        FragmentManager manager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.AuthenticatorContainerID, new LoginFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void goToSignUp(){
        FragmentManager manager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.AuthenticatorContainerID, new SignUpFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

}


