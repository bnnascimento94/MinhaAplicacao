package android.curso.minhaaplicacao.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.curso.minhaaplicacao.controller.ControlePedidos;
import android.curso.minhaaplicacao.model.Cliente;
import android.curso.minhaaplicacao.model.ItemPedido;
import android.curso.minhaaplicacao.model.Pedidos;
import android.curso.minhaaplicacao.model.Produto;
import android.curso.minhaaplicacao.view.CarrinhoActivity;
import android.curso.minhaaplicacao.view.ImagemAmpliadaActivity;
import android.curso.minhaaplicacao.view.TelaPrincipalActivity;
import android.curso.minhaaplicacao.view.adapters.PedidosAdapter;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PedidosListagem extends Fragment {
    Context context;
    View view;
    RecyclerView rv;
    FragmentManager fragmentManager;
    Button btnListar;
    PedidosAdapter pedidosAdapter;

    public PedidosListagem() {
        // Required empty public constructor
        context  = getContext();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context  = getContext();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view= inflater.inflate(R.layout.fragment_pedidos_listagem, container, false);
        rv= view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        rv.setLayoutManager(llm);
        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Pedidos");

        ControlePedidos cp = new ControlePedidos(getContext());

        pedidosAdapter = new PedidosAdapter(cp.getAllPedidos());
        rv.setAdapter(pedidosAdapter);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.tela_principal, menu);
        MenuItem item = menu.findItem(R.id.action_search);

        SearchView searchView = new SearchView(((TelaPrincipalActivity) getContext()).getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                pedidosAdapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                pedidosAdapter.getFilter().filter(query);
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
