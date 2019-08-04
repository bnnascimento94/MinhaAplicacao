package android.curso.minhaaplicacao.view.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.curso.minhaaplicacao.classes.OnBackPressed;
import android.curso.minhaaplicacao.controller.ControleDiasPrazo;
import android.curso.minhaaplicacao.controller.ControleItemCarrinho;
import android.curso.minhaaplicacao.controller.ControlePrazo;
import android.curso.minhaaplicacao.model.PrazosPagamento;
import android.curso.minhaaplicacao.view.adapters.PrazoDiasPagamentoCadastroAdapter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;


public class PrazoDiasPagamento extends Fragment  {
Integer idprazo;
List<android.curso.minhaaplicacao.model.PrazoDiasPagamento> diasPagamentos;
View view;
RecyclerView rv;
TextView porcentagem;
private AlertDialog alerta;

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
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Prazos de Pagamento");
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
                View alertLayout = inflater.inflate(R.layout.financeiro_prazo_dias_pagamento, null);
                final EditText txtDias = alertLayout.findViewById(R.id.txtDias);
                final EditText txtPorcentagem = alertLayout.findViewById(R.id.txtPorcentagem);
                final Button buttonQuestion1 = alertLayout.findViewById(R.id.buttonQuestion1);
                final Button buttonQuestion2 = alertLayout.findViewById(R.id.buttonQuestion2);
                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setTitle("Cadastro Prazo Dias Pagamento");
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
                        builder.setMessage("Insira a porcentagem do total do pagamento deseja para esta data");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        });
                        alerta = builder.create();
                        alerta.show();

                    }
                });

                buttonQuestion2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setTitle("Informação");
                        builder.setMessage("Insira a quantidade de dias a partir da data do pedido para o vencimento da parcela. Ex: Para vencimento em 1 mês digite 30.");
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
                        if(!(txtDias.getText().length()>0)){
                            txtDias.setError("*");
                            txtDias.requestFocus();
                            dadosValidados = false;
                        }
                        else if(!(txtPorcentagem.getText().length()>0)){
                            txtPorcentagem.setError("*");
                            txtPorcentagem.requestFocus();
                            dadosValidados = false;
                        }

                        if(dadosValidados){
                            android.curso.minhaaplicacao.model.PrazoDiasPagamento prazoDiasPagamento = new android.curso.minhaaplicacao.model.PrazoDiasPagamento();
                            prazoDiasPagamento.setIdPrazo(idprazo);
                            prazoDiasPagamento.setNumeroDias(Integer.valueOf(txtDias.getText().toString()));
                            prazoDiasPagamento.setPorcentagem(Double.valueOf(txtPorcentagem.getText().toString()));
                            ControleDiasPrazo controleDiasPrazo = new ControleDiasPrazo(getContext());
                            if(controleDiasPrazo.salvar(prazoDiasPagamento)){
                                rv.setAdapter(getAdapter());
                            }else{
                                Toast.makeText(getContext(),"Não foi possível inserir, porcentagem total maior que 100%", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();
            }
        });


        if(diasPagamentos != null){
            rv= view.findViewById(R.id.rv);
            rv.setHasFixedSize(true);
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

    public void onPause() {
        ControleDiasPrazo controleDiasPrazo = new ControleDiasPrazo(getContext());
        if(controleDiasPrazo.getTotalPorcentagemPrazo(idprazo)<100){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Atenção");
            builder.setMessage("O total da porcentagem dos prazos não chegou a 100%");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {

                }
            });

            alerta = builder.create();
            alerta.show();


        }




        super.onPause();

// add your code here which executes when user leaving the current fragment or fragment is no longer intractable.
    }



}
