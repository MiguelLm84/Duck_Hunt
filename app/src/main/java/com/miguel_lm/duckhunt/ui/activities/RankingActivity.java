package com.miguel_lm.duckhunt.ui.activities;

import static com.miguel_lm.duckhunt.app.Constants.EXTRA_DIALOG_SELECT;
import static com.miguel_lm.duckhunt.app.Constants.EXTRA_EMAIL;
import static com.miguel_lm.duckhunt.app.Constants.EXTRA_ID;
import static com.miguel_lm.duckhunt.app.Constants.EXTRA_LOGIN_ACTIVITY;
import static com.miguel_lm.duckhunt.app.Constants.EXTRA_NICK;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.miguel_lm.duckhunt.R;
import com.miguel_lm.duckhunt.app.dialogs.DialogToast;
import com.miguel_lm.duckhunt.app.SharedPreferences;
import com.miguel_lm.duckhunt.databinding.ActivityRankingBinding;
import com.miguel_lm.duckhunt.ui.fragments.UserRankingFragment;


public class RankingActivity extends AppCompatActivity {

    AppBarConfiguration appBarConfiguration;
    ActivityRankingBinding binding;
    SharedPreferences sharedPreferences;
    Bundle extras;
    DialogToast dialogToast;
    boolean loginBack = false;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRankingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment_content_ranking, new UserRankingFragment()).commit();

        sharedPreferences = SharedPreferences.getInstance();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_ranking);
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    private void extraArgs(){

        extras = getIntent().getExtras();
        dialogToast = DialogToast.getInstance();

        if(extras != null) {
            loginBack = extras.getBoolean(EXTRA_LOGIN_ACTIVITY);

            if(loginBack){
                i = new Intent(RankingActivity.this, LoginActivity.class);
            } else {
                i = new Intent(RankingActivity.this, GameActivity.class);
            }

        } else {
            i = new Intent(RankingActivity.this, LoginActivity.class);
        }
        goToActivity(i);
    }

    private void goToActivity(Intent i) {

        i.putExtra(EXTRA_NICK, sharedPreferences.getValueNickPreference(RankingActivity.this));
        i.putExtra(EXTRA_ID, sharedPreferences.getValueIDPreference(RankingActivity.this));
        i.putExtra(EXTRA_EMAIL, sharedPreferences.getValueEmailPreference(RankingActivity.this));
        i.putExtra(EXTRA_DIALOG_SELECT, true);
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        extraArgs();
    }
}