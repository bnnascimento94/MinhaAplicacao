package android.curso.minhaaplicacao.view.adapters;

import android.content.DialogInterface;
import android.curso.minhaaplicacao.R;
import android.curso.minhaaplicacao.controller.ControleCondicaoPagamento;
import android.curso.minhaaplicacao.model.Cliente;
import android.curso.minhaaplicacao.model.CondicoesPagamento;
import android.curso.minhaaplicacao.view.fragments.FinalizarPedido;
import android.curso.minhaaplicacao.view.fragments.PrazoPagamentoPedidoListagem;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CondicaoPagamentoPedidoAdapter extends RecyclerView.Adapter<CondicaoPagamentoPedidoAdapter.ProdutoViewHolder>{
    List<CondicoesPagamento> condicaoPagamentos;
    ArrayList<Cliente> cliente;


    public CondicaoPagamentoPedidoAdapter(List<CondicoesPagamento> condicao, ArrayList<Cliente> cliente){
        this.condicaoPagamentos = condicao;
        this.cliente = cliente;

    }

    public static class ProdutoViewHolder extends RecyclerView.ViewHolder {

        TextView condicaoPagamento;
        ProdutoViewHolder(final View itemView) {
            super(itemView);
            condicaoPagamento = itemView.findViewById(R.id.txtPedidoCondicaoPagamento);

        }

    }

    public ProdutoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pedido_condicao_pagamento, viewGroup, false);
        ProdutoViewHolder pvh = new ProdutoViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ProdutoViewHolder produtoViewHolder,final int i) {

        produtoViewHolder.condicaoPagamento.setText(condicaoPagamentos.get(i).getNomeCondiçãoPagamento());


            produtoViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                PrazoPagamentoPedidoListagem produtosPedidoListagem = new PrazoPagamentoPedidoListagem ();
                Bundle bundle = new Bundle();
                bundle.putSerializable("cliente",cliente);
                bundle.putSerializable("condicao",condicaoPagamentos.get(i));
                produtosPedidoListagem.setArguments(bundle);

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_fragment, produtosPedidoListagem).addToBackStack(null).commit();
            }
        });

    }




    @Override
    public int getItemCount() {
        return condicaoPagamentos.size();
    }
}
