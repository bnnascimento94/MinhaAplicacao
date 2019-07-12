package android.curso.minhaaplicacao.classes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import java.io.IOException;

public class CarregadorDeFoto {
    public static Bitmap carrega(String caminhoFoto) throws IOException {
        Bitmap foto = null;
        ExifInterface exif = new ExifInterface(caminhoFoto);
        String orientacao = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int codigoOrientacao = Integer.parseInt(orientacao);

        switch (codigoOrientacao) {
            case ExifInterface.ORIENTATION_NORMAL:
                foto = abreFotoERotaciona(caminhoFoto, 0);
            case ExifInterface.ORIENTATION_ROTATE_90:
                foto = abreFotoERotaciona(caminhoFoto, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                foto = abreFotoERotaciona(caminhoFoto, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                foto = abreFotoERotaciona(caminhoFoto, 270);
        }
        return foto;
    }

    private static Bitmap abreFotoERotaciona(String caminhoFoto, int angulo) {
        // Abre o bitmap a partir do caminho da foto
        Bitmap bitmap = BitmapFactory.decodeFile(caminhoFoto);

        // Prepara a operação de rotação com o ângulo escolhido
        Matrix matrix = new Matrix();
        matrix.postRotate(angulo);

        // Cria um novo bitmap a partir do original já com a rotação aplicada
        return Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(),
                matrix, true);
    }
}
