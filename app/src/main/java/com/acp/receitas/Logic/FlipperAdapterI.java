package com.acp.receitas.Logic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.acp.receitas.R;

public class FlipperAdapterI extends BaseAdapter {
    Context ctx;
    int[] images;
    String[] text;
    LayoutInflater inflater;

    public FlipperAdapterI(Context context, int[] myImages/*, String[] myText*/) {
        this.ctx = context;
        this.images = myImages;
        //this.text = myText;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.flipper_itens_s, null);
        //TextView txtName = view.findViewById(R.id.idTxtVw);
        ImageView txtImage = view.findViewById(R.id.idImgVw);
        //txtName.setText(text[i]);
        txtImage.setImageResource(images[i]);
        return view;
    }
}
