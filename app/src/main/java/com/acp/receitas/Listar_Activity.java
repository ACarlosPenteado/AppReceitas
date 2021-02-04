package com.acp.receitas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.acp.receitas.Fragmentos.Frag_dialog_vazia;
import com.acp.receitas.Logic.Global;
import com.acp.receitas.Logic.RecAdapter;
import com.acp.receitas.Model.RecItens;
import com.acp.receitas.Model.Receitas;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class Listar_Activity extends AppCompatActivity
        implements Frag_dialog_vazia.OnVaziaListener {

    private String id_rec, tip_rec;
    private FirebaseFirestore fireDB = FirebaseFirestore.getInstance();
    private CollectionReference colRef = fireDB.collection("Receitas");
    private RecyclerView rcvReceitas;
    private RecAdapter receitaAdapter;
    private RecyclerView.LayoutManager mLayout;
    private ArrayList<RecItens> receitasList;
    private FloatingTextButton add_btn;
    private ProgressBar mProgressCircle;


    @Override
    public void confirmaVazia(Boolean confirma) {
        if (confirma) {
            Intent intent1 = new Intent(this, Editar_Activity.class);
            Global.option = "Incluir";
            intent1.putExtra("TIPO_REC", tip_rec);
            startActivity(intent1);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else {
            Intent intent2 = new Intent(this, MainActivity.class);
            startActivity(intent2);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);
        ShimmerFrameLayout container1 =
                (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container1);
        container1.startShimmer();

        mProgressCircle = findViewById(R.id.progress1);

        Intent intent = getIntent();
        tip_rec = intent.getStringExtra("TIPO_REC");

        TextView txvLista = findViewById(R.id.txvLista);
        FloatingTextButton addBtn = findViewById(R.id.add_btn);
        rcvReceitas = findViewById(R.id.rcvReceitas);

        String trec = "Lista de Receitas " + tip_rec;
        txvLista.setText(trec);

        receitasList = new ArrayList<RecItens>();

        colRef.whereEqualTo("tipo", tip_rec).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        if(!querySnapshot.isEmpty()){
                            for(DocumentSnapshot doc:querySnapshot){
                                RecItens receitas = doc.toObject(RecItens.class);
                                receitas.setImagem(doc.getString("imagem"));
                                receitas.setDescrição(doc.getString("descrição"));
                                receitas.setTempoPreparo(doc.getString("tempoPreparo"));
                                receitas.setRendimento(doc.getString("rendimento"));
                                receitasList.add(new RecItens(doc.getString("id"), doc.getString("tipo"),
                                        doc.getString("descrição"), doc.getString("tempoPreparo"),
                                        doc.getString("rendimento"), doc.getString("imagem")));
                                mProgressCircle.setVisibility(View.INVISIBLE);
                            }

                            receitaAdapter = new RecAdapter(receitasList, Listar_Activity.this);
                            rcvReceitas.setHasFixedSize(true);
                            rcvReceitas.setLayoutManager(new LinearLayoutManager(Listar_Activity.this));
                            rcvReceitas.setAdapter(receitaAdapter);
                            rcvReceitas.addItemDecoration(new DividerItemDecoration(Listar_Activity.this, DividerItemDecoration.VERTICAL));

                            receitaAdapter.setOnItemClickListener(new RecAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    Intent i = new Intent(getApplicationContext(), Mostrar_Activity.class);
                                    i.putExtra("ID_REC", receitasList.get(position).getID());
                                    i.putExtra("TIPO_REC", receitasList.get(position).getTipo());
                                    startActivity(i);
                                }
                            });
                        } else {
                            Log.e("Listar", "Erro carregando documentos.");
                        }
                    }
                });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Listar_Activity.this, Editar_Activity.class);
                Global.option = "Incluir";
                intent.putExtra("TIPO_REC", tip_rec);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}