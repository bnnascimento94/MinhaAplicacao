package android.curso.minhaaplicacao.view.fragments;

import android.content.Context;
import android.curso.minhaaplicacao.controller.ControlePrazo;
import android.curso.minhaaplicacao.model.Cliente;
import android.curso.minhaaplicacao.model.CondicoesPagamento;
import android.curso.minhaaplicacao.view.adapters.PrazoPagamentoPedidoAdapter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Selecione o prazo de pagamento do pedido");

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
