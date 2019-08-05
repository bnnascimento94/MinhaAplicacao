package com.vullpes.app.view.adapters;

import android.content.DialogInterface;
import android.content.Intent;
import android.curso.minhaaplicacao.R;
import com.vullpes.app.classes.ImageSaver;
import com.vullpes.app.controller.ControlePedidos;
import com.vullpes.app.controller.ControleProdutos;
import com.vullpes.app.model.Produto;
import com.vullpes.app.view.ImagemAmpliadaActivity;
import com.vullpes.app.view.fragments.CadastroProduto;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;

import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProdutoCadastroAdapter extends RecyclerView.Adapter<ProdutoCadastroAdapter.ProdutoViewHolder> implements Filterable {
    List<Produto> produto;
    List<Produto> produtoFiltrado;
    private AlertDialog alerta;
    byte[] fotoArray;
    Bitmap raw;
    public ProdutoCadastroAdapter(List<Produto> produto){
        this.produto = produto;
        this.produtoFiltrado = produto;
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
        Button btnDeletar;
        ProdutoViewHolder(final View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            nomeView =  itemView.findViewById(R.id.txtNomeProduto);
            price = itemView.findViewById(R.id.txtValor);
            fotoView =  itemView.findViewById(R.id.imageProdutoSelecionado);
            btnDeletar = itemView.findViewById(R.id.btnDeletarProduto);

        }

    }

    @Override
    public ProdutoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.produto_cadastro_listagem, viewGroup, false);
        ProdutoViewHolder pvh = new ProdutoViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ProdutoViewHolder produtoViewHolder, final int i) {
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

        produtoViewHolder.fotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentVaiProFormulario = new Intent(v.getContext(), ImagemAmpliadaActivity.class );
                intentVaiProFormulario.putExtra("nomeArquivo",produtoFiltrado.get(i).getNomeArquivo());
                intentVaiProFormulario.putExtra("diretorio",produtoFiltrado.get(i).getDiretorioFoto());
                v.getContext().startActivity(intentVaiProFormulario);

            }
        });


        produtoViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Produto> produtos = new ArrayList();
                produtos.add(produtoFiltrado.get(i));

                CadastroProduto cadastroProduto = new CadastroProduto();
                Bundle bundle = new Bundle();
                bundle.putSerializable("produto",produtos);
                cadastroProduto.setArguments(bundle);

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment myFragment = new Fragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_fragment, cadastroProduto).addToBackStack(null).commit();
            }
        });

        produtoViewHolder.btnDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Atenção");
                builder.setMessage("Deseja Realmente Excluir este Registro?");
                builder.setPositiveButton("Positivo", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        ControlePedidos controlePedidos = new ControlePedidos(v.getContext());
                        if(!(controlePedidos.getAllItemPedidoByIdProduto(produtoFiltrado.get(i).getIdProduto()).size()>0)){
                            ControleProdutos controller = new ControleProdutos(v.getContext());
                            if(controller.deletar(produtoFiltrado.get(i))){
                                if(new ImageSaver(v.getContext(),produtoFiltrado.get(i).getDiretorioFoto(),produtoFiltrado.get(i).getNomeArquivo()).deleteFile()){
                                    produtoFiltrado.remove(produtoFiltrado.get(i));
                                    notifyItemRemoved(i); //seta o elemento que foi excluido
                                    notifyItemRangeChanged(i, produtoFiltrado.size()); //muda em tela a quantidade de elementos exibidos
                                    produto = produtoFiltrado; // seta os dados para não aparecer os elementos já excluidos
                                    Toast.makeText(v.getContext(),"Deletado com Êxito",Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(v.getContext(),"Não foi possível deletar",Toast.LENGTH_LONG).show();
                            }
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
            }
        });
    }

    @Override
    public int getItemCount() {
        return produtoFiltrado.size();
    }



}
