package com.acp.receitas;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.acp.receitas.Fragmentos.Frag_dialog_deleta;
import com.acp.receitas.Logic.Global;
import com.acp.receitas.Model.IncLisIngItem;
import com.acp.receitas.Model.IncLisPreItem;
import com.acp.receitas.Logic.MostraIngredientes;
import com.acp.receitas.Logic.MostraPreparo;
import com.acp.receitas.Model.Receitas;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class Mostrar_Activity extends AppCompatActivity
        implements Frag_dialog_deleta.OnReceitaListener {

    private FirebaseFirestore fireDB = FirebaseFirestore.getInstance();
    private CollectionReference colRef = fireDB.collection("Receitas");
    private String id_rec, tip_rec;
    private ImageView foto;
    private TextView desc, temp, rend;
    private ArrayList<IncLisIngItem> mlistIngre;
    private ArrayList<IncLisPreItem> mlistPrepa;
    private RecyclerView rvIngre, rvPrepa;
    private RecyclerView.Adapter ingreAdapter;
    private RecyclerView.Adapter prepaAdapter;
    private Animation myAnim;

    public void confirmaExclusao(String id) {
        StorageReference imageRef = FirebaseStorage.getInstance().getReference()
                .child("Imagens_Receitas").child(id + ".jpg");
        colRef.document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                imageRef.delete();
                Toast.makeText(Mostrar_Activity.this, "Receita apagada", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Mostrar_Activity.this, Listar_Activity.class);
                intent.putExtra("TIPO_REC", tip_rec);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar);

        Intent intent = getIntent();
        id_rec = intent.getStringExtra("ID_REC");
        tip_rec = intent.getStringExtra("TIPO_REC");

        ShimmerFrameLayout container1 =
                (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container1);
        container1.startShimmer();

        desc = (TextView) findViewById(R.id.mostrar);
        temp = (TextView) findViewById(R.id.txtMTemp);
        rend = (TextView) findViewById(R.id.txtMRend);
        foto = (ImageView) findViewById(R.id.ivMFoto);
        rvIngre = findViewById(R.id.rv_ingre);
        rvPrepa = findViewById(R.id.rv_prepa);
        myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        foto.startAnimation(myAnim);

        mlistIngre = new ArrayList<IncLisIngItem>();
        mlistPrepa = new ArrayList<IncLisPreItem>();

        colRef.whereEqualTo("id", id_rec).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot querySnapshot) {
                    for(QueryDocumentSnapshot doc : querySnapshot){
                        Receitas receitas = doc.toObject(Receitas.class);
                        desc.setText(receitas.getDescrição());
                        temp.setText(receitas.getTempoPreparo());
                        rend.setText(receitas.getRendimento());
                        Picasso.get().load(receitas.getImagem())
                                .into(foto);
                        for( IncLisIngItem ing : receitas.getIngredientes()) {
                            mlistIngre.add(new IncLisIngItem(ing.getMtxtListQtd(), ing.getMtxtLisMed(), ing.getMtxtListIng()));
                        }
                        for( IncLisPreItem pre : receitas.getModoPreparo()) {
                            mlistPrepa.add(new IncLisPreItem((pre.getMtxtListPrep())));
                        }
                            ingreAdapter = new MostraIngredientes(mlistIngre);
                            rvIngre.setHasFixedSize(true);
                            rvIngre.setLayoutManager(new LinearLayoutManager(Mostrar_Activity.this));
                            rvIngre.setAdapter(ingreAdapter);
                            rvIngre.addItemDecoration(new DividerItemDecoration(Mostrar_Activity.this, DividerItemDecoration.VERTICAL));

                            prepaAdapter = new MostraPreparo(mlistPrepa);
                            rvPrepa.setHasFixedSize(true);
                            rvPrepa.setLayoutManager(new LinearLayoutManager(Mostrar_Activity.this));
                            rvPrepa.setAdapter(prepaAdapter);
                            rvPrepa.addItemDecoration(new DividerItemDecoration(Mostrar_Activity.this, DividerItemDecoration.VERTICAL));
                    }
                }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_mostrar_receitas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, Listar_Activity.class);
                intent.putExtra("TIPO_REC", tip_rec);
                startActivity(intent);
                break;
            case R.id.editaR:
                Intent intent1 = new Intent(this, Editar_Activity.class);
                Global.option = "Alterar";
                intent1.putExtra("ID_REC", id_rec);
                intent1.putExtra("TIPO_REC", tip_rec);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.apagaR:
                Frag_dialog_deleta dialog1 = new Frag_dialog_deleta();
                Bundle bd = new Bundle();
                bd.putString("ID_REC", id_rec);
                dialog1.setArguments(bd);
                dialog1.show(getSupportFragmentManager(), "Apagar Receita");
                break;
            case R.id.compartilhaR:
                //compartilhar();
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

}