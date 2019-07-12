package android.curso.minhaaplicacao.view.adapters;

import android.content.DialogInterface;
import android.curso.minhaaplicacao.R;
import android.curso.minhaaplicacao.controller.ControlePedidos;
import android.curso.minhaaplicacao.model.Pedidos;
import android.curso.minhaaplicacao.view.fragments.ProdutosPedidoSelecionado;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PedidosAdapter extends RecyclerView.Adapter<PedidosAdapter.PedidoViewHolder> implements Filterable {
    List<Pedidos> pedidos;
    List<Pedidos> pedidosFiltrados;
    private AlertDialog alerta;
    public PedidosAdapter(List<Pedidos> pedidos){
        this.pedidos = pedidos;
        this.pedidosFiltrados = pedidos;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    pedidosFiltrados = pedidos;
                } else {
                    List<Pedidos> filteredList = new ArrayList<>();
                    for (Pedidos row : pedidos) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getCliente().getNomeCliente().toLowerCase().contains(charString.toLowerCase()) ||String.valueOf(row.getCliente().getNomeCliente()).contains(constraint)) {
                            filteredList.add(row);
                        }
                    }

                    pedidosFiltrados = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = pedidosFiltrados;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                pedidosFiltrados = (ArrayList<Pedidos>) results.values;
                notifyDataSetChanged();

            }
        };

    }

    public static class PedidoViewHolder extends RecyclerView.ViewHolder {
        TextView nomeCliente;
        TextView valorTotal;
        TextView data;
        Button btnListarProdutos, btnExcluirPedidos;
        PedidoViewHolder(final View itemView) {
            super(itemView);
            nomeCliente = itemView.findViewById(R.id.txtCondicaoPagamento);
            valorTotal = itemView.findViewById(R.id.txtValorPedido);
            data = itemView.findViewById(R.id.txtDataPedido);
            btnListarProdutos = itemView.findViewById(R.id.btnListarProdutos);
            btnExcluirPedidos = itemView.findViewById(R.id.btnExcluirCondicao);

        }
    }
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pedido_listagem, viewGroup, false);
        PedidoViewHolder pvh = new PedidoViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final PedidoViewHolder produtoViewHolder,final int i) {
        produtoViewHolder.nomeCliente.setText(pedidosFiltrados.get(i).getCliente().getNomeCliente());
        produtoViewHolder.valorTotal.setText("R$ "+String.valueOf(pedidosFiltrados.get(i).getValorTotal()));
        SimpleDateFormat dataFormatada = new SimpleDateFormat("dd/MM/yyyy");
        produtoViewHolder.data.setText(dataFormatada.format(pedidosFiltrados.get(i).getData()));

        produtoViewHolder.btnListarProdutos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProdutosPedidoSelecionado pps = new ProdutosPedidoSelecionado();
                Bundle bundle = new Bundle();
                bundle.putSerializable("itensPedido",pedidosFiltrados.get(i).getItensPedido());
                pps.setArguments(bundle);


                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment myFragment = new Fragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_fragment, pps).addToBackStack(null).commit();
            }
        });

        produtoViewHolder.btnExcluirPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Atenção");
                builder.setMessage("Deseja Realmente Excluir este Registro?");
                builder.setPositiveButton("Positivo", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        ControlePedidos cp = new ControlePedidos(v.getContext());
                        if(cp.deletar(pedidosFiltrados.get(i))){
                            pedidosFiltrados.remove(pedidosFiltrados.get(i));
                            notifyItemRemoved(i); //seta o elemento que foi excluido
                            notifyItemRangeChanged(i, pedidosFiltrados.size()); //muda em tela a quantidade de elementos exibidos
                            pedidos = pedidosFiltrados; // seta os dados para não aparecer os elementos já excluidos
                            Toast.makeText(v.getContext(),"Deletado com Êxito",Toast.LENGTH_LONG).show();
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
        return pedidosFiltrados.size();
    }

}

