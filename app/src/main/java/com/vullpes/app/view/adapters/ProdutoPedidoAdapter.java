package com.vullpes.app.view.adapters;


import android.content.Intent;
import android.curso.minhaaplicacao.R;
import com.vullpes.app.classes.ImageSaver;
import com.vullpes.app.model.ItemPedido;
import com.vullpes.app.model.Produto;
import com.vullpes.app.view.DetalheActivity;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProdutoPedidoAdapter extends RecyclerView.Adapter<ProdutoPedidoAdapter.ProdutoViewHolder> implements Filterable {
    List<Produto> produto;
    List<Produto> produtoFiltrado;
    public ArrayList<ItemPedido> itensPedido = new ArrayList() ;
    ProdutoViewHolder pvh;
    byte[] fotoArray;
    Bitmap raw;
    String teste;

    public ProdutoPedidoAdapter(List<Produto> produto){
       this.produtoFiltrado = produto;
       this.produto = produto;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {

                    produtoFiltrado = produto;
                } else {
                    List<Produto> filteredList = new ArrayList<>();
                    for (Produto row : produto) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getNomeProduto().toLowerCase().contains(charString.toLowerCase()) ||String.valueOf(row.getNomeProduto()).contains(constraint)) {
                            filteredList.add(row);
                        }
                    }

                    produtoFiltrado = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = produtoFiltrado;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                produtoFiltrado = (ArrayList<Produto>) results.values;
                notifyDataSetChanged();

            }
        };

    }
    public static class ProdutoViewHolder extends RecyclerView.ViewHolder {
        TextView nomeView;
        TextView price;
        CircleImageView fotoView;
        CardView cv;
        ProdutoViewHolder(final View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            nomeView =  itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.product_price);
            fotoView =  itemView.findViewById(R.id.product_photo);
        }
    }
    @NonNull
    @Override
    public ProdutoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pedido_produto, viewGroup, false);
        pvh = new ProdutoViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ProdutoViewHolder produtoViewHolder,final int i) {
        NumberFormat z = NumberFormat.getCurrencyInstance();
        produtoViewHolder.nomeView.setText(produtoFiltrado.get(i).getNomeProduto());
        produtoViewHolder.price.setText(z.format(produtoFiltrado.get(i).getValorUnitario()));
        Bitmap bitmap = new ImageSaver(produtoViewHolder.itemView.getContext()).
                setFileName(produtoFiltrado.get(i).getNomeArquivo()).
                setDirectoryName(produtoFiltrado.get(i).getDiretorioFoto()).
                load();
        if(bitmap!=null){
            produtoViewHolder.fotoView.setImageBitmap(bitmap);
        }else{
            produtoViewHolder.fotoView.setImageResource(R.drawable.produto);
        }

        produtoViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Intent intentVaiProFormulario = new Intent(v.getContext(), DetalheActivity.class );
                intentVaiProFormulario.putExtra("produto",produtoFiltrado.get(i));
                activity.startActivity(intentVaiProFormulario);

            }
        });



    }

    @Override
    public int getItemCount() {
        return produtoFiltrado.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}