package android.curso.minhaaplicacao.view.fragments;


import android.curso.minhaaplicacao.classes.ImageSaver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.curso.minhaaplicacao.R;
import com.github.chrisbanes.photoview.PhotoView;




public class ImagemAmpliada extends Fragment {
    View view;
    PhotoView photoView;
    private OnFragmentInteractionListener mListener;
    String nomeArquivo;
    String diretorio;

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
