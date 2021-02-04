package com.acp.receitas.Login;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.acp.receitas.Logic.Conexao;
import com.acp.receitas.MainActivity;
import com.acp.receitas.R;
import com.acp.receitas.Utils.MaskEditUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import de.hdodenhof.circleimageview.CircleImageView;
import static android.util.Patterns.EMAIL_ADDRESS;
import static android.util.Patterns.PHONE;

public class Cadastro extends AppCompatActivity {
    private static final String KEY_ID = "id" ;
    public static final String KEY_NOME = "nome";
    public static final String KEY_PHONE = "fone";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_SENHA = "senha";
    public static final String KEY_DATA = "data";
    public static final String KEY_IMAGEM = "imagem";
    private static final String TAG = "Cadastro";
    private final int TIRAR_FOTO = 1;
    private final int GALERIA_IMAGENS = 2;
    private final int PERMISSAO_REQUEST = 3;
    private final int CAMERA = 4;
    private Uri selectedImage;
    private CircleImageView imgCadastro;
    private TextInputLayout txtInputName, txtInputFone, txtInputEmail, txtInputSenha, txtInputConfirma;
    private TextInputEditText edtName, edtFone, edtEmail, edtSenha, edtConfirma;
    private Button btnRegistrar, btnCancelar;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore fireDB;
    private Bitmap imagemGaleria;
    private String uid, profileImageUrl, nome, fone, email;
    String getImagem;
    private ProgressBar mProgressCircle;

    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();

        edtName.setEnabled(false);
        edtFone.requestFocus();
        edtFone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                openKeyboard(edtFone);
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        Intent intent = getIntent();
        nome = intent.getStringExtra("nome");

        mProgressCircle = findViewById(R.id.progress1);
        mProgressCircle.setVisibility(View.GONE);
        fireDB = FirebaseFirestore.getInstance();
        imgCadastro = findViewById(R.id.imgCadastro);
        txtInputName = findViewById(R.id.txtInputName);
        edtName = findViewById(R.id.edtName);
        txtInputFone = findViewById(R.id.txtInputPhone);
        edtFone = findViewById(R.id.edtPhone);
        txtInputEmail = findViewById(R.id.txtInputEmail);
        edtEmail = findViewById(R.id.edtEmailCadastro);
        txtInputSenha = findViewById(R.id.txtInputSenha);
        edtSenha = findViewById(R.id.edtSenhaCadastro);
        txtInputConfirma = findViewById(R.id.txtInputConfirma);
        edtConfirma = findViewById(R.id.edtConfirmaSenhaCadastro);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnCancelar = findViewById(R.id.btnCancelar);

        edtName.addTextChangedListener(new ValidationTextWatcher(edtName));
        edtFone.addTextChangedListener(MaskEditUtil.mask(edtFone, MaskEditUtil.FORMAT_FONE));
        edtFone.addTextChangedListener(new ValidationTextWatcher(edtFone));
        edtEmail.addTextChangedListener(new ValidationTextWatcher(edtEmail));
        edtSenha.addTextChangedListener(new ValidationTextWatcher(edtSenha));
        edtConfirma.addTextChangedListener(new ValidationTextWatcher(edtConfirma));

        edtName.setText(nome);

        imgCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verificaPermissao()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Cadastro.this);
                    builder.setTitle("AVISO");
                    builder.setMessage("De onde virá a imagem?");
                    builder.setPositiveButton("Nova Imagem", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                startActivityForResult(takePictureIntent, TIRAR_FOTO);
                            }
                        }
                    });
                    builder.setNegativeButton("Buscar na galeria", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore
                                    .Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, GALERIA_IMAGENS);
                        }
                    });
                    AlertDialog alerta = builder.create();
                    alerta.show();
                }
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nome = edtName.getText().toString().trim();
                fone = edtFone.getText().toString().trim();
                email = edtEmail.getText().toString().trim();
                String senha = edtSenha.getText().toString().trim();
                String csenha = edtConfirma.getText().toString().trim();
                if (!validateName(nome)){
                    return;
                } else {
                    if (!validateFone(fone)) {
                        return;
                    } else {
                        if (!validateEmail(email)) {
                            return;
                        } else {
                            Boolean isValid = EMAIL_ADDRESS.matcher(email).matches();
                            if (!isValid) {
                                Toast.makeText(Cadastro.this, "Invalid Email address, ex: abc@example.com", Toast.LENGTH_SHORT).show();
                                edtEmail.requestFocus();
                                return;
                            } else {
                                if (!validatePassword(senha)) {
                                    if(senha.length() < 6) {
                                        Toast.makeText(Cadastro.this, "Senha tem que ter no mínimo 6 digitos", Toast.LENGTH_SHORT).show();
                                        edtSenha.requestFocus();
                                        return;
                                    }
                                } else {
                                    if (!validateConfirmPassword(csenha, senha)) {
                                        return;
                                    } else {
                                        mProgressCircle.setVisibility(View.VISIBLE);
                                        criarUser(nome, email, senha);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });

    }

    private String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void criarUser( String nome, String email, String senha){
        auth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(Cadastro.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if( task.isSuccessful()){
                            Toast.makeText(Cadastro.this, "Usuário cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                            imageToFirebaseStorage();
                        } else {
                            Toast.makeText(Cadastro.this, "Erro de cadastro", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        mProgressCircle.setVisibility(View.INVISIBLE);
    }

    private void imageToFirebaseStorage() {
        uid = auth.getCurrentUser().getUid();
        StorageReference profileImageRef = FirebaseStorage.getInstance().getReference()
        .child("profileImages").child(uid + ".jpg");
        if(selectedImage != null ){
            profileImageRef.putFile(selectedImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            getDownloadUrl(profileImageRef);
                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    getImagem = uri.toString();
                                    String data = getDate();
                                    String fone = edtFone.getText().toString();
                                    Map<String, Object> dados = new HashMap<>();
                                    dados.put(KEY_ID, uid);
                                    dados.put(KEY_NOME, nome);
                                    dados.put(KEY_PHONE, fone);
                                    dados.put(KEY_EMAIL, email);
                                    dados.put(KEY_DATA, data);
                                    dados.put(KEY_IMAGEM, getImagem);
                                    fireDB.collection("Users").document(uid).set(dados);
                                    user = Conexao.getFirebaseUser();
                                    if(user!= null && profileImageUrl != null){
                                        UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(nome).setPhotoUri(Uri.parse(profileImageUrl)).build();
                                        user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if( task.isSuccessful()){
                                                    Toast.makeText(Cadastro.this, "Usuário cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                                                    mProgressCircle.setVisibility(View.INVISIBLE);
                                                    Intent i = new Intent(Cadastro.this, MainActivity.class);
                                                    i.putExtra("email", email);
                                                    i.putExtra("nome", nome);
                                                    startActivity(i);
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Cadastro.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Uri origen = getUriToResource(this, R.drawable.avatar);
            profileImageRef.putFile(origen)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            getDownloadUrl(profileImageRef);
                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    getImagem= uri.toString();
                                    String data = getDate();
                                    String fone = edtFone.getText().toString();
                                    Map<String, Object> dados = new HashMap<>();
                                    dados.put(KEY_ID, uid);
                                    dados.put(KEY_NOME, nome);
                                    dados.put(KEY_PHONE, fone);
                                    dados.put(KEY_EMAIL, email);
                                    dados.put(KEY_DATA, data);
                                    dados.put(KEY_IMAGEM, getImagem);
                                    fireDB.collection("Users").document(uid).set(dados);
                                    user = Conexao.getFirebaseUser();
                                    if(user!= null && profileImageUrl != null){
                                        UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(nome).setPhotoUri(Uri.parse(profileImageUrl)).build();
                                        user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if( task.isSuccessful()){
                                                    Toast.makeText(Cadastro.this, "Usuário cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                                                    mProgressCircle.setVisibility(View.INVISIBLE);
                                                    Intent i = new Intent(Cadastro.this, MainActivity.class);
                                                    i.putExtra("email", email);
                                                    i.putExtra("nome", nome);
                                                    startActivity(i);
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Cadastro.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void getDownloadUrl(StorageReference profileImageRef) {
        profileImageRef.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d(TAG, "onSuccess: "+ uri);
                        setUserProfileUrl(uri);
                    }
                });
    }

    private void setUserProfileUrl(Uri uri) {
        FirebaseUser user = auth.getCurrentUser();
            UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(uri)
                    .build();
            user.updateProfile(request)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                             Intent i = new Intent(Cadastro.this, MainActivity.class);
                            i.putExtra("email", email);
                            i.putExtra("nome", nome);
                            startActivity(i);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Cadastro.this, "Profile Image failed...", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private boolean validateName(String nome){
        if( nome.isEmpty() ){
            txtInputName.setError("Campo Requerido!");
            edtName.requestFocus();
            return false;
        } else {
            txtInputName.isErrorEnabled();
        }
        return true;
    }

    private boolean validateFone(String fone) {
        if (fone.isEmpty()) {
            txtInputFone.setError("Campo Requerido!");
            edtFone.requestFocus();
            return false;
        } else if(!TextUtils.isEmpty(fone)){
            return Patterns.PHONE.matcher(fone).matches();
        } else {
            txtInputFone.setError("Telefone inválido!");
            edtFone.requestFocus();
            return false;
        }
    }

    private boolean validateEmail(String email) {
        if (email.isEmpty()) {
            txtInputEmail.setError("Campo Requerido!");
            edtEmail.requestFocus();
            return false;
        } else if(!TextUtils.isEmpty(email)) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        } else {
            txtInputEmail.setError("Email inválido!");
            edtEmail.requestFocus();
            return false;
        }
    }

    private boolean validatePassword(String senha) {
        if (senha.isEmpty()) {
            txtInputSenha.setError("Campo Requerido!");
            edtSenha.requestFocus();
            return false;
        } else if(senha.length() < 6) {
            txtInputSenha.setError("Senha tem que ter no mínimo 6 digitos");
            edtSenha.requestFocus();
            return false;
        } else {
            txtInputSenha.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateConfirmPassword(String csenha, String senha) {
        if (csenha.isEmpty()) {
            txtInputConfirma.setError("Password is required");
            edtConfirma.requestFocus();
            return false;
        } else {
            if(csenha.contentEquals(senha)){
                return true;
            } else {
                txtInputConfirma.setError("Senha diferente!");
                Toast.makeText(Cadastro.this, "Senhas tem que ser iguais.", Toast.LENGTH_SHORT).show();
                edtConfirma.requestFocus();
                return false;
            }
        }
    }

    private class ValidationTextWatcher implements TextWatcher {

        private View view;
        String nome = edtName.getText().toString().trim();
        String fone = edtFone.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String senha = edtSenha.getText().toString().trim();
        String csenha = edtConfirma.getText().toString().trim();

        private ValidationTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        public void onTextChanged(CharSequence charSequence, int start, int i1, int i2) {

        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.edtName:
                    validateName(nome);
                    break;
                case R.id.edtPhone:
                    validateFone(fone);
                    break;
                case R.id.edtEmailCadastro:
                    String.valueOf(editable).toLowerCase();
                    validateEmail(email);
                    break;
                case R.id.edtSenhaCadastro:
                    validatePassword(senha);
                    break;
                case R.id.edtConfirmaSenhaCadastro:
                    validateConfirmPassword(csenha, senha);
                    break;

            }
        }
    }

    private boolean verificaPermissao() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA);
        } else {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            PERMISSAO_REQUEST);
                }
            }
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSAO_REQUEST);
                }
            }
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (requestCode == TIRAR_FOTO ) {
            switch ( resultCode ){
                case RESULT_OK:
                    selectedImage = data.getData();
                    Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                    if(imageBitmap != null){
                        exibirImagem(getResizedBitmap(imageBitmap));
                    }
            }
            salvarFoto();
        }

        if (requestCode == GALERIA_IMAGENS && resultCode == RESULT_OK) {
            selectedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            String picturePath = c.getString(columnIndex);
            c.close();
            imagemGaleria = (BitmapFactory.decodeFile(picturePath));
            exibirImagem(getResizedBitmap(imagemGaleria));
        }
    }

    private void exibirImagem(Bitmap image) {
        int targetW = image.getWidth();
        int targetH = image.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        imgCadastro.setImageBitmap(image);
    }

    private void salvarFoto() {

        OutputStream fOut = null;
        Uri outputFileUri;

        imgCadastro.buildDrawingCache();
        Bitmap bm = imgCadastro.getDrawingCache();
        try{
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imagem  = timeStamp + ".jpg";
            File pasta = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    + File.separator + "JPG" + File.separator);
            pasta.mkdirs();
            File sdImagem = new File(pasta, imagem);
            outputFileUri = Uri.fromFile(sdImagem);
            fOut = new FileOutputStream(sdImagem);
        } catch (Exception e){
            Toast.makeText(Cadastro.this, "Ocorreu um erro.", Toast.LENGTH_SHORT).show();
        }
        try {
            bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e){
            Toast.makeText(Cadastro.this, "Não salvou.", Toast.LENGTH_SHORT).show();
        }

    }

    public static final Uri getUriToResource(@NonNull Context context, @AnyRes int resId) throws Resources.NotFoundException {

        Resources res = context.getResources();

        Uri resUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + res.getResourcePackageName(resId)
                + '/' + res.getResourceTypeName(resId)
                + '/' + res.getResourceEntryName(resId));

        return resUri;
    }

    public Bitmap resizeImage (Bitmap bitmap) {
        Bitmap resized = Bitmap. createScaledBitmap ( bitmap , 80 , 80 , true ) ;
        return resized;
    }

    public Bitmap getResizedBitmap(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float rate = 0.0f;

        int maxResolution = 300;

        int newWidth = width;
        int newHeight = height;

        if(width > height) {
            if(maxResolution < width) {
                rate = maxResolution/ (float) width;
                newHeight = (int) (height * rate);
                newWidth = maxResolution;
            }

        } else {
            if(maxResolution < height) {
                rate = maxResolution/(float)height;
                newWidth = (int) (width * rate);
                newHeight = maxResolution;
            }
        }

        Bitmap resizedImage = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
        return resizedImage;
    }

    private void openKeyboard(View view) {
        view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInputFromInputMethod(view.getWindowToken(), 1);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }
}
