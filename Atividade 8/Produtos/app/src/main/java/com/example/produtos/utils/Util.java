package com.example.produtos.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.DisplayMetrics;

import java.io.FileNotFoundException;
import java.io.InputStream;

public final class Util {
    /**
     * Calcula o numero de colunas que cabem na tela ao usar o tipo de
     * visualizacao GRID no RecycleView
     *
     * @param context contexto utilizado, geralmente instancia da activity que
     *                contem o RecycleView
     * @param columnWidthDp largura do item do grid em dp
     * @return numero de colunas a serem usadas no GridLayoutManager
     */
    public static int calculateNoOfColumns(Context context, float columnWidthDp) { // For example columnWidthdp=180
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels;
        int noOfColumns = (int) (screenWidthDp / columnWidthDp + 0.5); // +0.5 for correct rounding to int.
        return noOfColumns;
    }

    /**
     * Gera um bitmap de tamanho definido
     * @param imagePath caminho local de onde esta localizado o arquivo de imagem
     * @param w largura que a imagem deve ter
     * @param h altura que a imagem deve ter
     * @return o bitmap
     */
    public static Bitmap getBitmap(String imagePath, int w, int h) {

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.max(photoW/w, photoH/h);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeFile(imagePath, bmOptions);
    }

    /**
     * Gera um bitmap sem realizar qualquer tipo de escala
     * @param imagePath caminho local de onde esta localizado o arquivo de imagem
     * @return o bitmap
     */
    public static Bitmap getBitmap(String imagePath) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imagePath, bmOptions);
    }

    /**
     * Gera um bitmap a partir de um URI. Essa função deve ser usada sempre que
     * o arquivo de imagem nao esta armazenado no espaço local da app. Por exemplo,
     * se a imagem está armazenada no espaço público do celular, essa função deve ser
     * utilizada. A imagem sofrerá escala de acordo com as dimensões escohidas.
     * @param context contexto utilizado, geralmente instancia da activity que
     *                chama a função
     * @param imageLocation endereço URI da imagem
     * @param w largura que a imagem deve ter
     * @param h altura que a imagem deve ter
     * @return o bitmap
     * @throws FileNotFoundException caso a imagem não seja achada
     */
    public static Bitmap getBitmap(Context context, Uri imageLocation, int w, int h) throws FileNotFoundException {

        InputStream is = context.getContentResolver().openInputStream(imageLocation);

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.max(photoW/w, photoH/h);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        is = context.getContentResolver().openInputStream(imageLocation);
        return BitmapFactory.decodeStream(is, null, bmOptions);
    }

    public static Bitmap base64ToBitmap(String imgData) {
        imgData = imgData.substring(imgData.indexOf(",") + 1);
        byte[] imageAsBytes = Base64.decode(imgData.getBytes(), Base64.DEFAULT);

        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }
}
