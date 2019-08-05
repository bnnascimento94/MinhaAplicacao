package com.vullpes.app.controller;

import android.content.Context;
import com.vullpes.app.dataSource.DataSource;

public class ControleVendas extends DataSource {
    public ControleVendas(Context context) {
        super(context);
    }


    public Double getValorTotalVendasMensais(){
        return valorTotalVendasMensais();
    }

    public Double getValorTotalVendasQuidatasMensais(){

        return valorTotalVendasQuidatasMensais();
    }

    public Double getValorTotalVendasAReceberMensais(){
        return valorTotalVendasAReceberMensais();
    }

    public String getProdutoMaisVendidoMensal(){
        return produtoMaisVendidoMensal();
    }

    public String getProdutoMenosVendidoMensal(){
        return produtoMenosVendidoMensal();
    }


    public Double getValorTotalVendasAnual(){
        return valorTotalVendasAnuais();
    }

    public Double getValorTotalVendasQuidatasAnuais(){

        return valorTotalVendasQuidatasAnuais();
    }

    public Double getValorTotalVendasAReceberAnuais(){
        return valorTotalVendasAReceberAnuais();
    }

    public String getProdutoMaisVendidoAnuais(){
        return produtoMaisVendidoAnuais();
    }

    public String getProdutoMenosVendidoAnuais(){
        return produtoMenosVendidoAnuais();
    }
}
