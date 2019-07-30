package android.curso.minhaaplicacao.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.curso.minhaaplicacao.controller.ControleChart;
import android.curso.minhaaplicacao.controller.ControleItemCarrinho;
import android.curso.minhaaplicacao.controller.ControleVendas;
import android.curso.minhaaplicacao.view.GuiaGraficosActivity;
import android.curso.minhaaplicacao.view.ReciboActivity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.curso.minhaaplicacao.R;
import android.widget.Button;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PieChartView;


public class Graficos extends Fragment {
View view;
TextView txtValorTotalMes,txtQuitadoMes,txtAReceberMes,txtProdutoMaisVendidoMes,txtMenosVendidoMes;
TextView txtValorTotalAno,txtQuitadoAno,txtAReceberAno,txtProdutoMaisVendidoAno,txtMenosVendidoAno;
Button button, buttonAno;
NumberFormat z;
Locale locale;
String replaceable;
ControleVendas controleVendas;
    public Graficos() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

        locale = new Locale("pt", "BR");
        replaceable = String.format("[%s.\\s]", NumberFormat.getCurrencyInstance(locale).getCurrency().getSymbol());
        z = NumberFormat.getCurrencyInstance();
        controleVendas = new ControleVendas(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_graficos, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Vullpes");

        button = view.findViewById(R.id.button);
        buttonAno = view.findViewById(R.id.buttonAno);

        txtValorTotalMes = view.findViewById(R.id.txtValorTotalMes);
        txtQuitadoMes = view.findViewById(R.id.txtQuitadoMes);
        txtAReceberMes = view.findViewById(R.id.txtAReceberMes);
        txtProdutoMaisVendidoMes = view.findViewById(R.id.txtProdutoMaisVendidoMes);
        txtMenosVendidoMes = view.findViewById(R.id.txtMenosVendidoMes);

        txtValorTotalAno = view.findViewById(R.id.txtValorTotalAno);
        txtQuitadoAno = view.findViewById(R.id.txtQuitadoAno);
        txtAReceberAno = view.findViewById(R.id.txtAReceberAno);
        txtMenosVendidoAno = view.findViewById(R.id.txtMenosVendidoAno);
        txtProdutoMaisVendidoAno = view.findViewById(R.id.txtProdutoMaisVendidoAno);

        txtValorTotalMes.setText(z.format(controleVendas.getValorTotalVendasMensais()));
        txtQuitadoMes.setText(z.format(controleVendas.getValorTotalVendasQuidatasMensais()));
        txtAReceberMes.setText(z.format(controleVendas.getValorTotalVendasAReceberMensais()));
        txtProdutoMaisVendidoMes.setText(controleVendas.getProdutoMaisVendidoMensal());
        txtMenosVendidoMes.setText(controleVendas.getProdutoMenosVendidoMensal());

        txtValorTotalAno.setText(z.format(controleVendas.getValorTotalVendasAnual()));
        txtQuitadoAno.setText(z.format(controleVendas.getValorTotalVendasQuidatasAnuais()));
        txtAReceberAno.setText(z.format(controleVendas.getValorTotalVendasAReceberAnuais()));
        txtProdutoMaisVendidoAno.setText(controleVendas.getProdutoMaisVendidoAnuais());
        txtMenosVendidoAno.setText(controleVendas.getProdutoMenosVendidoAnuais());

        buttonAno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentVaiProFormulario = new Intent(getContext(), GuiaGraficosActivity.class);
                startActivity(intentVaiProFormulario);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentVaiProFormulario = new Intent(getContext(), GuiaGraficosActivity.class);
                startActivity(intentVaiProFormulario);
            }
        });



        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
