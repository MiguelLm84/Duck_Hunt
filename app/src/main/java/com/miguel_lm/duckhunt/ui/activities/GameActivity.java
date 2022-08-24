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
import android.widget.Toast;

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
    ImageView iv_duck, iv_duck2, iv_duck3, iv_duck4, iv_duck5, iv_duck6, iv_duck_black_gold, iv_duck_blue_gold, iv_duck_brown_gold;
    pl.droidsonroids.gif.GifImageView iv_dog;
    public int counter = 0;
    int widthScreen;
    int highScreen;
    Random random;
    boolean gameOver = false;
    String nick, id, email;
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

    //Method to start and manage the components
    // and methods of the activity and manage the display of the dialog.
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

    //Method to start all the functions of the activity
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
                //extraTime(secondsLeft);
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

    //Method where an extra time is applied when a number of hunted ducks is exceeded
    //coinciding with the level change
    /*@SuppressLint("SetTextI18n")
    private void extraTime(long secondsLeft) {

        tv_timer.setText(secondsLeft + "s");
        if(counter == 200){
            tv_timer.setText(secondsLeft + 10 + "s");
        } else if(counter == 400) {
            tv_timer.setText(secondsLeft + 10 + "s");
        } else if(counter == 600) {
            tv_timer.setText(secondsLeft + 10 + "s");
        } else if(counter == 800) {
            tv_timer.setText(secondsLeft + 10 + "s");
        } else if(counter == 1000) {
            tv_timer.setText(secondsLeft + 10 + "s");
        }
    }*/

    //Method to save user results to the Firebase Firestore DB
    private void saveResultFirestore() {

        if(extras != null) {
            id = extras.getString(EXTRA_ID);
            if(id != null){
                userProvider.updateCounter(id, counter);
            } else {
                checkIDSharedPreferences();
            }

        } else {
            checkIDSharedPreferences();
        }
    }

    //Method to verify ID SharedPreferences
    private void checkIDSharedPreferences(){

        if(sharedPreferences.getValueIDPreference(this) != null){
            id = sharedPreferences.getValueIDPreference(this);
            userProvider.updateCounter(id, counter);

        } else {
            String errorText = "El ID es null";
            dialogToast.showDialogToast(this, GameActivity.this, errorText, false, Toast.LENGTH_SHORT);
        }
    }

    //Method to get the screen type of the device
    private void initScreen() {

        //1. Get the screen size of the device on which the app is to run
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        widthScreen = size.x;
        highScreen = size.y;

        // 2. We initialize the object to generate random numbers
        random = new Random();
    }

    //Method to map the components
    private void initComponents(){

        tv_counter = findViewById(R.id.tv_counter);
        tv_nick = findViewById(R.id.tv_nick);
        tv_timer = findViewById(R.id.tv_timer);
        iv_duck = findViewById(R.id.iv_duck);
        iv_duck2 = findViewById(R.id.iv_duck2);
        iv_duck3 = findViewById(R.id.iv_duck3);

        iv_duck4 = findViewById(R.id.iv_duck4);
        iv_duck5 = findViewById(R.id.iv_duck5);
        iv_duck6 = findViewById(R.id.iv_duck6);
        iv_dog = findViewById(R.id.iv_dog);

        tv_value_1  = findViewById(R.id.tv_value_1);
        tv_value_2 = findViewById(R.id.tv_value_2);
        tv_value_3 = findViewById(R.id.tv_value_3);

        iv_duck_black_gold = findViewById(R.id.iv_duck_black_gold);
        iv_duck_blue_gold = findViewById(R.id.iv_duck_blue_gold);
        iv_duck_brown_gold = findViewById(R.id.iv_duck_brown_gold);

        constraint_ducks = findViewById(R.id.constraint_ducks);
    }

    //Method to map the different components of the view and receive the nickname through the bundle
    private void initAndExtras(){

        initComponents();
        iv_dog.setVisibility(View.INVISIBLE);
        dialogToast = DialogToast.getInstance();
        userProvider = new UserProvider();
        sharedPreferences = SharedPreferences.getInstance();
        argsExtras(tv_nick);
    }

    //Method to display the dialog
    private void getDialog() {

        if(dialog.dialogOptionSelect != null){
            if(dialog.dialogOptionSelect.isShowing()){
                dialog.dialogOptionSelect.dismiss();
            }
        }
        dialog.showDialogOptionSelect(GameActivity.this);
    }

    //Method to receive the user's nickname through the bundle
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

    //Method to manage the hunted ducks counter
    @SuppressLint("SetTextI18n")
    private void getCounterDucks() {

        if(sharedPreferences.getValueEmailPreference(this) != null){
            String emailPlayer = sharedPreferences.getValueEmailPreference(this);
            userProvider.getEmailUser(null, emailPlayer, GameActivity.this);

        } else {
            getCounterAndTimer(0);
        }
    }

    //Method to retrieve the counter from the database
    public void documentManager(QueryDocumentSnapshot document) {

        if (document != null) {
            User userPlayer = document.toObject(User.class);
            counter = userPlayer.getDucks();
            getCounterAndTimer(counter);

        } else {
            getCounterAndTimer(0);
        }
    }

    //Method to show the hunted duck counter and set the timer to not started
    @SuppressLint("SetTextI18n")
    public void getCounterAndTimer(int num){

        tv_counter.setText(String.valueOf(num));
        tv_timer.setText("60s");
        gameOver = false;
    }

    //Method to perform action on the image of the duck every time you click on it
    @SuppressLint("UseCompatLoadingForDrawables")
    private void eventOnClickDuck() {

        duckMove(iv_duck);
        duckMove(iv_duck2);
        duckMove(iv_duck3);
    }

    //Method to manage ducks
    private void duckMove(ImageView duck) {

        duck.setOnClickListener(v -> {
            if(!gameOver){
                selectOption(duck);
                tv_counter.setText(String.valueOf(counter));
                levelChange(counter);
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
                    } else if(duck == iv_duck4) {
                        tv_value_3.setVisibility(View.INVISIBLE);
                        duck.setImageResource(R.drawable.duck);
                    } else if(duck == iv_duck5) {
                        tv_value_2.setVisibility(View.INVISIBLE);
                        duck.setImageResource(R.drawable.duck_blue);
                    } else if(duck == iv_duck6) {
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

    //Method to manage the movement of ducks and when they are hunted
    private void selectOption(ImageView duck) {

        if(duck == iv_duck){
            counter = counter+3;
            visibilityDucks(duck, View.VISIBLE, View.INVISIBLE, View.INVISIBLE, R.drawable.duck_clicked);

        } else if(duck == iv_duck2) {
            counter = counter+2;
            visibilityDucks(duck, View.INVISIBLE, View.VISIBLE, View.INVISIBLE, R.drawable.duck_blue_clicked);

        } else if(duck == iv_duck3) {
            counter++;
            visibilityDucks(duck, View.INVISIBLE, View.INVISIBLE, View.VISIBLE, R.drawable.duck_brown_clicked);

        } else if(duck == iv_duck4){
            counter = counter+3;
            visibilityDucks(duck, View.VISIBLE, View.INVISIBLE, View.INVISIBLE, R.drawable.duck_clicked);

        } else if(duck == iv_duck5) {
            counter = counter+2;
            visibilityDucks(duck, View.INVISIBLE, View.VISIBLE, View.INVISIBLE, R.drawable.duck_blue_clicked);

        } else if(duck == iv_duck6) {
            counter++;
            visibilityDucks(duck, View.INVISIBLE, View.INVISIBLE, View.VISIBLE, R.drawable.duck_brown_clicked);
        }
    }

    //Method to show or hide the ducks
    private void visibilityDucks(ImageView duck, int visibility3, int visibility2, int visibility1, int duck_clicked) {

        tv_value_3.setVisibility(visibility3);
        tv_value_2.setVisibility(visibility2);
        tv_value_1.setVisibility(visibility1);
        duck.setImageResource(duck_clicked);
    }

    //Method for level change upon reaching a certain figure
    private void levelChange(int count) {

        switch (count) {
            case 200:
                newLevel(iv_duck4, 2, count);
                break;

            case 400:
                newLevel(iv_duck5, 3, count);
                break;

            case 600:
                newLevel(iv_duck6, 4, count);
                break;

            case 800:
                newLevel(iv_duck5, 5, count);
                newLevel(iv_duck6, 0, 0);
                break;

            case 1000:
                newLevel(iv_duck4, 6, count);
                newLevel(iv_duck5, 0, 0);
                newLevel(iv_duck6, 0, 0);
                break;
        }
    }

    //Method to increase the number of ducks when reaching a new level
    // and show toast with the level change
    private void newLevel(ImageView duck, int num, int count) {

        tv_counter.setText(String.valueOf(count + 100));
        counter = count + 100;
        duck.setVisibility(View.VISIBLE);
        duckMove(duck);
        if(num != 0){
            String errorText = "Nivel completado " + num;
            dialogToast.showDialogToast(this, GameActivity.this, errorText, false, Toast.LENGTH_LONG);
        }
    }

    //Method that manages the random movement of the duck
    private void moveDuck() {

        //Random numbers are used to move the duck to that position
        iv_duck.setX(randomX(iv_duck));
        iv_duck.setY(randomY(iv_duck));

        iv_duck2.setX(randomX(iv_duck2));
        iv_duck2.setY(randomY(iv_duck2));

        iv_duck3.setX(randomX(iv_duck3));
        iv_duck3.setY(randomY(iv_duck3));

        iv_duck4.setX(randomX(iv_duck4));
        iv_duck4.setY(randomY(iv_duck4));

        iv_duck5.setX(randomX(iv_duck5));
        iv_duck5.setY(randomY(iv_duck5));

        iv_duck6.setX(randomX(iv_duck6));
        iv_duck6.setY(randomY(iv_duck6));
    }

    //Method to move the ducks on the X axis of the screen
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
        if(duck == iv_duck4){
            maxX = constraint_ducks.getWidth() - iv_duck4.getWidth();

        } else if(duck == iv_duck5) {
            maxX = constraint_ducks.getWidth() - iv_duck5.getWidth();

        } else if(duck == iv_duck6) {
            maxX = constraint_ducks.getWidth() - iv_duck6.getWidth();
        }

        return random.nextInt(((maxX - min) + 1) + min);
    }

    //Method to move the ducks on the Y axis of the screen
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
        if(duck == iv_duck4){
            maxY = constraint_ducks.getHeight() - iv_duck4.getHeight();

        } else if(duck == iv_duck5) {
            maxY = constraint_ducks.getHeight() - iv_duck5.getHeight();

        } else if(duck == iv_duck6) {
            maxY = constraint_ducks.getHeight() - iv_duck6.getHeight();
        }

        return random.nextInt(((maxY - min) + 1) + min);
    }

    //Method to stop the timer and show the counter reached in the game
    // until one of the buttons in the dialog is pressed
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