package android.curso.minhaaplicacao.controller;

import android.content.ContentValues;
import android.content.Context;
import android.curso.minhaaplicacao.dataModel.ClienteDataModel;
import android.curso.minhaaplicacao.dataSource.DataSource;
import android.curso.minhaaplicacao.model.Cliente;

import java.util.ArrayList;
import java.util.List;

public class ControleClientes extends DataSource {

    ContentValues dados;
    public ControleClientes(Context context){
        super(context);
    }
    public boolean salvar(Cliente obj){
        boolean sucesso = false;
        List<Cliente> clienteCadastrado = getAllClientesByName(obj.getNomeCliente());
        if(!(clienteCadastrado.size()>0)){ //consulta se há algum cliente já cadastrado
            dados = new ContentValues();
            dados.put(ClienteDataModel.getNomeCliente(),obj.getNomeCliente());
            dados.put(ClienteDataModel.getEmailCliente(),obj.getEmailCliente());
            dados.put(ClienteDataModel.getTelefoneCliente(),obj.getTelefoneCliente());
            dados.put(ClienteDataModel.getEnderecoCliente(),obj.getEnderecoCliente());
            dados.put(ClienteDataModel.getDiretorioFoto(),obj.getDiretorioFoto());
            dados.put(ClienteDataModel.getNomeFoto(),obj.getNomeArquivo());

            sucesso= insert(ClienteDataModel.getTabela(),dados);
        }

        return sucesso;
    }
    public boolean deletar(Cliente obj){
        boolean sucesso = true;
        sucesso = deletar(ClienteDataModel.getTabela(),obj.getIdCliente());
        return sucesso;
    }


    public boolean alterar(Cliente obj){
        boolean sucesso = true;
        dados = new ContentValues();
        dados.put(ClienteDataModel.getid(),obj.getIdCliente());
        dados.put(ClienteDataModel.getNomeCliente(),obj.getNomeCliente());
        dados.put(ClienteDataModel.getEmailCliente(),obj.getEmailCliente());
        dados.put(ClienteDataModel.getTelefoneCliente(),obj.getTelefoneCliente());
        dados.put(ClienteDataModel.getEnderecoCliente(),obj.getEnderecoCliente());
        dados.put(ClienteDataModel.getDiretorioFoto(),obj.getDiretorioFoto());
        dados.put(ClienteDataModel.getNomeFoto(),obj.getNomeArquivo());
        sucesso = alterar(ClienteDataModel.getTabela(),dados);
        return true;
    }
    public List<Cliente> listar() {
        return getAllClientes();
    }
    public List<Cliente> listarByNome(String nome){return getAllClientesByName(nome);}
    public List<Cliente> listarById(int id){return getAllClientesById(id);}
}
