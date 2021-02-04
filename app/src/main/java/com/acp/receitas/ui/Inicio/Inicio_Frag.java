package com.acp.receitas.ui.Inicio;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterViewFlipper;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.acp.receitas.Editar_Activity;
import com.acp.receitas.Listar_Activity;
import com.acp.receitas.Logic.Conexao;
import com.acp.receitas.Logic.FlipperAdapterS;
import com.acp.receitas.Logic.Global;
import com.acp.receitas.Model.Receitas;
import com.acp.receitas.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

public class Inicio_Frag extends Fragment {

    private static final int IO_BUFFER_SIZE = 100;
    private static final String TAG = "Inicio";
    private AdapterViewFlipper adapterViewFlipper;

    private static ArrayList<String> novoTxt2;
    private static ArrayList<Bitmap> novaImg2;
    private String tip_rec;
    private FirebaseAuth auth;
    private FirebaseFirestore fireDB;
    private CollectionReference colleRef;
    private Receitas receitas;
    private LinearLayout llDoces, llSalgadas;
    private ImageButton btnDoces, btnSalgadas;
    private Animation rotate, bounce;
    private Animation blink_anim, mixed_anim;
    private static ImageView imgTeste;

    public Inicio_Frag(){}

    @Override
    public void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        fireDB = FirebaseFirestore.getInstance();
        colleRef = fireDB.collection("Receitas");

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_main_inicio, container, false);
        novoTxt2 = new ArrayList<String>();
        novaImg2 = new ArrayList<Bitmap>();
        adapterViewFlipper = root.findViewById(R.id.viewFlipperI);
        imgTeste = root.findViewById(R.id.imgTeste);

        novoTxt2.add("Doces");
        novoTxt2.add("Salgadas");
        novaImg2.add(BitmapFactory.decodeResource(getResources(), R.drawable.doces));
        novaImg2.add(BitmapFactory.decodeResource(getResources(), R.drawable.salgadas));
        FlipperAdapterS flipperAdapter = new FlipperAdapterS(getContext(), novoTxt2, novaImg2);
        adapterViewFlipper.setAdapter(flipperAdapter);
        adapterViewFlipper.setFlipInterval(5000);
        adapterViewFlipper.setAutoStart(true);

        btnDoces = root.findViewById(R.id.imgBtnDoce);
        btnDoces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tip_rec = "Doces";
                fireDB.collection("Receitas")
                        .whereEqualTo("tipo", tip_rec)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot querySnapshot) {
                                if(querySnapshot.isEmpty()){
                                    Intent intent = new Intent(getActivity(), Editar_Activity.class);
                                    Global.option="Incluir";
                                    intent.putExtra("TIPO_REC", tip_rec);
                                    startActivity(intent);
                                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                } else {
                                    Intent intent = new Intent(getActivity(), Listar_Activity.class);
                                    intent.putExtra("TIPO_REC", tip_rec);
                                    startActivity(intent);
                                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                }
                            }
                        });
            }
        });

        btnSalgadas = root.findViewById(R.id.imgBtnSalgada);
        btnSalgadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tip_rec = "Salgadas";
                fireDB.collection("Receitas")
                        .whereEqualTo("tipo", tip_rec)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot querySnapshot) {
                                if(querySnapshot.isEmpty()){
                                    Intent intent = new Intent(getActivity(), Editar_Activity.class);
                                    Global.option="Incluir";
                                    intent.putExtra("TIPO_REC", tip_rec);
                                    startActivity(intent);
                                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                } else {
                                    Intent intent = new Intent(getActivity(), Listar_Activity.class);
                                    intent.putExtra("TIPO_REC", tip_rec);
                                    startActivity(intent);
                                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                }
                            }
                        });
            }
        });

        ShimmerFrameLayout container1 =
                (ShimmerFrameLayout) root.findViewById(R.id.shimmer_view_container1);
        container1.startShimmer();
        ShimmerFrameLayout container2 =
                (ShimmerFrameLayout) root.findViewById(R.id.shimmer_view_container2);
        container2.startShimmer();

        rotate = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
        bounce = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);
        blink_anim = AnimationUtils.loadAnimation(getActivity(), R.anim.blink_anim);
        mixed_anim = AnimationUtils.loadAnimation(getActivity(), R.anim.mixed_anim);

        llDoces = root.findViewById(R.id.llDoces);
        llSalgadas = root.findViewById(R.id.llSalgadas);
        //        llDoces.setAnimation(blink_anim);
        //        llSalgadas.setAnimation(blink_anim);

        return root;
    }

}
