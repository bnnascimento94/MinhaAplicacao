package com.vullpes.app.view.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.curso.minhaaplicacao.R;

import com.vullpes.app.controller.ControleCondicaoPagamento;
import com.vullpes.app.model.CondicoesPagamento;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CondicaoPagamentoCadastroAdapter extends RecyclerView.Adapter<CondicaoPagamentoCadastroAdapter.ProdutoViewHolder>{
    List<CondicoesPagamento> condicaoPagamentos;
    private AlertDialog alerta;
    public String valorTotal = "";

    public CondicaoPagamentoCadastroAdapter(List<CondicoesPagamento> condicao){
        this.condicaoPagamentos = condicao;

    }

    public static class ProdutoViewHolder extends RecyclerView.ViewHolder {
        TextView condicaoPagamento;
        Button excluirCondicaoPagamento;
        ProdutoViewHolder(final View itemView) {
            super(itemView);
            condicaoPagamento = itemView.findViewById(R.id.txtCondicaoPagamento);
            excluirCondicaoPagamento = itemView.findViewById(R.id.btnExcluirCondicao);
        }
    }

    public ProdutoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.financeiro_cadastro_condicao_pagamento, viewGroup, false);
        ProdutoViewHolder pvh = new ProdutoViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ProdutoViewHolder produtoViewHolder,final int i) {
        produtoViewHolder.condicaoPagamento.setText(condicaoPagamentos.get(i).getNomeCondiçãoPagamento());

        produtoViewHolder.excluirCondicaoPagamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Atenção");
                builder.setMessage("Deseja Realmente Excluir este Produto?");
                builder.setPositiveButton("Positivo", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        if(condicaoPagamentos.size() == 1){
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            builder.setTitle("Atenção");
                            builder.setMessage("Esta é a ultima condição de Pagamento cadastrada, deseja confirmar?");
                            builder.setPositiveButton("Positivo", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    ControleCondicaoPagamento controller = new ControleCondicaoPagamento(v.getContext());
                                    if(controller.deletar(condicaoPagamentos.get(i))){
                                        condicaoPagamentos.remove(condicaoPagamentos.get(i));
                                        notifyItemRemoved(i); //seta o elemento que foi excluido
                                        notifyItemRangeChanged(i, condicaoPagamentos.size());
                                        Toast.makeText(v.getContext(),"Deletado com Êxito",Toast.LENGTH_LONG).show();
                                        AppCompatActivity activity = (AppCompatActivity) v.getContext();
                                    }
                                    else{
                                        Toast.makeText(v.getContext(),"Não foi possível deletar",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            builder.setNegativeButton("Negativo", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });
                            alerta = builder.create();
                            alerta.show();

                        }else{
                            ControleCondicaoPagamento controller = new ControleCondicaoPagamento(v.getContext());
                            if(controller.deletar(condicaoPagamentos.get(i))){
                                condicaoPagamentos.remove(condicaoPagamentos.get(i));
                                notifyItemRemoved(i); //seta o elemento que foi excluido
                                notifyItemRangeChanged(i, condicaoPagamentos.size());
                                Toast.makeText(v.getContext(),"Deletado com Êxito",Toast.LENGTH_LONG).show();
                                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                            }
                            else{
                                Toast.makeText(v.getContext(),"Não foi possível deletar",Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                });
                builder.setNegativeButton("Negativo", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
                alerta = builder.create();
                alerta.show();
            }
        });
        produtoViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View alertLayout = inflater.inflate(R.layout.financeiro_condicao_pagamento, null);
                final EditText condicaoPagamento = alertLayout.findViewById(R.id.txtNomeCondicaoPagamento);
                final Button buttonQuestion1 = alertLayout.findViewById(R.id.buttonQuestion1);
                condicaoPagamento.setText(condicaoPagamentos.get(i).getNomeCondiçãoPagamento());
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setTitle("Cadastro Condição Pagamento");
                alert.setView(alertLayout);
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
                        builder.setMessage("Insira a condição de pagamento em que trabalha. Ex: Boleto, Cartão de crédito, Cheque.");
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
                        if(!(condicaoPagamento.getText().length()>0)){
                            condicaoPagamento.setError("*");
                            condicaoPagamento.requestFocus();
                            dadosValidados = false;
                        }

                        if(dadosValidados){
                            CondicoesPagamento condicoesPagamento = new CondicoesPagamento();
                            condicoesPagamento.setIdCondicaoPagamento(condicaoPagamentos.get(i).getIdCondicaoPagamento());
                            condicoesPagamento.setNomeCondiçãoPagamento(condicaoPagamento.getText().toString());
                            ControleCondicaoPagamento controleCondicaoPagamento = new ControleCondicaoPagamento(v.getContext());

                            if(controleCondicaoPagamento.alterar(condicoesPagamento)){
                               Toast.makeText(v.getContext(),"Alteração Realizada com Sucesso", Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                });
                AlertDialog dialog = alert.create();
                dialog.getWindow().setLayout(393, 220); //Controlling width and height.
                dialog.show();

            }
        });

    }




    @Override
    public int getItemCount() {
        return condicaoPagamentos.size();
    }
}
