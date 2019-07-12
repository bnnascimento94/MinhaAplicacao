package android.curso.minhaaplicacao.controller;

import android.content.ContentValues;
import android.content.Context;
import android.curso.minhaaplicacao.dataModel.ClienteDataModel;
import android.curso.minhaaplicacao.dataModel.ProdutoDataModel;
import android.curso.minhaaplicacao.dataSource.DataSource;
import android.curso.minhaaplicacao.model.Cliente;
import android.curso.minhaaplicacao.model.Produto;
import android.util.Log;

import java.util.List;

public class ControleProdutos extends DataSource {
    ContentValues dados;
    public ControleProdutos(Context context) {
        super(context);
    }

    public boolean salvar(Produto obj){
        boolean sucesso = false;
        try{
            List<Produto> produto = getAllProdutosByName(obj.getNomeProduto());
            if(!(produto.size()>0)){
                dados = new ContentValues();
                dados.put(ProdutoDataModel.getNomeProduto(),obj.getNomeProduto());
                dados.put(ProdutoDataModel.getCustoProduto(),obj.getCustoProduto());
                dados.put(ProdutoDataModel.getValorUnitario(),obj.getValorUnitario());
                dados.put(ClienteDataModel.getDiretorioFoto(),obj.getDiretorioFoto());
                dados.put(ClienteDataModel.getNomeFoto(),obj.getNomeArquivo());
                sucesso= insert(ProdutoDataModel.getTabela(),dados);
            }

        }catch(Exception e){

            Log.i("Erro -> Insert",""+e);
        }


        return sucesso;
    }
    public boolean deletar(Produto obj){
        boolean sucesso = false;
        if(!(getAllItemPedidoByIdProduto(obj.getIdProduto()).size()>0)){
            try{
                sucesso = deletar(ProdutoDataModel.getTabela(),obj.getIdProduto());
            }catch(Exception e){

                Log.i("Erro -> Delete",""+e);
            }

        }

        return sucesso;
    }
    public boolean alterar(Produto obj){
        boolean sucesso = true;
        try{
            dados = new ContentValues();
            dados.put(ProdutoDataModel.getid(),obj.getIdProduto());
            dados.put(ProdutoDataModel.getNomeProduto(),obj.getNomeProduto());
            dados.put(ProdutoDataModel.getCustoProduto(),obj.getCustoProduto());
            dados.put(ProdutoDataModel.getValorUnitario(),obj.getValorUnitario());
            dados.put(ClienteDataModel.getDiretorioFoto(),obj.getDiretorioFoto());
            dados.put(ClienteDataModel.getNomeFoto(),obj.getNomeArquivo());
            sucesso = alterar(ProdutoDataModel.getTabela(),dados);
        }catch(Exception e){

            Log.i("Erro -> Update",""+e);
        }


        return true;
    }
    public List<Produto> listar() {
        return getAllProdutos();
    }
    public List<Produto> listarProdutoByName(String nome){return getAllProdutosByName(nome);}
    public List<Produto> listarProdutoById(int id){return getAllProdutosById(id);}
}
