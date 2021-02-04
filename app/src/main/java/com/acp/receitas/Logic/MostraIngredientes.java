package com.acp.receitas.Logic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.acp.receitas.Model.IncLisIngItem;
import com.acp.receitas.R;

import java.util.ArrayList;

public class MostraIngredientes extends
        RecyclerView.Adapter<MostraIngredientes.MyViewHolder> {

    private ArrayList<IncLisIngItem> mListIngre;

    public MostraIngredientes(ArrayList<IncLisIngItem> mListIngre) {
        this.mListIngre = mListIngre;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from( parent.getContext()).inflate( R.layout.frag_lista_ingrei, parent, false );
        MyViewHolder viewHolder = new MyViewHolder( itemView );
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {

        holder.mText1.setText( mListIngre.get(i).getMtxtListQtd());
        holder.mText2.setText( mListIngre.get(i).getMtxtLisMed());
        holder.mText3.setText( mListIngre.get(i).getMtxtListIng());

    }

    @Override
    public int getItemCount() {
        return mListIngre.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mText1;
        private TextView mText2;
        private TextView mText3;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mText1 = itemView.findViewById(R.id.fratxtQua);
            mText2 = itemView.findViewById(R.id.fratxtMed);
            mText3 = itemView.findViewById(R.id.fratxtIng);

        }
    }
}
