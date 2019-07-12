package android.curso.minhaaplicacao.view.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.curso.minhaaplicacao.classes.EscreverRecibo;
import android.curso.minhaaplicacao.model.Cliente;
import android.curso.minhaaplicacao.model.ItemPedido;
import android.curso.minhaaplicacao.model.Pedidos;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.curso.minhaaplicacao.R;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class Recibo extends Fragment {
    private static final int SOLICITAR_PERMISSAO = 1;
    Pedidos pedidos;
    View view;
    ImageView imgRecibo;
    Button compartilhar, cancelar;
    EscreverRecibo escrever;
    public Recibo() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
           pedidos =(Pedidos) bundle.getSerializable("pedido");

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_recibo, container, false);
         imgRecibo = view.findViewById(R.id.imgRecibo);
         compartilhar = view.findViewById(R.id.btnCompartilhar);
         escrever = new EscreverRecibo(pedidos,getActivity().getApplicationContext());
         imgRecibo.setImageBitmap(escrever.imagemRecibo());

         compartilhar.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 checarPermissao();
             }
         });


         return view;
    }

    private void checarPermissao(){

        // Verifica  o estado da permissão de WRITE_EXTERNAL_STORAGE
        int permissionCheck = ContextCompat.checkSelfPermission(this.getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);


        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            // Se for diferente de PERMISSION_GRANTED, então vamos exibir a tela padrão
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, SOLICITAR_PERMISSAO);
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
        String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), b, "Titulo da Imagem", null);
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
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);//Restaura o Activity

    }



}
