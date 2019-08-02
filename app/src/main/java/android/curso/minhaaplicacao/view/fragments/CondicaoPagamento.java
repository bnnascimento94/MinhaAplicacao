package android.curso.minhaaplicacao.view.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.curso.minhaaplicacao.classes.OnBackPressed;
import android.curso.minhaaplicacao.controller.ControleCondicaoPagamento;
import android.curso.minhaaplicacao.controller.ControleContasReceber;
import android.curso.minhaaplicacao.model.CondicoesPagamento;
import android.curso.minhaaplicacao.model.ContasReceber;
import android.curso.minhaaplicacao.view.adapters.CondicaoPagamentoCadastroAdapter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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


public class CondicaoPagamento extends Fragment implements OnBackPressed {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    View view;
    RecyclerView rv;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_condicao_pagamento, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Condição Pagamento");
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
                View alertLayout = inflater.inflate(R.layout.financeiro_condicao_pagamento, null);
                final EditText condicaoPagamento = alertLayout.findViewById(R.id.txtNomeCondicaoPagamento);
                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setTitle("Cadastro Condição Pagamento");
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
                                rv.setAdapter(getAdapter());
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

    public CondicaoPagamentoCadastroAdapter getAdapter(){
        ControleCondicaoPagamento controlePrazo = new ControleCondicaoPagamento(getContext());
        List<CondicoesPagamento> prazosPagamentoList = controlePrazo.getAllCondicaoPagamento();
        CondicaoPagamentoCadastroAdapter condicaoPagamentoAdapter = new CondicaoPagamentoCadastroAdapter(prazosPagamentoList);
        return condicaoPagamentoAdapter;
    }


    @Override
    public void OnBackPressed() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_fragment, new Graficos()).commit();
    }
}
