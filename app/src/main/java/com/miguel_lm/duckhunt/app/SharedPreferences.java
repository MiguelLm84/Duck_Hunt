package com.miguel_lm.duckhunt.app;

import static android.content.Context.MODE_PRIVATE;
import static com.miguel_lm.duckhunt.app.Constants.EXTRA_EMAIL;
import static com.miguel_lm.duckhunt.app.Constants.EXTRA_ID_PLAYER;
import static com.miguel_lm.duckhunt.app.Constants.EXTRA_NICKNAME;
import static com.miguel_lm.duckhunt.app.Constants.PREFERENCES_DATA;

import android.content.Context;



public class SharedPreferences {

    private static SharedPreferences instance = null;

    //Instance for creating cache to store the player's nickname and ID.
    public static SharedPreferences getInstance() {
        if(instance == null){
            instance = new SharedPreferences();
        }
        return instance;
    }

    //Method to save the nickname of the registered player in the Shered Preferences.
    public void saveNick(Context context, String nickname) {

        android.content.SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_DATA, MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EXTRA_NICKNAME, nickname);
        editor.apply();
    }

    //Method to save the email of the registered player in the Shered Preferences.
    public void saveEmail(Context context, String email) {

        android.content.SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_DATA, MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EXTRA_EMAIL, email);
        editor.apply();
    }

    //Method to save the ID of the registered player in the Shered Preferences.
    public void saveID(Context context, String id) {

        android.content.SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_DATA, MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EXTRA_ID_PLAYER, id);
        editor.apply();
    }

    //Method to display the email stored in the Shared Preferences.
    public String getValueEmailPreference(Context context) {

        android.content.SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_DATA, MODE_PRIVATE);

        return preferences.getString(EXTRA_EMAIL, "NO EMAIL");
    }

   //Method to display the nickname stored in the Shared Preferences.
    public String getValueNickPreference(Context context) {

        android.content.SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_DATA, MODE_PRIVATE);

        return preferences.getString(EXTRA_NICKNAME, "NO NAME");
    }

    //Method to display the ID stored in the Shared Preferences.
    public String getValueIDPreference(Context context) {

        android.content.SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_DATA, MODE_PRIVATE);

        return preferences.getString(EXTRA_ID_PLAYER, "ID NULL");
    }

    //Method to remove all data stored in Shared Preferences.
    public void deleteValuesSharedPreferences(Context context) {

        android.content.SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_DATA, MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
