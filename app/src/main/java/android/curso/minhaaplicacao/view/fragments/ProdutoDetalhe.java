package android.curso.minhaaplicacao.view.fragments;


import android.curso.minhaaplicacao.classes.ImageSaver;
import android.curso.minhaaplicacao.controller.ControleItemCarrinho;
import android.curso.minhaaplicacao.model.ItemCarrinho;
import android.curso.minhaaplicacao.model.Produto;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.curso.minhaaplicacao.R;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;


public class ProdutoDetalhe extends Fragment {

    Produto produto = new Produto();
    TextView txtNomeProduto,txtValorUnitario,txtValorVenda;
    EditText editQtde;
    Button btnAdicionarCarrinho;
    ImageView imageViewProduto;
    View view;
    NumberFormat z;
    Locale locale;
    String replaceable;
    ControleItemCarrinho controleItemCarrinho;
    Boolean itemSelecionado;
    List <ItemCarrinho> itemCarrinho;

    public ProdutoDetalhe() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            produto =(Produto) bundle.getSerializable("produto");
        }
        locale = new Locale("pt", "BR");
        replaceable = String.format("[%s.\\s]", NumberFormat.getCurrencyInstance(locale).getCurrency().getSymbol());
        z = NumberFormat.getCurrencyInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_produto_detalhe, container, false);
        txtNomeProduto = view.findViewById(R.id.txtNomeProduto);
        txtValorUnitario = view.findViewById(R.id.txtValorUnitario);
        txtValorVenda = view.findViewById(R.id.txtValorVenda);
        editQtde= view.findViewById(R.id.editQtde);
        btnAdicionarCarrinho = view.findViewById(R.id.btnAdicionarCarrinho);
        imageViewProduto = view.findViewById(R.id.imageViewProduto);
        controleItemCarrinho = new ControleItemCarrinho(getContext());
        itemCarrinho = controleItemCarrinho.getItemCarrinhoByNome(produto.getIdProduto());



        if(itemCarrinho.size()>0){
            itemSelecionado = true;
            txtNomeProduto.setText(produto.getNomeProduto());
            txtValorUnitario.setText(z.format(produto.getValorUnitario()));

            Bitmap bitmap = new ImageSaver(getContext()).
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
            Bitmap bitmap = new ImageSaver(getContext()).
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

        imageViewProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagemAmpliada imagemAmpliada = new ImagemAmpliada();

                Bundle bundle = new Bundle();
                bundle.putSerializable("nomeArquivo",produto.getNomeArquivo());
                bundle.putSerializable("diretorio",produto.getDiretorioFoto());
                imagemAmpliada.setArguments(bundle);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_fragment, imagemAmpliada).commit();
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

        btnAdicionarCarrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                controleItemCarrinho = new ControleItemCarrinho(getContext());

                                if(controleItemCarrinho.alterar(item)){
                                    getFragmentManager().popBackStack();
                                    Toast.makeText(getContext(),"Item inserido no carrinho",Toast.LENGTH_LONG).show();
                                }

                            }else if(i==0){
                                ItemCarrinho item = new ItemCarrinho();
                                item.setIdItemCarrinho(itemCarrinho.get(0).getIdItemCarrinho());
                                item.setQtde(1);
                                item.setProduto(produto);
                                item.setItemValorVenda(Double.parseDouble(txtValorVenda.getText().toString().replaceAll(replaceable, "").replaceAll(",",".")));
                                controleItemCarrinho = new ControleItemCarrinho(getContext());

                                if(controleItemCarrinho.alterar(item)){
                                    getFragmentManager().popBackStack();
                                    Toast.makeText(getContext(),"Item inserido no carrinho",Toast.LENGTH_LONG).show();
                                }

                            }else{
                                Toast.makeText(getContext(),"Insira um numero válido",Toast.LENGTH_LONG).show();
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
                            controleItemCarrinho = new ControleItemCarrinho(getContext());

                            if(controleItemCarrinho.salvar(item)){
                                getFragmentManager().popBackStack();
                                Toast.makeText(getActivity().getApplicationContext(),"Item inserido no carrinho",Toast.LENGTH_LONG).show();
                            }
                        }catch(Exception e){
                            Toast.makeText(getActivity().getApplicationContext(),"Erro ao Setar is Itens do Carrinho",Toast.LENGTH_LONG).show();
                            Log.e("ERRO ->"," "+e);
                        }

                    }
                }
            }
        });



        return view ;
    }


}
