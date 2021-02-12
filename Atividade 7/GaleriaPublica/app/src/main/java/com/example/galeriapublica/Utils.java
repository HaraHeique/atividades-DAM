package com.example.galeriapublica;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.DisplayMetrics;

import java.io.FileNotFoundException;
import java.io.InputStream;

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

    public static Bitmap getBitmap(Context context, Uri imageLocation, int newWidth, int newHeight) throws FileNotFoundException {
        InputStream is = context.getContentResolver().openInputStream(imageLocation);

        // Pega o tamanho das dimensões do bitmap image
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, bmOptions);

        int photoWidth = bmOptions.outWidth;
        int photoHeight = bmOptions.outHeight;

        // Determina o tamanho da escala da imagem
        int scaleFactor = Math.max(photoWidth/newWidth, photoHeight/newHeight);

        // Decodifica o arquivo da imagem para bitmap no tamanho para preencher a view
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        is = context.getContentResolver().openInputStream(imageLocation);

        return BitmapFactory.decodeStream(is, null, bmOptions);
    }

    // Função para calcular a quantidade de colunas para saber quantas fotos cabem por linha
    public static int calculateNumberOfColumns(Context context, float columnWidth) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidth = displayMetrics.widthPixels;

        // Pega o tamanho total da tela e divide pelo tamanho de cada item
        return (int)(screenWidth / columnWidth + 0.5);
    }
}
