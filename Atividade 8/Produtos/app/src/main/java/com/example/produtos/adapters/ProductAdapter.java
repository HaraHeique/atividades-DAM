package com.example.produtos.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.produtos.models.Product;
import com.example.produtos.R;
import com.example.produtos.activities.ViewProductActivity;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private final List<Product> productList;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Obtenção do inflador de layout para construir o layout de cada item da lista
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.adapter_list_products_items, parent, false);

        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        // Setar as informações de cada item da lista nos campos do layout
        Product product = this.productList.get(position);

        // Obtendo e setando a informação de text view da lista do produto
        TextView tvNameList = holder.itemView.findViewById(R.id.tv_name_list);
        tvNameList.setText(product.name);

        // Binding do evento de click no item da lista
        this.onClickBtnProductList(holder.itemView, product);
    }

    @Override
    public int getItemCount() {
        // Conta a quantidade de produtos na lista de produtos do adapter
        return this.productList.size();
    }

    private void onClickBtnProductList(View itemView, Product product) {
        itemView.setOnClickListener(v -> {
            // Pega o context a partir do itemView
            Context context = itemView.getContext();

            // Cria a intenção de ir para a ViewProductActivity e coloca o ID do produto para ser buscadas outras info na ViewProductActivity
            Intent intent = new Intent(context, ViewProductActivity.class);
            intent.putExtra("pid", product.pid);
            context.startActivity(intent);
        });
    }
}
