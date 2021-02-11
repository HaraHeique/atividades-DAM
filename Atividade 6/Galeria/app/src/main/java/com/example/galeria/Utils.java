package com.example.galeria;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.Display;

public class Utils {
    // Função para escalar imagem e reduzir a quantidade de memória usada
    public static Bitmap getScaledBitmap(String imagePath, int width, int height) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        // Carregando info a cerca da imagem (altura, largura, espaço que ocupa)
        bmOptions.inJustDecodeBounds = true;

        // Carregar as informações do arquivo no bmOptions
        BitmapFactory.decodeFile(imagePath, bmOptions);

        // Lógica para diminuição da escala imagem
        int photoWidth = bmOptions.outWidth;
        int photoHeight = bmOptions.outHeight;

        int scaleFactor = Math.max(photoWidth/width, photoHeight/height);

        // Atribuição do fator de escala calculado acima
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeFile(imagePath, bmOptions);
    }

    // Função que retorna o bitmap do path da imagem passado como argumento
    public static Bitmap getBitmap(String imagePath) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();

        // Atribuição do fator de escala calculado acima
        bmOptions.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(imagePath, bmOptions);
    }

    // Função para calcular a quantidade de colunas para saber quantas fotos cabem por linha
    public static int calculateNumberOfColumns(Context context, float columnWidth) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidth = displayMetrics.widthPixels;

        // Pega o tamanho total da tela e divide pelo tamanho de cada item
        return (int)(screenWidth / columnWidth + 0.5);
    }
}
