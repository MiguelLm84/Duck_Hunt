package com.miguel_lm.duckhunt.ui.activities;

import static com.miguel_lm.duckhunt.common.constants.EXTRA_ID;
import static com.miguel_lm.duckhunt.common.constants.EXTRA_NICK;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.miguel_lm.duckhunt.R;
import com.miguel_lm.duckhunt.model.User;

public class LoginActivity extends AppCompatActivity {

    EditText ed_nick;
    Button btn_iniciar;
    String nick;
    User nuevoUsuario;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        db = FirebaseFirestore.getInstance();

        init();
        onClickBtnIniciar();
    }

    //Método para mapear los componentes de la vista.
    private void init(){

        ed_nick = findViewById(R.id.ed_nombre);
        btn_iniciar = findViewById(R.id.btn_iniciar);
    }

    //Método onClick para botón de iniciar.
    private void onClickBtnIniciar(){

        btn_iniciar.setOnClickListener(v -> {
            nick = ed_nick.getText().toString();
            if(nick.isEmpty()){
                ed_nick.setError("El nombre de ususario es obligatorio");

            } else if(nick.length() < 3){
                ed_nick.setError("Debe tener al menos 3 caracteres");

            } else {
               addNickAndStart();
            }
        });
    }

    //Método para añadir nick a la pantalla del juego y comenzar partida.
    private void addNickAndStart() {
        db.collection("users").whereEqualTo("nick", nick)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if(queryDocumentSnapshots.size() > 0){
                        ed_nick.setError("El nick no está disponible.");
                    } else {
                        addNickToFirestore();
                    }
                });
    }

    //Método para añadir el nick en la BD de Firestore en Firebase.
    private void addNickToFirestore() {
        nuevoUsuario = new User(nick, 0);
        db.collection("users").add(nuevoUsuario)
        .addOnSuccessListener(documentReference -> {
            ed_nick.setText("");
            Intent intent = new Intent(LoginActivity.this, GameActivity.class);
            intent.putExtra(EXTRA_NICK, nick);
            intent.putExtra(EXTRA_ID, documentReference.getId());
            startActivity(intent);
        });
    }
}