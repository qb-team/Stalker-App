package com.example.stalkerapp.ui.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.stalkerapp.HomePage;
import com.example.stalkerapp.MainActivity;
import com.example.stalkerapp.Presenter.LoginContract;
import com.example.stalkerapp.Presenter.LoginPresenter;
import com.example.stalkerapp.R;

public class Login extends Fragment implements LoginContract.View, View.OnClickListener {
    public final static String TAG="Login_Fragment";
    private LoginPresenter loginPresenter;
    ProgressDialog mProgressDialog;
    private EditText mEmail,mPassword;
    private Button mLoginBtn;



    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //QUI COSTRUISCO LA VIEW DI LOGIN

        View view=inflater.inflate(R.layout.fragment_login,container,false);
        mEmail = view.findViewById(R.id.Emailtext);
        mPassword = view.findViewById(R.id.passwordtext);
        mLoginBtn = view.findViewById(R.id.buttonAccesso);

        //set listener

        mLoginBtn.setOnClickListener(this);

        //set init

        loginPresenter = new LoginPresenter(this);
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage("Aspetta il completamento del login");

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonAccesso:
                checkLoginDetails();
                break;
        }
    }

    private void checkLoginDetails() {
        if(!TextUtils.isEmpty(mEmail.getText().toString()) && !TextUtils.isEmpty(mPassword.getText().toString())){
            initLogin(mEmail.getText().toString(), mPassword.getText().toString());
        }else{
            if(TextUtils.isEmpty(mEmail.getText().toString())){
                mEmail.setError("Inserisci una email valida");
            }if(TextUtils.isEmpty(mPassword.getText().toString())){
                mPassword.setError("Inserisci una password valida");
            }
        }
    }

    private void initLogin(String email, String password) {
        mProgressDialog.show();
        loginPresenter.login(this, email, password);
    }

    @Override
    public void onLoginSuccess(String message) {
        mProgressDialog.dismiss();

        Toast.makeText(getActivity(), "Login effettuato con successo" , Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), HomePage.class);
        startActivity(intent);
    }

    @Override
    public void onLoginFailure(String message) {
        mProgressDialog.dismiss();
        Toast.makeText(getActivity(),message , Toast.LENGTH_SHORT).show();
    }

}