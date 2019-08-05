package com.vullpes.app.model;

import java.io.Serializable;

public class Cliente implements Serializable {
    private String nomeCliente;
    private String emailCliente;
    private String telefoneCliente;
    private String enderecoCliente;
    private String diretorioFoto;
    private String nomeArquivo;

    private int idCliente;

    public Cliente(){ }

    public Cliente(String nomeCliente, String emailCliente, String telefoneCliente, String enderecoCliente, String diretorioFoto, String nomeArquivo){
        this.nomeCliente = nomeCliente;
        this.emailCliente = emailCliente;
        this.telefoneCliente = telefoneCliente;
        this.enderecoCliente = enderecoCliente;
        this.diretorioFoto = diretorioFoto;
        this.nomeArquivo = nomeArquivo;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getEmailCliente() {
        return emailCliente;
    }

    public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
    }

    public String getTelefoneCliente() {
        return telefoneCliente;
    }

    public void setTelefoneCliente(String telefoneCliente) {
        this.telefoneCliente = telefoneCliente;
    }

    public String getEnderecoCliente() {
        return enderecoCliente;
    }

    public void setEnderecoCliente(String enderecoCliente) {
        this.enderecoCliente = enderecoCliente;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
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
