package android.curso.minhaaplicacao.view;

import android.curso.minhaaplicacao.R;
import android.curso.minhaaplicacao.controller.ControleItemCarrinho;
import android.curso.minhaaplicacao.view.adapters.ExpandableAdapter;
import android.curso.minhaaplicacao.view.fragments.CadastroCliente;
import android.curso.minhaaplicacao.view.fragments.CadastroClienteListagem;
import android.curso.minhaaplicacao.view.fragments.CadastroProdutoListagem;
import android.curso.minhaaplicacao.view.fragments.Graficos;
import android.curso.minhaaplicacao.view.fragments.PedidosListagem;
import android.curso.minhaaplicacao.view.fragments.CadastroProduto;
import android.curso.minhaaplicacao.view.fragments.ClientesPedidoListagem;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TelaPrincipalActivity extends AppCompatActivity {
    private List<String> listGroup;
    private HashMap<String, List<String>> listData;
    FragmentManager fragmentManager;

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("Ciclo", "Activity: Metodo onStart() chamado");
        Toast.makeText(TelaPrincipalActivity.this, "Activity: Metodo onStart() chamado", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("Ciclo", "Activity: Metodo onRestart() chamado");
        Toast.makeText(TelaPrincipalActivity.this, "Activity: Metodo onRestart() chamado", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Ciclo", "Activity: Metodo onResume() chamado");
        Toast.makeText(TelaPrincipalActivity.this, "Activity: Metodo onResume() chamado", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("Ciclo", "Activity: Metodo onPause() chamado");
        Toast.makeText(TelaPrincipalActivity.this, "Activity: Metodo onPause() chamado", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("Ciclo", "Activity: Metodo onSavedInstanceState() chamado");
        Toast.makeText(TelaPrincipalActivity.this, "Activity: Metodo onSavedInstanceState() chamado", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("Ciclo", "Activity: Metodo onStop() chamado");
        Toast.makeText(TelaPrincipalActivity.this, "Activity: Metodo onDestroy() chamado", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("Ciclo", "Activity: Metodo onDestroy() chamado");
        Toast.makeText(TelaPrincipalActivity.this, "Activity: Metodo onDestroy() chamado", Toast.LENGTH_SHORT).show();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        fragmentManager = getSupportFragmentManager(); //chama o fragment Principal
        fragmentManager.beginTransaction().replace(R.id.content_fragment, new Graficos()).commit();

        buildList();
        View listHeaderView = getLayoutInflater().inflate(R.layout.nav_header_tela_principal, null,false);
        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListView.addHeaderView(listHeaderView);
        expandableListView.setAdapter(new ExpandableAdapter(TelaPrincipalActivity.this, listGroup, listData));

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener(){
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(TelaPrincipalActivity.this, "Group: " + groupPosition + "| Item: " + childPosition, Toast.LENGTH_SHORT).show();

                switch (groupPosition) {
                    case 0:
                        switch (childPosition) {
                            case 0:
                                fragmentManager = getSupportFragmentManager();
                                fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_fragment, new CadastroCliente()).commit();
                                setTitle("Cadastro Clientes");
                                break;
                            case 1:
                                fragmentManager = getSupportFragmentManager();
                                fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_fragment, new CadastroClienteListagem()).commit();
                                setTitle("Lista de Clientes");
                                break;
                        }
                        break;
                    case 1:
                        switch (childPosition) {
                            case 0:
                                fragmentManager = getSupportFragmentManager();
                                fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_fragment, new CadastroProduto()).commit();
                                setTitle("Cadastro de Produto");
                                break;
                            case 1:
                                fragmentManager = getSupportFragmentManager();
                                fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_fragment, new CadastroProdutoListagem()).commit();
                                setTitle("Lista de Produtos");

                                break;
                        }
                        break;
                    case 2:
                        switch (childPosition) {
                            case 0:
                                ControleItemCarrinho controleItemCarrinho = new ControleItemCarrinho(TelaPrincipalActivity.this);
                                controleItemCarrinho.deletarAllItemCarinho();
                                fragmentManager = getSupportFragmentManager();
                                fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_fragment, new ClientesPedidoListagem()).commit();
                                setTitle("Pedidos");
                                break;
                            case 1:
                                fragmentManager = getSupportFragmentManager();
                                fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_fragment, new PedidosListagem()).commit();
                                setTitle("Pedidos Realizados");
                                break;
                        }
                        break;
                }
                return false;
            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener(){
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(TelaPrincipalActivity.this, "Group (Expand): "+groupPosition, Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener(){
            @Override

            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(TelaPrincipalActivity.this, "Group (Collapse): "+groupPosition, Toast.LENGTH_SHORT).show();
            }
        });


       // expandableListView.setGroupIndicator(getResources().getDrawable(R.drawable.flecha_direita));




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();





    }

    @Override
    public void onBackPressed() {
       // DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
       // if (drawer.isDrawerOpen(GravityCompat.START)) {
       //     drawer.closeDrawer(GravityCompat.START);
      //  } else {
       //     super.onBackPressed();
       // }
        if (getFragmentManager().getBackStackEntryCount() > 0 ){
            getFragmentManager().popBackStack();
        } else {
            setTitle("Vullpes");
            super.onBackPressed();
        }
        //getFragmentManager().popBackStack();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tela_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void buildList(){
        listGroup = new ArrayList<String>();
        listData = new HashMap<String, List<String>>();

        // GROUP
        listGroup.add("Clientes");
        listGroup.add("Produtos");
        listGroup.add("Pedidos");

        // CHILDREN
        List<String> auxList = new ArrayList<String>();
        auxList.add("Cadastrar Clientes");
        auxList.add("Listar Clientes");
        listData.put(listGroup.get(0), auxList);

        auxList = new ArrayList<String>();
        auxList.add("Cadastrar Produtos");
        auxList.add("Listar Produtos");
        listData.put(listGroup.get(1), auxList);

        auxList = new ArrayList<String>();
        auxList.add("Cadastrar Pedidos");
        auxList.add("Listar Pedidos");
        listData.put(listGroup.get(2), auxList);

    }





}
