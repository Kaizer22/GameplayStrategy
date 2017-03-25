package com.example.denis.gamestrategy;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.view.View;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Created by denis on 19.02.17.
 */

public class GlobalMap {
    private Cell[][] map;
    private int maxX ,maxY ;


    public int getMaxX(){return maxX;}
    public int getMaxY(){return maxY;}

    public void updateParam(){
        maxX = map[0].length;
        maxY = map.length;
    }



    public Cell[][] getMap(){return map;}

    public void setCell(int Y,int X, Cell c){
        map[Y][X] = c;
    }

    public void setMarker(int Y,int X,boolean t){
        if(map[Y][X].unitOnIt == null)
            map[Y][X].someMarkerOnIt = t;
    }

    public void setTerritory(int Y,int X,boolean t){
            map[Y][X].isSomeonsTerritory = t;
    }


    public void setLine(int y,Cell[] c) {
        map[y] = c;
    }

    public void loadMap(int mY, int mX){                   //!!!!

        map = new Cell[mY][mX];

        for (int i = 0; i < mY ; i++) {
            for (int j = 0; j < mX; j++) {
                map[i][j] = new Cell();
            }
        }
    }

    public void loadMap(final AssetManager am, final Texture[] textures) {

        //количество клеток в строке и в столбце  в текстовом документе
                try{

                    InputStream in = am.open("map");
                    Scanner sc = new Scanner(in);
                    int buf;

                    maxX = sc.nextInt();
                    sc.nextLine();
                    maxY = sc.nextInt();
                    sc.nextLine();
                    map = new Cell[maxY][maxX];

                    while(sc.hasNext()) {

                        for (int j = 0; j < maxY; j++) {
                            for (int i = 0; i < maxX; i++) {

                                buf = sc.nextInt();
                                map[j][i] = new Cell();
                                //switch(buf){map[j][i].setTerrain();}


                                }
                            }
                            sc.nextLine();
                        }
                    sc.close();
                    in.close();

                }catch(FileNotFoundException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

    }

    public void generateMap(AssetManager am,int mXY){
        MapGenerator mg = new MapGenerator();
        mg.generateMap(am,mXY);
        updateParam();

    }

    public void setMap(Cell[][] m) {
        map = m;
        maxX = map[0].length;
        maxY = map.length;
    }


    private class MapGenerator {

        private void fillCell(Cell[][] map,int x,int y,int step,Cell.Terrain t){
            for (int i = y; i < y+step ; i++) {
                for (int j = x; j <x+step ; j++) {
                    map[i][j].setTerrain(t);
                }
            }

        }

        private void makePattern(final AssetManager am, Cell[][] map, int step){ //размеры карты должны быть кратны step
            try{

                InputStream in = am.open("map_pattern1");   //Pattern1
                Scanner sc = new Scanner(in);
                Cell.Terrain t ;
                int buf;
                for (int i = 0; i < map.length ; i += step) {
                    for (int j = 0; j < map[i].length; j += step) {
                        buf= sc.nextInt();
                        switch(buf){
                            case 0:
                                t = Cell.Terrain.WATER;
                                break;
                            case 1:
                                t = Cell.Terrain.HILLS;
                                break;
                            case 2:
                                t = Cell.Terrain.DESERT;
                                break;
                            case 3:
                                t = Cell.Terrain.SAVANNAH;
                                break;
                            case 4:
                                t = Cell.Terrain.JUNGLE;
                                break;
                            default:
                                t = Cell.Terrain.PEAKS;
                                break;
                        }
                        fillCell(map,j,i,step,t);

                    }

                }


            }catch(FileNotFoundException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        private void makeFractal(Cell[][] map, int step){
            Cell.Terrain t;
            int cy,cx;

            for (int i = 0; i < map.length ; i += step) {
                for (int j = 0; j < map[i].length ; j += step) {

                    cy = i+ ((Math.random()<0.5)  ? 0 : step); // случайное смещение и получение значения ландшафта
                    cx = j+ ((Math.random()<0.5) ? 0 : step);

                    if (cy == map.length)
                        cy-=step;
                    if(cx == map[i].length)
                        cx-=step;

                    t = map[cy][cx].getTerrain();
                    fillCell(map,j,i,step,t);
                }
            }

            if (step > 1)
                makeFractal(map,step/2); //контроль детализации

        }

        public void generateMap(AssetManager am,int mXY) {
            int step = mXY / 8;
            map = new Cell[mXY][mXY];

            for (int i = 0; i < mXY; i++) {
                for (int j = 0; j < mXY; j++) {
                    map[i][j] = new Cell();
                }
            }
            makePattern(am,map,step); //подготовка шаблона с заданной детализацией
            step /= 2;                //увеличение детализации
            makeFractal(map,step);    //рекурсивное построение фрактала

        }
    }

}