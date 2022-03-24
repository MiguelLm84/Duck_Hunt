package com.miguel_lm.duckhunt.ui.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Explode;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.miguel_lm.duckhunt.R;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    ImageView iv_titulo;
    TextView tv_version;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
        animacionLogoSplash();
        animacionTextoVersionSplashIn();
        splashTime();
    }

    //Método para mapear los distintos componentes de la vista.
    public void init(){
        iv_titulo = findViewById(R.id.iv_titulo);
        tv_version = findViewById(R.id.tv_version);
    }

    //Método mostrar splash durante un tiempo determinado y después ir a la pantalla de login.
    private void splashTime(){
        new Handler().postDelayed(() -> startActivity(new Intent(SplashActivity.this, LoginActivity.class)), 4000);

        Explode explode = new Explode();
        explode.setDuration(1500);
        getWindow().setExitTransition(explode);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    //Método para animación del logo del juego.
    public void animacionLogoSplash(){
        Animation animacion1 = AnimationUtils.loadAnimation(this,R.anim.desplazamiento_arriba);
        iv_titulo.setAnimation(animacion1);
    }

    //Método para la animación del TextView de la versión.
    @RequiresApi(api = Build.VERSION_CODES.P)
    public void animacionTextoVersionSplashIn() {
        Animation animacion2 = AnimationUtils.loadAnimation(this, R.anim.fade_in_2);
        tv_version.setAnimation(animacion2);
    }
}