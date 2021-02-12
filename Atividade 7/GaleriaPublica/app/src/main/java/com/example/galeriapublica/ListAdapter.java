package com.example.galeriapublica;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter {
    Context context;
    List<ImageData> imageDataList;

    public ListAdapter(Context context, List<ImageData> imageDataList) {
        this.context = context;
        this.imageDataList = imageDataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla o layout com o context que no caso é a Activity que o chamou
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        // Criar view do layout de list items
        View view = layoutInflater.inflate(R.layout.list_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Obtém os dados da imagem
        ImageData imageData = imageDataList.get(position);

        // Obtém o o text view de nome e seta o nome da imagem nele
        TextView tvName = holder.itemView.findViewById(R.id.tvName);
        tvName.setText(imageData.filename);

        // Obtém o text view de data e seta o data nele formatado
        TextView tvDate = holder.itemView.findViewById(R.id.tvDate);
        tvDate.setText("Data: " + new SimpleDateFormat("HH:mm dd/MM/yyyy").format(imageData.date));

        // Obtém o text view de tamanho da imagem e seta nele o tamanho da imagem
        TextView tvSize = holder.itemView.findViewById(R.id.tvSize);
        tvSize.setText("Tamanho: " + String.valueOf(imageData.size));

        // Obtém a image view da imagem da lista e seta a imagem nela
        ImageView imageView = holder.itemView.findViewById(R.id.imvList);
        imageView.setImageBitmap(imageData.thumb);
    }

    @Override
    public int getItemCount() {
        // Retorna o tamanho de elementos da lista
        return imageDataList.size();
    }
}
