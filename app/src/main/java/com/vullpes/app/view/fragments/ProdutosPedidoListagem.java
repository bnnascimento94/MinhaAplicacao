package com.vullpes.app.view.fragments;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.vullpes.app.controller.ControleItemCarrinho;
import com.vullpes.app.controller.ControleProdutos;
import com.vullpes.app.model.Cliente;
import com.vullpes.app.model.ItemCarrinho;
import com.vullpes.app.view.CarrinhoActivity;
import com.vullpes.app.view.TelaPrincipalActivity;
import com.vullpes.app.view.adapters.ProdutoPedidoAdapter;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.curso.minhaaplicacao.R;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ProdutosPedidoListagem extends Fragment {

    View view;
    RecyclerView rv;
    Context context;
    Button finalizarPedido;
    TextView valorTotal;
    ArrayList<Cliente> cliente;
    ProdutoPedidoAdapter adapter;
    List<ItemCarrinho> itens;
    private AdView mAdView;
    ControleItemCarrinho controleItemCarrinho;
    NumberFormat z;
    Locale locale;
    String replaceable;
    public ProdutosPedidoListagem() {
        // Required empty public constructor
        context = getContext();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            cliente =(ArrayList<Cliente>) bundle.getSerializable("cliente");
        }
        locale = new Locale("pt", "BR");
        replaceable = String.format("[%s.\\s]", NumberFormat.getCurrencyInstance(locale).getCurrency().getSymbol());
        controleItemCarrinho = new ControleItemCarrinho(getContext());
        z = NumberFormat.getCurrencyInstance();
        itens  = controleItemCarrinho.getAllItens();
        startTimerThread();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_produto_pedidos_listagem, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Produtos");
        valorTotal = view.findViewById(R.id.txtValorTotal);
        finalizarPedido = view.findViewById(R.id.btnFinalizarPedido);
        rv= view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        rv.setLayoutManager(llm);
        setHasOptionsMenu(true);

        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        ControleProdutos controle = new ControleProdutos(this.getContext());
        adapter = new ProdutoPedidoAdapter(controle.listar());
        rv.setAdapter(adapter);

        finalizarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ControleItemCarrinho controleItemCarrinho = new ControleItemCarrinho(getContext());
               if(controleItemCarrinho.temRegistro()){
                   CondicaoPagamentoPedidoListagem condicaoPagamentoPedidoListagem = new CondicaoPagamentoPedidoListagem();
                   Bundle bundle = new Bundle();
                   bundle.putSerializable("cliente",cliente);
                   condicaoPagamentoPedidoListagem.setArguments(bundle);

                   FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                   fragmentManager.beginTransaction().replace(R.id.content_fragment, condicaoPagamentoPedidoListagem).commit();

               }else{
                   Toast.makeText(getActivity().getApplicationContext(),"Selecione os produtos!", Toast.LENGTH_SHORT).show();
               }

            }
        });

        return view;
    }





    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.tela_principal, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        MenuItem item1 = menu.findItem(R.id.action_chart);
        SearchView searchView = new SearchView(((TelaPrincipalActivity) getContext()).getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setShowAsAction(item1, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, searchView);
        MenuItemCompat.setActionView(item1,null);

        // listening to search query text change
        item1.setVisible(true);
        item1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intentVaiProFormulario = new Intent(getContext(), CarrinhoActivity.class );
                startActivity(intentVaiProFormulario);
                return false;
            }
        });

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapter.getFilter().filter(query);

                return true;
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);//Salva Activity
    }


    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);//Restaura o Activity

    }

    @Override
    public void onResume() {
        super.onResume();
        startTimerThread();
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
                                controleItemCarrinho = new ControleItemCarrinho(getContext());
                                itens  = controleItemCarrinho.getAllItens();
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
