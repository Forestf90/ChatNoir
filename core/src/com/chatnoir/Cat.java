package com.chatnoir;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class Cat {

    Texture texture;

    int posX = 5;
    int posY = 5;

    ArrayList<Node> open;
    ArrayList<Node> visited;
    ArrayList<Node> path;

    public Cat(Texture t){

        this.texture =t;
        this.open = new ArrayList<Node>();
        this.visited = new ArrayList<Node>();
        this.path = new ArrayList<Node>();
    }

    public Texture getTexture() {
        return texture;
    }

    public void findPath(Sector[][] grid){

        Node[][] map = createGrid(grid);
        dijkstra(map);
    }
    private Node[][] createGrid(Sector[][] map){

        Node[][] grid = new Node[map.length][map[0].length];
        for(int i=0 ; i<map.length ; i++) {
            for(int j=0 ; j<map[i].length; j++) {
                grid[i][j] = new Node();
                if(!map[i][j].open) grid[i][j].open=false;
                else grid[i][j].open=true;
                grid[i][j].x = i;
                grid[i][j].y = j;

            }
        }

        return grid;
    }

    private void dijkstra(Node[][] grid){



    }

}
