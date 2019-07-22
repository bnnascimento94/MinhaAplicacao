package android.curso.minhaaplicacao.view.fragments;

import android.curso.minhaaplicacao.controller.ControlePrazo;
import android.curso.minhaaplicacao.model.PrazosPagamento;
import android.curso.minhaaplicacao.view.adapters.PrazoPagamentoCadastroAdapter;
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
import android.widget.Toast;

import java.util.List;


public class PrazoPagamento extends Fragment {
    EditText prazoPagamento;
    Button confirmarPrazoPagamento;
    View view;
    RecyclerView rv;
    public PrazoPagamento() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static PrazoPagamento newInstance(String param1, String param2) {
        PrazoPagamento fragment = new PrazoPagamento();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
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
        view = inflater.inflate(R.layout.fragment_prazo_pagamento, container, false);

        prazoPagamento = view.findViewById(R.id.txtNomePrazo);
        confirmarPrazoPagamento = view.findViewById(R.id.btnConfirmarPrazoPagamento);

        confirmarPrazoPagamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean dadosValidados = true;
                if(!(prazoPagamento.getText().length()>0)){
                    prazoPagamento.setError("*");
                    prazoPagamento.requestFocus();
                    dadosValidados = false;
                }

                if(dadosValidados){
                    ControlePrazo controlePrazo = new ControlePrazo(getContext());
                    PrazosPagamento prazosPagamento = new PrazosPagamento();
                    prazosPagamento.setNomePrazoPagamento(prazoPagamento.getText().toString());
                    if(controlePrazo.salvar(prazosPagamento)){
                        rv= view.findViewById(R.id.rv);
                        rv.setHasFixedSize(true);
                        LinearLayoutManager llm = new LinearLayoutManager(getContext());
                        rv.setLayoutManager(llm);
                        rv.setAdapter(getAdapter());
                    }

                }else{
                    Toast.makeText(getContext(),"Insira os campos",Toast.LENGTH_LONG).show();

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

    // TODO: Rename method, update argument and hook method into UI event


public PrazoPagamentoCadastroAdapter getAdapter(){
    ControlePrazo controlePrazo = new ControlePrazo(getContext());
    List<PrazosPagamento> prazosPagamentoList = controlePrazo.getAllPrazosPagamento();
    PrazoPagamentoCadastroAdapter prazosPagamentoAdapter = new PrazoPagamentoCadastroAdapter(prazosPagamentoList);

    return prazosPagamentoAdapter;

}




}
