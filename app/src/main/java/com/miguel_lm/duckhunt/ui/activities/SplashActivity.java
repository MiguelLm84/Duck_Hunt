package com.miguel_lm.duckhunt.ui.activities;

import static com.miguel_lm.duckhunt.app.Constants.EXTRA_DIALOG;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Explode;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseUser;
import com.miguel_lm.duckhunt.R;
import com.miguel_lm.duckhunt.provider.AuthProvider;


@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    ImageView iv_title, iv_dog_jump, iv_dog_jump2, iv_dog_stand_up;
    pl.droidsonroids.gif.GifImageView iv_dog, iv_dog_run;
    TextView tv_version;
    //FirebaseAuth firebaseAuth;
    AuthProvider authProvider;
    FirebaseUser currentUser;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        init();
        animLogoSplash();
        animTextVersionSplashIn();
        initSplash();
        splashTime();
    }

    //Método para mapear los distintos componentes de la vista.
    public void init(){

        iv_title = findViewById(R.id.iv_titulo);
        tv_version = findViewById(R.id.tv_version);

        iv_dog = findViewById(R.id.iv_dog2);
        iv_dog_run = findViewById(R.id.iv_dog_run2);
        iv_dog_jump = findViewById(R.id.iv_dog_jump4);
        iv_dog_jump2 = findViewById(R.id.iv_dog_jump3);
        iv_dog_stand_up = findViewById(R.id.iv_dog_stand_up2);

        iv_dog.setVisibility(View.INVISIBLE);
        iv_dog_run.setVisibility(View.VISIBLE);
        iv_dog_run.setImageResource(R.drawable.dog_run);
    }

    public void initSplash() {

        iv_dog_stand_up.setVisibility(View.INVISIBLE);
        iv_dog_jump.setVisibility(View.INVISIBLE);
        iv_dog_jump2.setVisibility(View.INVISIBLE);

        new Handler().postDelayed(() ->  {
            overridePendingTransition(R.anim.fade_in_2, R.anim.fade_out_2);
            iv_dog_run.setVisibility(View.VISIBLE);
            iv_dog_run.setImageResource(R.drawable.dog_run);
            overridePendingTransition(R.anim.fade_in_2, R.anim.fade_out_2);
        }, 500);

        new Handler().postDelayed(() -> {
            overridePendingTransition(R.anim.fade_in_2, R.anim.fade_out_2);
            iv_dog_run.setVisibility(View.INVISIBLE);
            iv_dog_stand_up.setVisibility(View.VISIBLE);
            overridePendingTransition(R.anim.fade_in_2, R.anim.fade_out_2);
        }, 4500);

        new Handler().postDelayed(() -> {
            overridePendingTransition(R.anim.fade_in_2, R.anim.fade_out_2);
            iv_dog_stand_up.setVisibility(View.INVISIBLE);
            iv_dog_run.setVisibility(View.INVISIBLE);
            iv_dog_jump.setVisibility(View.VISIBLE);
            iv_dog_jump.setImageResource(R.drawable.dog_7);
            overridePendingTransition(R.anim.fade_in_2, R.anim.fade_out_2);
        }, 4900);

        new Handler().postDelayed(() -> {
            overridePendingTransition(R.anim.fade_in_2, R.anim.fade_out_2);
            iv_dog_jump.setVisibility(View.INVISIBLE);
            iv_dog_jump2.setVisibility(View.VISIBLE);
            iv_dog_jump2.setImageResource(R.drawable.dog_8);
            overridePendingTransition(R.anim.fade_in_2, R.anim.fade_out_2);
        }, 5200);
        iv_dog_run.setVisibility(View.INVISIBLE);
        iv_dog_jump2.setVisibility(View.INVISIBLE);

        new Handler().postDelayed(() -> {
            overridePendingTransition(R.anim.fade_in_2, R.anim.fade_out_2);
            iv_dog.setVisibility(View.VISIBLE);
            iv_dog_run.setVisibility(View.INVISIBLE);
            iv_dog_stand_up.setVisibility(View.INVISIBLE);
            iv_dog_jump.setVisibility(View.INVISIBLE);
            iv_dog_jump2.setVisibility(View.INVISIBLE);
            overridePendingTransition(R.anim.fade_in_2, R.anim.fade_out_2);
        }, 5400);

        new Handler().postDelayed(() -> {
            overridePendingTransition(R.anim.fade_in_2, R.anim.fade_out_2);
            iv_dog.setVisibility(View.INVISIBLE);
            overridePendingTransition(R.anim.fade_in_2, R.anim.fade_out_2);
        }, 7000);

        new Handler().postDelayed(() -> {
            overridePendingTransition(R.anim.fade_in_2, R.anim.fade_out_2);
            overridePendingTransition(R.anim.fade_in_2, R.anim.fade_out_2);
        }, 8300);

        new Handler().postDelayed(() -> {
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            iv_dog_stand_up.setVisibility(View.INVISIBLE);
            iv_dog_jump.setVisibility(View.INVISIBLE);
            iv_dog_jump2.setVisibility(View.INVISIBLE);
        }, 9000);
    }

    private boolean getSessionActive(){

        //firebaseAuth = FirebaseAuth.getInstance();
        authProvider = new AuthProvider();
        //currentUser = firebaseAuth.getCurrentUser();
        currentUser = authProvider.getUserSession();
        boolean sessionActive = false;

        if(currentUser != null) {
            sessionActive = true;
        }
        return sessionActive;
    }

    //Método mostrar splash durante un tiempo determinado y después ir a la pantalla de login.
    private void splashTime(){

        new Handler().postDelayed(() -> {
            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
            i.putExtra(EXTRA_DIALOG, getSessionActive());
            startActivity(i);
        }, 8000);

        Explode explode = new Explode();
        explode.setDuration(1500);
        getWindow().setExitTransition(explode);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    //Método para animación del logo del juego.
    public void animLogoSplash(){

        Animation anim1 = AnimationUtils.loadAnimation(this,R.anim.desplazamiento_arriba);
        iv_title.setAnimation(anim1);
    }

    //Método para la animación del TextView de la versión.
    @RequiresApi(api = Build.VERSION_CODES.P)
    public void animTextVersionSplashIn() {

        Animation anim2 = AnimationUtils.loadAnimation(this, R.anim.fade_in_2);
        tv_version.setAnimation(anim2);
    }
}