package com.example.galeriapublica;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GridAdapter extends RecyclerView.Adapter {
    Context context;
    List<ImageData> imageDataList;

    public GridAdapter(Context context, List<ImageData> imageDataList) {
        this.context = context;
        this.imageDataList = imageDataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla o layout com o context que no caso é a Activity que o chamou
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        // Criar view do layout de grid
        View view = layoutInflater.inflate(R.layout.grid_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Obtém a imagem que é um bitmap
        Bitmap thumb = imageDataList.get(position).thumb;

        // Recupera o imageview referente ao grid e seta a imagem bitmap nele
        ImageView imageView = holder.itemView.findViewById(R.id.imvGrid);
        imageView.setImageBitmap(thumb);
    }

    @Override
    public int getItemCount() {
        // Retorna o tamanho de elementos da lista
        return imageDataList.size();
    }
}
