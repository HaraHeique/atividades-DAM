package com.example.mylist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter {
    List<MyItem> itens;
    MainActivity mainActivity;

    public MyAdapter(MainActivity mainActivity, List<MyItem> itens) {
        this.mainActivity = mainActivity;
        this.itens = itens;
    }

    // Constrói o layout (esqueleto dela, e não preenche ela)
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.mainActivity);
        View v = inflater.inflate(R.layout.item_list, parent, false);

        return new MyViewHolder(v);
    }

    // Preenchendo o layout com os dados dos itens guardados na lista de itens
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Obtém os itens na posição
        MyItem myItem = itens.get(position);
        // Obtém a instância do ViewHolder passada como argumento do método
        View v = holder.itemView;

        // Obtém a instância da foto da lista e seta a imagem na lista
        ImageView imvPhoto = v.findViewById(R.id.imvPhoto);
        imvPhoto.setImageURI(myItem.photo);

        // Obtém a instância do título da lista e seta a seu conteúdo
        TextView tvTitle = v.findViewById(R.id.tvTitle);
        tvTitle.setText(myItem.title);

        // Obtém a instância da descrição da lista e seta o seu conteúdo
        TextView tvDescription = v.findViewById(R.id.tvDescription);
        tvDescription.setText(myItem.description);
    }


    // Quantidade de itens na lista
    @Override
    public int getItemCount() {
        return itens.size();
    }
}
