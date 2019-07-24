package android.curso.minhaaplicacao.view.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.curso.minhaaplicacao.R;
import android.curso.minhaaplicacao.controller.ControleContasReceber;
import android.curso.minhaaplicacao.model.ContasReceber;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class ContasReceberAdapter extends RecyclerView.Adapter<ContasReceberAdapter.ContasReceberViewHolder> {
    List<ContasReceber> contasReceber;
    private AlertDialog alerta;


    public ContasReceberAdapter(List<ContasReceber> contasReceber){
        this.contasReceber = contasReceber;

    }

    @NonNull
    @Override
    public ContasReceberViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pagamento_contas_receber_listagem, viewGroup,false);
        ContasReceberViewHolder crv = new ContasReceberViewHolder(v);
        return crv;
    }

    @Override
    public void onBindViewHolder(@NonNull ContasReceberViewHolder contasReceberViewHolder, final int i) {
        NumberFormat z = NumberFormat.getCurrencyInstance();
        contasReceberViewHolder.txtCliente.setText(contasReceber.get(i).getPedido().getCliente().getNomeCliente());
        contasReceberViewHolder.txtValorConta.setText(z.format(contasReceber.get(i).getValor()));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        contasReceberViewHolder.txtDataConta.setText(simpleDateFormat.format(contasReceber.get(i).getData()));

        contasReceberViewHolder.btnQuitar.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(final View v) {
                                             LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                             View alertLayout = inflater.inflate(R.layout.pagamento_conta_receber, null);
                                             final EditText editName = alertLayout.findViewById(R.id.editName);
                                             final EditText editLiquidado = alertLayout.findViewById(R.id.editLiquidado);
                                             editName.setText(String.valueOf(contasReceber.get(i).getValor()));
                                             editName.setEnabled(false);
                                             editLiquidado.setText(String.valueOf(contasReceber.get(i).getValorLiquidado()));

                                             AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                                             alert.setTitle("Quitação de Conta");
                                             // this is set the view from XML inside AlertDialog
                                             alert.setView(alertLayout);
                                             // disallow cancel of AlertDialog on click of back button and outside touch
                                             alert.setCancelable(false);
                                             alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                                 @Override
                                                 public void onClick(DialogInterface dialog, int which) {
                                                     Toast.makeText(v.getContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
                                                 }
                                             });

                                             alert.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                                                 @Override
                                                 public void onClick(DialogInterface dialog, int which) {
                                                     ControleContasReceber controleContasReceber = new ControleContasReceber(v.getContext());
                                                     ContasReceber contaReceber = new ContasReceber();
                                                     contaReceber.setIdContaReceber(contasReceber.get(i).getIdContaReceber());
                                                     contaReceber.setValor(contasReceber.get(i).getValor());
                                                     contaReceber.setValorLiquidado(Double.parseDouble(editLiquidado.getText().toString()));
                                                     contaReceber.setData(contasReceber.get(i).getData());
                                                     contaReceber.setPedido(contasReceber.get(i).getPedido());
                                                     if(controleContasReceber.alterar(contaReceber)){
                                                         Toast.makeText(v.getContext(), "Valor Cadastrado", Toast.LENGTH_SHORT).show();
                                                     }
                                                 }
                                             });
                                             AlertDialog dialog = alert.create();
                                             dialog.show();
                                         }

                                     }
                );

        contasReceberViewHolder.btnDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Atenção");
                builder.setMessage("Deseja Realmente Excluir este Registro?");
                builder.setPositiveButton("Positivo", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        ControleContasReceber controleContasReceber = new ControleContasReceber(v.getContext());
                        if(controleContasReceber.deletar(contasReceber.get(i))){
                            contasReceber.remove(contasReceber.get(i));
                            notifyItemRemoved(i); //seta o elemento que foi excluido
                            notifyItemRangeChanged(i, contasReceber.size()); //muda em tela a quantidade de elementos exibidos
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
        return contasReceber.size();
    }

    public static class ContasReceberViewHolder extends RecyclerView.ViewHolder {
        TextView txtCliente, txtValorConta, txtDataConta;
        Button btnQuitar, btnDeletar;
        public ContasReceberViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCliente = itemView.findViewById(R.id.txtCliente);
            txtValorConta = itemView.findViewById(R.id.txtValorConta);
            txtDataConta = itemView.findViewById(R.id.txtDataConta);
            btnQuitar = itemView.findViewById(R.id.btnQuitar);
            btnDeletar = itemView.findViewById(R.id.btnDeletar);
        }
    }


}
