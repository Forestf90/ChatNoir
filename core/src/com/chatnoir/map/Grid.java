package com.chatnoir.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.chatnoir.ai.Node;

import java.util.ArrayList;
import java.util.Random;

public class Grid {

    public Sector[][] map;

    public int WIDTH =40;
    public int HEIGHT =40;
    private int RADIUS =20;
    private int PADDING_W = 35;
    private int PADDING_H = 35;
    private int BORDER =20;
    private int iteration =0;
    private int iterationPath =0;
    public boolean animation= false;

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
                map[i][j].y = j* HEIGHT+PADDING_H+HEIGHT/2+BORDER*1.5f;
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
        sr.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
                Color.valueOf("a1c4fd"),
                Color.valueOf("a1c4fd"),
                Color.valueOf("c2e9fb"),
                Color.valueOf("c2e9fb"));

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

    public void drawAnimation(ShapeRenderer sr, ArrayList<Node> path,
                              ArrayList<Node> visited){
        if(!animation) return;

        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.valueOf("FFA07A"));

        for(int i =0 ;i<iteration; i++){
            Node n =visited.get(i);
            sr.circle(map[n.x][n.y].x, map[n.x][n.y].y, map[n.x][n.y].radius);
        }
        try
        {
            Thread.sleep(100);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
        if(iteration!=visited.size())iteration++;

        if(iteration==visited.size()){
            if(path==null){
                resetAnimation();
                sr.end();
                return;
            }
            if(iterationPath>path.size()){
                resetAnimation();
                sr.end();
                return;
            }
            sr.setColor(Color.valueOf("cc0000"));
            for(int i =0 ;i<iterationPath; i++){
                Node n =path.get(path.size()-i-1);
                sr.circle(map[n.x][n.y].x, map[n.x][n.y].y, map[n.x][n.y].radius);
            }
            try
            {
                Thread.sleep(100);
            }
            catch(InterruptedException ex)
            {
                Thread.currentThread().interrupt();
            }
            iterationPath++;

        }
        sr.end();
    }

    public void resetAnimation(){
        iteration =1;
        iterationPath=1;
        animation = false;
    }
}
