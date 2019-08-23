package com.vullpes.app.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.curso.minhaaplicacao.R;

import com.vullpes.app.classes.ImageSaver;
import com.vullpes.app.classes.MoneyTextWatcher;
import com.vullpes.app.controller.ControleClientes;
import com.vullpes.app.controller.ControleContasReceber;
import com.vullpes.app.controller.ControlePedidos;
import com.vullpes.app.model.ContasReceber;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ContasReceberAdapter extends RecyclerView.Adapter<ContasReceberAdapter.ContasReceberViewHolder> {
    List<ContasReceber> contasReceber;
    private AlertDialog alerta;
    private ActionMode mActionMode;
    View v;
    private int posicao; //posição do elemento i no array
    Context context;

    public ContasReceberAdapter(List<ContasReceber> contasReceber, Context context){
        this.contasReceber = contasReceber;
        this.context = context;

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
        contasReceberViewHolder.txtNumeroPedido.setText("Nº Pedido: "+String.valueOf(contasReceber.get(i).getPedido().getIdPedido()));

        contasReceberViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View alertLayout = inflater.inflate(R.layout.pagamento_conta_receber, null);
                final EditText editName = alertLayout.findViewById(R.id.editName);
                NumberFormat z = NumberFormat.getCurrencyInstance();
                final EditText editLiquidado = alertLayout.findViewById(R.id.editLiquidado);
                editName.setText(z.format(contasReceber.get(i).getValor()));
                editName.setEnabled(false);
                editLiquidado.setText(z.format(contasReceber.get(i).getValorLiquidado()));
                final Locale mLocale = new Locale("pt", "BR");
                editLiquidado.addTextChangedListener(new MoneyTextWatcher(editLiquidado, mLocale));

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
                        String replaceable = String.format("[%s.\\s]", NumberFormat.getCurrencyInstance(mLocale).getCurrency().getSymbol());
                        final String cleanLiquidado = editLiquidado.getText().toString().replaceAll(replaceable, "").replaceAll(",",".");
                        final ControleContasReceber controleContasReceber = new ControleContasReceber(v.getContext());
                        double valor =Double.parseDouble(cleanLiquidado);
                        if(valor > contasReceber.get(i).getValor()){
                            Toast.makeText(v.getContext(), "O Valor a quitado é superior", Toast.LENGTH_SHORT).show();
                        }
                        else if(valor != contasReceber.get(i).getValor()){
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            builder.setTitle("Atenção");
                            builder.setMessage("O valor inserido não é o valor total da compra, deseja prosseguir?");
                            builder.setPositiveButton("Positivo", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    ContasReceber contaReceber = new ContasReceber();
                                    contaReceber.setIdContaReceber(contasReceber.get(i).getIdContaReceber());
                                    contaReceber.setValor(contasReceber.get(i).getValor());
                                    contaReceber.setValorLiquidado(Double.parseDouble(cleanLiquidado));
                                    contaReceber.setData(contasReceber.get(i).getData());
                                    contaReceber.setPedido(contasReceber.get(i).getPedido());
                                    if(controleContasReceber.alterar(contaReceber)){
                                        Toast.makeText(v.getContext(), "Valor Cadastrado", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            builder.setNegativeButton("Negativo", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });
                            alerta = builder.create();
                            alerta.show();

                        }else{
                            ContasReceber contaReceber = new ContasReceber();
                            contaReceber.setIdContaReceber(contasReceber.get(i).getIdContaReceber());
                            contaReceber.setValor(contasReceber.get(i).getValor());
                            contaReceber.setValorLiquidado(Double.parseDouble(cleanLiquidado));
                            contaReceber.setData(contasReceber.get(i).getData());
                            contaReceber.setPedido(contasReceber.get(i).getPedido());
                            if(controleContasReceber.alterar(contaReceber)){
                                Toast.makeText(v.getContext(), "Valor Cadastrado", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                AlertDialog dialog = alert.create();
                dialog.getWindow().setLayout(393, 220); //Controlling width and height.
                dialog.show();
            }
        });

        contasReceberViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
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
        return contasReceber.size();
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
                            ControleContasReceber controleContasReceber = new ControleContasReceber(context);
                            if(controleContasReceber.deletar(contasReceber.get(posicao))){
                                contasReceber.remove(contasReceber.get(posicao));
                                notifyItemRemoved(posicao); //seta o elemento que foi excluido
                                notifyItemRangeChanged(posicao, contasReceber.size()); //muda em tela a quantidade de elementos exibidos
                                Toast.makeText(context,"Deletado com Êxito",Toast.LENGTH_LONG).show();
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

    public static class ContasReceberViewHolder extends RecyclerView.ViewHolder {
        TextView txtCliente, txtValorConta, txtDataConta, txtNumeroPedido;
        public ContasReceberViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCliente = itemView.findViewById(R.id.txtClienteContaReceber);
            txtValorConta = itemView.findViewById(R.id.txtValorConta);
            txtDataConta = itemView.findViewById(R.id.txtDataConta);
            txtNumeroPedido = itemView.findViewById(R.id.numeroPedido);
        }
    }


}
