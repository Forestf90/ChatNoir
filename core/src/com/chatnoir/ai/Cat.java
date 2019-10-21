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
//                if (map[i][j].open) grid[i][j].open = true;
//                else grid[i][j].open = false;
                grid[i][j].open = map[i][j].open;
                grid[i][j].x = i;
                grid[i][j].y = j;

            }
        }

        return grid;
    }

    private void dijkstra(Node[][] grid) {
        Node currentNode = grid[posX][posY];
        ArrayList<Node> route = new ArrayList<Node>();

        int[] yDirection = {0, -1, -1, 0, 1, 1};
        int[] xDirectionEven = {-1, -1, 0, 1, -1, 0};
        int[] xDirectionNotEven = {-1, 0, 1, 1, 0, 1};
        int[] xDirection;

        while (true) {
            visited.add(currentNode);


            if (currentNode.y % 2 == 0) xDirection = xDirectionEven;
            else xDirection = xDirectionNotEven;
            for (int k = 0; k < xDirection.length; k++) {
                int j = yDirection[k];
                int i = xDirection[k];

                if (currentNode.x + i < grid.length && currentNode.y + j < grid.length &&
                        currentNode.x + i > -1 && currentNode.y + j > -1) {
                    if (grid[currentNode.x + i][currentNode.y + j].open && !visited.contains(grid[currentNode.x + i][currentNode.y + j])) {
                        if (open.contains(grid[currentNode.x + i][currentNode.y + j])) {
                            if (grid[currentNode.x + i][currentNode.y + j].parent.weight > grid[currentNode.x][currentNode.y].weight + 10) {
                                grid[currentNode.x + i][currentNode.y + j].parent = currentNode;
                                grid[currentNode.x + i][currentNode.y + j].weight = currentNode.weight + 10;
                            }
                        } else {
                            grid[currentNode.x + i][currentNode.y + j].parent = currentNode;
                            open.add(grid[currentNode.x + i][currentNode.y + j]);
                            grid[currentNode.x + i][currentNode.y + j].weight = currentNode.weight + 10;
                        }
                    }
                }
            }

            if (open.size() == 0) {
                this.path = null;
                return;
            }

            Node nextNode = open.get(0);
            for (Node n : open) {

                if (n.weight < nextNode.weight) nextNode = n;

            }
            open.remove(nextNode);
            currentNode = nextNode;


            if (currentNode.x == 0 || currentNode.x == grid.length - 1 || currentNode.y == 0 || currentNode.y == grid.length - 1) {
                Node lastPathNode = currentNode;
                visited.add(lastPathNode);
                route.add(lastPathNode);
                while (true) {
                    if (lastPathNode.parent == null) break;
                    lastPathNode = lastPathNode.parent;
                    route.add(lastPathNode);

                }
                this.path = route;
                return;


            }

        }
    }
}