package android.curso.minhaaplicacao.view.adapters;

import android.content.DialogInterface;
import android.curso.minhaaplicacao.R;
import android.curso.minhaaplicacao.classes.ImageSaver;
import android.curso.minhaaplicacao.controller.ControleCondicaoPagamento;
import android.curso.minhaaplicacao.controller.ControleItemCarrinho;
import android.curso.minhaaplicacao.model.CondicoesPagamento;
import android.curso.minhaaplicacao.model.ItemCarrinho;
import android.curso.minhaaplicacao.view.fragments.CondicaoPagamento;
import android.curso.minhaaplicacao.view.fragments.Graficos;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

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


        produtoViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
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

        }




    @Override
    public int getItemCount() {
        return condicaoPagamentos.size();
    }
}
