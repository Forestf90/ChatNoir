package com.chatnoir;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Random;

public class Grid {

    private boolean[][] map;

    int WIDTH =40;
    int HEIGHT =40;
    int RADIUS =20;
    int PADDING_W = 35;
    int PADDING_H = 35;
    int BORDER =20;


    public Grid(){
        this.map = new boolean[13][13];
        calculateBorder();
        init_map();
    }

    private void init_map(){
        int BLOCKS =15;

        for(int i=0; i<map.length; i++){
            for(int j=0; j<map[i].length; j++){
                map[i][j] = true;
            }
        }
        Random r= new Random();
        int x=0 ,y=0;
        for(int i=0 ;i<BLOCKS;i++) {
            x= r.nextInt(11);
            y=r.nextInt(11);
            if((x==7 && y==7) || !map[y][x]){
                i--;
                continue;
            }
            map[x][y]=false;

        }
    }

    private void calculateBorder(){
        int wid = Gdx.graphics.getWidth();
        int hei = Gdx.graphics.getHeight();

        WIDTH = wid/15;
        HEIGHT = wid/15;
        PADDING_H = wid/15;
        PADDING_W = wid/15;

        RADIUS = (wid/15)/2;

        BORDER = (hei- wid)/2;

    }

    public void draw(ShapeRenderer sr){




        for(int i=0; i<map.length; i++){
            for(int j=0; j<map[i].length; j++){
                if(map[i][j]){
                    sr.setColor(Color.valueOf("c7ea46"));
                }else{
                    sr.setColor(Color.valueOf("718500"));
                }
                sr.circle(i *WIDTH+(PADDING_W+(j%2*WIDTH/2)+WIDTH/2),
                        j* HEIGHT+PADDING_H+HEIGHT/2+BORDER, RADIUS);

            }
        }

    }


}
