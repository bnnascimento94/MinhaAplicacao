package com.vullpes.app.controller;

import android.content.Context;
import com.vullpes.app.dataSource.DataSource;

import java.util.Map;

public class ControleChart extends DataSource {
    public ControleChart(Context context) {
        super(context);
    }

    public Map<String,Double> getBestSellerProducts(){
        return topSalesProducts();
    }
    public Map<String, Double> getBestClients(){
        return topClientesPedidos();

    }
    public Map<String,Double> getValoresVendas(){
        return valoresVendas();
    }
}
