package com.example.galeria;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter {
    MainActivity mainActivity;
    List<String> photos;

    public MainAdapter(MainActivity mainActivity, List<String> photos) {
        this.mainActivity = mainActivity;
        this.photos = photos;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Criação da interface (construindo o item do recycle view)
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        View v = layoutInflater.inflate(R.layout.list_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Obtém a instância da foto da lista de itens do recycle view
        ImageView imgPhoto = holder.itemView.findViewById(R.id.imgItem);

        int width = (int)mainActivity.getResources().getDimension(R.dimen.itemWidth);
        int height = (int)mainActivity.getResources().getDimension(R.dimen.itemHeight);

        // Obtém a escala calculada da foto
        Bitmap bitmap = Utils.getScaledBitmap(photos.get(position), width, height);

        // Seta o novo bitmap para a imagem
        imgPhoto.setImageBitmap(bitmap);

        // Ao clicar na foto
        imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chama o intent passando a foto da posição clicada
                mainActivity.startPhotoActivity(photos.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        // Retorna a quantidade de itens de foto
        return photos.size();
    }
}
