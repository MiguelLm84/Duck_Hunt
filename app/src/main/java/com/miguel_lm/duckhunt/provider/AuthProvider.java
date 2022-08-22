package com.miguel_lm.duckhunt.provider;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.miguel_lm.duckhunt.ui.activities.LoginActivity;
import com.miguel_lm.duckhunt.ui.activities.RegistrationActivity;



public class AuthProvider {

    private final FirebaseAuth mAuth;

    public AuthProvider(){
        mAuth = FirebaseAuth.getInstance();
    }

    public void register(String email, String password, Context context){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(((RegistrationActivity) context), task -> {

                    if (task.isSuccessful()) {
                        FirebaseUser user = getUserSession();
                        ((RegistrationActivity) context).updateUI(user);

                    } else {
                        Log.w("TAG", "createUserError", task.getException());
                        ((RegistrationActivity) context).updateUI(null);
                    }
                });
    }

    public void login(String email, String password, Context context){

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(((LoginActivity) context), task -> {

                    if (task.isSuccessful()) {
                        FirebaseUser user = getUserSession();
                        ((LoginActivity) context).updateUI(user);

                    } else {
                        Log.w("TAG", "loginError", task.getException());
                        ((LoginActivity) context).updateUI(null);
                    }
                });
    }

    public String getUid(){

        if(mAuth.getCurrentUser() != null){
            return mAuth.getCurrentUser().getUid();

        } else {
            return null;
        }
    }

    public String getEmailUser(){

        if(mAuth.getCurrentUser() != null){
            return mAuth.getCurrentUser().getEmail();

        } else {
            return null;
        }
    }

    public void logout(Context context){

        if(mAuth != null){
            mAuth.signOut();
        } else {
            Toast.makeText(context, "No se ha podido cerrar la sesión", Toast.LENGTH_SHORT).show();
        }
    }

    public FirebaseUser getUserSession(){

        if(mAuth.getCurrentUser() != null){
            return mAuth.getCurrentUser();

        } else {
            return null;
        }
    }

}
