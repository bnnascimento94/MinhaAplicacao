package android.curso.minhaaplicacao.classes;

import android.content.Context;
import android.curso.minhaaplicacao.R;
import android.curso.minhaaplicacao.controller.ControlePedidos;
import android.curso.minhaaplicacao.model.ItemPedido;
import android.curso.minhaaplicacao.model.Pedidos;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;

public class EscreverRecibo {
    Pedidos pedidos;
    ControlePedidos cp;
    Context context;
    int imageTargetViewWidth;
    int imageTargetViewHeight;
    private static final float GESTURE_THRESHOLD_DIP = 10.0f;
    public EscreverRecibo(Pedidos pedidos,Context context, int imageTargetViewHeight, int imageTargetViewWidth){
        this.context = context;
        this.pedidos = pedidos;
        this.imageTargetViewHeight = imageTargetViewHeight;
        this.imageTargetViewWidth = imageTargetViewWidth;
        cp = new ControlePedidos(context);
    }

    public Bitmap imagemRecibo(){
        ;
       // Bitmap bitmap = .. // Load your bitmap here
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.recibo).copy(Bitmap.Config.ARGB_8888, true);;


        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        int altura = bitmap.getHeight();
        int comprimento = bitmap.getWidth();
        paint.setColor(Color.BLACK);

// Convert the dips to pixels
        final float scale = context.getResources().getDisplayMetrics().density;

        paint.setTextSize(27);
        SimpleDateFormat dataFormatada = new SimpleDateFormat("dd/MM/yyyy");
        canvas.drawText("Recibo de Venda          Data Venda:"+dataFormatada.format(pedidos.getData()), 50, 150, paint);
        canvas.drawText("Cliente: "+pedidos.getCliente().getNomeCliente(), 50, 180, paint);
        canvas.drawText("Condição: "+pedidos.getCondicoesPagamento().getNomeCondiçãoPagamento(), 50, 210, paint);
        canvas.drawText("Prazo: "+pedidos.getPrazosPagamento().getNomePrazoPagamento(), 50, 240, paint);
        canvas.drawText("Valor Total: R$ "+Double.toString(pedidos.getValorTotal()), 50, 270, paint);
        canvas.drawText("=======================================", 50, 300, paint);
        if(pedidos.getItensPedido().size()>0){
            int y = 330;

            for(ItemPedido item : pedidos.getItensPedido()){
                canvas.drawText(item.getProduto().getNomeProduto(), 50, y, paint);
                canvas.drawText("Qtde: "+item.getQtde(), 269, y, paint);
                canvas.drawText("Valor: "+"R$"+Double.toString(item.getItemValorVenda()), 460, y, paint);
                y = y + 25;
            }
        }


        Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);

        return bitmap;
    }




}
