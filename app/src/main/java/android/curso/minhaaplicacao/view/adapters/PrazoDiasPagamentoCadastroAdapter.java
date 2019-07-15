package android.curso.minhaaplicacao.view.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.curso.minhaaplicacao.R;
import android.curso.minhaaplicacao.controller.ControleDiasPrazo;
import android.curso.minhaaplicacao.model.PrazosPagamento;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class PrazoDiasPagamentoCadastroAdapter extends RecyclerView.Adapter<PrazoDiasPagamentoCadastroAdapter.PrazoViewHolder>  {
    List<android.curso.minhaaplicacao.model.PrazoDiasPagamento> prazo;
    List<android.curso.minhaaplicacao.model.PrazoDiasPagamento> prazoFiltrado;
    View v;
    private AlertDialog alerta;
    boolean excluir = false;
    public PrazoDiasPagamentoCadastroAdapter(List<android.curso.minhaaplicacao.model.PrazoDiasPagamento> prazo){
        this.prazo = prazo;
        this.prazoFiltrado = prazo;
    }

    @NonNull
    @Override
    public PrazoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.financeiro_cadastro_prazo_dias, viewGroup, false);
        PrazoDiasPagamentoCadastroAdapter.PrazoViewHolder cvh = new PrazoDiasPagamentoCadastroAdapter.PrazoViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final PrazoViewHolder prazoViewHolder, final int i) {
        prazoViewHolder.txtPrazodias.setText(String.valueOf(prazoFiltrado.get(i).getNumeroDias()));
        prazoViewHolder.txtPorcentagem.setText(String.valueOf(prazoFiltrado.get(i).getPorcentagem())+ " %");

        prazoViewHolder.btnDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Atenção");
                builder.setMessage("Deseja Realmente Excluir este Registro?");
                builder.setPositiveButton("Positivo", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        ControleDiasPrazo controller = new ControleDiasPrazo(prazoViewHolder.context);
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




    }

    @Override
    public int getItemCount() {
        return prazoFiltrado.size();
    }



    public interface PrazoCadastroAdapterListener {
        void onContactSelected(PrazosPagamento prazo);
    }


    public static class PrazoViewHolder extends RecyclerView.ViewHolder {

        TextView txtPrazodias;
        TextView txtPorcentagem;
        CardView cli_cv;
        Button btnDeletar;
        public View view;
        Context context;

        PrazoViewHolder(final View itemView) {
            super(itemView);
            this.view = itemView;
            cli_cv = itemView.findViewById(R.id.cli_cv);
            txtPrazodias =  itemView.findViewById(R.id.txtPrazodias);
            btnDeletar = itemView.findViewById(R.id.btnExcluir);
            txtPorcentagem = itemView.findViewById(R.id.txtPorcentagem);
            context = itemView.getContext();
        }
    }
}
