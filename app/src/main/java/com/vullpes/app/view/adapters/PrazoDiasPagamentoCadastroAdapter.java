package com.vullpes.app.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.curso.minhaaplicacao.R;

import com.vullpes.app.classes.ImageSaver;
import com.vullpes.app.controller.ControleDiasPrazo;
import com.vullpes.app.model.PrazosPagamento;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
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

import com.vullpes.app.model.PrazoDiasPagamento;

import java.util.List;

public class PrazoDiasPagamentoCadastroAdapter extends RecyclerView.Adapter<PrazoDiasPagamentoCadastroAdapter.PrazoViewHolder>  {
    List<PrazoDiasPagamento> prazo;
    List<PrazoDiasPagamento> prazoFiltrado;
    View v;
    private AlertDialog alerta;
    private int posicao; //variavel global que seta a posição do elemento para excluir
    Context context;
    private ActionMode mActionMode;
    public PrazoDiasPagamentoCadastroAdapter(List<PrazoDiasPagamento> prazo, Context context){
        this.prazo = prazo;
        this.prazoFiltrado = prazo;
        this.context = context;
    }

    @NonNull
    @Override
    public PrazoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.financeiro_cadastro_prazo_dias, viewGroup, false);
        PrazoDiasPagamentoCadastroAdapter.PrazoViewHolder cvh = new PrazoDiasPagamentoCadastroAdapter.PrazoViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final PrazoViewHolder prazoViewHolder, final int i) {
        prazoViewHolder.txtPrazodias.setText(String.valueOf(prazoFiltrado.get(i).getNumeroDias())+" Dias");
        prazoViewHolder.txtPorcentagem.setText(String.valueOf(prazoFiltrado.get(i).getPorcentagem())+ " %");

        prazoViewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View alertLayout = inflater.inflate(R.layout.financeiro_prazo_dias_pagamento, null);
                final EditText txtDias = alertLayout.findViewById(R.id.txtDias);
                final EditText txtPorcentagem = alertLayout.findViewById(R.id.txtPorcentagem);
                final Button buttonQuestion1 = alertLayout.findViewById(R.id.buttonQuestion1);
                final Button buttonQuestion2 = alertLayout.findViewById(R.id.buttonQuestion2);
                txtDias.setText(String.valueOf(prazoFiltrado.get(i).getNumeroDias()));
                txtPorcentagem.setText(String.valueOf(prazoFiltrado.get(i).getPorcentagem()));
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setTitle("Cadastro Prazo Dias Pagamento");
                // this is set the view from XML inside AlertDialog
                alert.setView(alertLayout);

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
                        builder.setTitle("Atenção");
                        builder.setMessage("Insira a quantidade de dias a partir do pagamento que deseja para esta parcela");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });

                        alerta = builder.create();
                        alerta.show();
                    }
                });
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
                            PrazoDiasPagamento prazoDiasPagamento = new PrazoDiasPagamento();
                            prazoDiasPagamento.setIdPrazo(prazoFiltrado.get(i).getIdPrazoDias());
                            prazoDiasPagamento.setIdPrazo(prazoFiltrado.get(i).getIdPrazo());
                            prazoDiasPagamento.setNumeroDias(Integer.valueOf(txtDias.getText().toString()));
                            prazoDiasPagamento.setPorcentagem(Double.valueOf(txtPorcentagem.getText().toString()));
                            ControleDiasPrazo controleDiasPrazo = new ControleDiasPrazo(v.getContext());
                            if(controleDiasPrazo.alterar(prazoDiasPagamento)){
                                Toast.makeText(v.getContext(),"Não foi possível alterar, porcentagem total maior que 100%", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(v.getContext(),"Não foi possível alterar, porcentagem total maior que 100%", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
                AlertDialog dialog = alert.create();
                dialog.getWindow().setLayout(393, 220); //Controlling width and height.
                dialog.show();

            }
        });

        prazoViewHolder.view.setOnLongClickListener(new View.OnLongClickListener() {
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
                    builder.setMessage("Deseja Realmente Excluir este Registro?");
                    builder.setPositiveButton("Positivo", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            ControleDiasPrazo controller = new ControleDiasPrazo(context);
                            if(controller.deletar(prazoFiltrado.get(posicao))){
                                prazoFiltrado.remove(prazoFiltrado.get(posicao));
                                notifyItemRemoved(posicao); //seta o elemento que foi excluido
                                notifyItemRangeChanged(posicao, prazoFiltrado.size()); //muda em tela a quantidade de elementos exibidos
                                prazo = prazoFiltrado; // seta os dados para não aparecer os elementos já excluidos
                                Toast.makeText(context,"Deletado com Êxito",Toast.LENGTH_LONG).show();
                            }else{
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
        return prazoFiltrado.size();
    }

    public interface PrazoCadastroAdapterListener {
        void onContactSelected(PrazosPagamento prazo);
    }


    public static class PrazoViewHolder extends RecyclerView.ViewHolder {

        TextView txtPrazodias;
        TextView txtPorcentagem;
        CardView cli_cv;
        public View view;
        Context context;

        PrazoViewHolder(final View itemView) {
            super(itemView);
            this.view = itemView;
            cli_cv = itemView.findViewById(R.id.cli_cv);
            txtPrazodias =  itemView.findViewById(R.id.txtPrazodias);
            txtPorcentagem = itemView.findViewById(R.id.txtPorcentagem);
            context = itemView.getContext();
        }
    }
}
