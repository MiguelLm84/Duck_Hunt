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
    //FirebaseAuth firebaseAuth;
    AuthProvider authProvider;
    //FirebaseFirestore db;
    UserProvider userProvider;
    View view;
    DialogToast dialogToast;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        init();
        events();
    }

    private void init() {

        ed_name_reg = findViewById(R.id.ed_name_reg);
        ed_email_reg = findViewById(R.id.ed_email_reg);
        ed_password_reg = findViewById(R.id.ed_password_reg);
        btn_reg = findViewById(R.id.btn_registrarse);
        progressBar_reg = findViewById(R.id.progressBar_registro);

        //firebaseAuth = FirebaseAuth.getInstance();
        authProvider = new AuthProvider();
        //db = FirebaseFirestore.getInstance();
        userProvider = new UserProvider();
        dialogToast = DialogToast.getInstance();
        sharedPreferences = SharedPreferences.getInstance();
    }

    private void events() {

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

    private void ManagerEmptyFieldsAndPerformRegistration(String name, String email, String pass) {

        if(name.isEmpty()){
            ed_name_reg.setError("El nick es obligatorio");

        } else if(email.isEmpty()){
            ed_email_reg.setError("El email es obligatorio");

        } else if(pass.isEmpty()){
            ed_password_reg.setError("La contraseña es obligatoria");

        } else {
            createUser(email, pass);
        }
    }

    private void createUser(String email_reg, String pass_reg) {

        authProvider.register(email_reg, pass_reg, RegistrationActivity.this);

        /*firebaseAuth.createUserWithEmailAndPassword(email_reg, pass_reg)
                .addOnCompleteListener(this, task -> {

                    if(task.isSuccessful()){
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        updateUI(user);

                    } else {
                        Log.w("TAG", "createUserError", task.getException());
                        updateUI(null);
                    }
                });*/
    }

    public void updateUI(FirebaseUser user) {

        if(user != null) {
            //Almacenar la información del usuario en Firestore.
            try {
                User newUser = new User(name_reg, 0, email_reg);

                userProvider.updateUser(user, newUser, RegistrationActivity.this);

                /*db.collection("users").document(user.getUid()).set(newUser).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        new Handler().postDelayed(() -> {
                            progressBar_reg.setVisibility(View.VISIBLE);
                            ed_name_reg.setText("");
                        }, 5000);

                        sharedPreferences.saveNick(this, name_reg);
                        sharedPreferences.saveID(this, user.getUid());
                        sharedPreferences.saveEmail(this, email_reg);

                        progressBar_reg.setVisibility(View.INVISIBLE);
                        finish();
                        Intent intent = new Intent(RegistrationActivity.this, GameActivity.class);
                        intent.putExtra(EXTRA_NICK, name_reg);
                        intent.putExtra(EXTRA_ID, user.getUid());
                        intent.putExtra(EXTRA_EMAIL, email_reg);
                        startActivity(intent);

                    } else {
                        String text = "No se ha podido registrar al " + name_reg;
                        dialogToast.showDialogToast(this,RegistrationActivity.this, text, false);
                    }
                });*/

            } catch (Exception e) {
                String text = "No se ha podido encriptar la password";
                dialogToast.showDialogToast(this,RegistrationActivity.this, text, false);
                Log.w("TAG", e.getMessage(), e);
                e.printStackTrace();
            }

        } else {
            ed_password_reg.setError("Nombre, email y/o password incorrectos.");
            ed_password_reg.requestFocus();
        }
            /*db.collection("users").add(newUser)
                    .addOnSuccessListener(documentReference -> {

                        new Handler().postDelayed(() -> {
                            progressBar_reg.setVisibility(View.VISIBLE);
                            ed_name_reg.setText("");
                        }, 5000);

                        progressBar_reg.setVisibility(View.INVISIBLE);
                        finish();
                        Intent intent = new Intent(RegistrationActivity.this, GameActivity.class);
                        intent.putExtra(EXTRA_NICK, name_reg);
                        intent.putExtra(EXTRA_ID, documentReference.getId());
                        intent.putExtra(EXTRA_EMAIL, email_reg);
                        startActivity(intent);
                    });
        }*/
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