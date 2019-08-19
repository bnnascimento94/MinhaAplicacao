package com.vullpes.app.view.adapters;

import android.app.Activity;
import android.content.Context;
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

import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
    private int posicao; //variavel global que seta a posição do elemento para excluir
    Context context;
    private ActionMode mActionMode;

    public ProdutoCadastroAdapter(List<Produto> produto, Context context){
        this.produto = produto;
        this.produtoFiltrado = produto;
        this.context = context;
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
            cv = itemView.findViewById(R.id.cv);
            nomeView =  itemView.findViewById(R.id.txtNomeProduto);
            price = itemView.findViewById(R.id.txtValor);
            fotoView =  itemView.findViewById(R.id.imageProdutoSelecionado);


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

        produtoViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(mActionMode != null){
                    return false;
                }
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
                            ControlePedidos controlePedidos = new ControlePedidos(context);
                            if(!(controlePedidos.getAllItemPedidoByIdProduto(produtoFiltrado.get(posicao).getIdProduto()).size()>0)){
                                ControleProdutos controller = new ControleProdutos(context);
                                if(controller.deletar(produtoFiltrado.get(posicao))){
                                    if(new ImageSaver(context,produtoFiltrado.get(posicao).getDiretorioFoto(),produtoFiltrado.get(posicao).getNomeArquivo()).deleteFile()){
                                        produtoFiltrado.remove(produtoFiltrado.get(posicao));
                                        notifyItemRemoved(posicao); //seta o elemento que foi excluido
                                        notifyItemRangeChanged(posicao, produtoFiltrado.size()); //muda em tela a quantidade de elementos exibidos
                                        produto = produtoFiltrado; // seta os dados para não aparecer os elementos já excluidos
                                        Toast.makeText(context,"Deletado com Êxito",Toast.LENGTH_LONG).show();
                                    }
                                }else{
                                    Toast.makeText(context,"Não foi possível deletar",Toast.LENGTH_LONG).show();
                                }
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
        return produtoFiltrado.size();
    }



}
