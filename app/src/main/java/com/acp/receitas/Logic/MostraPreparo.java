package com.acp.receitas.Logic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.acp.receitas.Model.IncLisPreItem;
import com.acp.receitas.R;

import java.util.ArrayList;

public class MostraPreparo extends
        RecyclerView.Adapter<MostraPreparo.MyViewHolder> {

    private ArrayList<IncLisPreItem> mListPrepa;

    public MostraPreparo(ArrayList<IncLisPreItem> mListPrepa) {
        this.mListPrepa = mListPrepa;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from( parent.getContext()).inflate( R.layout.frag_lista_prepai, parent, false );
        MyViewHolder viewHolder = new MyViewHolder( itemView );
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {

        holder.mText1.setText( mListPrepa.get(i).getMtxtListPrep());

    }

    @Override
    public int getItemCount() {
        return mListPrepa.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mText1;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mText1 = itemView.findViewById(R.id.fratxtPre);

        }
    }
}
