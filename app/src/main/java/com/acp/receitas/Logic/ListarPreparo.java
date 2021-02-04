package com.acp.receitas.Logic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.acp.receitas.Model.IncLisPreItem;
import com.acp.receitas.R;

import java.util.List;

public class ListarPreparo extends
        RecyclerView.Adapter<ListarPreparo.MyViewHolder> {

    private List<IncLisPreItem> mListPrepa;
    private Context context;
    private OnItemCliclListener mListener;
    private int position;

    public interface OnItemCliclListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemCliclListener listener){
        mListener = listener;
    }

    public void setmListPrepa(List<IncLisPreItem> mListPrepa) {
        this.mListPrepa = mListPrepa;
    }

    public ListarPreparo(List<IncLisPreItem> mListPrepa, Context context) {
        this.mListPrepa = mListPrepa;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from( parent.getContext()).inflate( R.layout.frag_lista_prepai, parent, false);
        MyViewHolder viewHolder = new MyViewHolder( itemView, mListener );
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        final IncLisPreItem mlistPrepaItem = mListPrepa.get( i );
        if( mlistPrepaItem != null){
            holder.mText1.setText( mListPrepa.get( i ).getMtxtListPrep());
        }
    }

    @Override
    public int getItemCount() {
        if( mListPrepa != null ){
            return mListPrepa.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mText1;

        public MyViewHolder(@NonNull View itemView, final OnItemCliclListener listener) {
            super(itemView);
            mText1 = itemView.findViewById(R.id.fratxtPre);
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
