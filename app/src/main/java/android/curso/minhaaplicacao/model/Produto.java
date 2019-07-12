package android.curso.minhaaplicacao.model;

import java.io.Serializable;

public class Produto implements Serializable {

    private double custoProduto;
    private double valorUnitario;
    private String nomeProduto;
    private double valorVenda;

    private int idProduto;
    private String diretorioFoto;
    private String nomeArquivo;

    public Produto(){

    }

  public Produto(String nomeProduto, double valorVenda) {
        this.setNomeProduto(nomeProduto);
        this.setValorVenda(valorVenda);

    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }





    public double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public double getCustoProduto() {
        return custoProduto;
    }

    public void setCustoProduto(double custoProduto) {
        this.custoProduto = custoProduto;
    }

    public double getValorVenda() {
        return valorVenda;
    }

    public void setValorVenda(double valorVenda) {
        this.valorVenda = valorVenda;
    }

    public String getDiretorioFoto() {
        return diretorioFoto;
    }

    public void setDiretorioFoto(String diretorioFoto) {
        this.diretorioFoto = diretorioFoto;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }
}
