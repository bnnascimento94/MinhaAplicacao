package android.curso.minhaaplicacao.view.fragments;

import android.content.Context;
import android.curso.minhaaplicacao.controller.ControleCondicaoPagamento;
import android.curso.minhaaplicacao.model.CondicoesPagamento;
import android.curso.minhaaplicacao.view.adapters.CondicaoPagamentoCadastroAdapter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.curso.minhaaplicacao.R;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;


public class CondicaoPagamento extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    View view;
    RecyclerView rv;
    EditText condicaoPagamento;
    Button btnConfirmarCondicao;

    public CondicaoPagamento() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_condicao_pagamento, container, false);

        condicaoPagamento = view.findViewById(R.id.txtNomeCondicaoPagamento);
        btnConfirmarCondicao = view.findViewById(R.id.btnConfirmarCondicao);

        btnConfirmarCondicao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean dadosValidados = true;


                if(!(condicaoPagamento.getText().length()>0)){
                    condicaoPagamento.setError("*");
                    condicaoPagamento.requestFocus();
                    dadosValidados = false;
                }

                if(dadosValidados){
                    CondicoesPagamento condicoesPagamento = new CondicoesPagamento();
                    condicoesPagamento.setNomeCondiçãoPagamento(condicaoPagamento.getText().toString());
                    ControleCondicaoPagamento controleCondicaoPagamento = new ControleCondicaoPagamento(getContext());

                    if(controleCondicaoPagamento.salvar(condicoesPagamento)){
                        rv= view.findViewById(R.id.rv);
                        rv.setHasFixedSize(true);
                        LinearLayoutManager llm = new LinearLayoutManager(getContext());
                        rv.setLayoutManager(llm);
                        rv.setAdapter(getAdapter());
                    }
                }

            }
        });

        rv= view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        rv.setAdapter(getAdapter());

        return view;
    }


    public CondicaoPagamentoCadastroAdapter getAdapter(){
        ControleCondicaoPagamento controlePrazo = new ControleCondicaoPagamento(getContext());
        List<CondicoesPagamento> prazosPagamentoList = controlePrazo.getAllCondicaoPagamento();
        CondicaoPagamentoCadastroAdapter condicaoPagamentoAdapter = new CondicaoPagamentoCadastroAdapter(prazosPagamentoList);

        return condicaoPagamentoAdapter;

    }



}
