package com.miguel_lm.duckhunt.app.dialogs;

import static com.miguel_lm.duckhunt.app.Constants.EXTRA_DIALOG;
import static com.miguel_lm.duckhunt.app.Constants.EXTRA_DIALOG_SELECT;
import static com.miguel_lm.duckhunt.app.Constants.EXTRA_LOGIN_ACTIVITY;
import static com.miguel_lm.duckhunt.app.Constants.EXTRA_NICK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import com.miguel_lm.duckhunt.R;
import com.miguel_lm.duckhunt.app.SharedPreferences;
import com.miguel_lm.duckhunt.provider.AuthProvider;
import com.miguel_lm.duckhunt.ui.activities.GameActivity;
import com.miguel_lm.duckhunt.ui.activities.LoginActivity;
import com.miguel_lm.duckhunt.ui.activities.RankingActivity;



public class Dialog {

    TextView tvTitle, tvInfo, tvInfo2, tvText;
    Button btnOne, btnTwo;
    @SuppressLint("StaticFieldLeak")
    private static Dialog instance = null;
    public AlertDialog dialogOptionSelect, dialogLogin, dialog;
    DialogToast dialogToast = DialogToast.getInstance();
    SharedPreferences sharedPreferences = SharedPreferences.getInstance();
    AuthProvider authProvider = new AuthProvider();

    //Instance of the Dialog for the creation of AlertDialog custom.
    public static Dialog getInstance() {
        if(instance == null){
            instance = new Dialog();
        }
        return instance;
    }

    //Dialog to go to the ranking screen or to restart the game
    @SuppressLint("SetTextI18n")
    public void showDialogGameOver(Context context, int counter) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogLayout = LayoutInflater.from(context).inflate(R.layout.dialog_game_over, null);
        builder.setView(dialogLayout);
        dialog = builder.create();
        TextView tvPoints = dialogLayout.findViewById(R.id.tv_points);
        Button btnOne = dialogLayout.findViewById(R.id.btn_one);
        Button btnTwo = dialogLayout.findViewById(R.id.btn_two);

        tvPoints.setText("" + counter);

        btnOne.setOnClickListener(v -> restartActivity(context));
        btnTwo.setOnClickListener(v -> goToRankingActivity(context));

        dialog.setCanceledOnTouchOutside(false);

        if(dialog.isShowing()){
            dialog.dismiss();
        }
        dialog.show();
    }

    //Dialog to restart the game or return to the login screen
    @SuppressLint("SetTextI18n")
    public void showDialogOptionSelect(Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogLayout = LayoutInflater.from(context).inflate(R.layout.dialog_game_over, null);
        builder.setView(dialogLayout);
        dialogOptionSelect = builder.create();
        initComponents(dialogLayout);
        dataComponentsDialogOptionSelect();

        btnOne.setOnClickListener(v -> goToGameActivity(context));
        btnTwo.setOnClickListener(v -> goToLoginActivity(context));

        dialogOptionSelect.setCanceledOnTouchOutside(false);

        if(dialogOptionSelect.isShowing()){
            dialogOptionSelect.dismiss();
        }
        dialogOptionSelect.show();
    }

    //Method to restart game
    private void restartActivity(Context context) {

        Intent i = ((GameActivity)context).getIntent();
        i.putExtra(EXTRA_DIALOG_SELECT, false);
        context.startActivity(i);
        dialog.dismiss();
    }

    //Method to go to GameActivity
    private void goToGameActivity(Context context) {

        String nick = sharedPreferences.getValueNickPreference(context);
        Intent i = new Intent(context, GameActivity.class);
        i.putExtra(EXTRA_NICK, nick);
        i.putExtra(EXTRA_DIALOG_SELECT, false);
        context.startActivity(i);
        ((GameActivity)context).finish();
        ((GameActivity)context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        ((GameActivity)context).restartGame();
        dialogOptionSelect.dismiss();
    }

    //Method to go to RankingActivity
    private void goToRankingActivity(Context context) {

        dialog.dismiss();
        Intent i = new Intent(context, RankingActivity.class);
        i.putExtra(EXTRA_LOGIN_ACTIVITY, false);
        context.startActivity(i);
        ((Activity)context).finish();
        ((Activity)context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    //Method to go to LoginActivity
    private void goToLoginActivity(Context context) {

        Intent i = new Intent(context, LoginActivity.class);
        i.putExtra(EXTRA_DIALOG, true);
        context.startActivity(i);
        ((GameActivity)context).finish();
        ((GameActivity)context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    //Dialog to log out or continue in the app
    @SuppressLint("SetTextI18n")
    public void showDialogLogin(Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogLayout = LayoutInflater.from(context).inflate(R.layout.dialog_game_over, null);
        builder.setView(dialogLayout);
        dialogLogin = builder.create();
        initComponents(dialogLayout);
        dataComponentsDialogLogin();

        btnOne.setOnClickListener(v -> dialogLogin.dismiss());
        btnTwo.setOnClickListener(v -> goToActivity(context));

        dialogLogin.setCanceledOnTouchOutside(false);

        if(dialogLogin.isShowing()){
            dialogLogin.dismiss();
        }
        dialogLogin.show();
    }

    //Method to start the components
    private void initComponents(View dialogLayout) {

        tvTitle = dialogLayout.findViewById(R.id.tv_title_dialog);
        tvInfo = dialogLayout.findViewById(R.id.tv_info);
        tvInfo2 = dialogLayout.findViewById(R.id.tv_info2);
        tvText = dialogLayout.findViewById(R.id.tv_points);
        btnOne = dialogLayout.findViewById(R.id.btn_one);
        btnTwo = dialogLayout.findViewById(R.id.btn_two);
    }

    //Method to handle the components of the login dialog
    @SuppressLint("SetTextI18n")
    private void dataComponentsDialogLogin() {

        String text = "Pulsa 'CANCEL' para continuar en la app o 'EXIT' para cerrar sesión";
        tvTitle.setText("OPTION SELECT");
        tvText.setVisibility(View.GONE);
        tvInfo2.setVisibility(View.GONE);
        tvInfo.setText(text);
        tvInfo.setGravity(View.TEXT_ALIGNMENT_CENTER);
        tvInfo.setPadding(32, 0, 32, 0);
        btnOne.setText("Cancel");
        btnTwo.setText("Exit");
    }

    //Method to handle the components of the option selection dialog
    @SuppressLint("SetTextI18n")
    private void dataComponentsDialogOptionSelect(){

        String text = "Para seguir jugando pulsa 'RESTART', sino pulsa 'BACK' para regresar";
        tvTitle.setText("OPTION SELECT");
        tvText.setVisibility(View.GONE);
        tvInfo2.setVisibility(View.GONE);
        tvInfo.setText(text);
        tvInfo.setGravity(View.TEXT_ALIGNMENT_CENTER);
        tvInfo.setPadding(32, 0, 32, 0);
        btnTwo.setText("Back");
    }

    //Method to go to LoginActivity when logout
    private void goToActivity(Context context) {

        signOut(context);
        sharedPreferences.deleteValuesSharedPreferences(context);
        String errorText = "Sesión cerrada correctamente";
        dialogToast.showDialogToast(context, ((Activity)context), errorText, false, Toast.LENGTH_SHORT);
        Intent i = new Intent(context, LoginActivity.class);
        i.putExtra(EXTRA_DIALOG, false);
        context.startActivity(i);
        ((Activity)context).finish();
        ((Activity)context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    //Method to close player session.
    private void signOut(Context context) {

        authProvider.logout(context);
    }
}
