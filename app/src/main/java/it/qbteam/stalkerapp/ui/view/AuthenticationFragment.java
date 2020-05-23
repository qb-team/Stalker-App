package it.qbteam.stalkerapp.ui.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import it.qbteam.stalkerapp.HomePageActivity;
import it.qbteam.stalkerapp.MainActivity;
import it.qbteam.stalkerapp.R;
import it.qbteam.stalkerapp.contract.LoginContract;
import it.qbteam.stalkerapp.presenter.LoginPresenter;
import it.qbteam.stalkerapp.tools.BackPressImplementation;
import it.qbteam.stalkerapp.tools.OnBackPressListener;

//Schermata iniziale per gli utenti non autenticati
public class AuthenticationFragment extends Fragment implements LoginContract.View, View.OnClickListener, OnBackPressListener {
    private Dialog myDialog;
    private LoginPresenter loginPresenter;
    ProgressDialog progressDialog;
    private EditText emailEditText, passwordEditText, insertEmailEditText;
    private Button loginButton;
    private TextView forgotPasswordTextView;

    //Creation of the fragment as a component.
    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //Creation of the graphic part displayed by the user.
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_authentication,container,false);
        TextView registerTextView = view.findViewById(R.id.textViewRegisterID);

        emailEditText = view.findViewById(R.id.Emailtext);
        passwordEditText = view.findViewById(R.id.passwordtextID);
        insertEmailEditText = view.findViewById(R.id.insertEmailID);
        forgotPasswordTextView = view.findViewById(R.id.forgotPasswordID);
        loginButton = view.findViewById(R.id.loginButtonID);
        loginButton.setOnClickListener(this);
        forgotPasswordTextView.setOnClickListener(this);
        loginPresenter = new LoginPresenter(this);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Aspetta il completamento del login");
        String registerString = "Non hai un account?\n Clicca qui";
        SpannableString registerSpannableString = new SpannableString(registerString);
        ClickableSpan registerSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                MainActivity.fragmentManager.beginTransaction().replace(R.id.containerID, new SignUpFragment()).addToBackStack(null).commit();
            }
        };
        registerSpannableString.setSpan(registerSpan, 21, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        registerTextView.setText(registerSpannableString);
        registerTextView.setMovementMethod(LinkMovementMethod.getInstance());
        return view;
    }

    //When the user click on the "Login" button, this method is invoked and it calls the `checkLoginDetails()` method.
    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.loginButtonID) {
            checkLoginDetails();
        }

        if(v.getId() == R.id.forgotPasswordID) {
            forgotPassword();
        }
    }

    private void forgotPassword() {
        myDialog = new Dialog(getContext());
        myDialog.setContentView(R.layout.dialog_forgotpassword);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button annulButton= myDialog.findViewById(R.id.annulID);
        Button accessButton= myDialog.findViewById(R.id.accessID);
        myDialog.show();

        //That is the annul button of the pop-up.
        annulButton.setOnClickListener(v12 -> myDialog.dismiss());

        //That is the access button of the pop-up.
        accessButton.setOnClickListener(v1 -> {
            insertEmailEditText = myDialog.findViewById(R.id.insertEmailID);
            String email = insertEmailEditText.getText().toString();

            if (!email.equals("")) {
                loginPresenter.forgotPassword(email);
                myDialog.dismiss();
            }
        });
    }

    //Check if the user has written their credentials and send them to the `initLogin (email: String, password: String)` method, otherwise it signals the absence of them.
    public void checkLoginDetails() {

        if(!TextUtils.isEmpty(emailEditText.getText().toString()) && !TextUtils.isEmpty(passwordEditText.getText().toString())) {
            checkLogin(emailEditText.getText().toString(), passwordEditText.getText().toString());
        }

        else {

            if(TextUtils.isEmpty(emailEditText.getText().toString())) {
                emailEditText.setError("Inserisci una email valida");
            }

            if(TextUtils.isEmpty(passwordEditText.getText().toString())) {
                passwordEditText.setError("Inserisci una password valida");
            }

        }
    }

    //Start the login method in the presenter.
    private void checkLogin(String email, String password) {
        progressDialog.show();
        loginPresenter.login( email, password);
    }

    //If the login was successful, the user is notified and directed to the HomePage screen dedicated to authenticated users.
    @Override
    public void onLoginSuccess() {
        progressDialog.dismiss();
        Toast.makeText(getActivity(), "Login effettuato con successo" , Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), HomePageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    //If the login wasn't successful, the user is notified.
    @Override
    public void onCredentialFailure(FirebaseException e) {
        progressDialog.dismiss();

        if(e instanceof FirebaseAuthInvalidCredentialsException) {
            Toast.makeText(getActivity(), "Le credenziali non sono state inserite correttamente" , Toast.LENGTH_LONG).show();
        }

        if(e instanceof FirebaseNetworkException){ //Credo che sia quello in caso l'utente esista già --> registrazione
            Toast.makeText(getActivity(), "Non sei connesso a internet" , Toast.LENGTH_LONG).show();
        }

        if(e instanceof FirebaseAuthInvalidUserException) {
            Toast.makeText(getActivity(), "L'e-mail non esiste" , Toast.LENGTH_LONG).show();
        }

    }



    @Override
    public void onSendEmailSuccess() {
        Toast.makeText(getActivity(), "Email inviata per il reset della password" , Toast.LENGTH_SHORT).show();
    }




    //Manages the back button.
    @Override
    public boolean onBackPressed() {
        return new BackPressImplementation(this).onBackPressed();
    }


}


