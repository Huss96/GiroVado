package com.example.hussnain.girovado;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.util.Util;
import com.squareup.picasso.Picasso;

import butterknife.internal.Utils;

public class MyViewHolder extends RecyclerView.ViewHolder {

    private TextView textViewView;
    private ImageView imageView;


    public MyViewHolder(View itemView) {
        super(itemView);

        //c'est ici que l'on fait nos findView

        textViewView =  itemView.findViewById(R.id.text);
        imageView = itemView.findViewById(R.id.image);

    }


    public void bind(FoundLeisure myObject){
        textViewView.setText(myObject.getText());
        Picasso.get().load(myObject.getUrl()).centerCrop().fit().into(imageView);
    }
}

