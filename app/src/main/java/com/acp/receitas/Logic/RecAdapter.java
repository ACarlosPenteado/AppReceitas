package com.acp.receitas.Logic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.acp.receitas.Model.RecItens;
import com.acp.receitas.Model.Receitas;
import com.acp.receitas.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class RecAdapter extends
        RecyclerView.Adapter<RecAdapter.RecHolder> {

    private List<RecItens> mreceitasList;
    private Context mcontext;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public RecAdapter(List<RecItens> receitasList, Context mcontext) {
        this.mreceitasList = receitasList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public RecHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.linha_receitas,
                parent, false);
        RecHolder viewHolder = new RecHolder( view );
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecHolder holder, int position) {
        RecItens receitas = mreceitasList.get(position);
        if( receitas != null) {
            Picasso.get().load(receitas.getImagem())
                    .fit()
                    .centerCrop()
                    .into(holder.imVimg_rec);
            holder.txVdes_rec.setText(receitas.getDescrição());
            holder.txVtem_rec.setText(receitas.getTempoPreparo());
            holder.txVren_rec.setText(receitas.getRendimento());
        }
    }

    @Override
    public int getItemCount() {
        if( mreceitasList != null ) {
            return mreceitasList.size();
        }
        return 0;
    }

    public class RecHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private CircleImageView imVimg_rec;
        private TextView txVdes_rec;
        private TextView txVtem_rec;
        private TextView txVren_rec;

        public RecHolder(@NonNull View itemView) {
            super(itemView);
            imVimg_rec = itemView.findViewById(R.id.imVimg_rec);
            txVdes_rec = itemView.findViewById(R.id.txVdes_rec);
            txVtem_rec = itemView.findViewById(R.id.txVtem_rec);
            txVren_rec = itemView.findViewById(R.id.txVren_rec);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if( mListener != null ){
                int position = getAbsoluteAdapterPosition();
                if( position != RecyclerView.NO_POSITION){
                    mListener.onItemClick(position);
                }
            }
        }

    }
}
