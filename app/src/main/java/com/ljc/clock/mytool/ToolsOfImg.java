package com.ljc.clock.mytool;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * 这是一个操作图片的工具类
 * 本类提供了各种方法，用对对图片进行旋转缩放等操作
 */

public class ToolsOfImg {
    /*
    这个方法用于旋转图片，传入两参数，第一个是图片，第二个是旋转度数
    不过用这方法实现旋转，效果并不比用ImageView本身的setRotation()方法做的效果好
     */
    public static Bitmap setRotate(Bitmap bitmap,int degrees){
        int widthOrig = bitmap.getWidth();
        int heightOrig = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, widthOrig, heightOrig, matrix, true);
        return newBitmap;
    }

    /*
    这个方法用于缩放图片，传入两参数，第一个是图片，第二个是水平缩放比例，第三个是竖直缩放比例
     */
    public static Bitmap setScale(Bitmap bitmap,float scaleWidth,float scaleHeight){
        int widthOrig = bitmap.getWidth();
        int heightOrig = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth,scaleHeight);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, widthOrig, heightOrig, matrix, true);
        return newBitmap;
    }
}