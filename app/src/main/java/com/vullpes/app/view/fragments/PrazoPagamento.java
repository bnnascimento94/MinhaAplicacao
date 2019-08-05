package com.vullpes.app.view.fragments;

import android.content.Context;
import android.content.DialogInterface;
import com.vullpes.app.classes.OnBackPressed;
import com.vullpes.app.controller.ControlePrazo;
import com.vullpes.app.model.PrazosPagamento;
import com.vullpes.app.view.adapters.PrazoPagamentoCadastroAdapter;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.curso.minhaaplicacao.R;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;


public class PrazoPagamento extends Fragment implements OnBackPressed {
    View view;
    RecyclerView rv;
    private AlertDialog alerta;
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
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Prazo Pagamento");


        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View alertLayout = inflater.inflate(R.layout.financeiro_prazo_pagamento, null);
                final EditText txtNomePrazo = alertLayout.findViewById(R.id.txtDias);
                final Button buttonQuestion1 = alertLayout.findViewById(R.id.buttonQuestion1);
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

                buttonQuestion1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setTitle("Informação");
                        builder.setMessage("Insira um título para as parcelas. Ex: Parcela em 10 vezes, À vista e etc. Obs: Para a parcela ser válida deve inserir as datas.");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });
                        alerta = builder.create();
                        alerta.show();

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


    @Override
    public void OnBackPressed() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_fragment, new Graficos()).commit();
    }
}
