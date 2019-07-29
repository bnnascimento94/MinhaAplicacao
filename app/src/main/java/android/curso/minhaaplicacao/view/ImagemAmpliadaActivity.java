package android.curso.minhaaplicacao.view;

import android.content.Intent;
import android.curso.minhaaplicacao.R;
import android.curso.minhaaplicacao.classes.ImageSaver;
import android.curso.minhaaplicacao.view.fragments.ImagemAmpliada;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.chrisbanes.photoview.PhotoView;

public class ImagemAmpliadaActivity extends AppCompatActivity {

    PhotoView photoView;
    private ImagemAmpliada.OnFragmentInteractionListener mListener;
    String nomeArquivo;
    String diretorio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagem_ampliada);
        photoView = findViewById(R.id.photo_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        nomeArquivo = (String) intent.getSerializableExtra("nomeArquivo");
        diretorio = (String) intent.getSerializableExtra("diretorio");

        Bitmap bitmap = new ImageSaver(this).
                setFileName(nomeArquivo).
                setDirectoryName(diretorio).
                load();
        photoView.setImageBitmap(bitmap);





    }


    }

