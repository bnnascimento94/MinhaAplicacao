package android.curso.minhaaplicacao.view;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.curso.minhaaplicacao.R;
import android.curso.minhaaplicacao.classes.EscreverRecibo;
import android.curso.minhaaplicacao.model.Pedidos;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class ReciboActivity extends AppCompatActivity {
    private static final int SOLICITAR_PERMISSAO = 1;
    Pedidos pedidos;

    ImageView imgRecibo;
    Button compartilhar;
    EscreverRecibo escrever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recibo);
        Intent intent = getIntent();
        pedidos = (Pedidos) intent.getSerializableExtra("pedido");
        imgRecibo = findViewById(R.id.imgRecibo);
        compartilhar = findViewById(R.id.btnCompartilhar);
        escrever = new EscreverRecibo(pedidos,getApplicationContext(),imgRecibo.getHeight(),imgRecibo.getWidth());
        imgRecibo.setImageBitmap(escrever.imagemRecibo());


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Recibo");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentVaiProFormulario = new Intent(getApplicationContext(), TelaPrincipalActivity.class );
                startActivity(intentVaiProFormulario);
            }
        });

        compartilhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checarPermissao();
            }
        });
    }

    private void checarPermissao(){

        // Verifica  o estado da permissão de WRITE_EXTERNAL_STORAGE
        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);


        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            // Se for diferente de PERMISSION_GRANTED, então vamos exibir a tela padrão
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, SOLICITAR_PERMISSAO);
        } else {
            // Senão vamos compartilhar a imagem
            sharedImage();
        }
    }

    private void sharedImage(){
        // Vamos carregar a imagem em um bitmap
        Bitmap b = escrever.imagemRecibo();
        Intent share = new Intent(Intent.ACTION_SEND);
        //setamos o tipo da imagem
        share.setType("image/jpeg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        // comprimomos a imagem
        b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        // Gravamos a imagem
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), b, "Titulo da Imagem", null);
        // criamos uam Uri com o endereço que a imagem foi salva
        Uri imageUri =  Uri.parse(path);
        // Setmaos a Uri da imagem
        share.putExtra(Intent.EXTRA_STREAM, imageUri);
        // chama o compartilhamento
        startActivity(Intent.createChooser(share, "Selecione"));
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);//Salva Activity
    }
}
