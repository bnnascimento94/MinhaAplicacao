package android.curso.minhaaplicacao.view.fragments;

import android.curso.minhaaplicacao.R;
import android.curso.minhaaplicacao.controller.ControleChart;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class GraficoCliente extends Fragment {
View view;
PieChartView pieChartView1;

public GraficoCliente(){


}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.guia_graficos_clientes, container, false);

        pieChartView1 = view.findViewById(R.id.chart1);

        List<SliceValue> pieData1 = new ArrayList<>();
        ControleChart controleChart = new ControleChart(getContext());
        Map<String,Double> clientes = controleChart.getBestClients();
        int controler = 0;
        for (String cliente  : clientes.keySet()) {
            Float c =  clientes.get (cliente).floatValue();

            switch (controler){
                case 0:
                    pieData1.add(new SliceValue(c, Color.BLUE).setLabel(cliente));
                    break;
                case 1:
                    pieData1.add(new SliceValue(c, Color.GRAY).setLabel(cliente));
                    break;
                case 2:
                    pieData1.add(new SliceValue(c, Color.RED).setLabel(cliente));
                    break;
                case 3:
                    pieData1.add(new SliceValue(c, Color.MAGENTA).setLabel(cliente));
                    break;
                case 4:
                    pieData1.add(new SliceValue(c, Color.GREEN).setLabel(cliente));
                    break;

            }
            controler += 1;
        }

        PieChartData pieChartData1 = new PieChartData(pieData1);
        pieChartData1.setHasLabels(true).setValueLabelTextSize(8);
        pieChartData1.setHasCenterCircle(true).setCenterText1("Cliente / Vendas").setCenterText1FontSize(15).setCenterText1Color(Color.parseColor("#0097A7"));
        pieChartView1.setPieChartData(pieChartData1);
        return view;
    }
}
