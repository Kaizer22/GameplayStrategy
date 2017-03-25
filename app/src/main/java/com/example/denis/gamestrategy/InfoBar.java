package com.example.denis.gamestrategy;

import android.graphics.Canvas;

/**
 * Created by denis on 28.02.17.
 */

public class InfoBar {

    public String message = new String();
    public int infomassage;
    public int width,height;
    public int x = 0,y;
    public  int textSize;
    private Texture texture;


    public InfoBar(int screenWidth,int screenHeight, ScreenManager scM, Texture tx){
        width = screenWidth;
        height = scM.cellHeight / 2;
        y = screenHeight - height;
        textSize = height / 4 * 3;
        texture = tx;
        texture.resizeTexture(width,height);
        texture = new Texture(tx.getBitmap());
    }

    public Texture getTexture(){return texture;}
}
