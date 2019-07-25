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

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class GraficoVendas extends Fragment {
    View view;
    LineChartView lineChartView;
    public GraficoVendas(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.guia_graficos_vendas, container, false);
        ControleChart controleChart = new ControleChart(getContext());
        Map<String,Double> valoresVendas = controleChart.getValoresVendas();
        lineChartView = view.findViewById(R.id.chart5);
        List yAxisValues = new ArrayList();
        List axisValues = new ArrayList();

        Line line = new Line(yAxisValues).setColor(Color.parseColor("#9C27B0"));
        int i =0;
        for (String mes  : valoresVendas.keySet()) {
            float valor =  valoresVendas.get (mes).floatValue();
            axisValues.add(i, new AxisValue(i).setLabel(mes));
            yAxisValues.add(new PointValue(i, valor));
            i +=1;
        }

        List lines = new ArrayList();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        Axis axis = new Axis();
        axis.setValues(axisValues);
        axis.setTextSize(16);
        axis.setTextColor(Color.parseColor("#03A9F4"));
        data.setAxisXBottom(axis);

        Axis yAxis = new Axis();
        yAxis.setName("Valores Vendas");
        yAxis.setTextColor(Color.parseColor("#03A9F4"));
        yAxis.setTextSize(16);
        data.setAxisYLeft(yAxis);

        lineChartView.setLineChartData(data);
        Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
        viewport.top = 110;
        lineChartView.setMaximumViewport(viewport);
        lineChartView.setCurrentViewport(viewport);


        return view;
    }

}
