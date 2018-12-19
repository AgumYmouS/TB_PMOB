package com.schwarzschild.absenonline;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class FavoritAdapter extends RecyclerView.Adapter<FavoritAdapter.FavoritHolder>{
    ArrayList<Mahasiswa> listMahasiswa = new ArrayList<>();

    public void setListMahasiswa(ArrayList<Mahasiswa> mahas){
        listMahasiswa = mahas;
        notifyDataSetChanged();
    }

    public void setListMahasiswa(List<Mahasiswa> mahas){
        listMahasiswa = new ArrayList<>(mahas);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoritAdapter.FavoritHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_favorit, viewGroup, false);
        FavoritHolder holder = new FavoritHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritAdapter.FavoritHolder favoritHolder, int i){

        final Mahasiswa daftar = listMahasiswa.get(i);
        String url = daftar.getFotoLink();
        Glide.with(favoritHolder.itemView)
                .load(url)
                .into(favoritHolder.foto);

    }

    @Override
    public int getItemCount() {
        if(listMahasiswa != null){
            return listMahasiswa.size();
        }
        return 0;
    }

    public class FavoritHolder extends RecyclerView.ViewHolder{
        ImageView foto;

        public FavoritHolder(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.f_foto);

        }
    }
}

