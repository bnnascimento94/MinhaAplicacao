package com.vullpes.app.view;

import android.curso.minhaaplicacao.R;
import com.vullpes.app.controller.ControleItemCarrinho;
import com.vullpes.app.model.ItemCarrinho;
import com.vullpes.app.view.adapters.ProdutosPedidoCarrinhoAdapter;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import java.util.List;

public class CarrinhoActivity extends AppCompatActivity {
    RecyclerView rv;
    ProdutosPedidoCarrinhoAdapter adapter;
    ControleItemCarrinho controleItemCarrinho;
    List<ItemCarrinho> itens;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Carrinho");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        controleItemCarrinho = new ControleItemCarrinho(this);
        itens  = controleItemCarrinho.getAllItens();

        rv= findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        adapter = new ProdutosPedidoCarrinhoAdapter(itens,true);
        rv.setAdapter(adapter);
    }
}
