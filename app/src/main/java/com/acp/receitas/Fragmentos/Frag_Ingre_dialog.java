package com.acp.receitas.Fragmentos;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.acp.receitas.Logic.Global;
import com.acp.receitas.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class Frag_Ingre_dialog extends DialogFragment {

    private static final String TAG = "Frag_Ingre_dialog";
    private int mpos;
    private String qua, med, des;

    private final String[] MEDIDAS = new String[]{ "Kilo(s)", "Xícara(s) de Chá",
            "Colher(es) de Sopa", "Colher(es) de Chá", "Colher(es) de Café", "Gramas", "Ml",
            "Copo(s) Americano", "Copo(s) de Requeijão", "Litro(s)" };

    private AutoCompleteTextView medi;
    private TextView txtNome;
    private EditText edTxqua, edTxing;
    private Button btnCancel, btnDel, btnOk;

    public interface OnIngreListener{
        void sendIngre(String qua, String med, String des);
        void alt_Ingre(int pos, String qua, String med, String des);
        void apagaIngre(int pos, String qua, String med, String des);
    }

    public OnIngreListener mOnIngreListener;

    public Frag_Ingre_dialog() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Script", "onCreate()");
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_dialog_ingre, container, false);

        Bundle bd = getArguments();
        mpos = bd.getInt("POS");
        qua = bd.getString("QUA_ING");
        med = bd.getString("MED_ING");
        des = bd.getString("DES_ING");

        txtNome = view.findViewById(R.id.txtnome);
        edTxqua = view.findViewById(R.id.edTxqua_ing);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, MEDIDAS);
        medi = view.findViewById(R.id.actvMedida1);
        medi.setAdapter(adapter);

        edTxing = view.findViewById(R.id.edTxdes_ing);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnDel = view.findViewById(R.id.btnDel);
        btnOk = view.findViewById(R.id.btnOk);

        if(Global.option == "Alterar"){
            txtNome.setText("ALTERAR INGREDIENTES");
            edTxqua.setText(qua);
            medi.setText(med);
            edTxing.setText(des);
            btnDel.setVisibility(View.VISIBLE);
        } else {
            txtNome.setText("INCLUIR INGREDIENTES");
            btnDel.setVisibility(View.INVISIBLE);
        }

        edTxqua.requestFocus();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
                onDestroyView();
            }
        });

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnIngreListener.apagaIngre(mpos, edTxqua.getText().toString(),
                        medi.getText().toString(), edTxing.getText().toString());
                onDestroyView();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (edTxing.getText().length() == 0){
                   mostrarToast("AVISO", "Nome do ingrediente não pode estar vazio!");
                   edTxing.requestFocus();
                } else {
                   if(Global.option == "Incluir") {
                       mOnIngreListener.sendIngre(edTxqua.getText().toString(),
                               medi.getText().toString(), edTxing.getText().toString());
                   } else if( Global.option == "Alterar" ){
                       mOnIngreListener.alt_Ingre( mpos, edTxqua.getText().toString(),
                               medi.getText().toString(), edTxing.getText().toString());
                   }
                   onDestroyView();
               }
            }
        });
        return view;
    }

    public void mostrarToast(String text1, String text2){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup)getActivity().findViewById(R.id.layout_base));
        TextView txt1 = layout.findViewById(R.id.txtToast1);
        TextView txt2 = layout.findViewById(R.id.txtToast2);
        ImageView img1 = layout.findViewById(R.id.imgToast);

        txt1.setText(text1);
        txt2.setText(text2);
        Glide.with(getActivity()).load(R.drawable.ic_baseline_perm_device_information_24)
                .apply(RequestOptions.circleCropTransform()).into(img1);
        Toast toast = new Toast(getActivity().getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL,0,0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            mOnIngreListener = (OnIngreListener) getActivity();
        } catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage() );
        }
    }
}
