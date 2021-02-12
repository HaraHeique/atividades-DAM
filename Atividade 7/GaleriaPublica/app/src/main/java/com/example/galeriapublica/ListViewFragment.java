package com.example.galeriapublica;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListViewFragment extends Fragment {

    public ListViewFragment() {
        // Required empty public constructor
    }

    public static ListViewFragment newInstance() {
        ListViewFragment fragment = new ListViewFragment();

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
        return inflater.inflate(R.layout.fragment_list_view, container, false);
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

        // Instancia o list item adapter passando o contexto atual e a lista de imagens
        ListAdapter ListAdapter = new ListAdapter(getContext(), new ArrayList<ImageData>(imageDataList.values()));

        // Obtém o recycle view da lista e define seu adapter e layout manager
        RecyclerView rvList = getView().findViewById(R.id.rvList);
        rvList.setAdapter(ListAdapter);
        rvList.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}