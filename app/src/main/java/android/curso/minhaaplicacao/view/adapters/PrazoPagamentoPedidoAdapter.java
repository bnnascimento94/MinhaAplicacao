package android.curso.minhaaplicacao.view.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.curso.minhaaplicacao.R;
import android.curso.minhaaplicacao.controller.ControleDiasPrazo;
import android.curso.minhaaplicacao.controller.ControlePrazo;
import android.curso.minhaaplicacao.model.Cliente;
import android.curso.minhaaplicacao.model.CondicoesPagamento;
import android.curso.minhaaplicacao.model.PrazosPagamento;
import android.curso.minhaaplicacao.view.fragments.FinalizarPedido;
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

public class PrazoPagamentoPedidoAdapter extends RecyclerView.Adapter<PrazoPagamentoPedidoAdapter.PrazoViewHolder> implements Filterable {
    List<PrazosPagamento> prazo;
    List<PrazosPagamento> prazoFiltrado;
    ArrayList<Cliente> cliente;
    CondicoesPagamento condicaoPagamentos;
    View v;
    private AlertDialog alerta;
    boolean excluir = false;
    public PrazoPagamentoPedidoAdapter(List<PrazosPagamento> prazo,ArrayList<Cliente> cliente, CondicoesPagamento condicaoPagamentos ){
        this.prazo = prazo;
        this.prazoFiltrado = prazo;
        this.cliente = cliente;
        this.condicaoPagamentos = condicaoPagamentos;
    }

    @NonNull
    @Override
    public PrazoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pedido_prazo_pagamento, viewGroup, false);
        PrazoPagamentoPedidoAdapter.PrazoViewHolder cvh = new PrazoPagamentoPedidoAdapter.PrazoViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final PrazoViewHolder prazoViewHolder, final int i) {
        prazoViewHolder.nomePrazo.setText(prazoFiltrado.get(i).getNomePrazoPagamento());

        prazoViewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ControleDiasPrazo controleDiasPrazo = new ControleDiasPrazo(v.getContext());
                if(prazoFiltrado.get(i).getPrazosDiasPagamento().size()>0) {
                    FinalizarPedido finalizarPedido = new FinalizarPedido();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("cliente", cliente);
                    bundle.putSerializable("condicaoPagamento", condicaoPagamentos);
                    bundle.putSerializable("prazoPagamento", prazoFiltrado.get(i));
                    finalizarPedido.setArguments(bundle);
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_fragment, finalizarPedido).addToBackStack(null).commit();
                }else{
                    Toast.makeText(v.getContext(),"Não é possível prosseguir com a venda, pois não foram cadastradas as datas",Toast.LENGTH_LONG).show();
                }
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
            nomePrazo =  itemView.findViewById(R.id.txtPedidoPrazoPagamento);

            context = itemView.getContext();

        }
    }
}
