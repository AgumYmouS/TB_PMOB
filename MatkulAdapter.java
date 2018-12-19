package com.schwarzschild.absenonline;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MatkulAdapter extends RecyclerView.Adapter<MatkulAdapter.ViewHolder>{

    private static final String TAG = "MatkulAdapter";

    ArrayList<Matkul> listMatkul;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_matkul, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Log.d(TAG, "onBindViewHolder: called.");

        final Matkul matkul = listMatkul.get(i);
        viewHolder.shift.setText(matkul.getShift());
        viewHolder.matkul.setText(matkul.getMatkul());

        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AbsenActivity.class);
                intent.putExtra("shift", String.valueOf(listMatkul.get(i).shift));
                intent.putExtra("matkul", String.valueOf(listMatkul.get(i).matkul));
                v.getContext().startActivity(intent);
                Toast.makeText(v.getContext(), "Mata Kuliah "+ String.valueOf(listMatkul.get(i).matkul), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listMatkul != null){
            return listMatkul.size();
        }
        return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView shift;
        TextView matkul;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shift = itemView.findViewById(R.id.shift);
            matkul = itemView.findViewById(R.id.matkul);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }

    public MatkulAdapter(ArrayList<Matkul> matkulArrayList){
        listMatkul = matkulArrayList;
    }
}
