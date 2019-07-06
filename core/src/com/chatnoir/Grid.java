package com.chatnoir;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Grid {

    private Texture texture;
    private boolean[][] map;


    public Grid(Texture t){

        this.texture = t;
        this.map = new boolean[13][13];
    }


    public void draw(SpriteBatch batch){

        for(int i=0; i<map.length; i++){
            for(int j=0; j<map[i].length; j++){

            }
        }

    }


}
