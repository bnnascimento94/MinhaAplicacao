package android.curso.minhaaplicacao.dataSource;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.curso.minhaaplicacao.controller.ControlePrazo;
import android.curso.minhaaplicacao.dataModel.ClienteDataModel;
import android.curso.minhaaplicacao.dataModel.CondicoesPagamentoDataModel;
import android.curso.minhaaplicacao.dataModel.ContaAReceberDataModel;
import android.curso.minhaaplicacao.dataModel.ItemCarrinhoDataModel;
import android.curso.minhaaplicacao.dataModel.ItemPedidoDataModel;
import android.curso.minhaaplicacao.dataModel.PedidoDataModel;
import android.curso.minhaaplicacao.dataModel.PrazoDiasPagamentoDataModel;
import android.curso.minhaaplicacao.dataModel.PrazosPagamentoDataModel;
import android.curso.minhaaplicacao.dataModel.ProdutoDataModel;
import android.curso.minhaaplicacao.model.Cliente;
import android.curso.minhaaplicacao.model.CondicoesPagamento;
import android.curso.minhaaplicacao.model.ContasReceber;
import android.curso.minhaaplicacao.model.ItemCarrinho;
import android.curso.minhaaplicacao.model.ItemPedido;
import android.curso.minhaaplicacao.model.Pedidos;
import android.curso.minhaaplicacao.model.PrazoDiasPagamento;
import android.curso.minhaaplicacao.model.PrazosPagamento;
import android.curso.minhaaplicacao.model.Produto;
import android.curso.minhaaplicacao.view.fragments.PrazoPagamento;
import android.database.AbstractWindowedCursor;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.sqlite.SQLiteBlobTooBigException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataSource extends SQLiteOpenHelper {
    private static final String nomeBanco = "media_escolar.sqlite";
    private static final int versaoBanco = 1;
    protected  SQLiteDatabase db; // permite a manipulação do banco de dados
    protected Cursor cursor;


    public DataSource(Context context) {
        super(context, nomeBanco, null, versaoBanco);
        db = getWritableDatabase(); //permite a gravação no banco de dados

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL(ClienteDataModel.criarTabela());
            db.execSQL(ProdutoDataModel.criarTabela());
            db.execSQL(PedidoDataModel.criarTabela());
            db.execSQL(ItemPedidoDataModel.criarTabela());
            db.execSQL(ItemCarrinhoDataModel.criarTabela());
            db.execSQL(CondicoesPagamentoDataModel.criarTabela());
            db.execSQL(PrazosPagamentoDataModel.criarTabela());
            db.execSQL(PrazoDiasPagamentoDataModel.criarTabela());
            db.execSQL(ContaAReceberDataModel.criarTabela());
        }
        catch(Exception e){
            Log.e("media","DB ---> ERRO: "+e.getMessage());

        }

    }

    public boolean insert(String nomeTabela, ContentValues dados){
        boolean sucesso= true;

            try{
                sucesso = db.insert(nomeTabela,null, dados) >0; // se foi inserido retorna o valor
            }catch(Exception e){

        }

        return sucesso;
    }

    public boolean insert(List<String> nomeTabela,List<ContentValues> dados){
        boolean sucesso= true;

        try{
            db.beginTransaction();
            sucesso = db.insert(nomeTabela.get(0),null, dados.get(0)) >0; // se foi inserido retorna o valor
            sucesso = db.insert(nomeTabela.get(1),null, dados.get(1)) >0;
            db.endTransaction();
        }catch(Exception e){


        }

        return sucesso;
    }

    public boolean deletar(String nomeTabela, int id){
        boolean sucesso = false;

        try{
            db.delete(nomeTabela,"id=?", new String[]{Integer.toString(id)});
            sucesso = true;
        }
        catch(Exception e){
            sucesso=false;
        }


        return sucesso;
    }

    public boolean deletarTodos(String nomeTabela){
        boolean sucesso = false;

        try{
            db.delete(nomeTabela,null, null);

            sucesso = true;
        }
        catch(Exception e){
            sucesso=false;
        }


        return sucesso;
    }

    public boolean alterar(String nomeTabela,ContentValues dados){
        boolean sucesso = true;

        try{
            int id = dados.getAsInteger("id");
            sucesso = db.update(nomeTabela,dados,"id=?", new String[]{Integer.toString(id)}) > 0;
        }
        catch(Exception e){
            sucesso = false;
        }

        return sucesso;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<Cliente> getAllClientes(){
        Cliente obj;

        List<Cliente> lista = new ArrayList<>();

        String sql ="SELECT * FROM "+ClienteDataModel.getTabela()+ " ORDER BY nome_completo" ;

        Cursor cursor = db.rawQuery(sql,null);


        try {

            if(cursor.moveToFirst()){
                do{
                    obj = new Cliente();
                    obj.setIdCliente(cursor.getInt(cursor.getColumnIndex(ClienteDataModel.getid())));
                    obj.setNomeCliente(cursor.getString(cursor.getColumnIndex(ClienteDataModel.getNomeCliente())));
                    obj.setTelefoneCliente(cursor.getString(cursor.getColumnIndex(ClienteDataModel.getTelefoneCliente())));
                    obj.setEmailCliente(cursor.getString(cursor.getColumnIndex(ClienteDataModel.getEmailCliente())));
                    obj.setEnderecoCliente(cursor.getString(cursor.getColumnIndex(ClienteDataModel.getEnderecoCliente())));
                    obj.setNomeArquivo(cursor.getString(cursor.getColumnIndex(ClienteDataModel.getNomeFoto())));
                    obj.setDiretorioFoto(cursor.getString(cursor.getColumnIndex(ClienteDataModel.getDiretorioFoto())));
                    lista.add(obj);
                }while(cursor.moveToNext());

            }
        } catch (Exception expected) {
            Log.e("Error cli_list =>"," "+expected);
        }


        cursor.close();
        return lista;

    }
    public List<Cliente> getAllClientesById(int id){
        Cliente obj;

        List<Cliente> lista = new ArrayList<>();

        String sql ="SELECT * FROM "+ClienteDataModel.getTabela()+ " where "+ClienteDataModel.getid()+" = ? ORDER BY nome_completo" ;

        Cursor cursor = db.rawQuery(sql,new String[]{Integer.toString(id)});

        if(cursor.moveToFirst()){
            do{
                obj = new Cliente();
                obj.setIdCliente(cursor.getInt(cursor.getColumnIndex(ClienteDataModel.getid())));
                obj.setNomeCliente(cursor.getString(cursor.getColumnIndex(ClienteDataModel.getNomeCliente())));
                obj.setTelefoneCliente(cursor.getString(cursor.getColumnIndex(ClienteDataModel.getTelefoneCliente())));
                obj.setEmailCliente(cursor.getString(cursor.getColumnIndex(ClienteDataModel.getEmailCliente())));
                obj.setEnderecoCliente(cursor.getString(cursor.getColumnIndex(ClienteDataModel.getEnderecoCliente())));
                obj.setNomeArquivo(cursor.getString(cursor.getColumnIndex(ClienteDataModel.getNomeFoto())));
                obj.setDiretorioFoto(cursor.getString(cursor.getColumnIndex(ClienteDataModel.getDiretorioFoto())));
                lista.add(obj);
            }while(cursor.moveToNext());

        }
        cursor.close();
        return lista;

    }
    public List<Cliente> getAllClientesByName(String nomeCliente){
        Cliente obj;

        List<Cliente> lista = new ArrayList<>();

        String sql ="SELECT * FROM "+ClienteDataModel.getTabela()+ " where "+ClienteDataModel.getNomeCliente()+" = ? ORDER BY nome_completo" ;

        Cursor cursor = db.rawQuery(sql,new String[]{nomeCliente});

        if(cursor.moveToFirst()){
            do{
                obj = new Cliente();
                obj.setIdCliente(cursor.getInt(cursor.getColumnIndex(ClienteDataModel.getid())));
                obj.setNomeCliente(cursor.getString(cursor.getColumnIndex(ClienteDataModel.getNomeCliente())));
                obj.setTelefoneCliente(cursor.getString(cursor.getColumnIndex(ClienteDataModel.getTelefoneCliente())));
                obj.setEmailCliente(cursor.getString(cursor.getColumnIndex(ClienteDataModel.getEmailCliente())));
                obj.setEnderecoCliente(cursor.getString(cursor.getColumnIndex(ClienteDataModel.getEnderecoCliente())));
                obj.setNomeArquivo(cursor.getString(cursor.getColumnIndex(ClienteDataModel.getNomeFoto())));
                obj.setDiretorioFoto(cursor.getString(cursor.getColumnIndex(ClienteDataModel.getDiretorioFoto())));
                lista.add(obj);
            }while(cursor.moveToNext());

        }
        cursor.close();
        return lista;

    }

    public List<Produto> getAllProdutos(){
        Produto obj;

        List<Produto> lista = new ArrayList<>();

        String sql ="SELECT * FROM "+ ProdutoDataModel.getTabela()+ " ORDER BY nome_produto" ;

        Cursor cursor = db.rawQuery(sql,null);

        if(cursor.moveToFirst()){
            do{
                obj = new Produto();
                obj.setIdProduto(cursor.getInt(cursor.getColumnIndex(ProdutoDataModel.getid())));
                obj.setNomeProduto(cursor.getString(cursor.getColumnIndex(ProdutoDataModel.getNomeProduto())));
                obj.setCustoProduto(cursor.getDouble(cursor.getColumnIndex(ProdutoDataModel.getCustoProduto())));
                obj.setValorUnitario(cursor.getDouble(cursor.getColumnIndex(ProdutoDataModel.getValorUnitario())));
                obj.setNomeArquivo(cursor.getString(cursor.getColumnIndex(ClienteDataModel.getNomeFoto())));
                obj.setDiretorioFoto(cursor.getString(cursor.getColumnIndex(ClienteDataModel.getDiretorioFoto())));
                lista.add(obj);
            }while(cursor.moveToNext());

        }
        cursor.close();
        return lista;

    }
    public List<Produto> getAllProdutosByName(String nomeProduto){
        Produto obj;

        List<Produto> lista = new ArrayList<>();

        String sql ="SELECT * FROM "+ ProdutoDataModel.getTabela()+ " where "+ProdutoDataModel.getNomeProduto()+" = ? ORDER BY nome_produto" ;

        Cursor cursor = db.rawQuery(sql,new String[]{nomeProduto});

        if(cursor.moveToFirst()){
            do{
                obj = new Produto();
                obj.setIdProduto(cursor.getInt(cursor.getColumnIndex(ProdutoDataModel.getid())));
                obj.setNomeProduto(cursor.getString(cursor.getColumnIndex(ProdutoDataModel.getNomeProduto())));
                obj.setCustoProduto(cursor.getDouble(cursor.getColumnIndex(ProdutoDataModel.getCustoProduto())));
                obj.setValorUnitario(cursor.getDouble(cursor.getColumnIndex(ProdutoDataModel.getValorUnitario())));
                obj.setNomeArquivo(cursor.getString(cursor.getColumnIndex(ClienteDataModel.getNomeFoto())));
                obj.setDiretorioFoto(cursor.getString(cursor.getColumnIndex(ClienteDataModel.getDiretorioFoto())));
                lista.add(obj);
            }while(cursor.moveToNext());

        }
        cursor.close();
        return lista;

    }
    public List<Produto> getAllProdutosById(int id){
        Produto obj;

        List<Produto> lista = new ArrayList<>();

        String sql ="SELECT * FROM "+ ProdutoDataModel.getTabela()+ " where "+ProdutoDataModel.getid()+"= ? ORDER BY nome_produto" ;

        Cursor cursor = db.rawQuery(sql,new String[]{Integer.toString(id)});

        if(cursor.moveToFirst()){
            do{
                obj = new Produto();
                obj.setIdProduto(cursor.getInt(cursor.getColumnIndex(ProdutoDataModel.getid())));
                obj.setNomeProduto(cursor.getString(cursor.getColumnIndex(ProdutoDataModel.getNomeProduto())));
                obj.setCustoProduto(cursor.getDouble(cursor.getColumnIndex(ProdutoDataModel.getCustoProduto())));
                obj.setValorUnitario(cursor.getDouble(cursor.getColumnIndex(ProdutoDataModel.getValorUnitario())));
                obj.setNomeArquivo(cursor.getString(cursor.getColumnIndex(ClienteDataModel.getNomeFoto())));
                obj.setDiretorioFoto(cursor.getString(cursor.getColumnIndex(ClienteDataModel.getDiretorioFoto())));
                lista.add(obj);
            }while(cursor.moveToNext());

        }
        cursor.close();
        return lista;

    }
    public List<ItemPedido> getAllItemPedidoByIdPedido(int id){
        ItemPedido obj;

        List<ItemPedido> lista = new ArrayList<>();

        String sql ="SELECT * FROM "+ ItemPedidoDataModel.getTabela()+ " where "+ItemPedidoDataModel.getIdPedido()+"= ? " ;

        Cursor cursor = db.rawQuery(sql,new String[]{Integer.toString(id)});

        if(cursor.moveToFirst()){
            do{
                obj = new ItemPedido();
                obj.setIdItemPedido(cursor.getInt(cursor.getColumnIndex(ItemPedidoDataModel.getid())));
                obj.setQtde(cursor.getInt(cursor.getColumnIndex(ItemPedidoDataModel.getQuantidade())));
                obj.setItemValorVenda(cursor.getDouble(cursor.getColumnIndex(ItemPedidoDataModel.getValorVenda())));
                List<Produto> produto = getAllProdutosById(cursor.getInt(cursor.getColumnIndex(ItemPedidoDataModel.getIdProduto())));
                if(produto.size()>0){ obj.setProduto(produto.get(0));}
                lista.add(obj);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return lista;

    }
    public List<ItemPedido> getAllItemPedidoByIdProduto(int id){
        ItemPedido obj;

        List<ItemPedido> lista = new ArrayList<>();

        String sql ="SELECT * FROM "+ ItemPedidoDataModel.getTabela()+ " where "+ItemPedidoDataModel.getIdProduto()+"= ? " ;

        Cursor cursor = db.rawQuery(sql,new String[]{Integer.toString(id)});

        if(cursor.moveToFirst()){
            do{
                obj = new ItemPedido();
                obj.setIdItemPedido(cursor.getInt(cursor.getColumnIndex(ItemPedidoDataModel.getid())));
                obj.setQtde(cursor.getInt(cursor.getColumnIndex(ItemPedidoDataModel.getQuantidade())));
                obj.setItemValorVenda(cursor.getDouble(cursor.getColumnIndex(ItemPedidoDataModel.getValorVenda())));
                List<Produto> produto = getAllProdutosById(cursor.getInt(cursor.getColumnIndex(ItemPedidoDataModel.getIdProduto())));
                if(produto.size()>0){ obj.setProduto(produto.get(0));}
                lista.add(obj);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return lista;

    }
    public Pedidos getLastPedido(){
        Pedidos obj = null;
        String sql ="SELECT "+PedidoDataModel.getid()+" FROM "+ PedidoDataModel.getTabela()+ " ORDER BY "+PedidoDataModel.getid()+" desc limit 1" ;

        Cursor cursor = db.rawQuery(sql,null);

        if(cursor.moveToFirst()){
            do{
                obj = new Pedidos();
                obj.setIdPedido(cursor.getInt(cursor.getColumnIndex(PedidoDataModel.getid())));
            }while(cursor.moveToNext());

        }
        cursor.close();
        return obj;

    }
    public List<Pedidos> getAllPedidos()  {
        Pedidos obj;

        List<Pedidos> lista = new ArrayList<>();

        String sql ="SELECT * FROM "+ PedidoDataModel.getTabela() ;

        Cursor cursor = db.rawQuery(sql,null);

        if(cursor.moveToFirst()){
            do{
                obj = new Pedidos();

                try{
                    obj.setIdPedido(cursor.getInt(cursor.getColumnIndex(PedidoDataModel.getid())));
                    obj.setValorTotal(cursor.getDouble(cursor.getColumnIndex(PedidoDataModel.getValorTotal())));
                    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

                    obj.setData(formato.parse(cursor.getString(cursor.getColumnIndex(PedidoDataModel.getData()))));
                    int idCliente = cursor.getInt(cursor.getColumnIndex(PedidoDataModel.getIdCliente()));
                    int idPedido =cursor.getInt(cursor.getColumnIndex(PedidoDataModel.getid()));
                    int idPrazoPagamento = cursor.getInt(cursor.getColumnIndex(PedidoDataModel.getIdNomePrazoPagamento()));
                    int idCondicaoPagamento =cursor.getInt(cursor.getColumnIndex(PedidoDataModel.getIdCondicaoPagamento()));
                    List<Cliente> cliente = getAllClientesById(idCliente);
                    List<ItemPedido> itens = getAllItemPedidoByIdPedido(idPedido);
                    List<PrazosPagamento> prazosPagamentos = getPrazosPagamentoById(idPrazoPagamento);
                    List<CondicoesPagamento> condicoesPagamentos = getCondicoesPagamentoById(idCondicaoPagamento);

                    if(cliente.size() >0) obj.setCliente(cliente.get(0));
                    if(itens.size() >0) obj.setItensPedido((ArrayList<ItemPedido>) itens);
                    if(prazosPagamentos.size() >0) obj.setPrazosPagamento(prazosPagamentos.get(0));
                    if(condicoesPagamentos.size() >0) obj.setCondicoesPagamento(condicoesPagamentos.get(0));

                    lista.add(obj);
                }catch (ParseException p){
                    String errp = p.getMessage();
                }


            }while(cursor.moveToNext());

        }
        cursor.close();
        return lista;
    }
    public List<Pedidos> getPedidoById(int id)  {
        Pedidos obj;

        List<Pedidos> lista = new ArrayList<>();

        String sql ="SELECT * FROM "+ PedidoDataModel.getTabela() + " where "+PedidoDataModel.getid()+ " = ?" ;

        Cursor cursor = db.rawQuery(sql,new String[]{Integer.toString(id)});

        if(cursor.moveToFirst()){
            do{
                obj = new Pedidos();

                try{
                    obj.setIdPedido(cursor.getInt(cursor.getColumnIndex(PedidoDataModel.getid())));
                    obj.setValorTotal(cursor.getDouble(cursor.getColumnIndex(PedidoDataModel.getValorTotal())));
                    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

                    obj.setData(formato.parse(cursor.getString(cursor.getColumnIndex(PedidoDataModel.getData()))));
                    int idCliente = cursor.getInt(cursor.getColumnIndex(PedidoDataModel.getIdCliente()));
                    int idPedido =cursor.getInt(cursor.getColumnIndex(PedidoDataModel.getid()));
                    int idPrazoPagamento = cursor.getInt(cursor.getColumnIndex(PedidoDataModel.getIdNomePrazoPagamento()));
                    int idCondicaoPagamento =cursor.getInt(cursor.getColumnIndex(PedidoDataModel.getIdCondicaoPagamento()));
                    List<Cliente> cliente = getAllClientesById(idCliente);
                    List<ItemPedido> itens = getAllItemPedidoByIdPedido(idPedido);
                    List<PrazosPagamento> prazosPagamentos = getPrazosPagamentoById(idPrazoPagamento);
                    List<CondicoesPagamento> condicoesPagamentos = getCondicoesPagamentoById(idCondicaoPagamento);

                    if(cliente.size() >0) obj.setCliente(cliente.get(0));
                    if(itens.size() >0) obj.setItensPedido((ArrayList<ItemPedido>) itens);
                    if(prazosPagamentos.size() >0) obj.setPrazosPagamento(prazosPagamentos.get(0));
                    if(condicoesPagamentos.size() >0) obj.setCondicoesPagamento(condicoesPagamentos.get(0));

                    lista.add(obj);
                }catch (ParseException p){
                    String errp = p.getMessage();
                }


            }while(cursor.moveToNext());

        }
        cursor.close();
        return lista;
    }
    public List<Pedidos> getPedidoByIdPrazo(int id){
        Pedidos obj;

        List<Pedidos> lista = new ArrayList<>();

        String sql ="SELECT * FROM "+ PedidoDataModel.getTabela() + " where "+PedidoDataModel.getIdNomePrazoPagamento()+ " = ?" ;

        Cursor cursor = db.rawQuery(sql,new String[]{Integer.toString(id)});

        if(cursor.moveToFirst()){
            do{
                obj = new Pedidos();

                try{
                    obj.setIdPedido(cursor.getInt(cursor.getColumnIndex(PedidoDataModel.getid())));
                    obj.setValorTotal(cursor.getDouble(cursor.getColumnIndex(PedidoDataModel.getValorTotal())));
                    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

                    obj.setData(formato.parse(cursor.getString(cursor.getColumnIndex(PedidoDataModel.getData()))));
                    int idCliente = cursor.getInt(cursor.getColumnIndex(PedidoDataModel.getIdCliente()));
                    int idPedido =cursor.getInt(cursor.getColumnIndex(PedidoDataModel.getid()));
                    int idPrazoPagamento = cursor.getInt(cursor.getColumnIndex(PedidoDataModel.getIdNomePrazoPagamento()));
                    int idCondicaoPagamento =cursor.getInt(cursor.getColumnIndex(PedidoDataModel.getIdCondicaoPagamento()));
                    List<Cliente> cliente = getAllClientesById(idCliente);
                    List<ItemPedido> itens = getAllItemPedidoByIdPedido(idPedido);
                    List<PrazosPagamento> prazosPagamentos = getPrazosPagamentoById(idPrazoPagamento);
                    List<CondicoesPagamento> condicoesPagamentos = getCondicoesPagamentoById(idCondicaoPagamento);

                    if(cliente.size() >0) obj.setCliente(cliente.get(0));
                    if(itens.size() >0) obj.setItensPedido((ArrayList<ItemPedido>) itens);
                    if(prazosPagamentos.size() >0) obj.setPrazosPagamento(prazosPagamentos.get(0));
                    if(condicoesPagamentos.size() >0) obj.setCondicoesPagamento(condicoesPagamentos.get(0));

                    lista.add(obj);
                }catch (ParseException p){
                    String errp = p.getMessage();
                }


            }while(cursor.moveToNext());

        }
        cursor.close();
        return lista;
    }
    public List<Pedidos> getPedidoByIdCliente(int id){
        Pedidos obj;

        List<Pedidos> lista = new ArrayList<>();

        String sql ="SELECT * FROM "+ PedidoDataModel.getTabela() + " where "+PedidoDataModel.getIdCliente()+ " = ?" ;

        Cursor cursor = db.rawQuery(sql,new String[]{Integer.toString(id)});

        if(cursor.moveToFirst()){
            do{
                obj = new Pedidos();

                try{
                    obj.setIdPedido(cursor.getInt(cursor.getColumnIndex(PedidoDataModel.getid())));
                    obj.setValorTotal(cursor.getDouble(cursor.getColumnIndex(PedidoDataModel.getValorTotal())));
                    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

                    obj.setData(formato.parse(cursor.getString(cursor.getColumnIndex(PedidoDataModel.getData()))));
                    int idCliente = cursor.getInt(cursor.getColumnIndex(PedidoDataModel.getIdCliente()));
                    int idPedido =cursor.getInt(cursor.getColumnIndex(PedidoDataModel.getid()));
                    int idPrazoPagamento = cursor.getInt(cursor.getColumnIndex(PedidoDataModel.getIdNomePrazoPagamento()));
                    int idCondicaoPagamento =cursor.getInt(cursor.getColumnIndex(PedidoDataModel.getIdCondicaoPagamento()));
                    List<Cliente> cliente = getAllClientesById(idCliente);
                    List<ItemPedido> itens = getAllItemPedidoByIdPedido(idPedido);
                    List<PrazosPagamento> prazosPagamentos = getPrazosPagamentoById(idPrazoPagamento);
                    List<CondicoesPagamento> condicoesPagamentos = getCondicoesPagamentoById(idCondicaoPagamento);

                    if(cliente.size() >0) obj.setCliente(cliente.get(0));
                    if(itens.size() >0) obj.setItensPedido((ArrayList<ItemPedido>) itens);
                    if(prazosPagamentos.size() >0) obj.setPrazosPagamento(prazosPagamentos.get(0));
                    if(condicoesPagamentos.size() >0) obj.setCondicoesPagamento(condicoesPagamentos.get(0));
                    lista.add(obj);
                }catch (ParseException p){
                    String errp = p.getMessage();
                }

            }while(cursor.moveToNext());

        }
        cursor.close();
        return lista;
    }
    public List<Pedidos> getPedidoByIdCondicao(int id){
        Pedidos obj;

        List<Pedidos> lista = new ArrayList<>();
        String sql ="SELECT * FROM "+ PedidoDataModel.getTabela() + " where "+PedidoDataModel.getIdCondicaoPagamento()+ " = ?" ;
        Cursor cursor = db.rawQuery(sql,new String[]{Integer.toString(id)});

        if(cursor.moveToFirst()){
            do{
                obj = new Pedidos();

                try{
                    obj.setIdPedido(cursor.getInt(cursor.getColumnIndex(PedidoDataModel.getid())));
                    obj.setValorTotal(cursor.getDouble(cursor.getColumnIndex(PedidoDataModel.getValorTotal())));
                    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

                    obj.setData(formato.parse(cursor.getString(cursor.getColumnIndex(PedidoDataModel.getData()))));
                    int idCliente = cursor.getInt(cursor.getColumnIndex(PedidoDataModel.getIdCliente()));
                    int idPedido =cursor.getInt(cursor.getColumnIndex(PedidoDataModel.getid()));
                    int idPrazoPagamento = cursor.getInt(cursor.getColumnIndex(PedidoDataModel.getIdNomePrazoPagamento()));
                    int idCondicaoPagamento =cursor.getInt(cursor.getColumnIndex(PedidoDataModel.getIdCondicaoPagamento()));
                    List<Cliente> cliente = getAllClientesById(idCliente);
                    List<ItemPedido> itens = getAllItemPedidoByIdPedido(idPedido);
                    List<PrazosPagamento> prazosPagamentos = getPrazosPagamentoById(idPrazoPagamento);
                    List<CondicoesPagamento> condicoesPagamentos = getCondicoesPagamentoById(idCondicaoPagamento);

                    if(cliente.size() >0) obj.setCliente(cliente.get(0));
                    if(itens.size() >0) obj.setItensPedido((ArrayList<ItemPedido>) itens);
                    if(prazosPagamentos.size() >0) obj.setPrazosPagamento(prazosPagamentos.get(0));
                    if(condicoesPagamentos.size() >0) obj.setCondicoesPagamento(condicoesPagamentos.get(0));

                    lista.add(obj);
                }catch (ParseException p){
                    String errp = p.getMessage();
                }


            }while(cursor.moveToNext());

        }
        cursor.close();
        return lista;
    }
    public List<ItemCarrinho> getAllItensCarrinhos(){
        ItemCarrinho obj;

        List<ItemCarrinho> lista = new ArrayList<>();

        String sql ="SELECT * FROM "+ItemCarrinhoDataModel.getTabela()+ " ORDER BY id" ;

        Cursor cursor = db.rawQuery(sql,null);

        if(cursor.moveToFirst()){
            do{
                obj = new ItemCarrinho();
                obj.setIdItemCarrinho(cursor.getInt(cursor.getColumnIndex(ItemCarrinhoDataModel.getIdItemCarrinho())));
                obj.setQtde(cursor.getInt(cursor.getColumnIndex(ItemCarrinhoDataModel.getQuantidade())));
                obj.setItemValorVenda(cursor.getDouble(cursor.getColumnIndex(ItemCarrinhoDataModel.getValorVenda())));
                List<Produto> produto = getAllProdutosById(cursor.getInt(cursor.getColumnIndex(ItemCarrinhoDataModel.getProduto())));
                if(produto.size()>0){ obj.setProduto(produto.get(0));}
                lista.add(obj);
            }while(cursor.moveToNext());

        }
        cursor.close();
        return lista;

    }
    public List<ItemCarrinho> getAllItemCarrinhoByName(int idProduto){
        ItemCarrinho obj;

        List<ItemCarrinho> lista = new ArrayList<>();


        String sql ="SELECT * FROM "+ItemCarrinhoDataModel.getTabela()+ " where "+ ItemCarrinhoDataModel.getProduto()+ " = ?";

        Cursor cursor = db.rawQuery(sql,new String[]{String.valueOf(idProduto)});

        if(cursor.moveToFirst()){
            do{
                obj = new ItemCarrinho();
                obj.setIdItemCarrinho(cursor.getInt(cursor.getColumnIndex(ItemCarrinhoDataModel.getIdItemCarrinho())));
                obj.setQtde(cursor.getInt(cursor.getColumnIndex(ItemCarrinhoDataModel.getQuantidade())));
                obj.setItemValorVenda(cursor.getDouble(cursor.getColumnIndex(ItemCarrinhoDataModel.getValorVenda())));
               // List<Produto> produto = getAllProdutosById(cursor.getInt(cursor.getColumnIndex(ItemCarrinhoDataModel.getProduto())));
                //if(produto.size()>0){ obj.setProduto(produto.get(0));}
                lista.add(obj);
            }while(cursor.moveToNext());

        }
        cursor.close();
        return lista;

    }
    public ItemCarrinho getLastItemCarrinho(){
        ItemCarrinho obj = null;
        String sql ="SELECT "+ItemCarrinhoDataModel.getIdItemCarrinho()+" FROM "+ ItemCarrinhoDataModel.getTabela()+ " ORDER BY "+ItemCarrinhoDataModel.getIdItemCarrinho()+" desc limit 1" ;

        Cursor cursor = db.rawQuery(sql,null);

        if(cursor.moveToFirst()){
            do{
                obj = new ItemCarrinho();
                obj.setIdItemCarrinho(cursor.getInt(cursor.getColumnIndex(ItemCarrinhoDataModel.getIdItemCarrinho())));
            }while(cursor.moveToNext());

        }
        cursor.close();
        return obj;

    }

    public List<CondicoesPagamento> getAllCondicoesPagamento(){
        CondicoesPagamento obj;

        List<CondicoesPagamento> lista = new ArrayList<>();

        String sql ="SELECT * FROM "+ CondicoesPagamentoDataModel.getTabela() +" ORDER BY "+CondicoesPagamentoDataModel.getIdCondicao() ;

        Cursor cursor = db.rawQuery(sql,null);

        if(cursor.moveToFirst()){
            do{
                obj = new CondicoesPagamento();
                obj.setIdCondicaoPagamento(cursor.getInt(cursor.getColumnIndex(CondicoesPagamentoDataModel.getIdCondicao())));
                obj.setNomeCondiçãoPagamento(cursor.getString(cursor.getColumnIndex(CondicoesPagamentoDataModel.getNomeCondicao())));
                lista.add(obj);
            }while(cursor.moveToNext());

        }
        cursor.close();
        return lista;
    }
    public List<CondicoesPagamento> getCondicoesPagamentoById(int idCondicoesPagamento){
        CondicoesPagamento obj;

        List<CondicoesPagamento> lista = new ArrayList<>();


        String sql ="SELECT * FROM "+CondicoesPagamentoDataModel.getTabela()+ " where "+ CondicoesPagamentoDataModel.getIdCondicao()+ " = ?";

        Cursor cursor = db.rawQuery(sql,new String[]{String.valueOf(idCondicoesPagamento)});

        if(cursor.moveToFirst()){
            do{
                obj = new CondicoesPagamento();
                obj.setIdCondicaoPagamento(cursor.getInt(cursor.getColumnIndex(CondicoesPagamentoDataModel.getIdCondicao())));
                obj.setNomeCondiçãoPagamento(cursor.getString(cursor.getColumnIndex(CondicoesPagamentoDataModel.getNomeCondicao())));
                lista.add(obj);
            }while(cursor.moveToNext());

        }
        cursor.close();
        return lista;

    }
    public List<CondicoesPagamento> getCondicoesPagamentoByName(String nameCondicaoPagamento){
        CondicoesPagamento obj;

        List<CondicoesPagamento> lista = new ArrayList<>();


        String sql ="SELECT * FROM "+CondicoesPagamentoDataModel.getTabela()+ " where "+ CondicoesPagamentoDataModel.getNomeCondicao()+ " = ?";

        Cursor cursor = db.rawQuery(sql,new String[]{nameCondicaoPagamento});

        if(cursor.moveToFirst()){
            do{
                obj = new CondicoesPagamento();
                obj.setIdCondicaoPagamento(cursor.getInt(cursor.getColumnIndex(CondicoesPagamentoDataModel.getIdCondicao())));
                obj.setNomeCondiçãoPagamento(cursor.getString(cursor.getColumnIndex(CondicoesPagamentoDataModel.getNomeCondicao())));
                lista.add(obj);
            }while(cursor.moveToNext());

        }
        cursor.close();
        return lista;

    }
    public List<PrazosPagamento> getPrazosPagamento(){
        PrazosPagamento obj;

        List<PrazosPagamento> lista = new ArrayList<>();

        String sql ="SELECT * FROM "+ PrazosPagamentoDataModel.getTabela()+ " ORDER BY id" ;

        Cursor cursor = db.rawQuery(sql,null);

        if(cursor.moveToFirst()){
            do{
                obj = new PrazosPagamento();
                obj.setIdPrazoPagamento(cursor.getInt(cursor.getColumnIndex(PrazosPagamentoDataModel.getIdPrazo())));
                obj.setNomePrazoPagamento(cursor.getString(cursor.getColumnIndex(PrazosPagamentoDataModel.getNomePrazo())));
                List<PrazoDiasPagamento> prazoDias = getPrazoDiaPagamentoById(cursor.getInt(cursor.getColumnIndex(PrazosPagamentoDataModel.getIdPrazo())));
                if(prazoDias.size() >0) obj.setPrazosDiasPagamento(prazoDias);
                lista.add(obj);
            }while(cursor.moveToNext());

        }
        cursor.close();
        return lista;


    }
    public List<PrazosPagamento> getPrazosPagamentoById(int idPrazo){
        PrazosPagamento obj;

        List<PrazosPagamento> lista = new ArrayList<>();

        String sql ="SELECT * FROM "+ PrazosPagamentoDataModel.getTabela()+
                " WHERE "+PrazosPagamentoDataModel.getIdPrazo() +" = ?"+" ORDER BY id" ;

        Cursor cursor = db.rawQuery(sql,new String[]{String.valueOf(idPrazo)});

        if(cursor.moveToFirst()){
            do{
                obj = new PrazosPagamento();
                obj.setIdPrazoPagamento(cursor.getInt(cursor.getColumnIndex(PrazosPagamentoDataModel.getIdPrazo())));
                obj.setNomePrazoPagamento(cursor.getString(cursor.getColumnIndex(PrazosPagamentoDataModel.getNomePrazo())));
                List<PrazoDiasPagamento> prazoDias = getPrazoDiaPagamentoById(cursor.getInt(cursor.getColumnIndex(PrazosPagamentoDataModel.getIdPrazo())));
                if(prazoDias.size() >0) obj.setPrazosDiasPagamento(prazoDias);
                lista.add(obj);
            }while(cursor.moveToNext());

        }
        cursor.close();
        return lista;


    }
    public List<PrazosPagamento> getPrazosPagamentoWithDates(){
        PrazosPagamento obj;

        List<PrazosPagamento> lista = new ArrayList<>();

        String sql ="SELECT * FROM "+ PrazosPagamentoDataModel.getTabela()+ " a INNER JOIN "
                +PrazoDiasPagamentoDataModel.getTabela()+" b ON "+"a." + PrazosPagamentoDataModel.getIdPrazo() +" = "+
                "b."+PrazoDiasPagamentoDataModel.getIdPrazo() + " GROUP BY a."+PrazosPagamentoDataModel.getNomePrazo();

        Cursor cursor = db.rawQuery(sql,null);

        if(cursor.moveToFirst()){
            do{
                obj = new PrazosPagamento();
                obj.setIdPrazoPagamento(cursor.getInt(cursor.getColumnIndex(PrazosPagamentoDataModel.getIdPrazo())));
                obj.setNomePrazoPagamento(cursor.getString(cursor.getColumnIndex(PrazosPagamentoDataModel.getNomePrazo())));
                List<PrazoDiasPagamento> prazoDias = getPrazoDiaPagamentoById(cursor.getInt(cursor.getColumnIndex(PrazoDiasPagamentoDataModel.getIdPrazo())));
                if(prazoDias.size() >0) obj.setPrazosDiasPagamento(prazoDias);
                lista.add(obj);
            }while(cursor.moveToNext());

        }
        cursor.close();
        return lista;


    }
    public List<PrazosPagamento> getPrazoPagamentoById(int idPrazoPagamento){
        PrazosPagamento obj;

        List<PrazosPagamento> lista = new ArrayList<>();


        String sql ="SELECT * FROM "+PrazosPagamentoDataModel.getTabela()+ " where "+ PrazosPagamentoDataModel.getIdPrazo()+ " = ?";

        Cursor cursor = db.rawQuery(sql,new String[]{String.valueOf(idPrazoPagamento)});

        if(cursor.moveToFirst()){
            do{
                obj = new PrazosPagamento();
                obj.setIdPrazoPagamento(cursor.getInt(cursor.getColumnIndex(PrazosPagamentoDataModel.getIdPrazo())));
                obj.setNomePrazoPagamento(cursor.getString(cursor.getColumnIndex(PrazosPagamentoDataModel.getNomePrazo())));
                lista.add(obj);
            }while(cursor.moveToNext());

        }
        cursor.close();
        return lista;

    }
    public List<PrazosPagamento> getPrazoPagamentoByName(String namePrazoPagamento){
        PrazosPagamento obj;

        List<PrazosPagamento> lista = new ArrayList<>();


        String sql ="SELECT * FROM "+PrazosPagamentoDataModel.getTabela()+ " where "+ PrazosPagamentoDataModel.getNomePrazo()+ " = ?";

        Cursor cursor = db.rawQuery(sql,new String[]{namePrazoPagamento});

        if(cursor.moveToFirst()){
            do{
                obj = new PrazosPagamento();
                obj.setIdPrazoPagamento(cursor.getInt(cursor.getColumnIndex(PrazosPagamentoDataModel.getIdPrazo())));
                obj.setNomePrazoPagamento(cursor.getString(cursor.getColumnIndex(PrazosPagamentoDataModel.getNomePrazo())));
                lista.add(obj);
            }while(cursor.moveToNext());

        }

        cursor.close();
        return lista;

    }
    public List<PrazoDiasPagamento> getPrazoDiaPagamentoById(int idPrazoPagamento){
        PrazoDiasPagamento obj;

        List<PrazoDiasPagamento> lista = new ArrayList<>();


        String sql ="SELECT * FROM "+PrazoDiasPagamentoDataModel.getTabela()+ " where "+ PrazoDiasPagamentoDataModel.getIdPrazo()+ " = ?";

        Cursor cursor = db.rawQuery(sql,new String[]{String.valueOf(idPrazoPagamento)});

        if(cursor.moveToFirst()){
            do{
                obj = new PrazoDiasPagamento();
                obj.setIdPrazoDias(cursor.getInt(cursor.getColumnIndex(PrazoDiasPagamentoDataModel.getIdPrazoDias())));
                obj.setIdPrazo(cursor.getInt(cursor.getColumnIndex(PrazoDiasPagamentoDataModel.getIdPrazo())));
                obj.setNumeroDias(cursor.getInt(cursor.getColumnIndex(PrazoDiasPagamentoDataModel.getNumeroDias())));
                obj.setPorcentagem(cursor.getDouble(cursor.getColumnIndex(PrazoDiasPagamentoDataModel.getPorcentagem())));
                lista.add(obj);
            }while(cursor.moveToNext());

        }
        cursor.close();
        return lista;

    }
    public double totalPorcentagemDiasPrazo(int idPrazo){
        PrazoDiasPagamento obj;
        double total = 0;

        String sql ="SELECT * FROM "+ PrazoDiasPagamentoDataModel.getTabela()+ " where "+ PrazoDiasPagamentoDataModel.getIdPrazo()+ " = ?";

        Cursor cursor = db.rawQuery(sql,new String[]{String.valueOf(idPrazo)});

        if(cursor.moveToFirst()){
            do{
                obj = new PrazoDiasPagamento();
                obj.setPorcentagem(cursor.getDouble(cursor.getColumnIndex(PrazoDiasPagamentoDataModel.getPorcentagem())));
                total +=obj.getPorcentagem();

            }while(cursor.moveToNext());

        }

        cursor.close();
        return total;

    }


    public List<ContasReceber> getAllContasReceber(){
        ContasReceber obj;
        List<ContasReceber> lista = new ArrayList<>();
        String sql ="SELECT * FROM "+ContaAReceberDataModel.getTabela()+ " ORDER BY id" ;
        Cursor cursor = db.rawQuery(sql,null);

        if(cursor.moveToFirst()){
            do{
                try{
                    obj = new ContasReceber();
                    obj.setIdContaReceber(cursor.getInt(cursor.getColumnIndex(ContaAReceberDataModel.getIdContaReceber())));
                    Pedidos pedido = getPedidoById(cursor.getInt(cursor.getColumnIndex(ContaAReceberDataModel.getIdPedido()))).get(0);
                    obj.setPedido(pedido);
                    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                    obj.setData(formato.parse(cursor.getString(cursor.getColumnIndex(ContaAReceberDataModel.getDataContaReceber()))));
                    obj.setValor(cursor.getDouble(cursor.getColumnIndex(ContaAReceberDataModel.getValor())));
                    obj.setValorLiquidado(cursor.getDouble(cursor.getColumnIndex(ContaAReceberDataModel.getValorLiquidado())));
                    lista.add(obj);

                }catch(Exception e){
                    Log.i("ERRO AO SETAR->"," "+e);
                }

            }while(cursor.moveToNext());

        }
        cursor.close();
        return lista;
    }

    public List<ContasReceber> getContasAReceberByData(String sdata){
        List<ContasReceber> lista = new ArrayList<>();
        ContasReceber obj;
        try{

            String sql ="SELECT * FROM "+ContaAReceberDataModel.getTabela()+ " where "+ContaAReceberDataModel.getDataContaReceber()+ "= date(?) ORDER BY id" ;
            Cursor cursor = db.rawQuery(sql,new String[]{converterParaDataSqlite(sdata)});
            if(cursor.moveToFirst()){
                do{
                    try{
                        obj = new ContasReceber();
                        obj.setIdContaReceber(cursor.getInt(cursor.getColumnIndex(ContaAReceberDataModel.getIdContaReceber())));
                        Pedidos pedido = getPedidoById(cursor.getInt(cursor.getColumnIndex(ContaAReceberDataModel.getIdPedido()))).get(0);
                        obj.setPedido(pedido);
                        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                        obj.setData(formato.parse(cursor.getString(cursor.getColumnIndex(ContaAReceberDataModel.getDataContaReceber()))));
                        obj.setValor(cursor.getDouble(cursor.getColumnIndex(ContaAReceberDataModel.getValor())));
                        obj.setValorLiquidado(cursor.getDouble(cursor.getColumnIndex(ContaAReceberDataModel.getValorLiquidado())));
                        lista.add(obj);
                    }
                    catch(Exception e){
                        Log.i("ERRO AO SETAR->"," "+e);
                    }
                }while(cursor.moveToNext());
            }
            cursor.close();
        }
        catch(Exception e){
            Log.i("Erro ao Setar ->"," "+e);
        }

        return lista;
    }
    public List<ContasReceber> getContasAReceberByDataAndQuitadas(String sdata){
        List<ContasReceber> lista = new ArrayList<>();
        ContasReceber obj;
        try{
            String sql ="SELECT * FROM "+ContaAReceberDataModel.getTabela()+ " where "+ContaAReceberDataModel.getDataContaReceber()+ "= date(?) and" +
                    ContaAReceberDataModel.getValor() +" = "+ContaAReceberDataModel.getValorLiquidado() +" ORDER BY id" ;
            Cursor cursor = db.rawQuery(sql,new String[]{converterParaDataSqlite(sdata)});
            if(cursor.moveToFirst()){
                do{
                    try{
                        obj = new ContasReceber();
                        obj.setIdContaReceber(cursor.getInt(cursor.getColumnIndex(ContaAReceberDataModel.getIdContaReceber())));
                        Pedidos pedido = getPedidoById(cursor.getInt(cursor.getColumnIndex(ContaAReceberDataModel.getIdPedido()))).get(0);
                        obj.setPedido(pedido);
                        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                        obj.setData(formato.parse(cursor.getString(cursor.getColumnIndex(ContaAReceberDataModel.getDataContaReceber()))));
                        obj.setValor(cursor.getDouble(cursor.getColumnIndex(ContaAReceberDataModel.getValor())));
                        obj.setValorLiquidado(cursor.getDouble(cursor.getColumnIndex(ContaAReceberDataModel.getValorLiquidado())));
                        lista.add(obj);
                    }
                    catch(Exception e){
                        Log.i("ERRO AO SETAR->"," "+e);
                    }
                }while(cursor.moveToNext());
            }
            cursor.close();
        }
        catch(Exception e){
            Log.i("Erro ao Setar ->"," "+e);
        }

        return lista;
    }
    public List<ContasReceber> getContasAReceberByDataAndAbertas(String sdata){
        List<ContasReceber> lista = new ArrayList<>();
        ContasReceber obj;
        try{

            String sql ="SELECT * FROM "+ContaAReceberDataModel.getTabela()+ " where "+ContaAReceberDataModel.getDataContaReceber()+ "= date(?) and" +
                    ContaAReceberDataModel.getValor() +" != "+ContaAReceberDataModel.getValorLiquidado() +" ORDER BY id" ;
            Cursor cursor = db.rawQuery(sql,new String[]{converterParaDataSqlite(sdata)});
            if(cursor.moveToFirst()){
                do{
                    try{
                        obj = new ContasReceber();
                        obj.setIdContaReceber(cursor.getInt(cursor.getColumnIndex(ContaAReceberDataModel.getIdContaReceber())));
                        Pedidos pedido = getPedidoById(cursor.getInt(cursor.getColumnIndex(ContaAReceberDataModel.getIdPedido()))).get(0);
                        obj.setPedido(pedido);
                        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                        obj.setData(formato.parse(cursor.getString(cursor.getColumnIndex(ContaAReceberDataModel.getDataContaReceber()))));
                        obj.setValor(cursor.getDouble(cursor.getColumnIndex(ContaAReceberDataModel.getValor())));
                        obj.setValorLiquidado(cursor.getDouble(cursor.getColumnIndex(ContaAReceberDataModel.getValorLiquidado())));
                        lista.add(obj);
                    }
                    catch(Exception e){
                        Log.i("ERRO AO SETAR->"," "+e);
                    }
                }while(cursor.moveToNext());
            }
            cursor.close();
        }
        catch(Exception e){
            Log.i("Erro ao Setar ->"," "+e);
        }

        return lista;
    }
    public List<ContasReceber> getAllContasReceberByCliente(String nomeCliente){
        ContasReceber obj;
        List<ContasReceber> lista = new ArrayList<>();
        String sql ="SELECT * FROM "+ContaAReceberDataModel.getTabela()+ " a INNER JOIN "+PedidoDataModel.getTabela()+
                " b ON a."+ContaAReceberDataModel.getIdPedido()+" = b."+PedidoDataModel.getid()+" WHERE b."+PedidoDataModel.getIdCliente()+
                " IN (SELECT "+ClienteDataModel.getid()+" FROM "+ ClienteDataModel.getTabela() +" WHERE "+ClienteDataModel.getNomeCliente()+" like ?)";

        Cursor cursor = db.rawQuery(sql,new String[]{'%'+nomeCliente+'%'});

        if(cursor.moveToFirst()){

            do{
                try{
                    obj = new ContasReceber();
                    obj.setIdContaReceber(cursor.getInt(cursor.getColumnIndex(ContaAReceberDataModel.getIdContaReceber())));
                    Pedidos pedido = getPedidoById(cursor.getInt(cursor.getColumnIndex(ContaAReceberDataModel.getIdPedido()))).get(0);
                    obj.setPedido(pedido);
                    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                    obj.setData(formato.parse(cursor.getString(cursor.getColumnIndex(ContaAReceberDataModel.getDataContaReceber()))));
                    obj.setValor(cursor.getDouble(cursor.getColumnIndex(ContaAReceberDataModel.getValor())));
                    obj.setValorLiquidado(cursor.getDouble(cursor.getColumnIndex(ContaAReceberDataModel.getValorLiquidado())));
                    lista.add(obj);

                }catch(Exception e){
                    Log.i("ERRO AO SETAR->"," "+e);
                }

            }while(cursor.moveToNext());

        }

        cursor.close();
        return lista;
    }
    public List<ContasReceber> getAllContasReceberByClienteAndDate(String nomeCliente, String sdata){
        ContasReceber obj;
        List<ContasReceber> lista = new ArrayList<>();
        String sql ="SELECT * FROM "+ContaAReceberDataModel.getTabela()+ " a INNER JOIN "+PedidoDataModel.getTabela()+
                " b ON a."+ContaAReceberDataModel.getIdPedido()+" = b."+PedidoDataModel.getid()+" WHERE  a."+ContaAReceberDataModel.getDataContaReceber()+" = date("+converterParaDataSqlite(sdata)+") and b."+PedidoDataModel.getIdCliente()+
                " IN (SELECT "+ClienteDataModel.getid()+" FROM "+ ClienteDataModel.getTabela() +" WHERE "+ClienteDataModel.getNomeCliente()+" like ?)";

        Cursor cursor = db.rawQuery(sql,new String[]{'%'+nomeCliente+'%'});
        if(cursor.moveToFirst()){
            do{
                try{
                    obj = new ContasReceber();
                    obj.setIdContaReceber(cursor.getInt(cursor.getColumnIndex(ContaAReceberDataModel.getIdContaReceber())));
                    Pedidos pedido = getPedidoById(cursor.getInt(cursor.getColumnIndex(ContaAReceberDataModel.getIdPedido()))).get(0);
                    obj.setPedido(pedido);
                    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                    obj.setData(formato.parse(cursor.getString(cursor.getColumnIndex(ContaAReceberDataModel.getDataContaReceber()))));
                    obj.setValor(cursor.getDouble(cursor.getColumnIndex(ContaAReceberDataModel.getValor())));
                    obj.setValorLiquidado(cursor.getDouble(cursor.getColumnIndex(ContaAReceberDataModel.getValorLiquidado())));
                    lista.add(obj);
                }catch(Exception e){
                    Log.i("ERRO AO SETAR->"," "+e);
                }
            }while(cursor.moveToNext());
        }

        cursor.close();
        return lista;

    }
    public List<ContasReceber> getAllContasReceberByClienteAndQuitada(String nomeCliente){
        ContasReceber obj;
        List<ContasReceber> lista = new ArrayList<>();
        String sql ="SELECT * FROM "+ContaAReceberDataModel.getTabela()+ " a INNER JOIN "+PedidoDataModel.getTabela()+
                " b ON a."+ContaAReceberDataModel.getIdPedido()+" = b."+PedidoDataModel.getid()+" WHERE  a."+ContaAReceberDataModel.getValorLiquidado()+" = "+ContaAReceberDataModel.getValor() +" and b."+PedidoDataModel.getIdCliente()+
                " IN (SELECT "+ClienteDataModel.getid()+" FROM "+ ClienteDataModel.getTabela() +" WHERE "+ClienteDataModel.getNomeCliente()+" like ?)";

        Cursor cursor = db.rawQuery(sql,new String[]{'%'+nomeCliente+'%'});
        if(cursor.moveToFirst()){
            do{
                try{
                    obj = new ContasReceber();
                    obj.setIdContaReceber(cursor.getInt(cursor.getColumnIndex(ContaAReceberDataModel.getIdContaReceber())));
                    Pedidos pedido = getPedidoById(cursor.getInt(cursor.getColumnIndex(ContaAReceberDataModel.getIdPedido()))).get(0);
                    obj.setPedido(pedido);
                    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                    obj.setData(formato.parse(cursor.getString(cursor.getColumnIndex(ContaAReceberDataModel.getDataContaReceber()))));
                    obj.setValor(cursor.getDouble(cursor.getColumnIndex(ContaAReceberDataModel.getValor())));
                    obj.setValorLiquidado(cursor.getDouble(cursor.getColumnIndex(ContaAReceberDataModel.getValorLiquidado())));
                    lista.add(obj);
                }catch(Exception e){
                    Log.i("ERRO AO SETAR->"," "+e);
                }
            }while(cursor.moveToNext());
        }

        cursor.close();
        return lista;

    }
    public List<ContasReceber> getAllContasReceberByClienteAndAberta(String nomeCliente){
        ContasReceber obj;
        List<ContasReceber> lista = new ArrayList<>();
        String sql ="SELECT * FROM "+ContaAReceberDataModel.getTabela()+ " a INNER JOIN "+PedidoDataModel.getTabela()+
                " b ON a."+ContaAReceberDataModel.getIdPedido()+" = b."+PedidoDataModel.getid()+" WHERE  a."+ContaAReceberDataModel.getValorLiquidado()+" != "+ContaAReceberDataModel.getValor() +" and b."+PedidoDataModel.getIdCliente()+
                " IN (SELECT "+ClienteDataModel.getid()+" FROM "+ ClienteDataModel.getTabela() +" WHERE "+ClienteDataModel.getNomeCliente()+" like ?)";

        Cursor cursor = db.rawQuery(sql,new String[]{'%'+nomeCliente+'%'});
        if(cursor.moveToFirst()){
            do{
                try{
                    obj = new ContasReceber();
                    obj.setIdContaReceber(cursor.getInt(cursor.getColumnIndex(ContaAReceberDataModel.getIdContaReceber())));
                    Pedidos pedido = getPedidoById(cursor.getInt(cursor.getColumnIndex(ContaAReceberDataModel.getIdPedido()))).get(0);
                    obj.setPedido(pedido);
                    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                    obj.setData(formato.parse(cursor.getString(cursor.getColumnIndex(ContaAReceberDataModel.getDataContaReceber()))));
                    obj.setValor(cursor.getDouble(cursor.getColumnIndex(ContaAReceberDataModel.getValor())));
                    obj.setValorLiquidado(cursor.getDouble(cursor.getColumnIndex(ContaAReceberDataModel.getValorLiquidado())));
                    lista.add(obj);
                }catch(Exception e){
                    Log.i("ERRO AO SETAR->"," "+e);
                }
            }while(cursor.moveToNext());
        }

        cursor.close();
        return lista;

    }
    public List<ContasReceber> getAllContasReceberByClienteAndCurrentMonth(String nomeCliente){
        ContasReceber obj;
        List<ContasReceber> lista = new ArrayList<>();
        String sql ="SELECT * FROM "+ContaAReceberDataModel.getTabela()+ " a INNER JOIN "+PedidoDataModel.getTabela()+
                " b ON a."+ContaAReceberDataModel.getIdPedido()+" = b."+PedidoDataModel.getid()+" WHERE  a."+ContaAReceberDataModel.getDataContaReceber() +" " +
                "BETWEEN date('now','start of month') AND date('now','start of month','+1 month','-1 day')  and b."+PedidoDataModel.getIdCliente()+
                " IN (SELECT "+ClienteDataModel.getid()+" FROM "+ ClienteDataModel.getTabela() +" WHERE "+ClienteDataModel.getNomeCliente()+" like ?)";

        Cursor cursor = db.rawQuery(sql,new String[]{'%'+nomeCliente+'%'});
        if(cursor.moveToFirst()){
            do{
                try{
                    obj = new ContasReceber();
                    obj.setIdContaReceber(cursor.getInt(cursor.getColumnIndex(ContaAReceberDataModel.getIdContaReceber())));
                    Pedidos pedido = getPedidoById(cursor.getInt(cursor.getColumnIndex(ContaAReceberDataModel.getIdPedido()))).get(0);
                    obj.setPedido(pedido);
                    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                    obj.setData(formato.parse(cursor.getString(cursor.getColumnIndex(ContaAReceberDataModel.getDataContaReceber()))));
                    obj.setValor(cursor.getDouble(cursor.getColumnIndex(ContaAReceberDataModel.getValor())));
                    obj.setValorLiquidado(cursor.getDouble(cursor.getColumnIndex(ContaAReceberDataModel.getValorLiquidado())));
                    lista.add(obj);
                }catch(Exception e){
                    Log.i("ERRO AO SETAR->"," "+e);
                }
            }while(cursor.moveToNext());
        }

        cursor.close();
        return lista;

    }
    public List<ContasReceber> getAllContasReceberQuitadas(){
        ContasReceber obj;
        List<ContasReceber> lista = new ArrayList<>();
        String sql ="SELECT * FROM "+ContaAReceberDataModel.getTabela()+ " WHERE "+ContaAReceberDataModel.getValor() +" = "+ContaAReceberDataModel.getValorLiquidado() +" ORDER BY id" ;

        Cursor cursor = db.rawQuery(sql,null);
        if(cursor.moveToFirst()){
            do{
                try{
                    obj = new ContasReceber();
                    obj.setIdContaReceber(cursor.getInt(cursor.getColumnIndex(ContaAReceberDataModel.getIdContaReceber())));
                    Pedidos pedido = getPedidoById(cursor.getInt(cursor.getColumnIndex(ContaAReceberDataModel.getIdPedido()))).get(0);
                    obj.setPedido(pedido);
                    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                    obj.setData(formato.parse(cursor.getString(cursor.getColumnIndex(ContaAReceberDataModel.getDataContaReceber()))));
                    obj.setValor(cursor.getDouble(cursor.getColumnIndex(ContaAReceberDataModel.getValor())));
                    obj.setValorLiquidado(cursor.getDouble(cursor.getColumnIndex(ContaAReceberDataModel.getValorLiquidado())));
                    lista.add(obj);
                }catch(Exception e){
                    Log.i("ERRO AO SETAR->"," "+e);
                }
            }while(cursor.moveToNext());
        }

        cursor.close();
        return lista;

    }
    public List<ContasReceber> getAllContasReceberAberta(){
        ContasReceber obj;
        List<ContasReceber> lista = new ArrayList<>();
        String sql ="SELECT * FROM "+ContaAReceberDataModel.getTabela()+ " WHERE "+ContaAReceberDataModel.getValor() +" != "+ContaAReceberDataModel.getValorLiquidado() +" ORDER BY id" ;

        Cursor cursor = db.rawQuery(sql,null);
        if(cursor.moveToFirst()){
            do{
                try{
                    obj = new ContasReceber();
                    obj.setIdContaReceber(cursor.getInt(cursor.getColumnIndex(ContaAReceberDataModel.getIdContaReceber())));
                    Pedidos pedido = getPedidoById(cursor.getInt(cursor.getColumnIndex(ContaAReceberDataModel.getIdPedido()))).get(0);
                    obj.setPedido(pedido);
                    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                    obj.setData(formato.parse(cursor.getString(cursor.getColumnIndex(ContaAReceberDataModel.getDataContaReceber()))));
                    obj.setValor(cursor.getDouble(cursor.getColumnIndex(ContaAReceberDataModel.getValor())));
                    obj.setValorLiquidado(cursor.getDouble(cursor.getColumnIndex(ContaAReceberDataModel.getValorLiquidado())));
                    lista.add(obj);
                }catch(Exception e){
                    Log.i("ERRO AO SETAR->"," "+e);
                }
            }while(cursor.moveToNext());
        }

        cursor.close();
        return lista;

    }
    public List<ContasReceber> getAllContasReceberCurrentMonth(){
        ContasReceber obj;
        List<ContasReceber> lista = new ArrayList<>();
        String sql ="SELECT * FROM "+ContaAReceberDataModel.getTabela()+ " WHERE "+ContaAReceberDataModel.getDataContaReceber() +" BETWEEN date('now','start of month') AND date('now','start of month','+1 month','-1 day') " ;

        Cursor cursor = db.rawQuery(sql,null);
        if(cursor.moveToFirst()){
            do{
                try{
                    obj = new ContasReceber();
                    obj.setIdContaReceber(cursor.getInt(cursor.getColumnIndex(ContaAReceberDataModel.getIdContaReceber())));
                    Pedidos pedido = getPedidoById(cursor.getInt(cursor.getColumnIndex(ContaAReceberDataModel.getIdPedido()))).get(0);
                    obj.setPedido(pedido);
                    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                    obj.setData(formato.parse(cursor.getString(cursor.getColumnIndex(ContaAReceberDataModel.getDataContaReceber()))));
                    obj.setValor(cursor.getDouble(cursor.getColumnIndex(ContaAReceberDataModel.getValor())));
                    obj.setValorLiquidado(cursor.getDouble(cursor.getColumnIndex(ContaAReceberDataModel.getValorLiquidado())));
                    lista.add(obj);
                }catch(Exception e){
                    Log.i("ERRO AO SETAR->"," "+e);
                }
            }while(cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }
    public List<ContasReceber> getAllContasReceberQuitadasCurrentMonth(){
        ContasReceber obj;
        List<ContasReceber> lista = new ArrayList<>();
        String sql ="SELECT * FROM "+ContaAReceberDataModel.getTabela()+ " WHERE "+ContaAReceberDataModel.getDataContaReceber() +" BETWEEN date('now','start of month') AND date('now','start of month','+1 month','-1 day')" +
                " and "+ContaAReceberDataModel.getValor() +" = "+ContaAReceberDataModel.getValorLiquidado() +" ORDER BY id" ;

        Cursor cursor = db.rawQuery(sql,null);
        if(cursor.moveToFirst()){
            do{
                try{
                    obj = new ContasReceber();
                    obj.setIdContaReceber(cursor.getInt(cursor.getColumnIndex(ContaAReceberDataModel.getIdContaReceber())));
                    Pedidos pedido = getPedidoById(cursor.getInt(cursor.getColumnIndex(ContaAReceberDataModel.getIdPedido()))).get(0);
                    obj.setPedido(pedido);
                    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                    obj.setData(formato.parse(cursor.getString(cursor.getColumnIndex(ContaAReceberDataModel.getDataContaReceber()))));
                    obj.setValor(cursor.getDouble(cursor.getColumnIndex(ContaAReceberDataModel.getValor())));
                    obj.setValorLiquidado(cursor.getDouble(cursor.getColumnIndex(ContaAReceberDataModel.getValorLiquidado())));
                    lista.add(obj);
                }catch(Exception e){
                    Log.i("ERRO AO SETAR->"," "+e);
                }
            }while(cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }
    public List<ContasReceber> getAllContasReceberAbertasCurrentMonth(){
        ContasReceber obj;
        List<ContasReceber> lista = new ArrayList<>();
        String sql ="SELECT * FROM "+ContaAReceberDataModel.getTabela()+ " WHERE "+ContaAReceberDataModel.getDataContaReceber() +" BETWEEN date('now','start of month') AND date('now','start of month','+1 month','-1 day')" +
                " and "+ContaAReceberDataModel.getValor() +" != "+ContaAReceberDataModel.getValorLiquidado() +" ORDER BY id" ;

        Cursor cursor = db.rawQuery(sql,null);
        if(cursor.moveToFirst()){
            do{
                try{
                    obj = new ContasReceber();
                    obj.setIdContaReceber(cursor.getInt(cursor.getColumnIndex(ContaAReceberDataModel.getIdContaReceber())));
                    Pedidos pedido = getPedidoById(cursor.getInt(cursor.getColumnIndex(ContaAReceberDataModel.getIdPedido()))).get(0);
                    obj.setPedido(pedido);
                    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                    obj.setData(formato.parse(cursor.getString(cursor.getColumnIndex(ContaAReceberDataModel.getDataContaReceber()))));
                    obj.setValor(cursor.getDouble(cursor.getColumnIndex(ContaAReceberDataModel.getValor())));
                    obj.setValorLiquidado(cursor.getDouble(cursor.getColumnIndex(ContaAReceberDataModel.getValorLiquidado())));
                    lista.add(obj);
                }catch(Exception e){
                    Log.i("ERRO AO SETAR->"," "+e);
                }
            }while(cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }
    public List<ContasReceber> getContasAReceberEntreDatas(String sdata1, String sdata2){
        List<ContasReceber> lista = new ArrayList<>();
        ContasReceber obj;
        try{

            String sql ="SELECT * FROM "+ContaAReceberDataModel.getTabela()+ " where "+ContaAReceberDataModel.getDataContaReceber()+ " between date(?) and date(?) ORDER BY id" ;
            Cursor cursor = db.rawQuery(sql,new String[]{converterParaDataSqlite(sdata1),converterParaDataSqlite(sdata2)});
            if(cursor.moveToFirst()){
                do{
                    try{
                        obj = new ContasReceber();
                        obj.setIdContaReceber(cursor.getInt(cursor.getColumnIndex(ContaAReceberDataModel.getIdContaReceber())));
                        Pedidos pedido = getPedidoById(cursor.getInt(cursor.getColumnIndex(ContaAReceberDataModel.getIdPedido()))).get(0);
                        obj.setPedido(pedido);
                        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                        obj.setData(formato.parse(cursor.getString(cursor.getColumnIndex(ContaAReceberDataModel.getDataContaReceber()))));
                        obj.setValor(cursor.getDouble(cursor.getColumnIndex(ContaAReceberDataModel.getValor())));
                        obj.setValorLiquidado(cursor.getDouble(cursor.getColumnIndex(ContaAReceberDataModel.getValorLiquidado())));
                        lista.add(obj);
                    }
                    catch(Exception e){
                        Log.i("ERRO AO SETAR->"," "+e);
                    }
                }while(cursor.moveToNext());
            }
            cursor.close();
        }
        catch(Exception e){
            Log.i("Erro ao Setar ->"," "+e);
        }

        return lista;
    }
    public List<ContasReceber> getContasAReceberEntreDatasAndCliente(String sdata1, String sdata2, String cliente){
        List<ContasReceber> lista = new ArrayList<>();
        ContasReceber obj;
        try{
            String sql ="select * from tb_conta_receber a inner join tb_pedidos b on a.id_pedido = b.id inner join tb_cliente c on b.id_cliente = c.id where a."+ContaAReceberDataModel.getDataContaReceber()+
                    " between date(?) and date(?) and c."+ClienteDataModel.getNomeCliente()+" = ? ORDER BY id" ;
            Cursor cursor = db.rawQuery(sql,new String[]{converterParaDataSqlite(sdata1),converterParaDataSqlite(sdata2),cliente});
            if(cursor.moveToFirst()){
                do{
                    try{
                        obj = new ContasReceber();
                        obj.setIdContaReceber(cursor.getInt(cursor.getColumnIndex(ContaAReceberDataModel.getIdContaReceber())));
                        Pedidos pedido = getPedidoById(cursor.getInt(cursor.getColumnIndex(ContaAReceberDataModel.getIdPedido()))).get(0);
                        obj.setPedido(pedido);
                        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                        obj.setData(formato.parse(cursor.getString(cursor.getColumnIndex(ContaAReceberDataModel.getDataContaReceber()))));
                        obj.setValor(cursor.getDouble(cursor.getColumnIndex(ContaAReceberDataModel.getValor())));
                        obj.setValorLiquidado(cursor.getDouble(cursor.getColumnIndex(ContaAReceberDataModel.getValorLiquidado())));
                        lista.add(obj);
                    }
                    catch(Exception e){
                        Log.i("ERRO AO SETAR->"," "+e);
                    }
                }while(cursor.moveToNext());
            }
            cursor.close();
        }
        catch(Exception e){
            Log.i("Erro ao Setar ->"," "+e);
        }

        return lista;
    }
    public List<ContasReceber> getContasAReceberEntreDatasAndQuitado(String sdata1, String sdata2){
        List<ContasReceber> lista = new ArrayList<>();
        ContasReceber obj;
        try{

            String sql ="SELECT * FROM "+ContaAReceberDataModel.getTabela()+ " where "+ContaAReceberDataModel.getDataContaReceber()+ " between date(?) and date(?) and "+ContaAReceberDataModel.getValor() +" = "+ContaAReceberDataModel.getValorLiquidado() +"  ORDER BY id" ;
            Cursor cursor = db.rawQuery(sql,new String[]{converterParaDataSqlite(sdata1),converterParaDataSqlite(sdata2)});
            if(cursor.moveToFirst()){
                do{
                    try{
                        obj = new ContasReceber();
                        obj.setIdContaReceber(cursor.getInt(cursor.getColumnIndex(ContaAReceberDataModel.getIdContaReceber())));
                        Pedidos pedido = getPedidoById(cursor.getInt(cursor.getColumnIndex(ContaAReceberDataModel.getIdPedido()))).get(0);
                        obj.setPedido(pedido);
                        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                        obj.setData(formato.parse(cursor.getString(cursor.getColumnIndex(ContaAReceberDataModel.getDataContaReceber()))));
                        obj.setValor(cursor.getDouble(cursor.getColumnIndex(ContaAReceberDataModel.getValor())));
                        obj.setValorLiquidado(cursor.getDouble(cursor.getColumnIndex(ContaAReceberDataModel.getValorLiquidado())));
                        lista.add(obj);
                    }
                    catch(Exception e){
                        Log.i("ERRO AO SETAR->"," "+e);
                    }
                }while(cursor.moveToNext());
            }
            cursor.close();
        }
        catch(Exception e){
            Log.i("Erro ao Setar ->"," "+e);
        }

        return lista;
    }
    public List<ContasReceber> getContasAReceberEntreDatasAndAReceber(String sdata1, String sdata2){
        List<ContasReceber> lista = new ArrayList<>();
        ContasReceber obj;
        try{

            String sql ="SELECT * FROM "+ContaAReceberDataModel.getTabela()+ " where "+ContaAReceberDataModel.getDataContaReceber()+ " between date(?) and date(?) and "+ContaAReceberDataModel.getValor() +" != "+ContaAReceberDataModel.getValorLiquidado() +"  ORDER BY id" ;
            Cursor cursor = db.rawQuery(sql,new String[]{converterParaDataSqlite(sdata1),converterParaDataSqlite(sdata2)});
            if(cursor.moveToFirst()){
                do{
                    try{
                        obj = new ContasReceber();
                        obj.setIdContaReceber(cursor.getInt(cursor.getColumnIndex(ContaAReceberDataModel.getIdContaReceber())));
                        Pedidos pedido = getPedidoById(cursor.getInt(cursor.getColumnIndex(ContaAReceberDataModel.getIdPedido()))).get(0);
                        obj.setPedido(pedido);
                        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                        obj.setData(formato.parse(cursor.getString(cursor.getColumnIndex(ContaAReceberDataModel.getDataContaReceber()))));
                        obj.setValor(cursor.getDouble(cursor.getColumnIndex(ContaAReceberDataModel.getValor())));
                        obj.setValorLiquidado(cursor.getDouble(cursor.getColumnIndex(ContaAReceberDataModel.getValorLiquidado())));
                        lista.add(obj);
                    }
                    catch(Exception e){
                        Log.i("ERRO AO SETAR->"," "+e);
                    }
                }while(cursor.moveToNext());
            }
            cursor.close();
        }
        catch(Exception e){
            Log.i("Erro ao Setar ->"," "+e);
        }

        return lista;
    }
    public int getNumberProducts(){
        int qtdeProdutos = 0;

        List<Pedidos> lista = new ArrayList<>();

        String sql ="SELECT Count(*) as total FROM "+ ItemPedidoDataModel.getTabela() +" Order By total DESC LIMIT 5 " ;

        Cursor cursor = db.rawQuery(sql,null);

        if(cursor.moveToFirst()){
            do{

                try{
                    qtdeProdutos =cursor.getInt(cursor.getColumnIndex("total"));
                }
                catch (Exception p){
                    String errp = p.getMessage();
                }

            }while(cursor.moveToNext());
        }
        cursor.close();

        return qtdeProdutos;
    }
    public int getNumberPedidos(){
        int qtdeProdutos = 0;

        String sql ="SELECT COUNT(*) as total from "+PedidoDataModel.getTabela()+" a inner join "+ ClienteDataModel.getTabela()+" b on a."+
                PedidoDataModel.getIdCliente()+" = b."+ClienteDataModel.getid()+" order by total desc limit 5 ";

        Cursor cursor = db.rawQuery(sql,null);

        if(cursor.moveToFirst()){
            do{

                try{
                    qtdeProdutos =cursor.getInt(cursor.getColumnIndex("total"));
                }
                catch (Exception p){
                    String errp = p.getMessage();
                }

            }while(cursor.moveToNext());
        }
        cursor.close();

        return qtdeProdutos;


    }
    public Map<String,Double> topSalesProducts(){
        Map<String, Double> valorProdutos = new HashMap<String, Double>();
        int total = getNumberProducts();

        String sql = "SELECT Count(*) as total, b."+ProdutoDataModel.getNomeProduto()+" from "+ ItemPedidoDataModel.getTabela()+" a  inner join "+ ProdutoDataModel.getTabela() +
                " b on a."+ItemPedidoDataModel.getIdProduto()+" = b."+ProdutoDataModel.getid()+ " group by a."+ItemPedidoDataModel.getIdProduto()+" order by total desc limit 5";

        Cursor cursor = db.rawQuery(sql,null);

        if(cursor.moveToFirst()){
            do{
                try{
                    String produto = cursor.getString(cursor.getColumnIndex(ProdutoDataModel.getNomeProduto()));
                    Integer totalElementos = cursor.getInt(cursor.getColumnIndex("total"));
                    double porcentagem = (Double.valueOf(String.valueOf(totalElementos))/total);
                    valorProdutos.put(produto,porcentagem);
                }
                catch (Exception p){
                    Log.e("Erro busca pieChart",""+p.getMessage());
                    String errp = p.getMessage();
                }

            }while(cursor.moveToNext());
        }
        cursor.close();


        return valorProdutos;
    }
    public Map<String, Double> topClientesPedidos(){
        Map<String, Double> topClientes = new HashMap<String, Double>();
        int total = getNumberPedidos();

        String sql ="SELECT COUNT(*) as total, b."+ClienteDataModel.getNomeCliente()+" from "+PedidoDataModel.getTabela()+" a inner join "+ ClienteDataModel.getTabela()+" b on a."+
                PedidoDataModel.getIdCliente()+" = b."+ClienteDataModel.getid()+" group by b."+ ClienteDataModel.getNomeCliente()+" order by total desc limit 5 ";

        Cursor cursor = db.rawQuery(sql,null);

        if(cursor.moveToFirst()){
            do{
                try{
                    String cliente = cursor.getString(cursor.getColumnIndex(ClienteDataModel.getNomeCliente()));
                    Integer totalElementos = cursor.getInt(cursor.getColumnIndex("total"));
                    double porcentagem = (Double.valueOf(String.valueOf(totalElementos))/total);
                    topClientes.put(cliente,porcentagem);
                }
                catch (Exception p){
                    Log.e("Erro busca pieChart",""+p.getMessage());
                    String errp = p.getMessage();
                }

            }while(cursor.moveToNext());
        }
        cursor.close();


        return topClientes;


    }
    public Map<String, Double> valoresVendas(){
        Map<String, Double> topClientes = new HashMap<String,Double>();
        int total = getNumberPedidos();

        String sql ="select SUM(valor) as valor, strftime('%m-%Y', data_conta) as 'mes' from tb_conta_receber group by strftime('%m-%Y', data_conta) order by strftime('%m', data_conta) desc limit 12";

        Cursor cursor = db.rawQuery(sql,null);

        if(cursor.moveToFirst()){
            do{
                try{
                    Double valor = cursor.getDouble(cursor.getColumnIndex("valor"));
                    String mes = cursor.getString(cursor.getColumnIndex("mes"));
                    topClientes.put(mes,valor);
                }
                catch (Exception p){
                    Log.e("Erro busca pieChart",""+p.getMessage());
                    String errp = p.getMessage();
                }

            }while(cursor.moveToNext());
        }
        cursor.close();


        return topClientes;


    }

    public Double valorTotalVendasMensais(){
        double valorTotal = 0.00;

        String sql ="select SUM(valor) as valor from " + ContaAReceberDataModel.getTabela()+
                " group by strftime('%m-%Y', "+ContaAReceberDataModel.getDataContaReceber()+")  order by strftime('%m', "+ContaAReceberDataModel.getDataContaReceber()+") desc limit 1";

        Cursor cursor = db.rawQuery(sql,null);

        if(cursor.moveToFirst()){
            do{
                try{
                    valorTotal = cursor.getDouble(cursor.getColumnIndex("valor"));
                }
                catch (Exception p){
                    Log.e("Erro  valorTotal mes",""+p.getMessage());
                    String errp = p.getMessage();
                }

            }while(cursor.moveToNext());
        }
        cursor.close();
        return valorTotal;
    }
    public Double valorTotalVendasQuidatasMensais(){
        double valorTotal = 0.00;
        String sql ="select SUM(valor) as valor from tb_conta_receber " + ContaAReceberDataModel.getTabela()+ " where "+ContaAReceberDataModel.getValor() +" = "+ContaAReceberDataModel.getValorLiquidado() +
                " group by strftime('%m-%Y', "+ContaAReceberDataModel.getDataContaReceber()+")  order by strftime('%m', "+ContaAReceberDataModel.getDataContaReceber()+") desc limit 1";

        Cursor cursor = db.rawQuery(sql,null);

        if(cursor.moveToFirst()){
            do{
                try{
                    valorTotal = cursor.getDouble(cursor.getColumnIndex("valor"));
                }
                catch (Exception p){
                    Log.e("Erro  valorTotal mes",""+p.getMessage());
                    String errp = p.getMessage();
                }

            }while(cursor.moveToNext());
        }
        cursor.close();
        return valorTotal;

    }
    public Double valorTotalVendasAReceberMensais(){
        double valorTotal = 0.00;

        String sql ="select SUM(valor) as valor from tb_conta_receber " + ContaAReceberDataModel.getTabela()+ " where "+ContaAReceberDataModel.getValor() +" != "+ContaAReceberDataModel.getValorLiquidado() +
                " group by strftime('%m-%Y', "+ContaAReceberDataModel.getDataContaReceber()+")  order by strftime('%m', "+ContaAReceberDataModel.getDataContaReceber()+") desc limit 1";

        Cursor cursor = db.rawQuery(sql,null);

        if(cursor.moveToFirst()){
            do{
                try{
                    valorTotal = cursor.getDouble(cursor.getColumnIndex("valor"));
                }
                catch (Exception p){
                    Log.e("Erro  valorTotal mes",""+p.getMessage());
                    String errp = p.getMessage();
                }

            }while(cursor.moveToNext());
        }
        cursor.close();


        return valorTotal;
    }
    public String produtoMaisVendidoMensal(){
        String nomeProduto = "";

        String sql ="select sum(a.valor) as valor, c.nome_produto  as nome from tb_conta_receber a inner join tb_item_produto b on a.id_pedido = b.id_pedido inner join tb_produto c on \n" +
                "b.id_produto = c.id\n" +
                " where a.data_conta BETWEEN date('now','start of month') AND date('now','start of month','+1 month','-1 day') group by c.nome_produto order by valor desc limit 1 ;";

        Cursor cursor = db.rawQuery(sql,null);

        if(cursor.moveToFirst()){
            do{
                try{
                    nomeProduto = cursor.getString(cursor.getColumnIndex("nome"));
                }
                catch (Exception p){
                    Log.e("Erro  nomeProduto mes",""+p.getMessage());
                    String errp = p.getMessage();
                }

            }while(cursor.moveToNext());
        }
        cursor.close();


        return nomeProduto;
    }
    public String produtoMenosVendidoMensal(){
        String nomeProduto = "";

        String sql ="select sum(a.valor) as valor, c.nome_produto  as nome from tb_conta_receber a inner join tb_item_produto b on a.id_pedido = b.id_pedido inner join tb_produto c on \n" +
                "b.id_produto = c.id\n" +
                " where a.data_conta BETWEEN date('now','start of month') AND date('now','start of month','+1 month','-1 day') group by c.nome_produto order by valor asc limit 1 ;";

        Cursor cursor = db.rawQuery(sql,null);

        if(cursor.moveToFirst()){
            do{
                try{
                    nomeProduto = cursor.getString(cursor.getColumnIndex("nome"));
                }
                catch (Exception p){
                    Log.e("Erro  nomeProduto mes",""+p.getMessage());
                    String errp = p.getMessage();
                }

            }while(cursor.moveToNext());
        }
        cursor.close();


        return nomeProduto;
    }
    public Double valorTotalVendasAnuais(){
        double valorTotal = 0.00;

        String sql ="select sum(valor) as valor from tb_conta_receber where data_conta BETWEEN date('now','start of year') AND date('now','start of year','+1 year','-1 day')";
        Cursor cursor = db.rawQuery(sql,null);

        if(cursor.moveToFirst()){
            do{
                try{
                    valorTotal = cursor.getDouble(cursor.getColumnIndex("valor"));
                }
                catch (Exception p){
                    Log.e("Erro  valorTotal mes",""+p.getMessage());
                    String errp = p.getMessage();
                }

            }while(cursor.moveToNext());
        }
        cursor.close();
        return valorTotal;
    }
    public Double valorTotalVendasQuidatasAnuais(){
        double valorTotal = 0.00;

        String sql ="select sum(valor) as valor from tb_conta_receber where valor = valor_liquidado and data_conta BETWEEN date('now','start of year') AND date('now','start of year','+1 year','-1 day')";

        Cursor cursor = db.rawQuery(sql,null);

        if(cursor.moveToFirst()){
            do{
                try{
                    valorTotal = cursor.getDouble(cursor.getColumnIndex("valor"));
                }
                catch (Exception p){
                    Log.e("Erro  valorTotal mes",""+p.getMessage());
                    String errp = p.getMessage();
                }

            }while(cursor.moveToNext());
        }
        cursor.close();


        return valorTotal;
    }
    public Double valorTotalVendasAReceberAnuais(){
        double valorTotal = 0.00;

        String sql ="select sum(valor) as valor from tb_conta_receber where valor != valor_liquidado and data_conta BETWEEN date('now','start of year') AND date('now','start of year','+1 year','-1 day')";

        Cursor cursor = db.rawQuery(sql,null);

        if(cursor.moveToFirst()){
            do{
                try{
                    valorTotal = cursor.getDouble(cursor.getColumnIndex("valor"));
                }
                catch (Exception p){
                    Log.e("Erro  valorTotal mes",""+p.getMessage());
                    String errp = p.getMessage();
                }

            }while(cursor.moveToNext());
        }
        cursor.close();


        return valorTotal;
    }
    public String produtoMaisVendidoAnuais(){
        String nomeProduto = "";
        String sql ="select sum(a.valor) as valor, c.nome_produto  as nome from tb_conta_receber a inner join tb_item_produto b on a.id_pedido = b.id_pedido inner join tb_produto c on \n" +
                "b.id_produto = c.id\n" +
                " where a.data_conta BETWEEN date('now','start of year') AND date('now','start of year','+1 year','-1 day') group by c.nome_produto order by valor desc limit 1 ;";
        Cursor cursor = db.rawQuery(sql,null);
        if(cursor.moveToFirst()){
            do{
                try{
                    nomeProduto = cursor.getString(cursor.getColumnIndex("nome"));
                }
                catch (Exception p){
                    Log.e("Erro  nomeProduto mes",""+p.getMessage());
                    String errp = p.getMessage();
                }
            }while(cursor.moveToNext());
        }
        cursor.close();
        return nomeProduto;
    }
    public String produtoMenosVendidoAnuais(){
        String nomeProduto = "";

        String sql ="select sum(a.valor) as valor, c.nome_produto  as nome from tb_conta_receber a inner join tb_item_produto b on a.id_pedido = b.id_pedido inner join tb_produto c on \n" +
                "b.id_produto = c.id\n" +
                " where a.data_conta BETWEEN date('now','start of year') AND date('now','start of year','+1 year','-1 day') group by c.nome_produto order by valor asc limit 1 ;";

        Cursor cursor = db.rawQuery(sql,null);

        if(cursor.moveToFirst()){
            do{
                try{
                    nomeProduto = cursor.getString(cursor.getColumnIndex("nome"));
                }
                catch (Exception p){
                    Log.e("Erro  nomeProduto mes",""+p.getMessage());
                    String errp = p.getMessage();
                }

            }while(cursor.moveToNext());
        }
        cursor.close();
        return nomeProduto;
    }
    private String converterParaDataSqlite(String sdata){
        Date data;
        String dataParaBanco = "";
        try {
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            data = formato.parse(sdata);
            dataParaBanco = new SimpleDateFormat("yyyy-MM-dd").format(data);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.i("ERRO ao Coverter ->",""+e);
        }
        return dataParaBanco;
    }
}
