package com.schwarzschild.absenonline;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MahasiswaAdapter extends RecyclerView.Adapter<MahasiswaAdapter.MahasiswaHolder> {

    ArrayList<Mahasiswa> listMahasiswa = new ArrayList<>();
    OnItemClick clickHandler;

    public void setListMahasiswa(ArrayList<Mahasiswa> mahas){
        listMahasiswa = mahas;
        notifyDataSetChanged();
    }

    public void setListMahasiswa(List<Mahasiswa> mahas){
        listMahasiswa = new ArrayList<>(mahas);
        notifyDataSetChanged();
    }

    public void setClickHandler(OnItemClick clickHandler){
        this.clickHandler = clickHandler;
    }

    @NonNull
    @Override
    public MahasiswaHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_mahasiswa, viewGroup, false);
        return new MahasiswaHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MahasiswaHolder mahasiswaHolder, int i){

        final Mahasiswa daftar = listMahasiswa.get(i);
        mahasiswaHolder.nama.setText(daftar.getNama());
        mahasiswaHolder.nim.setText(String.valueOf(daftar.getNim()));
    }

    @Override
    public int getItemCount() {
        if(listMahasiswa != null){
            return listMahasiswa.size();
        }
        return 0;
    }

    public class MahasiswaHolder extends RecyclerView.ViewHolder{
        TextView nama;
        TextView nim;

        public MahasiswaHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.nama);
            nim = itemView.findViewById(R.id.bp);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Mahasiswa mahasiswa = listMahasiswa.get(getAdapterPosition());
                    clickHandler.mahasiswaClick(mahasiswa);
                }
            });
        }
    }

    public interface OnItemClick{
        void mahasiswaClick(Mahasiswa mahasiswa);
    }
}
