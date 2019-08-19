package com.vullpes.app.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.curso.minhaaplicacao.R;

import com.vullpes.app.classes.ImageSaver;
import com.vullpes.app.controller.ControleCondicaoPagamento;
import com.vullpes.app.model.CondicoesPagamento;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
    private int posicao;
    private ActionMode mActionMode;
    public String valorTotal = "";
    Context context;

    public CondicaoPagamentoCadastroAdapter(List<CondicoesPagamento> condicao, Context context){
        this.condicaoPagamentos = condicao;
        this.context = context;

    }

    public static class ProdutoViewHolder extends RecyclerView.ViewHolder {
        TextView condicaoPagamento;

        ProdutoViewHolder(final View itemView) {
            super(itemView);
            condicaoPagamento = itemView.findViewById(R.id.txtCondicaoPagamento);

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
                produtoViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if(mActionMode != null){
                            return false;
                        }
                        //clienteViewHolder.view.setOnClickListener(null);
                        Activity activity = (Activity) context;
                        posicao = i; //setando numa variavel global o item da listview
                        mActionMode =  activity.startActionMode(mActionModeCallBack);
                        return true;
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

    private ActionMode.Callback mActionModeCallBack = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.delete_menu, menu);
            mode.setTitle("Deletar Item");
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()){
                case R.id.menu_delete:
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Atenção");
                    builder.setMessage("Deseja Realmente Excluir este Produto?");
                    builder.setPositiveButton("Positivo", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            if(condicaoPagamentos.size() == 1){
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Atenção");
                                builder.setMessage("Esta é a ultima condição de Pagamento cadastrada, deseja confirmar?");
                                builder.setPositiveButton("Positivo", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        ControleCondicaoPagamento controller = new ControleCondicaoPagamento(context);
                                        if(controller.deletar(condicaoPagamentos.get(posicao))){
                                            condicaoPagamentos.remove(condicaoPagamentos.get(posicao));
                                            notifyItemRemoved(posicao); //seta o elemento que foi excluido
                                            notifyItemRangeChanged(posicao, condicaoPagamentos.size());
                                            Toast.makeText(context,"Deletado com Êxito",Toast.LENGTH_LONG).show();
                                            AppCompatActivity activity = (AppCompatActivity) context;
                                        }
                                        else{
                                            Toast.makeText(context,"Não foi possível deletar",Toast.LENGTH_LONG).show();
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
                                ControleCondicaoPagamento controller = new ControleCondicaoPagamento(context);
                                if(controller.deletar(condicaoPagamentos.get(posicao))){
                                    condicaoPagamentos.remove(condicaoPagamentos.get(posicao));
                                    notifyItemRemoved(posicao); //seta o elemento que foi excluido
                                    notifyItemRangeChanged(posicao, condicaoPagamentos.size());
                                    Toast.makeText(context,"Deletado com Êxito",Toast.LENGTH_LONG).show();
                                    AppCompatActivity activity = (AppCompatActivity) context;
                                }
                                else{
                                    Toast.makeText(context,"Não foi possível deletar",Toast.LENGTH_LONG).show();
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


                    break;

            }


            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }

    };


    @Override
    public int getItemCount() {
        return condicaoPagamentos.size();
    }
}
