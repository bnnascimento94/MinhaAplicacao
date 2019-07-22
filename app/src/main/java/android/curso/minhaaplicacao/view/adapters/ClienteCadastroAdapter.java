package android.curso.minhaaplicacao.view.adapters;

import android.curso.minhaaplicacao.classes.ImageSaver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.curso.minhaaplicacao.R;
import android.curso.minhaaplicacao.controller.ControleClientes;
import android.curso.minhaaplicacao.model.Cliente;
import android.curso.minhaaplicacao.view.MainActivity;
import android.curso.minhaaplicacao.view.fragments.CadastroCliente;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClienteCadastroAdapter extends RecyclerView.Adapter<ClienteCadastroAdapter.ClienteViewHolder> implements Filterable {
    List<Cliente> cliente;
    List<Cliente> clienteFiltrado;
    byte[] fotoArray;
    Bitmap raw;
    View v;
    private AlertDialog alerta;
    boolean excluir = false;
    public ClienteCadastroAdapter(List<Cliente> cliente){
        this.cliente = cliente;
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

        clienteViewHolder.btnDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Atenção");
                builder.setMessage("Deseja Realmente Excluir este Registro?");
                builder.setPositiveButton("Positivo", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        ControleClientes controller = new ControleClientes(clienteViewHolder.context);
                        if(controller.deletar(clienteFiltrado.get(i))){
                            if(new ImageSaver(v.getContext(),clienteFiltrado.get(i).getDiretorioFoto(),clienteFiltrado.get(i).getNomeArquivo()).deleteFile()){}
                                clienteFiltrado.remove(clienteFiltrado.get(i));
                                notifyItemRemoved(i); //seta o elemento que foi excluido
                                notifyItemRangeChanged(i, clienteFiltrado.size()); //muda em tela a quantidade de elementos exibidos
                                cliente = clienteFiltrado; // seta os dados para não aparecer os elementos já excluidos
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

    public interface ClienteCadastroAdapterListener {
        void onContactSelected(Cliente cliente);
    }


    public static class ClienteViewHolder extends RecyclerView.ViewHolder {
        CircleImageView fotoCliente;
        TextView nomeCliente;
        TextView telefoneCliente;
        CardView cli_cv;
        Button btnDeletar;
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
            btnDeletar = itemView.findViewById(R.id.btnDeletar);
            context = itemView.getContext();

        }
    }
}
