package com.vullpes.app.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.curso.minhaaplicacao.R;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.vullpes.app.controller.ControleItemCarrinho;
import com.vullpes.app.view.adapters.ExpandableAdapter;
import com.vullpes.app.view.fragments.CadastroCliente;
import com.vullpes.app.view.fragments.CadastroClienteListagem;
import com.vullpes.app.view.fragments.CadastroProdutoListagem;
import com.vullpes.app.view.fragments.CondicaoPagamento;
import com.vullpes.app.view.fragments.ContasReceber;
import com.vullpes.app.view.fragments.FinalizarPedido;
import com.vullpes.app.view.fragments.Graficos;
import com.vullpes.app.view.fragments.PedidosListagem;
import com.vullpes.app.view.fragments.CadastroProduto;
import com.vullpes.app.view.fragments.ClientesPedidoListagem;
import com.vullpes.app.view.fragments.PrazoPagamento;
import com.vullpes.app.view.fragments.ProdutosPedidoSelecionado;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.view.ActionMode;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.SharedMemory;
import android.util.Log;
import android.view.View;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TelaPrincipalActivity extends AppCompatActivity {
    public List<String> listGroup;
    public HashMap<String, List<String>> listData;
    FragmentManager fragmentManager;
    SharedPreferences sharedPreferences;
    String tokenApp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_tela_principal);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences("PushPref",MODE_PRIVATE);

        getTokenFCM();


        String menuFragment = getIntent().getStringExtra("menuFragment");

        if (menuFragment != null) {

            // Here we can decide what do to -- perhaps load other parameters from the intent extras such as IDs, etc
            if (menuFragment.equals("produtos")) {
                fragmentManager = getSupportFragmentManager(); //chama o fragment Principal
                fragmentManager.beginTransaction().replace(R.id.content_fragment, new PedidosListagem()).commit();
            }
        } else {
            // Activity was not launched with a menuFragment selected -- continue as if this activity was opened from a launcher (for example)
            fragmentManager = getSupportFragmentManager(); //chama o fragment Principal
            fragmentManager.beginTransaction().replace(R.id.content_fragment, new Graficos()).commit();
        }


        buildList();
        View listHeaderView = getLayoutInflater().inflate(R.layout.nav_header_tela_principal, null,false);
        ExpandableListView expandableListView =  findViewById(R.id.expandableListView);
        expandableListView.addHeaderView(listHeaderView);
        expandableListView.setAdapter(new ExpandableAdapter(TelaPrincipalActivity.this, listGroup, listData));

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener(){
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //Toast.makeText(TelaPrincipalActivity.this, "Group: " + groupPosition + "| Item: " + childPosition, Toast.LENGTH_SHORT).show();

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
                                fragmentManager = getSupportFragmentManager();
                                fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_fragment, new CondicaoPagamento()).commit();
                                setTitle("Condição Pagamento");
                                break;
                            case 1:
                                fragmentManager = getSupportFragmentManager();
                                fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_fragment, new PrazoPagamento()).commit();
                                setTitle("Prazo Pagamento");

                                break;
                        }
                        break;
                    case 3:
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
                    case 4:
                        switch (childPosition) {
                            case 0:
                                fragmentManager = getSupportFragmentManager();
                                fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_fragment, new ContasReceber()).commit();
                                setTitle("Contas a Receber");
                                break;
                        }
                        break;

                    case 5:
                        switch (childPosition) {
                            case 0:
                                Intent telaPrincipal = new Intent(TelaPrincipalActivity.this,GuiaGraficosActivity.class );
                                startActivity(telaPrincipal);
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
                //Toast.makeText(TelaPrincipalActivity.this, "Group (Expand): "+groupPosition, Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener(){
            @Override

            public void onGroupCollapse(int groupPosition) {
                //Toast.makeText(TelaPrincipalActivity.this, "Group (Collapse): "+groupPosition, Toast.LENGTH_SHORT).show();
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }

    private void getTokenFCM() {
        //solicitar o token
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnSuccessListener(TelaPrincipalActivity.this, new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        tokenApp = instanceIdResult.getToken();
                        salvarTokenFCM();//salvar o token no sharedPreferences
                    }
                });
    }

    private void salvarTokenFCM() {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("tokenAPP",tokenApp);
        edit.apply();
    }

    boolean twice;
    @Override
    public void onBackPressed() {
        if(twice == true){

            Log.d("teste","CLICK");
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            System.exit(0);
        }else{
            tellFragments();
        }

        Toast.makeText(TelaPrincipalActivity.this, "Please press back again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                twice = false;
                Log.d("teste", "twice" +twice);
            }
        },3000);
        twice = true;
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
        listGroup.add("Financeiro");
        listGroup.add("Pedidos");
        listGroup.add("Contas a Receber");
        listGroup.add("Graficos");

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
        auxList.add("Condição de Pagamento");
        auxList.add("Prazo Pagamento");
        listData.put(listGroup.get(2), auxList);

        auxList = new ArrayList<String>();
        auxList.add("Cadastrar Pedidos");
        auxList.add("Listar Pedidos");
        listData.put(listGroup.get(3), auxList);

        auxList = new ArrayList<String>();
        auxList.add("Contas a Receber");
        listData.put(listGroup.get(4), auxList);

        auxList = new ArrayList<String>();
        auxList.add("Graficos");
        listData.put(listGroup.get(5), auxList);

    }


    private void tellFragments(){
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for(Fragment f : fragments){
            if(f != null && f instanceof CadastroCliente){
                ((CadastroCliente)f).OnBackPressed();
            }else if(f != null && f instanceof CadastroClienteListagem){
                ((CadastroClienteListagem)f).OnBackPressed();
            }else if(f != null && f instanceof CadastroProduto){
                ((CadastroProduto)f).OnBackPressed();
            }else if(f != null && f instanceof CadastroProdutoListagem){
                ((CadastroProdutoListagem)f).OnBackPressed();
            }else if(f != null && f instanceof CondicaoPagamento){
                ((CondicaoPagamento)f).OnBackPressed();
            }else if(f != null && f instanceof ContasReceber){
                ((ContasReceber)f).OnBackPressed();
            }else if(f != null && f instanceof FinalizarPedido){
                ((FinalizarPedido)f).OnBackPressed();
            }else if(f != null && f instanceof PedidosListagem){
                ((PedidosListagem)f).OnBackPressed();
            }else if(f != null && f instanceof PrazoPagamento){
                ((PrazoPagamento)f).OnBackPressed();
            }else if(f != null && f instanceof ProdutosPedidoSelecionado){
                ((ProdutosPedidoSelecionado)f).OnBackPressed();
            }else{
                getFragmentManager().popBackStack();
            }

        }
    }

}
