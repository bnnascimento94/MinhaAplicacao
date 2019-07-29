package android.curso.minhaaplicacao.view.fragments;


import android.curso.minhaaplicacao.classes.ImageSaver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.curso.minhaaplicacao.R;
import android.support.v7.widget.Toolbar;

import com.github.chrisbanes.photoview.PhotoView;

import java.util.HashMap;
import java.util.List;


public class ImagemAmpliada extends Fragment {
    View view;
    PhotoView photoView;
    private OnFragmentInteractionListener mListener;
    String nomeArquivo;
    String diretorio;
    public List<String> listGroup;
    public HashMap<String, List<String>> listData;
    FragmentManager fragmentManager;

    public ImagemAmpliada() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {

            nomeArquivo = (String) bundle.getSerializable("nomeArquivo") ;
            diretorio = (String) bundle.getSerializable("diretorio");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_imagem_ampliada, container, false);
        photoView = view.findViewById(R.id.photo_view);
        //photoView.setImageResource(R.drawable.image);


        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Detalhes Produto");
        Toolbar bar=Toolbar.class.cast(getActivity().findViewById(R.id.toolbar));

        ( (AppCompatActivity)getActivity()).getSupportActionBar();



        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        Bitmap bitmap = new ImageSaver(getContext()).
                setFileName(nomeArquivo).
                setDirectoryName(diretorio).
                load();
        photoView.setImageBitmap(bitmap);


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
