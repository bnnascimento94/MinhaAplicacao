package android.curso.minhaaplicacao.view.fragments;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.curso.minhaaplicacao.controller.ControleClientes;
import android.curso.minhaaplicacao.model.Cliente;
import android.curso.minhaaplicacao.view.MainActivity;
import android.curso.minhaaplicacao.view.TelaPrincipalActivity;
import android.curso.minhaaplicacao.view.adapters.ClienteCadastroAdapter;
import android.curso.minhaaplicacao.view.adapters.ClientePedidoAdapter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.curso.minhaaplicacao.R;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;


public class CadastroClienteListagem extends Fragment {
    View view;
    RecyclerView rv;
    Context context;
    ClienteCadastroAdapter adapter;

    public CadastroClienteListagem() {
        // Required empty public constructor
        context = getContext();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cadastro_cliente_listagem, container, false);
        rv= view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        rv.setLayoutManager(llm);
        setHasOptionsMenu(true);

        ControleClientes controle = new ControleClientes(this.getContext());
        adapter = new ClienteCadastroAdapter(controle.listar());
        rv.setAdapter(adapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
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
