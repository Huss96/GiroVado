package com.example.hussnain.girovado;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    List<FoundLeisure> list;
    private FoundLeisure myObject;
    Context context;

    public MyAdapter(List<FoundLeisure> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int itemType) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row,viewGroup,false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Leisure leisure = new Leisure(list.get(itemType).getLeisureId());
                context.startActivity(new Intent(context,SelectedLeisure.class));
            }
        });
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int position) {
        myObject = list.get(position);
        myViewHolder.bind(myObject);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    @Override
    public int getItemViewType(int position)
    {
        return position;
    }

}
