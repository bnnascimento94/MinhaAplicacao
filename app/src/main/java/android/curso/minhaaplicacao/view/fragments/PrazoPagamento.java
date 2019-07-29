package android.curso.minhaaplicacao.view.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.curso.minhaaplicacao.controller.ControlePrazo;
import android.curso.minhaaplicacao.model.PrazosPagamento;
import android.curso.minhaaplicacao.view.adapters.PrazoPagamentoCadastroAdapter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.curso.minhaaplicacao.R;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;


public class PrazoPagamento extends Fragment {
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
        rv= view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        rv.setAdapter(getAdapter());


        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View alertLayout = inflater.inflate(R.layout.financeiro_prazo_pagamento, null);
                final EditText txtNomePrazo = alertLayout.findViewById(R.id.txtDias);
                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setTitle("Cadastro Prazo de Pagamento");
                // this is set the view from XML inside AlertDialog
                alert.setView(alertLayout);
                // disallow cancel of AlertDialog on click of back button and outside touch
                alert.setCancelable(false);
                alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean dadosValidados = true;
                        if(!(txtNomePrazo.getText().length()>0)){
                            txtNomePrazo.setError("*");
                            txtNomePrazo.requestFocus();
                            dadosValidados = false;
                        }

                        if(dadosValidados){
                            ControlePrazo controlePrazo = new ControlePrazo(getContext());
                            PrazosPagamento prazosPagamento = new PrazosPagamento();
                            prazosPagamento.setNomePrazoPagamento(txtNomePrazo.getText().toString());
                            if(controlePrazo.salvar(prazosPagamento)){
                                rv.setAdapter(getAdapter());
                                Toast.makeText(view.getContext(),"Inserido com sucesso", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(view.getContext(),"Problemas ao inserir", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();
            }
        });





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
