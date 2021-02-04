package com.acp.receitas.Logic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import com.acp.receitas.R;

public class FlipperAdapterS extends BaseAdapter {
    private Context ctx;
    private ArrayList<String> text;
    private ArrayList<Bitmap> imagens;
    private LayoutInflater inflater;
    int mposition = -1;

    public FlipperAdapterS(Context context, ArrayList<String> text, ArrayList<Bitmap> imagens) {
        this.ctx = context;
        this.imagens = imagens;
        this.text = text;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return text.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.flipper_itens_i, null);
        TextView textView = view.findViewById(R.id.idTxtVw);
        ImageView imageView = view.findViewById(R.id.idImgVw);
        textView.setText(text.get(position));
        imageView.setImageBitmap(imagens.get(position));
        return view;
    }
}
