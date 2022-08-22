package com.miguel_lm.duckhunt.provider;

import static com.miguel_lm.duckhunt.app.Constants.EXTRA_EMAIL;
import static com.miguel_lm.duckhunt.app.Constants.EXTRA_ID;
import static com.miguel_lm.duckhunt.app.Constants.EXTRA_NICK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.miguel_lm.duckhunt.R;
import com.miguel_lm.duckhunt.app.dialogs.DialogToast;
import com.miguel_lm.duckhunt.app.SharedPreferences;
import com.miguel_lm.duckhunt.model.User;
import com.miguel_lm.duckhunt.ui.activities.GameActivity;
import com.miguel_lm.duckhunt.ui.activities.LoginActivity;
import com.miguel_lm.duckhunt.ui.activities.RegistrationActivity;
import java.util.HashMap;
import java.util.Map;


public class UserProvider {

    CollectionReference mCollection;
    String nickPlayer;
    SharedPreferences sharedPreferences;
    DialogToast dialogToast = DialogToast.getInstance();

    public UserProvider(){
        mCollection = FirebaseFirestore.getInstance().collection("users");
    }

    public void updateCounter(String id, int counter) {

        Map<String, Object> map = new HashMap<>();
        map.put("ducks",counter);

        mCollection.document(id).update(map);
    }

    public void getEmailUser(FirebaseUser user, String emailPlayer, Context context) {

        mCollection.whereEqualTo("email", user != null ? user.getEmail() : emailPlayer).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if(context instanceof LoginActivity){
                                documentManagerLogin(document, context);

                            } else if(context instanceof GameActivity){
                                ((GameActivity)context).documentManager(document);
                            }
                        }

                    } else {
                        if(context instanceof LoginActivity){
                            ((LoginActivity)context).ed_email.setHint(R.string.email);

                        } else if(context instanceof GameActivity){
                            ((GameActivity)context).getCounterAndTimer(0);
                        }
                        String text = "El usuario no existe";
                        dialogToast.showDialogToast(context, ((Activity)context), text, false);
                    }
                });
    }

    public void documentManagerLogin(QueryDocumentSnapshot document, Context context) {

        sharedPreferences = SharedPreferences.getInstance();
        String email = sharedPreferences.getValueEmailPreference(context);

        if(document != null){
            User userPlayer = document.toObject(User.class);
            String emailPlayer = userPlayer.getEmail();

            if(emailPlayer != null){
                ((LoginActivity)context).ed_email.setText(emailPlayer);
            } else if(email != null) {
                ((LoginActivity)context).ed_email.setText(email);
            } else {
                ((LoginActivity)context).ed_email.setHint(R.string.email);
            }

        } else {
            ((LoginActivity)context).ed_email.setHint(R.string.email);
        }
    }

    public void getNickUser(FirebaseUser user, Context context){

        mCollection.whereEqualTo("email", user.getEmail()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if(document != null){
                                User userPlayer = document.toObject(User.class);
                                nickPlayer = userPlayer.getNick();
                                String idPlayer = user.getUid();
                                String emailPlayer = user.getEmail();
                                ((LoginActivity)context).saveAndSendData(nickPlayer, idPlayer, emailPlayer);

                            } else {
                                nickPlayer = "NAME NULL";
                            }
                        }
                    } else {
                        String text = "El usuario no existe";
                        dialogToast.showDialogToast(context, ((LoginActivity)context), text, false);
                    }
                });
    }

    public void updateUser(FirebaseUser user, User newUser, Context context) {

        mCollection.document(user.getUid()).set(newUser).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                new Handler().postDelayed(() -> {
                    ((RegistrationActivity)context).progressBar_reg.setVisibility(View.VISIBLE);
                    ((RegistrationActivity)context).ed_name_reg.setText("");
                }, 5000);
                sharedPreferences.saveNick(context, newUser.getNick());
                sharedPreferences.saveID(context, user.getUid());
                sharedPreferences.saveEmail(context, newUser.getEmail());

                ((RegistrationActivity)context).progressBar_reg.setVisibility(View.INVISIBLE);
                ((RegistrationActivity)context).finish();
                Intent intent = new Intent(context, GameActivity.class);
                intent.putExtra(EXTRA_NICK, newUser.getNick());
                intent.putExtra(EXTRA_ID, user.getUid());
                intent.putExtra(EXTRA_EMAIL, newUser.getEmail());
                context.startActivity(intent);

            } else {
                String text = "No se ha podido registrar al " + newUser.getNick();
                dialogToast.showDialogToast(context,((RegistrationActivity)context), text, false);
            }
        });
    }
}
