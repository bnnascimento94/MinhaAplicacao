package android.curso.minhaaplicacao.view.fragments;


import android.content.Intent;
import android.curso.minhaaplicacao.controller.ControleContasReceber;
import android.curso.minhaaplicacao.controller.ControleItemCarrinho;
import android.curso.minhaaplicacao.controller.ControlePedidos;
import android.curso.minhaaplicacao.model.Cliente;
import android.curso.minhaaplicacao.model.CondicoesPagamento;
import android.curso.minhaaplicacao.model.ContasReceber;
import android.curso.minhaaplicacao.model.ItemCarrinho;
import android.curso.minhaaplicacao.model.ItemPedido;
import android.curso.minhaaplicacao.model.Pedidos;

import android.curso.minhaaplicacao.model.PrazosPagamento;
import android.curso.minhaaplicacao.view.ImagemAmpliadaActivity;
import android.curso.minhaaplicacao.view.ReciboActivity;
import android.curso.minhaaplicacao.view.adapters.ProdutosPedidoCarrinhoAdapter;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.curso.minhaaplicacao.R;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FinalizarPedido extends Fragment {
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

            rv= view.findViewById(R.id.rv);
            rv.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getContext());
            rv.setLayoutManager(llm);
            adapter = new ProdutosPedidoCarrinhoAdapter(itens);
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

                        for(android.curso.minhaaplicacao.model.PrazoDiasPagamento prazoDiasPagamento:prazosPagamento.getPrazosDiasPagamento() )
                        {
                            Calendar c = Calendar.getInstance();
                            c.setTime(new Date());
                            c.add(Calendar.DATE, +prazoDiasPagamento.getNumeroDias());

                            Date dataDoUsuario = c.getTime();
                            ContasReceber contasReceber = new ContasReceber();
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


}
