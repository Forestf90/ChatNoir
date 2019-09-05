package com.chatnoir.ai;

import com.badlogic.gdx.graphics.Texture;
import com.chatnoir.map.Sector;

import java.util.ArrayList;

public class Cat {

    private Texture texture;


    private int posX = 5;
    private int posY = 5;

    private ArrayList<Node> open;
    private ArrayList<Node> visited;
    private ArrayList<Node> path;

    public ArrayList<Node> getOpen() {
        return open;
    }

    public ArrayList<Node> getVisited() {
        return visited;
    }

    public ArrayList<Node> getPath() {
        return path;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public Cat(Texture t) {

        this.texture = t;
        this.open = new ArrayList<Node>();
        this.visited = new ArrayList<Node>();
        this.path = new ArrayList<Node>();
    }

    public Texture getTexture() {
        return texture;
    }

    public void makeMove() {
        Node temp = path.get(path.size() - 2);
        posX = temp.x;
        posY = temp.y;
        System.out.println("Path: " + path.size());
        System.out.println("X:" + posX);
        System.out.println("Y:" + posY);
        open.clear();
        visited.clear();
        path.clear();
    }

    public boolean runAway(Sector[][] grid) {

        if (posX == 0 || posX == grid.length - 1) return true;
        else if (posY == 0 || posY == grid[0].length - 1) return true;
        else return false;
    }

    public void findPath(Sector[][] grid) {

        Node[][] map = createGrid(grid);
        dijkstra(map);
    }

    private Node[][] createGrid(Sector[][] map) {

        Node[][] grid = new Node[map.length][map[0].length];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                grid[i][j] = new Node();
                if (!map[i][j].open) grid[i][j].open = false;
                else grid[i][j].open = true;
                grid[i][j].x = i;
                grid[i][j].y = j;

            }
        }

        return grid;
    }

    private void dijkstra(Node[][] grid) {
        Node aktualny = grid[posX][posY];
        ArrayList<Node> trasa = new ArrayList<Node>();

        int[] igreki = {0, -1, -1, 0, 1, 1};
        int[] temp1_iksy = {-1, -1, 0, 1, -1, 0};
        int[] temp2_iksy = {-1, 0, 1, 1, 0, 1};
        int[] iksy;

        while (true) {
            visited.add(aktualny);


            if (aktualny.y % 2 == 0) iksy = temp1_iksy;
            else iksy = temp2_iksy;
            for (int k = 0; k < iksy.length; k++) {
                int j = igreki[k];
                int i = iksy[k];

                if (aktualny.x + i < grid.length && aktualny.y + j < grid.length &&
                        aktualny.x + i > -1 && aktualny.y + j > -1) {
                    if (grid[aktualny.x + i][aktualny.y + j].open && !visited.contains(grid[aktualny.x + i][aktualny.y + j])) {
                        if (open.contains(grid[aktualny.x + i][aktualny.y + j])) {
                            if (grid[aktualny.x + i][aktualny.y + j].parent.weight > grid[aktualny.x][aktualny.y].weight + 10) {
                                grid[aktualny.x + i][aktualny.y + j].parent = aktualny;
                                grid[aktualny.x + i][aktualny.y + j].weight = aktualny.weight + 10;
                            }
                        } else {
                            grid[aktualny.x + i][aktualny.y + j].parent = aktualny;
                            open.add(grid[aktualny.x + i][aktualny.y + j]);
                            grid[aktualny.x + i][aktualny.y + j].weight = aktualny.weight + 10;
                        }
                    }
                }
            }

            if (open.size() == 0) {
                trasa = null;
                //   open.removeAll(open);
                //   visited.removeAll(visited);
                iksy = null;
                this.path = trasa;
                return;
            }

            Node temp = open.get(0);
            for (Node n : open) {

                if (n.weight < temp.weight) temp = n;

            }
            open.remove(temp);
            aktualny = temp;


            if (aktualny.x == 0 || aktualny.x == grid.length - 1 || aktualny.y == 0 || aktualny.y == grid.length - 1) {
                Node temp2 = aktualny;
                visited.add(temp2);
                trasa.add(temp2);
                while (true) {
                    if (temp2.parent == null) break;
                    temp2 = temp2.parent;
                    trasa.add(temp2);

                }
                //  open.removeAll(open);
                // visited.removeAll(visited);
                iksy = null;
                this.path = trasa;
                return;


            }

        }
    }
}
