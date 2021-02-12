package com.example.galeriapublica;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GridViewFragment extends Fragment {

    public GridViewFragment() {
        // Required empty public constructor
    }

    public static GridViewFragment newInstance() {
        GridViewFragment fragment = new GridViewFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_grid_view, container, false);
    }

    // Este método é mais seguro de realizar a lógica porque já tem certeza que a activity foi criada
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Obtém a instância da view model
        MainActivityViewModel mainActivityViewModel = new ViewModelProvider(getActivity())
                .get(MainActivityViewModel.class);

        // Obtém a lista de imagens da view model
        HashMap<Long, ImageData> imageDataList = mainActivityViewModel.getImageDataList();

        // Instancia o grid adapter passando o contexto atual e a lista de imagens
        GridAdapter gridAdapter = new GridAdapter(getContext(), new ArrayList<ImageData>(imageDataList.values()));

        // Obtém o número de colunas definidas a partir do tamanho da tela
        float w = getResources().getDimension(R.dimen.im_width);
        int nColumns = Utils.calculateNumberOfColumns(getContext(), w);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), nColumns);

        // Obtém o recycle view do grid e define seu adapter e layout manager
        RecyclerView rvGrid = getView().findViewById(R.id.rvGrid);
        rvGrid.setAdapter(gridAdapter);
        rvGrid.setLayoutManager(gridLayoutManager);
    }
}