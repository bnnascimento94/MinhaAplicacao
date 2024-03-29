package com.vullpes.app.view.fragments;


import com.vullpes.app.controller.ControleCondicaoPagamento;
import com.vullpes.app.model.Cliente;
import com.vullpes.app.view.adapters.CondicaoPagamentoPedidoAdapter;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.curso.minhaaplicacao.R;

import java.util.ArrayList;


public class CondicaoPagamentoPedidoListagem extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    View view;
    RecyclerView rv;
    ArrayList<Cliente> cliente;
    public CondicaoPagamentoPedidoListagem() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            //itemPedidos =(ArrayList<ItemPedido>) bundle.getSerializable("itemPedido");
            cliente =(ArrayList<Cliente>) bundle.getSerializable("cliente");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_condicao_pagamento_pedido_listagem, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Condição de Pagamento");
        rv= view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        setHasOptionsMenu(true);


        ControleCondicaoPagamento controleCondicaoPagamento = new ControleCondicaoPagamento(this.getContext());
        CondicaoPagamentoPedidoAdapter condicaoPagamentoPedidoAdapter = new CondicaoPagamentoPedidoAdapter(controleCondicaoPagamento.getAllCondicaoPagamento(),cliente);
        rv.setAdapter(condicaoPagamentoPedidoAdapter);


        return view;
    }




    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
