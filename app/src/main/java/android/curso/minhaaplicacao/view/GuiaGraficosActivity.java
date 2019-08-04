package android.curso.minhaaplicacao.view;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

public class GuiaGraficosActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guia_graficos);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Gráficos");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.AddFragment(new GraficoCliente(),"Cliente");
        adapter.AddFragment(new GraficoProdutos(),"Produto");
        adapter.AddFragment(new GraficoVendas(),"Vendas");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onBackPressed() {

        Intent telaPrincipal = new Intent(GuiaGraficosActivity.this,TelaPrincipalActivity.class );
        startActivity(telaPrincipal);
    }
}
