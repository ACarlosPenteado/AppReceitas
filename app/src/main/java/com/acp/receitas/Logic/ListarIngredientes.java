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

public class ListarIngredientes extends
        RecyclerView.Adapter<ListarIngredientes.MyViewHolder> {

    private List<IncLisIngItem> mListIngre;
    private Context context;
    private OnItemCliclListener mListener;
    private int position;

    public interface OnItemCliclListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemCliclListener listener){
        mListener = listener;
    }

    public void setmListIngre(List<IncLisIngItem> mListIngre){
        this.mListIngre = mListIngre;
    }

    public ListarIngredientes(List<IncLisIngItem> mListIngre, Context context) {
        this.mListIngre = mListIngre;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from( parent.getContext()).inflate( R.layout.frag_lista_ingrei,
                parent, false );
        MyViewHolder viewHolder = new MyViewHolder( itemView, mListener );
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        final IncLisIngItem mlistIngreItem = mListIngre.get( i );
        if( mlistIngreItem != null){
            holder.mText1.setText( mListIngre.get( i ).getMtxtListQtd());
            holder.mText2.setText(mListIngre.get( i ).getMtxtLisMed());
            holder.mText3.setText( mListIngre.get( i ).getMtxtListIng());
        }
    }

    @Override
    public int getItemCount() {
        if( mListIngre != null ){
            return mListIngre.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mText1;
        private TextView mText2;
        private TextView mText3;

        public MyViewHolder(@NonNull View itemView, final OnItemCliclListener listener) {
            super(itemView);
            mText1 = itemView.findViewById(R.id.fratxtQua);
            mText2 = itemView.findViewById(R.id.fratxtMed);
            mText3 = itemView.findViewById(R.id.fratxtIng);
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
