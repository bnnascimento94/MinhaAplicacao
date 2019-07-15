package android.curso.minhaaplicacao.view.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.curso.minhaaplicacao.R;
import android.curso.minhaaplicacao.controller.ControlePrazo;
import android.curso.minhaaplicacao.model.PrazosPagamento;
import android.curso.minhaaplicacao.view.fragments.PrazoDiasPagamento;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
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
    public PrazoPagamentoCadastroAdapter(List<PrazosPagamento> prazo){
        this.prazo = prazo;
        this.prazoFiltrado = prazo;
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

        prazoViewHolder.btnDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Atenção");
                builder.setMessage("Deseja Realmente Excluir este Registro?");
                builder.setPositiveButton("Positivo", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        ControlePrazo controller = new ControlePrazo(prazoViewHolder.context);
                        if(controller.deletar(prazoFiltrado.get(i))){
                                prazoFiltrado.remove(prazoFiltrado.get(i));
                                notifyItemRemoved(i); //seta o elemento que foi excluido
                                notifyItemRangeChanged(i, prazoFiltrado.size()); //muda em tela a quantidade de elementos exibidos
                                prazo = prazoFiltrado; // seta os dados para não aparecer os elementos já excluidos
                                Toast.makeText(v.getContext(),"Deletado com Êxito",Toast.LENGTH_LONG).show();
                        }else{
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
            }
        });

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

    }

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
        Button btnDeletar;
        Button btnAdicionarDatas;
        public View view;
        Context context;
        private AlertDialog alerta;
        PrazoViewHolder(final View itemView) {
            super(itemView);
            this.view = itemView;
            cli_cv = itemView.findViewById(R.id.cli_cv);
            nomePrazo =  itemView.findViewById(R.id.txtPrazo);
            btnDeletar = itemView.findViewById(R.id.btnExcluirPrazo);
            btnAdicionarDatas = itemView.findViewById(R.id.btnAdicionarDatas);
            context = itemView.getContext();

        }
    }
}
