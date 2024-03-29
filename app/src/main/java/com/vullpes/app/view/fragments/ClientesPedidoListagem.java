package com.vullpes.app.view.fragments;

import android.content.Context;

import com.vullpes.app.controller.ControleClientes;
import com.vullpes.app.view.TelaPrincipalActivity;
import com.vullpes.app.view.adapters.ClientePedidoAdapter;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.curso.minhaaplicacao.R;

public class ClientesPedidoListagem extends Fragment {
    View view;
    RecyclerView rv;
    Context context;
    ClientePedidoAdapter adapter;

    public ClientesPedidoListagem() {
        // Required empty public constructor
        context = getContext();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_clientes_pedido_listagem, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Selecione o Cliente");
        rv= view.findViewById(R.id.rvc);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        rv.setLayoutManager(llm);
        setHasOptionsMenu(true);

        ControleClientes controle = new ControleClientes(this.getContext());
        adapter = new ClientePedidoAdapter(controle.listar());


        rv.setAdapter(adapter);


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
