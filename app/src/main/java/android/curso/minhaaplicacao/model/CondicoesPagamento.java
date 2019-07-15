package android.curso.minhaaplicacao.model;

import java.io.Serializable;

public class CondicoesPagamento implements Serializable {

    private int idCondicaoPagamento;
    private String nomeCondiçãoPagamento;

    public int getIdCondicaoPagamento() {
        return idCondicaoPagamento;
    }

    public void setIdCondicaoPagamento(int idCondicaoPagamento) {
        this.idCondicaoPagamento = idCondicaoPagamento;
    }

    public String getNomeCondiçãoPagamento() {
        return nomeCondiçãoPagamento;
    }

    public void setNomeCondiçãoPagamento(String nomeCondiçãoPagamento) {
        this.nomeCondiçãoPagamento = nomeCondiçãoPagamento;
    }
}
