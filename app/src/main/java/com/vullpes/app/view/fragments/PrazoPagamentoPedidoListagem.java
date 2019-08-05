package com.vullpes.app.view.fragments;

import com.vullpes.app.controller.ControlePrazo;
import com.vullpes.app.model.Cliente;
import com.vullpes.app.model.CondicoesPagamento;
import com.vullpes.app.view.adapters.PrazoPagamentoPedidoAdapter;
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


public class PrazoPagamentoPedidoListagem extends Fragment {

    ArrayList<Cliente> cliente;
    CondicoesPagamento condicoesPagamento;
    View view;
    RecyclerView rv;
    public PrazoPagamentoPedidoListagem() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            cliente =(ArrayList<Cliente>) bundle.getSerializable("cliente");
            condicoesPagamento =(CondicoesPagamento) bundle.getSerializable("condicao");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_prazo_pagamento_pedido_listagem, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Prazo de Pagamento");

        rv= view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        setHasOptionsMenu(true);

        ControlePrazo controlePrazo = new ControlePrazo(this.getContext());
        PrazoPagamentoPedidoAdapter prazoPagamentoPedidoAdapter = new PrazoPagamentoPedidoAdapter(controlePrazo.getPrazosByDates(),cliente,condicoesPagamento);
        rv.setAdapter(prazoPagamentoPedidoAdapter);
        return view;
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
