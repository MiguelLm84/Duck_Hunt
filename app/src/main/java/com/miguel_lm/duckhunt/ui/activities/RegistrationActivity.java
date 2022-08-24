package com.miguel_lm.duckhunt.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseUser;
import com.miguel_lm.duckhunt.app.dialogs.DialogToast;
import com.miguel_lm.duckhunt.app.SharedPreferences;
import com.miguel_lm.duckhunt.model.User;
import com.miguel_lm.duckhunt.R;
import com.miguel_lm.duckhunt.provider.AuthProvider;
import com.miguel_lm.duckhunt.provider.UserProvider;


public class RegistrationActivity extends AppCompatActivity {

    String name_reg, email_reg, password_reg;
    EditText ed_email_reg, ed_password_reg;
    public EditText ed_name_reg;
    Button btn_reg;
    public ProgressBar progressBar_reg;
    AuthProvider authProvider;
    UserProvider userProvider;
    View view;
    DialogToast dialogToast;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        init();
        eventButtonRegister();
    }

    //Method to map the components and create the necessary instances
    private void init() {

        ed_name_reg = findViewById(R.id.ed_name_reg);
        ed_email_reg = findViewById(R.id.ed_email_reg);
        ed_password_reg = findViewById(R.id.ed_password_reg);
        btn_reg = findViewById(R.id.btn_registrarse);
        progressBar_reg = findViewById(R.id.progressBar_registro);

        authProvider = new AuthProvider();
        userProvider = new UserProvider();
        dialogToast = DialogToast.getInstance();
        sharedPreferences = SharedPreferences.getInstance();
    }

    //Method for the event of the button to register a new user
    private void eventButtonRegister() {

        btn_reg.setOnClickListener(v -> {

            name_reg = ed_name_reg.getText().toString();
            email_reg = ed_email_reg.getText().toString().toLowerCase();
            password_reg = ed_password_reg.getText().toString();

            ManagerEmptyFieldsAndPerformRegistration(name_reg, email_reg, password_reg);

            view = this.getCurrentFocus();
            view.clearFocus();

            if(view != null){
                InputMethodManager input = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);
                input.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });
    }

    //Method to manage empty fields and register
    private void ManagerEmptyFieldsAndPerformRegistration(String name, String email, String pass) {

        if(name.isEmpty()){
            ed_name_reg.setError("El nick es obligatorio");

        } else if(email.isEmpty()){
            ed_email_reg.setError("El email es obligatorio");

        } else if(pass.isEmpty()){
            ed_password_reg.setError("La contraseña es obligatoria");

        } else {
            authProvider.register(email, pass, RegistrationActivity.this);
        }
    }

    //Method to update the screen and store the new user
    public void updateUI(FirebaseUser user) {

        if(user != null) {
            try {
                //Store user information in Firestore
                User newUser = new User(name_reg, 0, email_reg);
                userProvider.updateUser(user, newUser, this);

            } catch (Exception e) {
                String text = "No se ha podido encriptar la password";
                dialogToast.showDialogToast(this,RegistrationActivity.this, text, false, Toast.LENGTH_SHORT);
                Log.w("TAG", e.getMessage(), e);
                e.printStackTrace();
            }

        } else {
            ed_password_reg.setError("Nombre, email y/o password incorrectos.");
            ed_password_reg.requestFocus();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}