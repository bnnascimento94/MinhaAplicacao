package android.curso.minhaaplicacao.view.fragments;

import android.curso.minhaaplicacao.controller.ControleDiasPrazo;
import android.curso.minhaaplicacao.view.adapters.PrazoDiasPagamentoCadastroAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.curso.minhaaplicacao.R;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class PrazoDiasPagamento extends Fragment {
Integer idprazo;
List<android.curso.minhaaplicacao.model.PrazoDiasPagamento> diasPagamentos;
View view;
RecyclerView rv;
TextView dias, porcentagem;
Button btnInserirPrazos;


    public PrazoDiasPagamento() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static PrazoDiasPagamento newInstance(String param1, String param2) {
        PrazoDiasPagamento fragment = new PrazoDiasPagamento();
        Bundle args = new Bundle();


        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            diasPagamentos =(List<android.curso.minhaaplicacao.model.PrazoDiasPagamento>) bundle.getSerializable("prazos");
            idprazo = (Integer) bundle.getSerializable("idPrazo");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_prazo_dias_pagamento, container, false);
        dias = view.findViewById(R.id.txtDias);
        porcentagem = view.findViewById(R.id.txtPorcentagem);
        btnInserirPrazos = view.findViewById(R.id.btnInserirPrazos);

        btnInserirPrazos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean dadosValidados = true;


                if(!(dias.getText().length()>0)){
                    dias.setError("*");
                    dias.requestFocus();
                    dadosValidados = false;
                }
                else if(!(porcentagem.getText().length()>0)){
                    porcentagem.setError("*");
                    porcentagem.requestFocus();
                    dadosValidados = false;
                }


                if(dadosValidados){
                    android.curso.minhaaplicacao.model.PrazoDiasPagamento prazoDiasPagamento = new android.curso.minhaaplicacao.model.PrazoDiasPagamento();
                    prazoDiasPagamento.setIdPrazo(idprazo);
                    prazoDiasPagamento.setNumeroDias(Integer.valueOf(dias.getText().toString()));
                    prazoDiasPagamento.setPorcentagem(Double.valueOf(porcentagem.getText().toString()));
                    ControleDiasPrazo controleDiasPrazo = new ControleDiasPrazo(getContext());
                    if(controleDiasPrazo.salvar(prazoDiasPagamento)){
                        rv= view.findViewById(R.id.rv);
                        rv.setHasFixedSize(true);
                        LinearLayoutManager llm = new LinearLayoutManager(getContext());
                        rv.setLayoutManager(llm);
                        rv.setAdapter(getAdapter());
                    }else{
                        Toast.makeText(getContext(),"Não foi possível inserir, porcentagem total maior que 100%", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getContext(), "Insira os Campos",Toast.LENGTH_LONG).show();

                }


            }
        });




        if(diasPagamentos != null){
            rv= view.findViewById(R.id.rv);
            rv.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getContext());
            rv.setLayoutManager(llm);
            rv.setAdapter(getAdapter());
        }




        return view;
    }

    public PrazoDiasPagamentoCadastroAdapter getAdapter(){
        ControleDiasPrazo controlePrazo = new ControleDiasPrazo(getContext());
        List<android.curso.minhaaplicacao.model.PrazoDiasPagamento> prazosPagamentoList = controlePrazo.getPrazoDiasPagamento(idprazo);
        PrazoDiasPagamentoCadastroAdapter prazosPagamentoAdapter = new PrazoDiasPagamentoCadastroAdapter(prazosPagamentoList);
        return prazosPagamentoAdapter;
    }




}
