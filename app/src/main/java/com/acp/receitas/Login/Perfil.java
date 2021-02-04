package com.acp.receitas.Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.acp.receitas.Logic.Conexao;
import com.acp.receitas.Logic.Global;
import com.acp.receitas.MainActivity;
import com.acp.receitas.Model.Receitas;
import com.acp.receitas.Model.Users;
import com.acp.receitas.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import de.hdodenhof.circleimageview.CircleImageView;

public class Perfil extends AppCompatActivity {

    private static final String TAG = "Perfil";
    private CircleImageView imgPerfil;
    private TextView txtNome, txtEmail;
    private Button btnLogout, btnVolta;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore fireDB = FirebaseFirestore.getInstance();
    private CollectionReference colRef = fireDB.collection("Users");
    private String imagem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        imgPerfil = findViewById(R.id.imgPerfil);
        txtNome = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        btnLogout = findViewById(R.id.btnLogout);
        btnVolta = findViewById(R.id.btnVolta);
        btnVolta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Perfil.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Conexao.logout();
                closeApplication(); // Close Application method called
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();
        user = Conexao.getFirebaseUser();

        txtNome.setText(Global.nom_user);
        txtEmail.setText(Global.ema_user);
        imagem = Global.img_user;
        verificaUser();
    }

    private void verificaUser() {
        if( user == null ){
            finish();
        } else {
            if(user.getPhotoUrl() != null){
                Glide.with(this)
                        .load(imagem)
                        .into(imgPerfil);
            }
        }
    }

    private void closeApplication() {
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        finish();
        moveTaskToBack(true);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
