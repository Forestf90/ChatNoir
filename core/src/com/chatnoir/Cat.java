package com.chatnoir;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Cat {

    Texture texture;

    int posX = 5;
    int posY = 5;

    public Cat(Texture t){

        this.texture =t;
    }

    public Texture getTexture() {
        return texture;
    }

    public void findPath(Sector[][] grid){

        dijkstra();
    }

    private void dijkstra(){


    }

}
