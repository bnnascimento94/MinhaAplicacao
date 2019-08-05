package com.vullpes.app.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.curso.minhaaplicacao.R;
import com.vullpes.app.view.adapters.ViewPagerAdapter;
import com.vullpes.app.view.fragments.GraficoCliente;
import com.vullpes.app.view.fragments.GraficoProdutos;
import com.vullpes.app.view.fragments.GraficoVendas;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
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
