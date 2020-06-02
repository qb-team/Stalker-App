package it.qbteam.stalkerapp.ui.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import it.qbteam.stalkerapp.HomePageActivity;
import it.qbteam.stalkerapp.MainActivity;
import it.qbteam.stalkerapp.contract.SignUpContract;
import it.qbteam.stalkerapp.presenter.SignUpPresenter;
import it.qbteam.stalkerapp.R;
import it.qbteam.stalkerapp.tools.BackPressImplementation;
import it.qbteam.stalkerapp.tools.OnBackPressListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class SignUpFragment extends Fragment implements View.OnClickListener, SignUpContract.View, OnBackPressListener {
    private EditText emailEditText, passwordEditText, confPasswordEditText;
    private SignUpPresenter signUpPresenter;
    private ProgressDialog progressDialog;
    private CheckBox termsofUseCheckBox;

    //Creation of the fragment as a component.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //Creation of the graphic part displayed by the user.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_registrati,container,false);
        emailEditText = view.findViewById(R.id.emailID);
        passwordEditText = view.findViewById(R.id.passwordID);
        confPasswordEditText = view.findViewById(R.id.confPasswordID);
        termsofUseCheckBox = view.findViewById(R.id.TermsofUseID);
        Button signUpButton= view.findViewById(R.id.signUpButtonID);
        signUpPresenter=new SignUpPresenter(this);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Stiamo registrando il tuo account sul Database");
        signUpButton.setOnClickListener(this);

        //Clickable string accept Term of Use
        TextView termsofUseTextView = view.findViewById(R.id.TermsofUseTextID);
        String termsofUseString = "Accetta le condizioni d'uso";
        SpannableString termsofUseSpannableString = new SpannableString(termsofUseString);
        ClickableSpan termsofUseSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                showTermsofUse();
                widget.invalidate();
            }
        };
        termsofUseSpannableString.setSpan(termsofUseSpan, 11, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        termsofUseTextView.setText(termsofUseSpannableString);
        termsofUseTextView.setMovementMethod(LinkMovementMethod.getInstance());

        //Clickable string go back to login
        TextView loginTextView = view.findViewById(R.id.textViewLoginID);
        String loginString = "Hai già un account?\n Clicca qui";
        SpannableString loginSpannableString = new SpannableString(loginString);
        ClickableSpan loginSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                MainActivity.fragmentManager.popBackStack();
            }
        };
        loginSpannableString.setSpan(loginSpan, 21, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        loginTextView.setText(loginSpannableString);
        loginTextView.setMovementMethod(LinkMovementMethod.getInstance());
        return view;
    }

    //Wait for a user click on the "Sign Up" button to invoke the `checkSignUpDetails ()` method, or a click on "Read the conditions of use" to invoke the `showTermsofUse ()` method.
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.signUpButtonID) {
            checkSignUpDetails();
        }
    }

    //Check if the user has written their credentials, confirmed the password and accepted the conditions of use, if so, send them to the `checkSignUp (email: String, password: String)` method, otherwise report the user of absence of them.
    private void checkSignUpDetails() {

        if(!TextUtils.isEmpty(emailEditText.getText()) && !TextUtils.isEmpty(passwordEditText.getText())
                && (passwordEditText.getText().length() >= 6) && termsofUseCheckBox.isChecked() && confPasswordEditText.getText().toString().equals(passwordEditText.getText().toString())){
            checkSignUp(emailEditText.getText().toString().trim(), passwordEditText.getText().toString());
        }
        else {

            if (!confPasswordEditText.getText().toString().equals(passwordEditText.getText().toString())){
                System.out.println("conf password" + confPasswordEditText.getText());
                System.out.println("password" + passwordEditText.getText());
                Toast.makeText(getContext(), "Le password non coincidono", Toast.LENGTH_LONG).show();
            }

            if(passwordEditText.getText().toString().length() < 6)
                Toast.makeText(getContext(), "Inserire una password di lunghezza minima di 6 caratteri", Toast.LENGTH_LONG).show();

            if(TextUtils.isEmpty(emailEditText.getText().toString().trim()))
                emailEditText.setError("Inserisci una email valida");

            if(TextUtils.isEmpty(passwordEditText.getText().toString()))
                passwordEditText.setError("Inserisci una password valida");

            if(!termsofUseCheckBox.isChecked())
                Toast.makeText(getContext(), "Per poterti registrare devi accettare le condizioni d'uso", Toast.LENGTH_SHORT).show();
        }
    }

    //Invokes the Firebase methods to register the email and the password, in case of success the `onSignUpSuccess (message: String)` method will be invoked while, in case of failure it will be invoked `onSignUpFailure (e: FirebaseException)`.
    private void checkSignUp(String email, String password) {
            progressDialog.show();
            signUpPresenter.signUp(email, password);
    }

    //Opens a pop-up showing the conditions of use that the user will have to accept.
    private void showTermsofUse(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Condizioni d'uso")
                .setMessage("I Contenuti dei Servizi sono destinati esclusivamente ad un utilizzo personale. Ogni diverso utilizzo è vietato in ogni forma. L’Istituto detiene tutti i diritti di sfruttamento dei marchi utilizzati in collegamento ai Servizi. I Servizi e i suoi Contenuti sono protetti dalle norme sul diritto d’autore vigente in Italia e dalle norme internazionali sul diritto d’autore. L’Utente non è autorizzato a modificare, pubblicare, trasmettere, condividere, cedere in uso a qualsiasi titolo, riprodurre (oltre i limiti di seguito precisati), tradurre, rielaborare, distribuire, eseguire, dare accesso o sfruttare commercialmente in qualsiasi modo i Servizi e i loro Contenuti (incluso il software) anche solo parzialmente. L’Istituto non assume alcuna responsabilità in relazione a danni o limitazioni d’uso di siti internet, computer o altri strumenti che abbiano utilizzato i Servizi e i loro Contenuti.")
                .setPositiveButton("ok", (dialogInterface, i) -> {
                });
        builder.show();
    }

    //The registration was successful and the user will be moved to the `HomePageActivity.class` and displays a message indicating that he has successfully authenticated.
    @Override
    public void onSignUpSuccess(FirebaseUser firebaseUser) {
        progressDialog.dismiss();
        Toast.makeText(getActivity(), "Registrazione effettuato con successo" , Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), HomePageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    //Registration has failed and the user displays a message indicating the error.
    @Override
    public void onSignUpFailure(FirebaseException e) {
        progressDialog.dismiss();

        if (e instanceof FirebaseNetworkException)
            Toast.makeText(getActivity(), "La tua connessione a internet è assente" , Toast.LENGTH_LONG).show();

        if (e instanceof FirebaseAuthInvalidCredentialsException)
            Toast.makeText(getActivity(), "Le credenziali non sono state inserite correttamente" , Toast.LENGTH_LONG).show();

        if (e instanceof FirebaseAuthUserCollisionException)
            Toast.makeText(getActivity(), "L'e-mail è già presente nel sistema" , Toast.LENGTH_LONG).show();
    }

    //Manages the back button.
    @Override
    public boolean onBackPressed() {
        return new BackPressImplementation(this).onBackPressed();
    }
}
