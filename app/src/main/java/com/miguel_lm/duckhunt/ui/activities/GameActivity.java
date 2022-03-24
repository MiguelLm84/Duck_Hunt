package com.miguel_lm.duckhunt.ui.activities;

import static com.miguel_lm.duckhunt.common.constants.EXTRA_ID;
import static com.miguel_lm.duckhunt.common.constants.EXTRA_NICK;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Display;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.miguel_lm.duckhunt.R;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    TextView tv_counter, tv_nick, tv_timer;
    ImageView iv_duck;
    int counter = 0;
    int anchoPantalla;
    int altoPantalla;
    Random aleatorio;
    boolean gameOver = false;
    String nick, id;
    FirebaseFirestore db;
    private long tiempoParaSalir = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        db = FirebaseFirestore.getInstance();

        initAndExtras();
        eventoOnClickDuck();
        initPantalla();
        moveDuck();
        initCuentaAtras();
    }

    //Método para iniciar la cuenta atrás del tiempo de juego.
    private void initCuentaAtras() {
        new CountDownTimer(60000, 1000) {

            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                long segundosRestantes = millisUntilFinished / 1000;
                tv_timer.setText(segundosRestantes + "s");
            }

            @SuppressLint("SetTextI18n")
            public void onFinish() {
                tv_timer.setText("0s");
                gameOver = true;
                mostrarDialogoGameOver();
                seveResutFirestore();
            }
        }.start();
    }

    //Método para guardar los resultados del usuario en la BD Firestore de Firebase.
    private void seveResutFirestore() {
        db.collection("users").document(id).update("ducks", counter);
    }

    private void mostrarDialogoGameOver() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Has conseguido cazar " + counter + " patos!!").setTitle("GAME OVER");

        builder.setPositiveButton("Reiniciar", (dialog, id) -> {
            counter = 0;
            tv_counter.setText("0");
            gameOver = false;
            initCuentaAtras();
            moveDuck();
        });
        builder.setNegativeButton("Ranking", (dialog, id) -> {
            dialog.dismiss();
            Intent i = new Intent(GameActivity.this, RankingActivity.class);
            startActivity(i);
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //Método para obtener el tipo de pantalla del dispositivo.
    private void initPantalla() {
        // 1. Obtener el tamaño de la pantalla del dispositivo en el que se va a ejecutar la app.
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        anchoPantalla = size.x;
        altoPantalla = size.y;

        // 2. Inicializamos el objeto para generar números aleatorios.
        aleatorio = new Random();

    }

    //Método parav mapear los distintos componentes de la vista y recibir el nick a través del bundle.
    private void initAndExtras(){
        tv_counter = findViewById(R.id.tv_counter);
        tv_nick = findViewById(R.id.tv_nick);
        tv_timer = findViewById(R.id.tv_timer);
        iv_duck = findViewById(R.id.iv_duck);

        argsExtras(tv_nick);
    }

    //Método para recibir el nick del usuario a través del bundle
    private void argsExtras(TextView tv_nick){
        Bundle extras = getIntent().getExtras();
        nick = extras.getString(EXTRA_NICK);
        id = extras.getString(EXTRA_ID);
        tv_nick.setText(nick);
    }

    //Método para realizar acción en la imagen del pato cada vez que se pulse sobre este.
    @SuppressLint("UseCompatLoadingForDrawables")
    private void eventoOnClickDuck() {
        iv_duck.setOnClickListener(v -> {
            if(!gameOver){
                counter++;
                tv_counter.setText(String.valueOf(counter));
                iv_duck.setImageResource(R.drawable.duck_clicked);
                new Handler().postDelayed(() -> {
                    iv_duck.setImageResource(R.drawable.duck);
                    moveDuck();
                }, 500);
            }
        });
    }

    //Método que gestiona el movimiento aleatorio del pato.
    private void moveDuck() {
        int min = 20;
        int maximoX = anchoPantalla - iv_duck.getWidth();
        int maximoY = altoPantalla - iv_duck.getHeight();

        //Generamos 2 números aleatorios, uno para la coordenada X y otra para la coordenada Y..
        int randomX = aleatorio.nextInt(((maximoX - min) + 1) + min);
        int randomY = aleatorio.nextInt(((maximoY - min) + 1) + min);

        //Utilizamos los números aleatorios oara mover el pato a esa posición.
        iv_duck.setX(randomX);
        iv_duck.setY(randomY);
    }

    @Override
    public void onBackPressed(){
        long tiempo = System.currentTimeMillis();
        if (tiempo - tiempoParaSalir > 3000) {
            tiempoParaSalir = tiempo;
            Toast.makeText(this, "Presione de nuevo 'Atrás' si desea salir",
                    Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }
}