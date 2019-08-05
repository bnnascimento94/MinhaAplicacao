package com.vullpes.app.view;

import android.content.Intent;
import android.curso.minhaaplicacao.R;
import com.vullpes.app.classes.ImageSaver;
import com.vullpes.app.controller.ControleItemCarrinho;
import com.vullpes.app.model.ItemCarrinho;
import com.vullpes.app.model.Produto;
import android.graphics.Bitmap;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DetalheActivity extends AppCompatActivity {

    Produto produto = new Produto();
    TextView txtNomeProduto,txtValorUnitario,txtValorVenda;
    EditText editQtde;
    Button btnAdicionarCarrinho;
    ImageView imageViewProduto;
    NumberFormat z;
    Locale locale;
    String replaceable;
    ControleItemCarrinho controleItemCarrinho;
    Boolean itemSelecionado;
    List<ItemCarrinho> itemCarrinho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe);
        txtNomeProduto = findViewById(R.id.txtNomeProduto);
        txtValorUnitario = findViewById(R.id.txtValorUnitario);
        txtValorVenda = findViewById(R.id.txtValorVenda);
        editQtde= findViewById(R.id.editQtde);
        imageViewProduto = findViewById(R.id.imageViewProduto);

        txtValorUnitario.setEnabled(false);
        txtValorVenda.setEnabled(false);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        locale = new Locale("pt", "BR");
        replaceable = String.format("[%s.\\s]", NumberFormat.getCurrencyInstance(locale).getCurrency().getSymbol());
        z = NumberFormat.getCurrencyInstance();

        Intent intent = getIntent();
        produto = (Produto) intent.getSerializableExtra("produto");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        controleItemCarrinho = new ControleItemCarrinho(this);
        itemCarrinho = controleItemCarrinho.getItemCarrinhoByNome(produto.getIdProduto());

       getSupportActionBar().setTitle("Detalhes Produto");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editQtde.getText().length()>0){
                    if(itemSelecionado){
                        try{
                            int i =Integer.parseInt(editQtde.getText().toString());

                            if(i>0){
                                ItemCarrinho item = new ItemCarrinho();
                                item.setIdItemCarrinho(itemCarrinho.get(0).getIdItemCarrinho());
                                item.setQtde(Integer.parseInt(editQtde.getText().toString()));
                                item.setProduto(produto);
                                item.setItemValorVenda(Double.parseDouble(txtValorVenda.getText().toString().replaceAll(replaceable, "").replaceAll(",",".")));
                                controleItemCarrinho = new ControleItemCarrinho(view.getContext());

                                if(controleItemCarrinho.alterar(item)){
                                    getFragmentManager().popBackStack();
                                    Snackbar.make(view, "Item inserido no carrinho", Snackbar.LENGTH_LONG).show();
                                }

                            }else if(i==0){
                                ItemCarrinho item = new ItemCarrinho();
                                item.setIdItemCarrinho(itemCarrinho.get(0).getIdItemCarrinho());
                                item.setQtde(1);
                                item.setProduto(produto);
                                item.setItemValorVenda(Double.parseDouble(txtValorVenda.getText().toString().replaceAll(replaceable, "").replaceAll(",",".")));
                                controleItemCarrinho = new ControleItemCarrinho(view.getContext());

                                if(controleItemCarrinho.alterar(item)){
                                    getFragmentManager().popBackStack();
                                    Snackbar.make(view, "Item inserido no carrinho", Snackbar.LENGTH_LONG).show();
                                }

                            }else{
                                Snackbar.make(view, "Insira um numero válido", Snackbar.LENGTH_LONG).show();
                            }


                        }catch(Exception e){
                            Log.e("ERRO ->"," "+e);
                        }
                    }
                    else{
                        try{
                            ItemCarrinho item = new ItemCarrinho();
                            item.setQtde(Integer.parseInt(editQtde.getText().toString()));
                            item.setProduto(produto);
                            item.setItemValorVenda((Double.parseDouble(txtValorVenda.getText().toString().replaceAll(replaceable, "").replaceAll(",","."))));
                            controleItemCarrinho = new ControleItemCarrinho(view.getContext());

                            if(!(controleItemCarrinho.getItemCarrinhoByNome(produto.getIdProduto()).size()>0)){
                                if(controleItemCarrinho.salvar(item)){
                                    getFragmentManager().popBackStack();
                                    Snackbar.make(view, "Item inserido no carrinho", Snackbar.LENGTH_LONG).show();

                                }

                            }else{

                                Snackbar.make(view, "Produto Já cadastrado", Snackbar.LENGTH_LONG).show();
                            }



                        }catch(Exception e){
                            Snackbar.make(view, "Erro ao Setar is Itens do Carrinho", Snackbar.LENGTH_LONG).show();
                            Log.e("ERRO ->"," "+e);
                        }

                    }
                }
            }
        });

        editQtde.addTextChangedListener(new TextWatcher() {
            double valorProduto;



            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(editQtde.isFocused()){
                    if(count ==0){ //verifica se é a primeira vez
                        String preco = txtValorUnitario.getText().toString().replaceAll(replaceable, "").replaceAll(",",".");
                        valorProduto = Double.parseDouble(preco);
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int quantidade;
                if(editQtde.isFocused()){
                    if(editQtde.getText().toString().equals("")){
                        quantidade = 1;
                        double valor_venda = quantidade * valorProduto;
                        txtValorVenda.setText(z.format(valor_venda));

                    }
                    else{
                        quantidade = Integer.parseInt(editQtde.getText().toString());
                        String preco = txtValorUnitario.getText().toString().replaceAll(replaceable, "").replaceAll(",",".");
                        double productPrice = Double.parseDouble(preco);
                        double valor_produto = quantidade * productPrice;
                        txtValorVenda.setText(z.format(valor_produto));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        preencherDetalhes();
    }

    private void preencherDetalhes() {

        if(itemCarrinho.size()>0){
            itemSelecionado = true;
            txtNomeProduto.setText(produto.getNomeProduto());
            txtValorUnitario.setText(z.format(produto.getValorUnitario()));

            Bitmap bitmap = new ImageSaver(this).
                    setFileName(produto.getNomeArquivo()).
                    setDirectoryName(produto.getDiretorioFoto()).
                    load();
            if(bitmap!=null){
                imageViewProduto.setImageBitmap(bitmap);
            }else{
                imageViewProduto.setImageResource(R.drawable.produto);
            }

            editQtde.setText(String.valueOf(itemCarrinho.get(0).getQtde()));
            txtValorVenda.setText(z.format(itemCarrinho.get(0).getItemValorVenda()));

        }else{
            itemSelecionado = false;
            txtNomeProduto.setText(produto.getNomeProduto());
            txtValorUnitario.setText(z.format(produto.getValorUnitario()));
            Bitmap bitmap = new ImageSaver(this).
                    setFileName(produto.getNomeArquivo()).
                    setDirectoryName(produto.getDiretorioFoto()).
                    load();
            if(bitmap!=null){
                imageViewProduto.setImageBitmap(bitmap);
            }else{
                imageViewProduto.setImageResource(R.drawable.produto);
            }
            editQtde.setText("");
            txtValorVenda.setText("");


        }


        //TextView textoTextView = (TextView) findViewById(R.id.detalhe_texto);

        //textoTextView.setText(nota.getTexto());
        //imagemImageView.setImageDrawable(nota.getImagem());

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Detalhe Produto");
    }
    }

