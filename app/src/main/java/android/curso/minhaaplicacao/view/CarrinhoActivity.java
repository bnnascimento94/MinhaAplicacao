package android.curso.minhaaplicacao.view;

import android.curso.minhaaplicacao.R;
import android.curso.minhaaplicacao.controller.ControleItemCarrinho;
import android.curso.minhaaplicacao.model.ItemCarrinho;
import android.curso.minhaaplicacao.view.adapters.ProdutosPedidoCarrinhoAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
