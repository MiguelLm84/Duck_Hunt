package com.miguel_lm.duckhunt.ui.activities;

import static com.miguel_lm.duckhunt.app.Constants.EXTRA_DIALOG;
import static com.miguel_lm.duckhunt.app.Constants.EXTRA_DIALOG_SELECT;
import static com.miguel_lm.duckhunt.app.Constants.EXTRA_EMAIL;
import static com.miguel_lm.duckhunt.app.Constants.EXTRA_ID;
import static com.miguel_lm.duckhunt.app.Constants.EXTRA_NICK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.miguel_lm.duckhunt.R;
import com.miguel_lm.duckhunt.app.dialogs.Dialog;
import com.miguel_lm.duckhunt.app.dialogs.DialogToast;
import com.miguel_lm.duckhunt.app.SharedPreferences;
import com.miguel_lm.duckhunt.model.User;
import com.miguel_lm.duckhunt.provider.UserProvider;
import java.util.Random;


public class GameActivity extends AppCompatActivity {

    public TextView tv_counter, tv_nick, tv_timer, tv_value_1, tv_value_2, tv_value_3;
    ImageView iv_duck, iv_duck2, iv_duck3, iv_duck_black_gold, iv_duck_blue_gold, iv_duck_brown_gold;
    pl.droidsonroids.gif.GifImageView iv_dog;
    public int counter = 0;
    int widthScreen;
    int highScreen;
    Random random;
    boolean gameOver = false;
    String nick, id, email;
    //FirebaseFirestore db;
    ConstraintLayout constraint_ducks;
    Bundle extras;
    boolean dialogActive = false;
    DialogToast dialogToast;
    Dialog dialog;
    SharedPreferences sharedPreferences;
    UserProvider userProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dialog.dialogOptionSelect != null){
            if(dialog.dialogOptionSelect.isShowing()){
                dialog.dialogOptionSelect.dismiss();
            }
        }
    }

    private void init() {

        extras = getIntent().getExtras();
        if(extras != null){
            dialogActive = extras.getBoolean(EXTRA_DIALOG_SELECT);
            if(dialogActive){
                restartGame();
                getDialog();
            } else {
                initMethods();
            }
        } else {
            initMethods();
        }
    }

    private void initMethods(){

        initAndExtras();
        eventOnClickDuck();
        initScreen();
        moveDuck();
        initCountDown();
    }

    //Método para iniciar la cuenta atrás del tiempo de juego.
    private void initCountDown() {

        new CountDownTimer(60000, 1000) {
            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                long secondsLeft = millisUntilFinished / 1000;
                tv_timer.setText(secondsLeft + "s");
            }

            @SuppressLint("SetTextI18n")
            public void onFinish() {
                tv_timer.setText("0s");
                gameOver = true;
                if (!GameActivity.this.isFinishing()) {
                    dialog.showDialogGameOver(GameActivity.this, counter);
                }
                saveResultFirestore();
            }
        }.start();
    }

    //Método para guardar los resultados del usuario en la BD Firestore de Firebase.
    private void saveResultFirestore() {

        if(extras != null) {
            id = extras.getString(EXTRA_ID);
            if(id != null){
                //db.collection("users").document(id).update("ducks", counter);
                userProvider.updateCounter(id, counter);
            } else {
                checkIDSharedPreferences();
            }

        } else {
            checkIDSharedPreferences();
        }
    }

    private void checkIDSharedPreferences(){

        if(sharedPreferences.getValueIDPreference(this) != null){
            id = sharedPreferences.getValueIDPreference(this);
            userProvider.updateCounter(id, counter);
            //db.collection("users").document(id).update("ducks", counter);

        } else {
            String errorText = "El ID es null";
            dialogToast.showDialogToast(this, GameActivity.this, errorText, false);
        }
    }

    //Método para obtener el tipo de pantalla del dispositivo.
    private void initScreen() {

        // 1. Obtener el tamaño de la pantalla del dispositivo en el que se va a ejecutar la app.
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        widthScreen = size.x;
        highScreen = size.y;

        // 2. Inicializamos el objeto para generar números aleatorios.
        random = new Random();
    }

    private void initComponents(){

        tv_counter = findViewById(R.id.tv_counter);
        tv_nick = findViewById(R.id.tv_nick);
        tv_timer = findViewById(R.id.tv_timer);
        iv_duck = findViewById(R.id.iv_duck);
        iv_duck2 = findViewById(R.id.iv_duck2);
        iv_duck3 = findViewById(R.id.iv_duck3);
        iv_dog = findViewById(R.id.iv_dog);

        tv_value_1  = findViewById(R.id.tv_value_1);
        tv_value_2 = findViewById(R.id.tv_value_2);
        tv_value_3 = findViewById(R.id.tv_value_3);

        iv_duck_black_gold = findViewById(R.id.iv_duck_black_gold);
        iv_duck_blue_gold = findViewById(R.id.iv_duck_blue_gold);
        iv_duck_brown_gold = findViewById(R.id.iv_duck_brown_gold);

        constraint_ducks = findViewById(R.id.constraint_ducks);
    }

    //Método parav mapear los distintos componentes de la vista y recibir el nick a través del bundle.
    private void initAndExtras(){

        initComponents();

        iv_dog.setVisibility(View.INVISIBLE);

        dialogToast = DialogToast.getInstance();

        userProvider = new UserProvider();
        //db = FirebaseFirestore.getInstance();
        sharedPreferences = SharedPreferences.getInstance();
        argsExtras(tv_nick);
    }

    private void getDialog() {

        if(dialog.dialogOptionSelect != null){
            if(dialog.dialogOptionSelect.isShowing()){
                dialog.dialogOptionSelect.dismiss();
            }
        }
        dialog.showDialogOptionSelect(GameActivity.this);
    }

    //Método para recibir el nick del usuario a través del bundle
    @SuppressLint("SetTextI18n")
    private void argsExtras(TextView tv_nick){

        dialog = Dialog.getInstance();

        if(extras != null) {
            nick = extras.getString(EXTRA_NICK);
            id = extras.getString(EXTRA_ID);
            email = extras.getString(EXTRA_EMAIL);
            tv_nick.setText(nick);

            dialogActive = extras.getBoolean(EXTRA_DIALOG_SELECT);
            if(dialogActive){
                getDialog();
            }

        } else {
            tv_nick.setText("NULL NAME");
        }
    }

    @SuppressLint("SetTextI18n")
    private void getCounterDucks() {

        if(sharedPreferences.getValueEmailPreference(this) != null){
            String emailPlayer = sharedPreferences.getValueEmailPreference(this);
            userProvider.getEmailUser(null, emailPlayer, GameActivity.this);
            /*db.collection("users").whereEqualTo("email", emailPlayer).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                documentManager(document);
                            }
                        } else {
                            getCounterAndTimer(0);
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    });*/
        } else {
            getCounterAndTimer(0);
        }
    }

    public void documentManager(QueryDocumentSnapshot document) {

        if (document != null) {
            User userPlayer = document.toObject(User.class);
            counter = userPlayer.getDucks();
            getCounterAndTimer(counter);

        } else {
            getCounterAndTimer(0);
        }
    }

    @SuppressLint("SetTextI18n")
    public void getCounterAndTimer(int num){

        tv_counter.setText(String.valueOf(num));
        tv_timer.setText("60s");
        gameOver = false;
    }

    //Método para realizar acción en la imagen del pato cada vez que se pulse sobre este.
    @SuppressLint("UseCompatLoadingForDrawables")
    private void eventOnClickDuck() {

        duckMove(iv_duck);
        duckMove(iv_duck2);
        duckMove(iv_duck3);
    }

    private void duckMove(ImageView duck) {

        duck.setOnClickListener(v -> {
            if(!gameOver){
                selectOption(duck);
                tv_counter.setText(String.valueOf(counter));
                iv_duck_black_gold.setVisibility(View.VISIBLE);
                iv_dog.setVisibility(View.VISIBLE);
                new Handler().postDelayed(() -> {
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    if(duck == iv_duck){
                        tv_value_3.setVisibility(View.INVISIBLE);
                        duck.setImageResource(R.drawable.duck);
                    } else if(duck == iv_duck2) {
                        tv_value_2.setVisibility(View.INVISIBLE);
                        duck.setImageResource(R.drawable.duck_blue);
                    } else if(duck == iv_duck3) {
                        tv_value_1.setVisibility(View.INVISIBLE);
                        duck.setImageResource(R.drawable.duck_brown);
                    }
                    iv_duck_black_gold.setVisibility(View.INVISIBLE);
                    iv_dog.setVisibility(View.INVISIBLE);
                    moveDuck();
                }, 1000);
            }
        });
    }

    private void selectOption(ImageView duck) {

        if(duck == iv_duck){
            counter = counter+3;
            tv_value_3.setVisibility(View.VISIBLE);
            tv_value_2.setVisibility(View.INVISIBLE);
            tv_value_1.setVisibility(View.INVISIBLE);
            duck.setImageResource(R.drawable.duck_clicked);

        } else if(duck == iv_duck2) {
            counter = counter+2;
            tv_value_2.setVisibility(View.VISIBLE);
            tv_value_3.setVisibility(View.INVISIBLE);
            tv_value_1.setVisibility(View.INVISIBLE);
            duck.setImageResource(R.drawable.duck_blue_clicked);

        } else if(duck == iv_duck3) {
            counter++;
            tv_value_1.setVisibility(View.VISIBLE);
            tv_value_2.setVisibility(View.INVISIBLE);
            tv_value_3.setVisibility(View.INVISIBLE);
            duck.setImageResource(R.drawable.duck_brown_clicked);
        }
    }

    //Método que gestiona el movimiento aleatorio del pato.
    private void moveDuck() {

        //Utilizamos los números aleatorios para mover el pato a esa posición.
        iv_duck.setX(randomX(iv_duck));
        iv_duck.setY(randomY(iv_duck));

        iv_duck2.setX(randomX(iv_duck2));
        iv_duck2.setY(randomY(iv_duck2));

        iv_duck3.setX(randomX(iv_duck3));
        iv_duck3.setY(randomY(iv_duck3));
    }

    private int randomX(ImageView duck) {

        int min = 10;
        int maxX = 0;

        if(duck == iv_duck){
            maxX = constraint_ducks.getWidth() - iv_duck.getWidth();

        } else if(duck == iv_duck2) {
            maxX = constraint_ducks.getWidth() - iv_duck2.getWidth();

        } else if(duck == iv_duck3) {
            maxX = constraint_ducks.getWidth() - iv_duck3.getWidth();
        }

        return random.nextInt(((maxX - min) + 1) + min);
    }

    private int randomY(ImageView duck) {

        int min = 10;
        int maxY = 0;

        if(duck == iv_duck){
            maxY = constraint_ducks.getHeight() - iv_duck.getHeight();

        } else if(duck == iv_duck2) {
            maxY = constraint_ducks.getHeight() - iv_duck2.getHeight();

        } else if(duck == iv_duck3) {
            maxY = constraint_ducks.getHeight() - iv_duck3.getHeight();
        }

        return random.nextInt(((maxY - min) + 1) + min);
    }

    @SuppressLint("SetTextI18n")
    public void restartGame(){

        initAndExtras();
        getCounterDucks();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent i = new Intent(GameActivity.this, LoginActivity.class);
        i.putExtra(EXTRA_DIALOG, true);
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}