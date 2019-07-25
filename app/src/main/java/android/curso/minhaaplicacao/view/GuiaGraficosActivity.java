package android.curso.minhaaplicacao.view;

import android.curso.minhaaplicacao.R;
import android.curso.minhaaplicacao.view.adapters.ViewPagerAdapter;
import android.curso.minhaaplicacao.view.fragments.GraficoCliente;
import android.curso.minhaaplicacao.view.fragments.GraficoProdutos;
import android.curso.minhaaplicacao.view.fragments.GraficoVendas;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GuiaGraficosActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guia_graficos);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);


        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.AddFragment(new GraficoCliente(),"Cliente");
        adapter.AddFragment(new GraficoProdutos(),"Produto");
        adapter.AddFragment(new GraficoVendas(),"Vendas");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}