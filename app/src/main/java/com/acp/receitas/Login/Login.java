package com.acp.receitas.Login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.acp.receitas.Logic.Conexao;
import com.acp.receitas.Logic.Global;
import com.acp.receitas.MainActivity;
import com.acp.receitas.Model.Users;
import com.acp.receitas.R;
import com.acp.receitas.Utils.MaskEditUtil;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity {

    private static final String TAG = "Login";
    private Boolean conectado;
    private FirebaseFirestore fireDB;
    private CollectionReference colref;
    private FirebaseAuth auth;
    private String mnome, memail, msenha, mimagem;
    private ImageView imgLogin;
    private TextInputLayout txtInputNome, txtInputEmail, txtInputSenha;
    private TextInputEditText edtNome, edtEmail, edtSenha;
    private TextView txtTime, txtEsqueci;
    private Button btnLogin;
    private ProgressBar mProgressCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fireDB = FirebaseFirestore.getInstance();
        colref = fireDB.collection("Users");

        imgLogin = findViewById(R.id.imgLogin);
        mProgressCircle = findViewById(R.id.progress1);
        mProgressCircle.setVisibility(View.GONE);
        txtInputNome = findViewById(R.id.txtInputNome);
        edtNome = findViewById(R.id.edtNomeLogin);
        txtInputEmail = findViewById(R.id.txtInputEmail);
        edtEmail = findViewById(R.id.edtEmailLogin);
        txtInputSenha = findViewById(R.id.txtInputSenha);
        edtSenha = findViewById(R.id.edtSenhaLogin);
        btnLogin = findViewById(R.id.btnLogin);
        txtTime = findViewById(R.id.time);
        txtEsqueci = findViewById(R.id.txtEsqueciLogin);

        Calendar c1 = Calendar.getInstance();
        int hora = c1.get(Calendar.HOUR_OF_DAY);

        if(hora > 0 && hora < 12){
            txtTime.setText("Bom Dia");
        }else if(hora > 12 && hora < 18){
            txtTime.setText("Boa Tarde");
        }else{
            txtTime.setText("Boa Noite");
        }

        limparCampos();

        edtEmail.setEnabled(false);
        edtSenha.setEnabled(false);
        txtEsqueci.setEnabled(false);
        btnLogin.setEnabled(false);

        conectado = isConnected(this);

        edtNome.requestFocus();
        openKeyboard(edtNome);

        edtNome.addTextChangedListener(new Validar(edtNome));
        edtEmail.addTextChangedListener(new Validar(edtEmail));
        edtSenha.addTextChangedListener(new Validar(edtSenha));

        txtEsqueci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memail = edtEmail.getText().toString().trim();
                if ( memail.isEmpty()){
                    Toast.makeText(Login.this, "Digite seu email", Toast.LENGTH_SHORT).show();
                } else {
                    recuperSenha();
                }
            }
        });

        edtNome.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                    mnome = edtNome.getText().toString().trim();
                    edtSenha.setEnabled(true);
                    mostraDados(edtNome.getText().toString());
                }
                return false;
            }
        });
        edtNome.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mnome = edtNome.getText().toString().trim();
                if(!hasFocus) {
                    edtSenha.setEnabled(true);
                    //mostraDados(edtNome.getText().toString());
                } else {
                    limparCampos();
                }
            }
        });
        edtSenha.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                    memail = edtEmail.getText().toString().trim();
                    msenha = edtSenha.getText().toString().trim();
                    login(memail, msenha);
                }
                return false;
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mnome = edtNome.getText().toString().trim();
                memail = edtEmail.getText().toString().trim();
                msenha = edtSenha.getText().toString().trim();
                if( mnome.isEmpty()) {
                    Toast.makeText(Login.this, "Digite seu nome.", Toast.LENGTH_SHORT).show();
                    edtNome.requestFocus();
                } else {
                    if( memail.isEmpty()) {
                        Toast.makeText(Login.this, "Cadastre seu email.", Toast.LENGTH_SHORT).show();
                    } else {
                        if( msenha.isEmpty()) {
                            Toast.makeText(Login.this, "Digite sua senha.", Toast.LENGTH_SHORT).show();
                            edtSenha.requestFocus();
                        } else {
                            mProgressCircle.setVisibility(View.VISIBLE);
                            Global.nom_user = mnome;
                            Global.ema_user = memail;
                            login(memail, msenha);
                        }
                    }
                }
            }
        });
    }

    private void limparCampos() {
        edtNome.setText("");
        edtEmail.setText("");
        edtSenha.setText("");
    }

    public void mostraDados(String nome){
        if (!nome.equals("") || nome != null || !nome.isEmpty()) {
            mProgressCircle.setVisibility(View.VISIBLE);
            if (conectado) {
                colref.whereEqualTo("nome", nome).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot querySnapshot) {
                                if( !querySnapshot.isEmpty()){
                                    for(QueryDocumentSnapshot doc : querySnapshot){
                                        Users users = doc.toObject(Users.class);
                                        edtEmail.setText(users.getEmail());
                                        btnLogin.setEnabled(true);
                                        txtEsqueci.setEnabled(true);
                                        imgLogin.setVisibility(View.VISIBLE);
                                        mimagem = users.getImagem();
                                        Glide.with(Login.this)
                                                .load(mimagem)
                                                .into(imgLogin);
                                        edtSenha.requestFocus();
                                    }
                                } else {
                                    showCustomDialog(Login.this, "Usuário não encontrado! Registre novo usuário.", "Cadastrar");
                                }
                                mProgressCircle.setVisibility(View.INVISIBLE);
                            }
                        });
            } else {
                showCustomDialog(Login.this, "Por favor connect na Internet para se logar!", "Conectar");
            }
        }
    }

    private Boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo activeConn = connectivityManager.getActiveNetworkInfo();
        if( activeConn != null ){
            if( activeConn.getType() == ConnectivityManager.TYPE_WIFI) {
                //Toast.makeText(context, "Wifi habilitada!", Toast.LENGTH_SHORT).show();
                return true;
            }
            if (activeConn.getType() == ConnectivityManager.TYPE_MOBILE) {
                //Toast.makeText(context, "Rede Celular habilitada!", Toast.LENGTH_SHORT).show();
                return true;
            }
        } else {
            Toast.makeText(context, "Sem conexão de Internet!", Toast.LENGTH_LONG).show();
            return  false;
        }
        return null;
    }

    private void showCustomDialog(Context context, String mensagem, String btnPos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(mensagem)
                .setCancelable(false)
                .setPositiveButton(btnPos, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        if(btnPos == "Conectar"){
                            context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        } else if (btnPos == "Cadastrar"){
                            Intent i1 = new Intent(Login.this, Cadastro.class);
                            i1.putExtra("nome", mnome);
                            startActivity(i1);
                            finish();
                        }

                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Conexao.logout();
                        closeApplication();
                    }
                });
        AlertDialog alerta = builder.create();
        alerta.show();
    }

    private void recuperSenha(){
        auth.sendPasswordResetEmail(edtEmail.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Login.this, "Enviamos uma MSG para seu email, com um link " +
                        "para redefinir a sua senha.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login.this, "Endereço de email inválido.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void login(String email, String senha) {
        auth.signInWithEmailAndPassword(email, senha)
        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if( task.isSuccessful()){
                    mProgressCircle.setVisibility(View.INVISIBLE);
                    Global.ema_user = memail;
                    Global.nom_user = mnome;
                    Global.img_user = mimagem;
                    Intent i = new Intent(Login.this, MainActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(Login.this, "e-mail ou senha errados.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();
        FirebaseUser currentUser = auth.getCurrentUser();
    }

    private class Validar implements TextWatcher {
        private View view;
        private Validar(View view) {
            this.view = view;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            switch (view.getId()){
                case R.id.edtNomeLogin:
                    openKeyboard(edtNome);
                    break;
                case R.id.edtEmailLogin:
                    //txtEsqueci.setEnabled(true);
                    break;
                case R.id.edtSenhaLogin:
                    //openKeyboard(edtSenha);
                    break;
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (view.getId()){
                case R.id.edtEmailLogin:
                    //txtEsqueci.setEnabled(true);
                    break;
                case R.id.edtSenhaLogin:
                    //btnLogin.setEnabled(true);
                    break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_ENTER:
                closeKeyboard();
                return true;
                case KeyEvent.ACTION_DOWN:
                    login(edtEmail.getText().toString(), edtSenha.getText().toString());
                    return true;

        }
        return super.onKeyDown(keyCode, event);
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void openKeyboard(View view) {
        view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInputFromInputMethod(view.getWindowToken(), 1);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void closeApplication() {
        System.out.println("closeApplication ");
        finish();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}