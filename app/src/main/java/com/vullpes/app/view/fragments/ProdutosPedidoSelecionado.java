package com.vullpes.app.view.fragments;

import android.content.Context;
import android.curso.minhaaplicacao.R;
import com.vullpes.app.classes.OnBackPressed;
import com.vullpes.app.model.ItemPedido;
import com.vullpes.app.view.adapters.ProdutosPedidoSelecionadoAdapter;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class ProdutosPedidoSelecionado extends Fragment implements OnBackPressed {
    ArrayList<ItemPedido> itensPedido;
    View view;
    RecyclerView rv;
    Context context;
    public ProdutosPedidoSelecionado() {
        // Required empty public constructor
        context = getContext();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            itensPedido =(ArrayList<ItemPedido>) bundle.getSerializable("itensPedido");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
            view = inflater.inflate(R.layout.fragment_produtos_pedido_selecionado, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Produtos do Pedido");
            rv= view.findViewById(R.id.rv);
            rv.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(context);
            rv.setLayoutManager(llm);
            ProdutosPedidoSelecionadoAdapter adapter = new ProdutosPedidoSelecionadoAdapter(itensPedido);
            rv.setAdapter(adapter);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);//Salva Activity
    }


    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);//Restaura o Activity

    }

    @Override
    public void OnBackPressed() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_fragment, new PedidosListagem()).commit();
    }
}
