package com.acp.receitas.Logic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.acp.receitas.Model.IncLisIngItem;
import com.acp.receitas.R;

import java.util.List;

public class MostraListaIngre extends
        RecyclerView.Adapter<MostraListaIngre.MyViewHolder> {

    private List<IncLisIngItem> mListIngre;
    private Context context;
    private OnItemClickListener mListener;
    private int position;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public void setmListIngre(List<IncLisIngItem> mListIngre){
        this.mListIngre = mListIngre;
    }

    public MostraListaIngre(List<IncLisIngItem> mListIngre, Context context) {
        this.mListIngre = mListIngre;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View ingreView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mostra_lista_ingre, parent,false);
        MyViewHolder mvh = new MyViewHolder(ingreView, mListener);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final IncLisIngItem ingredientes = mListIngre.get(position);
        if( ingredientes != null){
            holder.fratxtQua.setText(ingredientes.getMtxtListQtd());
            holder.fratxtMed.setText(ingredientes.getMtxtLisMed());
            holder.fratxtIng.setText(ingredientes.getMtxtListIng());
        }
    }

    @Override
    public int getItemCount() {
        if( mListIngre != null ){
            return mListIngre.size();
        }
        return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView fratxtQua;
        TextView fratxtMed;
        TextView fratxtIng;

        MyViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            fratxtQua = itemView.findViewById(R.id.fratxtQua);
            fratxtMed = itemView.findViewById(R.id.fratxtMed);
            fratxtIng = itemView.findViewById(R.id.fratxtIng);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if( listener != null ){
                        int position = getAbsoluteAdapterPosition();
                        if( position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }

    }
}
