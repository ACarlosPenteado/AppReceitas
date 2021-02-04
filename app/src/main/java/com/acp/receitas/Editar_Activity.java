package com.acp.receitas;

import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.acp.receitas.Fragmentos.Frag_Ingre_dialog;
import com.acp.receitas.Fragmentos.Frag_Prepa_dialog;
import com.acp.receitas.Logic.Global;
import com.acp.receitas.Model.IncLisIngItem;
import com.acp.receitas.Model.IncLisPreItem;
import com.acp.receitas.Logic.ListarIngredientes;
import com.acp.receitas.Logic.ListarPreparo;
import com.acp.receitas.Logic.MostraListaPreparo;
import com.acp.receitas.Logic.MostraListaIngre;
import com.acp.receitas.Model.Receitas;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class Editar_Activity extends AppCompatActivity
        implements Frag_Ingre_dialog.OnIngreListener,
                    Frag_Prepa_dialog.OnPrepaListener {

    private static final String TAG = "Editar Receitas";
    private final int TIRAR_FOTO = 1;
    private final int GALERIA_IMAGENS = 2;
    private final int PERMISSAO_REQUEST = 3;
    private final int CAMERA = 4;

    private EditText desc, temp, rend;
    private Button btnAddIng, btnAddPre;
    private ImageView foto;
    private RecyclerView rcvIngre, rcvModo;

    private ListarIngredientes ingreAdapter;
    private ListarPreparo prepaAdapter;

    private ArrayList<IncLisIngItem> mlistIngre;
    private ArrayList<IncLisPreItem> mlistPrepa;

    private FirebaseFirestore fireDB = FirebaseFirestore.getInstance();
    private CollectionReference colRef = fireDB.collection("Receitas");
    private Receitas receitas;

    private int posIngre = 0, posPrepa = 0, nposi, nposp;
    private String id_rec;
    private String tip_rec;
    private String mqua, mmed, mdes, mpre;
    private String de, ti, te, re, im;
    private Uri imgUri;

    public void sendIngre(String qua, String med, String des) {
        mqua = qua;
        mmed = med;
        mdes = des;
        insertIngre(mqua, mmed, mdes);
        rcvIngreMostrar();
    }

    public void alt_Ingre(int pos, String qua, String med, String des){
        mqua = qua;
        mmed = med;
        mdes = des;
        changeIngre(pos, qua, med, des);
        rcvIngreMostrar();
    }

    public void apagaIngre(int pos, String qua, String med, String des){
        removeIngre(pos);
        rcvIngreMostrar();
    }

    public void sendPrepa(String pre) {
        mpre = pre;
        insertPrepa(mpre);
        rcvPrepaMostrar();
    }

    public void alt_Prepa(int pos, String pre) {
        mpre = pre;
        changePrepa(pos, pre);
        rcvPrepaMostrar();
    }

    public void apagaPrepa(int pos, String pre) {
        mpre = pre;
        removePrepa(pos);
        rcvPrepaMostrar();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MyTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);

        Intent intent = getIntent();
        id_rec = intent.getStringExtra("ID_REC");
        tip_rec = intent.getStringExtra("TIPO_REC");

        setTitle(Global.option + " " + tip_rec);
        mlistIngre = new ArrayList<>();
        mlistPrepa = new ArrayList<>();

        desc = findViewById(R.id.edTxdes_rec);
        foto = findViewById(R.id.imgVimg_rec);
        temp = findViewById(R.id.edTxtem_rec);
        rend = findViewById(R.id.edTxren_rec);
        btnAddIng = findViewById(R.id.btnAddIng);
        btnAddPre = findViewById(R.id.btnAddPre);
        rcvIngre = findViewById(R.id.rcvIngredientes);
        rcvModo = findViewById(R.id.rcvModoPreparo);

        if(Global.option == "Alterar"){
            carregar_dados();
        }

        desc.requestFocus();

        foto.setImageResource(R.drawable.ic_menu_camera);
        foto.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Preparando camera.");
                if (verificaPermissao()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Editar_Activity.this);
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

        btnAddIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( desc.getText().length() == 0){
                    mostrarToast("Aviso", "Nome da receita não pode estar vazio.");
                    desc.requestFocus();
                } else {
                    Frag_Ingre_dialog dialog1 = new Frag_Ingre_dialog();
                    Global.option = "Incluir";
                    Bundle bd = new Bundle();
                    bd.putInt("POS", nposi);
                    dialog1.setArguments(bd);
                    dialog1.show(getSupportFragmentManager(), "Incluir ingredientes");
                }
            }
        });

        btnAddPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(desc.getText().length() == 0){
                    mostrarToast("Aviso", "Nome da receita não pode estar vazio.");
                    desc.requestFocus();
                } else {
                    Frag_Prepa_dialog dialog2 = new Frag_Prepa_dialog();
                    Global.option = "Incluir";
                    Bundle bd = new Bundle();
                    bd.putInt("POS", nposp);
                    dialog2.setArguments(bd);
                    dialog2.show(getSupportFragmentManager(), "Incluir Modo de Preparo");
                }

            }
        });
    }

    private void insertIngre(String qua, String med, String des){
        mlistIngre.add(new IncLisIngItem(qua, med, des));
    }

    private void changeIngre(int pos, String qua, String med, String des){
        mlistIngre.get(pos).setMtxtListQtd(qua);
        mlistIngre.get(pos).setMtxtLisMed(med);
        mlistIngre.get(pos).setMtxtListIng(des);
        ingreAdapter.notifyItemChanged(pos);
    }

    private void removeIngre(int pos){
        mlistIngre.remove(pos);
        ingreAdapter.notifyItemRemoved(pos);
    }

    private void insertPrepa(String pre){
        mlistPrepa.add(new IncLisPreItem(pre));
    }

    private void changePrepa(int pos, String pre){
        mlistPrepa.get(pos).setMtxtListPrep(pre);
        prepaAdapter.notifyItemChanged(pos);
    }

    private void removePrepa(int pos){
        mlistPrepa.remove(pos);
        prepaAdapter.notifyItemRemoved(pos);
    }

    private void carregar_dados(){
        colRef.whereEqualTo("id", id_rec).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                for(QueryDocumentSnapshot doc : querySnapshot){
                    receitas = doc.toObject(Receitas.class);
                    desc.setText(receitas.getDescrição());
                    temp.setText(receitas.getTempoPreparo());
                    rend.setText(receitas.getRendimento());
                    Picasso.get().load(receitas.getImagem())
                            .into(foto);
                    for( IncLisIngItem ing : receitas.getIngredientes()) {
                        insertIngre(ing.getMtxtListQtd(), ing.getMtxtLisMed(), ing.getMtxtListIng());
                        posIngre++;
                    }
                    rcvIngreMostrar();
                    for( IncLisPreItem pre : receitas.getModoPreparo()) {
                        insertPrepa(pre.getMtxtListPrep());
                        posPrepa++;
                    }
                    rcvPrepaMostrar();
                }
            }
        });
    }

    private void rcvIngreMostrar() {
        ingreAdapter = new ListarIngredientes(mlistIngre, this);
        rcvIngre.setHasFixedSize(true);
        rcvIngre.setLayoutManager(new LinearLayoutManager(this));
        rcvIngre.setAdapter(ingreAdapter);
        rcvIngre.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        nposi = mlistIngre.size() + 1;
        ingreAdapter.setOnItemClickListener(new ListarIngredientes.OnItemCliclListener() {
            @Override
            public void onItemClick(int position) {
                Frag_Ingre_dialog dialogI = new Frag_Ingre_dialog();
                Global.option = "Alterar";
                Bundle bd = new Bundle();
                bd.putInt("POS", position);
                bd.putString("QUA_ING", mlistIngre.get(position).getMtxtListQtd());
                bd.putString("MED_ING", mlistIngre.get(position).getMtxtLisMed());
                bd.putString("DES_ING", mlistIngre.get(position).getMtxtListIng());
                dialogI.setArguments(bd);
                dialogI.show(getSupportFragmentManager(), "Alterar Ingredientes");
            }
        });
    }

    private void rcvPrepaMostrar() {
        prepaAdapter = new ListarPreparo(mlistPrepa, this);
        rcvModo.setHasFixedSize(true);
        rcvModo.setLayoutManager(new LinearLayoutManager(this));
        rcvModo.setAdapter(prepaAdapter);
        rcvModo.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        nposp = mlistPrepa.size() + 1;
        prepaAdapter.setOnItemClickListener(new ListarPreparo.OnItemCliclListener() {
            @Override
            public void onItemClick(int position) {
                Global.option = "Alterar";
                Frag_Prepa_dialog dialogI = new Frag_Prepa_dialog();
                Bundle bd = new Bundle();
                bd.putInt("POS", position);
                bd.putString("DES_PRE", mlistPrepa.get(position).getMtxtListPrep());
                dialogI.setArguments(bd);
                dialogI.show(getSupportFragmentManager(), "Alterar Modo de Preparo");
            }
        });
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_incluir_receitas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.salvarI:
                ProgressDialog pd = new ProgressDialog(this);
                if(imgUri == null) {
                    foto.setImageResource(R.drawable.receitas);
                }
                if(id_rec == null){
                    id_rec = UUID.randomUUID().toString();
                }
                if( Global.option == "Incluir") {
                    pd.setTitle("Incluindo receita...");
                    pd.show();
                } else if( Global.option == "Alterar") {
                    pd.setTitle("Alterando receita...");
                    pd.show();
                }
                if (desc.getText().length() == 0) {
                    mostrarToast("AVISO", "Nome da Receita não pode estar vazio!");
                    desc.requestFocus();
                    pd.dismiss();
                } else {
                    de = desc.getText().toString();
                    ti = tip_rec;
                    te = temp.getText().toString();
                    re = rend.getText().toString();
                    StorageReference imageRef = FirebaseStorage.getInstance().getReference()
                            .child("Imagens_Receitas").child(id_rec + ".jpg");
                    foto.setDrawingCacheEnabled(true);
                    foto.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) foto.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();
                    imageRef.putBytes(data).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task1) {
                                        if(task1.isSuccessful()){
                                            pd.dismiss();
                                            im = task1.getResult().toString();
                                            ArrayList<IncLisIngItem> in = new ArrayList<>();
                                            if( ingreAdapter != null){
                                                for(int i = 0; i < ingreAdapter.getItemCount(); i++){
                                                    in.add(new IncLisIngItem(mlistIngre.get(i).getMtxtListQtd(),
                                                            mlistIngre.get(i).getMtxtLisMed(),
                                                            mlistIngre.get(i).getMtxtListIng()));
                                                }
                                            }
                                            ArrayList<IncLisPreItem> mo = new ArrayList<>();
                                            if( prepaAdapter != null){
                                                for(int i = 0; i < prepaAdapter.getItemCount(); i++){
                                                    mo.add(new IncLisPreItem(mlistPrepa.get(i).getMtxtListPrep()));
                                                }
                                            }
                                            receitas = new Receitas(id_rec, ti, de, te, re, im, in, mo);
                                            //Toast.makeText(Editar_Activity.this, id_rec, Toast.LENGTH_SHORT).show();
                                            colRef.document(id_rec).set(receitas).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task2) {
                                                    if(task2.isSuccessful()){
                                                        Toast.makeText(Editar_Activity.this, "Dados alterados.", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(Editar_Activity.this, Listar_Activity.class);
                                                        intent.putExtra("TIPO_REC", tip_rec);
                                                        startActivity(intent);
                                                    } else {
                                                        Toast.makeText(Editar_Activity.this, "Falha para alterar.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }else {
                                            pd.dismiss();
                                            imageRef.delete();
                                        }
                                    }
                                });

                            } else {
                                pd.dismiss();
                                Toast.makeText(Editar_Activity.this, "Imagem não salva.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, Listar_Activity.class);
        intent.putExtra("TIPO_REC", tip_rec);
        startActivity(intent);
    }

    private void limparCampos() {
        desc.setText("");
        foto.setImageResource(R.drawable.ic_menu_camera);
        temp.setText("");
        rend.setText("");
        id_rec = "";
        ti = "";
        de = "";
        te = "";
        re = "";
        mlistIngre.clear();
        mlistPrepa.clear();
        rcvIngre.setAdapter(null);
        rcvModo.setAdapter(null);
        desc.requestFocus();
    }

    public void mostrarToast(String text1, String text2) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.layout_base));
        TextView txt1 = layout.findViewById(R.id.txtToast1);
        TextView txt2 = layout.findViewById(R.id.txtToast2);
        ImageView img1 = layout.findViewById(R.id.imgToast);

        txt1.setText(text1);
        txt2.setText(text2);
        Glide.with(getBaseContext()).load(R.drawable.ic_baseline_perm_device_information_24)
                .apply(RequestOptions.circleCropTransform()).into(img1);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TIRAR_FOTO && resultCode == RESULT_OK) {
            Bitmap imageBitmap = (Bitmap)data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), imageBitmap, "val", null);
            imgUri = Uri.parse(path);
            exibirImagem(imageBitmap);
        }

        if (requestCode == GALERIA_IMAGENS && resultCode == RESULT_OK) {
            imgUri = data.getData();
            Bitmap imagemGaleria;
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(imgUri, filePath, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            String picturePath = c.getString(columnIndex);
            c.close();
            imagemGaleria = (BitmapFactory.decodeFile(picturePath));
            exibirImagem(imagemGaleria);
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
        foto.setImageBitmap(image);
    }

    public static final Uri getUriToResource(@NonNull Context context, @AnyRes int resId) throws Resources.NotFoundException {

        Resources res = context.getResources();

        Uri resUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + res.getResourcePackageName(resId)
                + '/' + res.getResourceTypeName(resId)
                + '/' + res.getResourceEntryName(resId));

        return resUri;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}