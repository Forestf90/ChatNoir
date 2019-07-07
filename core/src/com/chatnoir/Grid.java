package com.chatnoir;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.Random;

public class Grid {

    public Sector[][] map;

    int WIDTH =40;
    int HEIGHT =40;
    int RADIUS =20;
    int PADDING_W = 35;
    int PADDING_H = 35;
    int BORDER =20;

    private static final int SIZE_W =11;
    private static final int SIZE_H =11;


    public Grid(){
        this.map = new Sector[SIZE_W][SIZE_H];
        calculateBorder();
        init_map();
    }

    private void init_map(){
        int BLOCKS =15;

        for(int i=0; i<map.length; i++){
            for(int j=0; j<map[i].length; j++){
                map[i][j] = new Sector();
                map[i][j].x=i *WIDTH+(PADDING_W+(j%2*WIDTH/2)+WIDTH/2);
                map[i][j].y = j* HEIGHT+PADDING_H+HEIGHT/2+BORDER;
                map[i][j].radius =RADIUS;
            }
        }
        Random r= new Random();
        int x=0 ,y=0;
        for(int i=0 ;i<BLOCKS;i++) {
            x = r.nextInt(SIZE_W);
            y = r.nextInt(SIZE_H);
            if((x==(SIZE_W-1)/2 && y==(SIZE_H-1)/2) || !map[y][x].open){
                i--;
                continue;
            }
            map[x][y].open=false;

        }
    }

    private void calculateBorder(){
        int wid = Gdx.graphics.getWidth();
        int hei = Gdx.graphics.getHeight();

        WIDTH = wid/(SIZE_W+2);
        HEIGHT = wid/(SIZE_W+2);
        PADDING_H = wid/(SIZE_W+2);
        PADDING_W = wid/(SIZE_W+2);

        RADIUS = (wid/(SIZE_W+2)/2);

        BORDER = (hei- wid)/2;

    }

    public void draw(ShapeRenderer sr){

        sr.begin(ShapeRenderer.ShapeType.Filled);
        for(int i=0; i<map.length; i++){
            for(int j=0; j<map[i].length; j++){
                if(map[i][j].open){
                    sr.setColor(Color.valueOf("c7ea46"));
                }else{
                    sr.setColor(Color.valueOf("718500"));
                }
                sr.circle(map[i][j].x, map[i][j].y, map[i][j].radius);

            }
        }
        sr.end();

    }

    public void drawAnimation(ShapeRenderer sr, ArrayList<Node> open, ArrayList<Node> visited){

        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.valueOf("FFA07A"));
        for(Node n: open){
            sr.circle(map[n.x][n.y].x, map[n.x][n.y].y, map[n.x][n.y].radius);
        }
        sr.setColor(Color.valueOf("8B0000"));
        for(Node n: visited){
            sr.circle(map[n.x][n.y].x, map[n.x][n.y].y, map[n.x][n.y].radius);
        }
        sr.end();
    }


}
