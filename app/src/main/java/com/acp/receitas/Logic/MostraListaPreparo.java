package com.acp.receitas.Logic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.acp.receitas.Model.IncLisPreItem;
import com.acp.receitas.R;

import java.util.List;

public class MostraListaPreparo extends
        RecyclerView.Adapter<MostraListaPreparo.MyViewHolder> {

    private List<IncLisPreItem> mListPrepa;
    private Context context;
    private int position;
    private OnItemClickListener1 mlistener1;

    public interface OnItemClickListener1 {
        void onItemClick1(int position);
    }

    public void setOnItemClickListener1(OnItemClickListener1 listener1) {
        mlistener1 = listener1;
    }

    public void setmListPrepa(List<IncLisPreItem> mListPrepa) {
        this.mListPrepa = mListPrepa;
    }

    public MostraListaPreparo(List<IncLisPreItem> mListPrepa, Context context) {
        this.mListPrepa = mListPrepa;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View ingreView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mostrar_lista_prepa, parent,false);
        MyViewHolder mvh = new MyViewHolder(ingreView, mlistener1);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final IncLisPreItem modoPreparo = mListPrepa.get(position);
        if( modoPreparo != null ){
            holder.fraimvPre.setImageResource(R.drawable.ic_check_24dp);
            holder.fratxtPre.setText(mListPrepa.get(position).getMtxtListPrep());
        }
    }

    @Override
    public int getItemCount() {
        if( mListPrepa != null){
            return mListPrepa.size();
        }
        return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView fraimvPre;
        TextView fratxtPre;

        MyViewHolder(@NonNull View itemView, final OnItemClickListener1 listener1) {
            super(itemView);
            fraimvPre = itemView.findViewById(R.id.fraimvPre);
            fratxtPre = itemView.findViewById(R.id.fratxtPre);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if( listener1 != null){
                        int position = getAbsoluteAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener1.onItemClick1(position);
                        }
                    }
                }
            });
        }
    }
}
