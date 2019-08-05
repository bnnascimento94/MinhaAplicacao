package com.vullpes.app.view.fragments;


import android.content.Intent;
import com.vullpes.app.classes.OnBackPressed;
import com.vullpes.app.controller.ControleContasReceber;
import com.vullpes.app.controller.ControleItemCarrinho;
import com.vullpes.app.controller.ControlePedidos;
import com.vullpes.app.model.Cliente;
import com.vullpes.app.model.CondicoesPagamento;
import com.vullpes.app.model.ContasReceber;
import com.vullpes.app.model.ItemCarrinho;
import com.vullpes.app.model.ItemPedido;
import com.vullpes.app.model.Pedidos;

import com.vullpes.app.model.PrazosPagamento;
import com.vullpes.app.view.ReciboActivity;
import com.vullpes.app.view.adapters.ProdutosPedidoCarrinhoAdapter;

import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.curso.minhaaplicacao.R;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import com.vullpes.app.model.PrazoDiasPagamento;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FinalizarPedido extends Fragment implements OnBackPressed {
    ArrayList<Cliente> cliente;
    CondicoesPagamento condicaoPagamentos;
    PrazosPagamento prazosPagamento;
    View view;
    NumberFormat z;
    Locale locale;
    String replaceable;
    TextView nomeCliente, txtCondicaoPagamento,txtPrazoPagamento ;
    public TextView valorTotal;
    Button btnFinalizarPedido;
    RecyclerView rv;
    ProdutosPedidoCarrinhoAdapter adapter;
    ControleItemCarrinho controleItemCarrinho;
    List<ItemCarrinho> itens;

    public FinalizarPedido() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Finalize o pedido");
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            cliente =(ArrayList<Cliente>) bundle.getSerializable("cliente");
            condicaoPagamentos = (CondicoesPagamento) bundle.getSerializable("condicaoPagamento") ;
            prazosPagamento = (PrazosPagamento) bundle.getSerializable("prazoPagamento");
        }
        locale = new Locale("pt", "BR");
        replaceable = String.format("[%s.\\s]", NumberFormat.getCurrencyInstance(locale).getCurrency().getSymbol());
        controleItemCarrinho = new ControleItemCarrinho(getContext());
        z = NumberFormat.getCurrencyInstance();
        itens  = controleItemCarrinho.getAllItens();
        startTimerThread();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        itens  = controleItemCarrinho.getAllItens();
        if(itens.size()>0){
            view = inflater.inflate(R.layout.fragment_finalizar_pedido, container, false);
            nomeCliente = view.findViewById(R.id.txtNome);
            valorTotal = view.findViewById(R.id.txtValorTotal);
            btnFinalizarPedido = view.findViewById(R.id.btnConfirmarPedido);
            txtCondicaoPagamento = view.findViewById(R.id.txtCondicaoPagamento);
            txtPrazoPagamento = view.findViewById(R.id.txtPrazoPagamento);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Finalizar Pagamento");

            rv= view.findViewById(R.id.rv);
            rv.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getContext());
            rv.setLayoutManager(llm);
            adapter = new ProdutosPedidoCarrinhoAdapter(itens,false);
            rv.setAdapter(adapter);

            txtCondicaoPagamento.setText(condicaoPagamentos.getNomeCondiçãoPagamento());
            txtPrazoPagamento.setText(prazosPagamento.getNomePrazoPagamento());
            nomeCliente.setText(cliente.get(0).getNomeCliente());

            btnFinalizarPedido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ControlePedidos controlePedido = new ControlePedidos(getContext());
                    Pedidos pedidos = new Pedidos();
                    pedidos.setItensPedido((ArrayList<ItemPedido>) controlePedido.setarItemPedido(itens));
                    pedidos.setCliente(cliente.get(0));
                    String preco = valorTotal.getText().toString().replaceAll(replaceable, "").replaceAll(",",".");;
                    pedidos.setValorTotal(Double.parseDouble(preco));
                    pedidos.setData(new Date());
                    pedidos.setCondicoesPagamento(condicaoPagamentos);
                    pedidos.setPrazosPagamento(prazosPagamento);

                    if(controlePedido.salvar(pedidos)){

                        for(PrazoDiasPagamento prazoDiasPagamento:prazosPagamento.getPrazosDiasPagamento() )
                        {
                            Calendar c = Calendar.getInstance();
                            c.setTime(new Date());
                            c.add(Calendar.DATE, +prazoDiasPagamento.getNumeroDias());

                            Date dataDoUsuario = c.getTime();
                            com.vullpes.app.model.ContasReceber contasReceber = new ContasReceber();
                            contasReceber.setPedido(controlePedido.getLastPedido());
                            contasReceber.setData(dataDoUsuario);
                            contasReceber.setValor(converterDoubleDoisDecimais(pedidos.getValorTotal()*(prazoDiasPagamento.getPorcentagem()/100)));
                            contasReceber.setValorLiquidado(0.00);

                            ControleContasReceber controleContasReceber = new ControleContasReceber(getContext());
                            controleContasReceber.salvar(contasReceber);
                        }

                        ControleItemCarrinho controleItemCarrinho = new ControleItemCarrinho(getContext());
                        controleItemCarrinho.deletarAllItemCarinho();
                        Toast.makeText(getActivity().getApplicationContext(),"Salvo com sucesso", Toast.LENGTH_LONG).show();

                        Intent intentVaiProFormulario = new Intent(getContext(), ReciboActivity.class );
                        intentVaiProFormulario.putExtra("pedido",pedidos);
                        startActivity(intentVaiProFormulario);

                    }
                    else{
                        Toast.makeText(getActivity().getApplicationContext(),"Não foi possível salvar", Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
        else{
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_fragment, new Graficos()).commit();
        }
        return view;

    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);//Salva Activity
    }


    public static double converterDoubleDoisDecimais(double precoDouble) {
        DecimalFormat fmt = new DecimalFormat("0.00");
        String string = fmt.format(precoDouble);
        String[] part = string.split("[,]");
        String string2 = part[0]+"."+part[1];
        //
        double preco = Double.parseDouble(string2);
        return preco;
    }

    private void startTimerThread() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                    while(true) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        handler.post(new Runnable() {
                            public void run() {
                                try{
                                    valorTotal.setText(z.format(controleItemCarrinho.setTotalCarrinho(itens)));
                                }
                                catch(Exception e){
                                    Log.e("Erro total carrinho=>",""+e);
                                }

                            }
                        });
                    }
            }
        };
        new Thread(runnable).start();
    }


    @Override
    public void OnBackPressed() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_fragment, new Graficos()).commit();
    }
}
