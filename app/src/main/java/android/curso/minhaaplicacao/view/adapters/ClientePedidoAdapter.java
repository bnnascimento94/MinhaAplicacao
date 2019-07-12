package android.curso.minhaaplicacao.view.adapters;

import android.content.Context;
import android.curso.minhaaplicacao.R;
import android.curso.minhaaplicacao.classes.ImageSaver;
import android.curso.minhaaplicacao.model.Cliente;
import android.curso.minhaaplicacao.view.fragments.ProdutosPedidoListagem;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClientePedidoAdapter extends RecyclerView.Adapter<ClientePedidoAdapter.ClienteViewHolder> implements Filterable {
    List<Cliente> cliente;
    List<Cliente> clienteFiltrado;
    byte[] fotoArray;
    Bitmap raw;



    public ClientePedidoAdapter(List<Cliente> cliente){
        this.cliente = cliente;
        this.clienteFiltrado = cliente;

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
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

    public static class ClienteViewHolder extends RecyclerView.ViewHolder {
        CircleImageView fotoCliente;
        TextView nomeCliente;
        TextView telefoneCliente;
        CardView cli_cv;
        public View view;
        Context context;
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


    @NonNull
    @Override
    public ClientePedidoAdapter.ClienteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pedido_clientes, viewGroup, false);
        ClienteViewHolder cvh = new ClienteViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ClientePedidoAdapter.ClienteViewHolder clienteViewHolder,final int i) {
        clienteViewHolder.nomeCliente.setText(clienteFiltrado.get(i).getNomeCliente());
        clienteViewHolder.telefoneCliente.setText(String.valueOf(clienteFiltrado.get(i).getTelefoneCliente()));
        Bitmap bitmap = new ImageSaver(clienteViewHolder.itemView.getContext()).
                setFileName(clienteFiltrado.get(i).getNomeArquivo()).
                setDirectoryName(clienteFiltrado.get(i).getDiretorioFoto()).
                load();
        if(bitmap!=null){
            clienteViewHolder.fotoCliente.setImageBitmap(bitmap);
        }else{
            clienteViewHolder.fotoCliente.setImageResource(R.drawable.user_no_pic);
        }
        clienteViewHolder.view.setOnClickListener(new View.OnClickListener() {  // <--- here
            @Override
            public void onClick(View v) {

                ArrayList<Cliente> cliente = new ArrayList();
                Cliente cli = new Cliente();
                cli.setNomeCliente(clienteFiltrado.get(i).getNomeCliente());
                cli.setIdCliente(clienteFiltrado.get(i).getIdCliente());
                cliente.add(cli);

                ProdutosPedidoListagem produtosPedidoListagem = new ProdutosPedidoListagem();
                Bundle bundle = new Bundle();
                bundle.putSerializable("cliente",cliente);
                produtosPedidoListagem.setArguments(bundle);

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment myFragment = new Fragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_fragment, produtosPedidoListagem).addToBackStack(null).commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return clienteFiltrado.size();
    }
}
