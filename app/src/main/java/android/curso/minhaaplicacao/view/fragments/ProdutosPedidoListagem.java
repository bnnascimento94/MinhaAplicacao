package android.curso.minhaaplicacao.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.curso.minhaaplicacao.controller.ControleItemCarrinho;
import android.curso.minhaaplicacao.controller.ControleProdutos;
import android.curso.minhaaplicacao.model.Cliente;
import android.curso.minhaaplicacao.model.Produto;
import android.curso.minhaaplicacao.view.CarrinhoActivity;
import android.curso.minhaaplicacao.view.TelaPrincipalActivity;
import android.curso.minhaaplicacao.view.adapters.ProdutoCadastroAdapter;
import android.curso.minhaaplicacao.view.adapters.ProdutoPedidoAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.curso.minhaaplicacao.R;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class ProdutosPedidoListagem extends Fragment {

    View view;
    RecyclerView rv;
    Context context;
    Button finalizarPedido;
    ArrayList<Cliente> cliente;
    ProdutoPedidoAdapter adapter;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_produto_pedidos_listagem, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Produtos");
        finalizarPedido = view.findViewById(R.id.btnFinalizarPedido);
        rv= view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        rv.setLayoutManager(llm);
        setHasOptionsMenu(true);

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


}
