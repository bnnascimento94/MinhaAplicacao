package com.vullpes.app.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.curso.minhaaplicacao.R;

import com.vullpes.app.classes.ImageSaver;
import com.vullpes.app.controller.ControlePrazo;
import com.vullpes.app.model.PrazosPagamento;
import com.vullpes.app.view.fragments.PrazoDiasPagamento;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PrazoPagamentoCadastroAdapter extends RecyclerView.Adapter<PrazoPagamentoCadastroAdapter.PrazoViewHolder> implements Filterable {
    List<PrazosPagamento> prazo;
    List<PrazosPagamento> prazoFiltrado;
    View v;
    private AlertDialog alerta;
    boolean excluir = false;
    private int posicao; //variavel global que seta a posição do elemento para excluir
    Context context;
    private ActionMode mActionMode;
    public PrazoPagamentoCadastroAdapter(List<PrazosPagamento> prazo,Context context){
        this.prazo = prazo;
        this.prazoFiltrado = prazo;
        this.context = context;
    }

    @NonNull
    @Override
    public PrazoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.financeiro_cadastro_prazo_pagamento, viewGroup, false);
        PrazoPagamentoCadastroAdapter.PrazoViewHolder cvh = new PrazoPagamentoCadastroAdapter.PrazoViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final PrazoViewHolder prazoViewHolder, final int i) {
        prazoViewHolder.nomePrazo.setText(prazoFiltrado.get(i).getNomePrazoPagamento());

        prazoViewHolder.btnAdicionarDatas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrazoDiasPagamento prazoDiasPagamento = new PrazoDiasPagamento();
                Bundle bundle = new Bundle();
                bundle.putSerializable("idPrazo",  prazoFiltrado.get(i).getIdPrazoPagamento());
                bundle.putSerializable("prazos", (Serializable) prazoFiltrado.get(i).getPrazosDiasPagamento());
                prazoDiasPagamento.setArguments(bundle);

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_fragment,prazoDiasPagamento).addToBackStack(null).commit();
            }
        });

        prazoViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View alertLayout = inflater.inflate(R.layout.financeiro_prazo_pagamento, null);
                final EditText nomePrazoPagamento = alertLayout.findViewById(R.id.txtDias);
                final Button buttonQuestion1 = alertLayout.findViewById(R.id.buttonQuestion1);
                nomePrazoPagamento.setText(prazoFiltrado.get(i).getNomePrazoPagamento());
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setTitle("Cadastro Nome Prazo de Pagamento");
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
                        if(!(nomePrazoPagamento.getText().length()>0)){
                            nomePrazoPagamento.setError("*");
                            nomePrazoPagamento.requestFocus();
                            dadosValidados = false;
                        }

                        if(dadosValidados){
                            PrazosPagamento prazosPagamento = new PrazosPagamento();
                            prazosPagamento.setIdPrazoPagamento(prazoFiltrado.get(i).getIdPrazoPagamento());
                            prazosPagamento.setNomePrazoPagamento(nomePrazoPagamento.getText().toString());
                            prazosPagamento.setPrazosDiasPagamento(prazoFiltrado.get(i).getPrazosDiasPagamento());
                            ControlePrazo controlePrazo = new ControlePrazo(v.getContext());
                            if(controlePrazo.alterar(prazosPagamento)){
                                Toast.makeText(v.getContext(),"Alteração Realizada com Sucesso", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
                AlertDialog dialog = alert.create();
                dialog.getWindow().setLayout(393, 199); //Controlling width and height.
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
                            ControlePrazo controller = new ControlePrazo(context);
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty() && excluir==false) {
                    prazoFiltrado = prazo;
                } else {
                    List<PrazosPagamento> filteredList = new ArrayList<>();
                    for (PrazosPagamento row : prazo) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getNomePrazoPagamento().toLowerCase().contains(charString.toLowerCase()) ||String.valueOf(row.getNomePrazoPagamento()).contains(constraint)) {
                            filteredList.add(row);
                        }
                    }

                    prazoFiltrado = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = prazoFiltrado;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                prazoFiltrado = (ArrayList<PrazosPagamento>) results.values;
                notifyDataSetChanged();

            }
        };

    }

    public interface PrazoCadastroAdapterListener {
        void onContactSelected(PrazosPagamento prazo);
    }


    public static class PrazoViewHolder extends RecyclerView.ViewHolder {
        TextView nomePrazo;
        CardView cli_cv;
        ImageButton btnAdicionarDatas;
        public View view;
        Context context;
        PrazoViewHolder(final View itemView) {
            super(itemView);
            this.view = itemView;
            cli_cv = itemView.findViewById(R.id.cli_cv);
            nomePrazo =  itemView.findViewById(R.id.txtPrazo);
            btnAdicionarDatas = itemView.findViewById(R.id.btnAdicionarDatas);
            context = itemView.getContext();
        }
    }
}
