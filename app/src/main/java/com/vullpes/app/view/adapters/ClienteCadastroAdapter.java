package com.vullpes.app.view.adapters;

import android.app.Activity;
import android.content.Intent;
import com.vullpes.app.classes.ImageSaver;
import com.vullpes.app.controller.ControlePedidos;
import com.vullpes.app.view.ImagemAmpliadaActivity;
import android.graphics.Bitmap;
import androidx.appcompat.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.curso.minhaaplicacao.R;
import com.vullpes.app.controller.ControleClientes;
import com.vullpes.app.model.Cliente;
import com.vullpes.app.view.fragments.CadastroCliente;
import android.os.Bundle;
import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class ClienteCadastroAdapter extends RecyclerView.Adapter<ClienteCadastroAdapter.ClienteViewHolder> implements Filterable {
    List<Cliente> cliente;
    List<Cliente> clienteFiltrado;
    private ActionMode mActionMode;
    View v;
    private int posicao; //posição do elemento i no array
    Context context;
    private AlertDialog alerta;
    boolean excluir = false;
    public ClienteCadastroAdapter(List<Cliente> cliente, Context context){
        this.cliente = cliente;
        this.context = context;
        this.clienteFiltrado = cliente;
    }

    @NonNull
    @Override
    public ClienteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.clientes_listagem, viewGroup, false);
        ClienteCadastroAdapter.ClienteViewHolder cvh = new ClienteCadastroAdapter.ClienteViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ClienteViewHolder clienteViewHolder, final int i) {
        clienteViewHolder.nomeCliente.setText(clienteFiltrado.get(i).getNomeCliente());
        clienteViewHolder.telefoneCliente.setText(String.valueOf(clienteFiltrado.get(i).getTelefoneCliente()));
        clienteViewHolder.fotoCliente.setImageBitmap(null);

        Bitmap bitmap = new ImageSaver(clienteViewHolder.itemView.getContext()).
                setFileName(clienteFiltrado.get(i).getNomeArquivo()).
                setDirectoryName(clienteFiltrado.get(i).getDiretorioFoto()).
                load();
        if(bitmap!=null){
            clienteViewHolder.fotoCliente.setImageBitmap(bitmap);
        }else{
            clienteViewHolder.fotoCliente.setImageResource(R.drawable.user_no_pic);
        }

        clienteViewHolder.fotoCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentVaiProFormulario = new Intent(v.getContext(), ImagemAmpliadaActivity.class );
                intentVaiProFormulario.putExtra("nomeArquivo",clienteFiltrado.get(i).getNomeArquivo());
                intentVaiProFormulario.putExtra("diretorio",clienteFiltrado.get(i).getDiretorioFoto());
                v.getContext().startActivity(intentVaiProFormulario);
            }
        });


        clienteViewHolder.view.setOnClickListener(new View.OnClickListener() {  // <--- here
            @Override
            public void onClick(View v) {

                ArrayList<Cliente> clientes = new ArrayList();
                clientes.add(clienteFiltrado.get(i));

                CadastroCliente cadastroCliente = new CadastroCliente();
                Bundle bundle = new Bundle();
                bundle.putSerializable("cliente",clientes);
                cadastroCliente.setArguments(bundle);

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment myFragment = new Fragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_fragment, cadastroCliente).addToBackStack(null).commit();

            }
        });



        clienteViewHolder.view.setOnLongClickListener(new View.OnLongClickListener() {

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

    @Override
    public int getItemCount() {
        return clienteFiltrado.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty() && excluir==false) {
                    clienteFiltrado = cliente;
                } else {
                    List<Cliente> filteredList = new ArrayList<>();
                    for (Cliente row : cliente) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getNomeCliente().toLowerCase().contains(charString.toLowerCase()) ||String.valueOf(row.getTelefoneCliente()).contains(constraint)) {
                            filteredList.add(row);
                        }
                    }

                    clienteFiltrado = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = clienteFiltrado;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                clienteFiltrado = (ArrayList<Cliente>) results.values;
                notifyDataSetChanged();

            }
        };

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

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Atenção");
                    builder.setMessage("Deseja Realmente Excluir este Registro?");
                    builder.setPositiveButton("Positivo", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            ControlePedidos controlePedidos = new ControlePedidos(v.getContext());
                            if(!(controlePedidos.getPedidoByIdCliente(clienteFiltrado.get(posicao).getIdCliente()).size()>0)){
                                ControleClientes controller = new ControleClientes(context);
                                if(controller.deletar(clienteFiltrado.get(posicao))){
                                    if(new ImageSaver(v.getContext(),clienteFiltrado.get(posicao).getDiretorioFoto(),clienteFiltrado.get(posicao).getNomeArquivo()).deleteFile()){}
                                    clienteFiltrado.remove(clienteFiltrado.get(posicao));
                                    notifyItemRemoved(posicao); //seta o elemento que foi excluido
                                    notifyItemRangeChanged(posicao, clienteFiltrado.size()); //muda em tela a quantidade de elementos exibidos
                                    cliente = clienteFiltrado; // seta os dados para não aparecer os elementos já excluidos
                                    Toast.makeText(v.getContext(),"Deletado com Êxito",Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(v.getContext(),"Não foi possível deletar",Toast.LENGTH_LONG).show();
                                }

                            }else{
                                Toast.makeText(v.getContext(),"Não foi possível deletar, cliente com vínculos",Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                    builder.setNegativeButton("Negativo", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    });
                    alerta = builder.create();
                    alerta.show();




                    Toast.makeText(context,"teste", Toast.LENGTH_LONG).show();
                    break;

            }


            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
                mActionMode = null;
        }

    };

    public interface ClienteCadastroAdapterListener {
        void onContactSelected(Cliente cliente);
    }


    public static class ClienteViewHolder extends RecyclerView.ViewHolder {
        CircleImageView fotoCliente;
        TextView nomeCliente;
        TextView telefoneCliente;
        CardView cli_cv;
        public View view;
        Context context;
        private AlertDialog alerta;
        ClienteViewHolder(final View itemView) {
            super(itemView);
            this.view = itemView;
            cli_cv = itemView.findViewById(R.id.cli_cv);
            nomeCliente =  itemView.findViewById(R.id.cliente_nome);
            telefoneCliente = itemView.findViewById(R.id.cliente_telefone);
            fotoCliente = itemView.findViewById(R.id.cliente_foto);
            context = itemView.getContext();

        }
    }
}
