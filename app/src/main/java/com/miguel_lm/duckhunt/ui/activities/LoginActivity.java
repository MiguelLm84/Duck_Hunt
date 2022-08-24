package com.miguel_lm.duckhunt.ui.activities;

import static com.miguel_lm.duckhunt.app.Constants.EXTRA_DIALOG;
import static com.miguel_lm.duckhunt.app.Constants.EXTRA_DIALOG_SELECT;
import static com.miguel_lm.duckhunt.app.Constants.EXTRA_EMAIL;
import static com.miguel_lm.duckhunt.app.Constants.EXTRA_ID;
import static com.miguel_lm.duckhunt.app.Constants.EXTRA_LOGIN_ACTIVITY;
import static com.miguel_lm.duckhunt.app.Constants.EXTRA_NICK;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.miguel_lm.duckhunt.R;
import com.miguel_lm.duckhunt.app.dialogs.Dialog;
import com.miguel_lm.duckhunt.app.dialogs.DialogToast;
import com.miguel_lm.duckhunt.app.SharedPreferences;
import com.miguel_lm.duckhunt.provider.AuthProvider;
import com.miguel_lm.duckhunt.provider.UserProvider;


public class LoginActivity extends AppCompatActivity {

    Button btn_start, btn_reg, btn_ranking;
    String email, password, nickPlayer, idPlayer, emailPlayer;
    public EditText ed_email;
    EditText ed_password;
    AuthProvider authProvider;
    private long timeToExit = 0;
    FirebaseUser currentUser;
    Bundle extras, extrasDialog;
    DialogToast dialogToast;
    View view;
    Dialog dialog;
    boolean dialogActive = false;
    SharedPreferences sharedPreferences;
    UserProvider userProvider;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dialog.dialogLogin != null){
            if(dialog.dialogLogin.isShowing()){
                dialog.dialogLogin.dismiss();
            }
        }
    }

    //Method to start and manage the components
    // and methods of the activity and manage the display of the dialog.
    private void init() {

        extras = getIntent().getExtras();
        dialog = Dialog.getInstance();
        userProvider = new UserProvider();

        if(extras != null){
            dialogActive = extras.getBoolean(EXTRA_DIALOG);
            if(dialogActive){
                if(currentUser != null){
                    userProvider.getEmailUser(currentUser, null, LoginActivity.this);
                    dialog.showDialogLogin(LoginActivity.this);
                }
            }
        }
        initMethods(LoginActivity.this);
        userProvider.getEmailUser(currentUser, null, LoginActivity.this);
    }

    //Method to start all the functions of the activity
    private void initMethods(Context context){

        initAndInstances();
        extrasData(context);
        eventButtonReg();
        eventBtnStart();
        eventButtonRanking();
    }

    //Method to manage the data received by the 'extras' bundle
    private void extrasData(Context context){

        if(dialog.dialogLogin != null){
            if(dialog.dialogLogin.isShowing()){
                dialog.dialogLogin.dismiss();
            }
        }
        argsDialog(context);
    }

    //Method to map the components of the view and generate instances
    private void initAndInstances() {

        ed_email = findViewById(R.id.ed_email);
        ed_password = findViewById(R.id.ed_password);
        btn_start = findViewById(R.id.btn_iniciar);
        btn_reg = findViewById(R.id.btn_reg);
        btn_ranking = findViewById(R.id.btn_ranking);

        authProvider = new AuthProvider();
        extrasDialog = getIntent().getExtras();
        dialogToast = DialogToast.getInstance();
        dialog = Dialog.getInstance();
        sharedPreferences = SharedPreferences.getInstance();
    }

    //Method to show dialog and show the email if there is a user logged in in the email field
    private void argsDialog(Context context) {

        currentUser = authProvider.getUserSession();
        extrasDialog = getIntent().getExtras();

        if(dialog.dialogLogin != null){
            if(dialog.dialogLogin.isShowing()){
                dialog.dialogLogin.dismiss();
            }
        }

        if(extrasDialog != null) {
            dialogActive = extrasDialog.getBoolean(EXTRA_DIALOG);
            if(dialogActive){
                if(currentUser != null){
                    userProvider.getEmailUser(currentUser, null, LoginActivity.this);
                    dialog.showDialogLogin(context);
                }
            }
        }
    }

    //Method for handling the 'Ranking' button event
    private void eventButtonRanking() {

        btn_ranking.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, RankingActivity.class);
            i.putExtra(EXTRA_LOGIN_ACTIVITY, true);
            startActivity(i);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }

    //Method for handling the 'Registration' button event
    private void eventButtonReg() {

        btn_reg.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(i);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }

    //Method to store the data in the SharedPreferences and send it to GameActivity
    public void saveAndSendData(String nick, String id, String email) {

        sharedPreferences.deleteValuesSharedPreferences(LoginActivity.this);
        sharedPreferences.saveNick(LoginActivity.this, nick);
        sharedPreferences.saveID(LoginActivity.this, id);
        sharedPreferences.saveEmail(LoginActivity.this, email);

        Intent i = new Intent(LoginActivity.this, GameActivity.class);
        i.putExtra(EXTRA_NICK, nick);
        i.putExtra(EXTRA_ID, id);
        i.putExtra(EXTRA_EMAIL, email);
        i.putExtra(EXTRA_DIALOG_SELECT, false);
        startActivity(i);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    //Method to log in the user
    private void loginUser() {

        email = ed_email.getText().toString().toLowerCase();
        password = ed_password.getText().toString();

        authProvider.login(email, password, LoginActivity.this);
    }

    //Method to update the screen and show the received data if any
    public void updateUI(FirebaseUser user) {

        if(user != null){
            userProvider.getNickUser(user, LoginActivity.this);

        } else {
            extras = getIntent().getExtras();
            if(extras != null) {
                nickPlayer = extras.getString(EXTRA_NICK);
                idPlayer = extras.getString(EXTRA_ID);
                emailPlayer = extras.getString(EXTRA_EMAIL);
                saveAndSendData(nickPlayer, idPlayer, emailPlayer);

            } else {
                String errorText = "El usuario no existe o no está registrado";
                dialogToast.showDialogToast(this, LoginActivity.this, errorText, false, Toast.LENGTH_SHORT);
            }
        }
    }

    //Method for handling the 'Start' button event
    private void eventBtnStart(){

        btn_start.setOnClickListener(v -> {
            email = ed_email.getText().toString().toLowerCase();
            password = ed_password.getText().toString();

            if(email.isEmpty()){
                ed_email.setError("El email es obligatorio");

            } else if(email.length() < 3){
                ed_email.setError("Debe tener al menos 3 caracteres");

            } else if(password.isEmpty()){
                ed_password.setError("La password es obligatoria");

            } else {
                loginUser();
            }

            view = this.getCurrentFocus();

            if(view != null){
                view.clearFocus();
                InputMethodManager input = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);
                input.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });
    }

    @Override
    public void onBackPressed(){
        long time = System.currentTimeMillis();
        if (time - timeToExit > 3000) {
            timeToExit = time;
            String text = "Presionar de nuevo para salir";
            dialogToast.showDialogToast(this, LoginActivity.this, text, false, Toast.LENGTH_SHORT);

        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }
}