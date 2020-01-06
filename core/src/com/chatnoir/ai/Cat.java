package com.chatnoir.ai;

import com.badlogic.gdx.graphics.Texture;
import com.chatnoir.Algorithm;
import com.chatnoir.map.Sector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Cat {

    private Texture texture;


    private int posX = 5;
    private int posY = 5;

    private int lastX = 5;
    private int lastY = 5;

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
        if (path.isEmpty()) return;
        Node temp = path.get(path.size() - 2);
        lastX = posX;
        lastY = posY;
        posX = temp.getX();
        posY = temp.getY();
        open.clear();
        visited.clear();
        path.clear();
    }

    public void undoMove() {
        posX = lastX;
        posY = lastY;
    }

    public boolean runAway(Sector[][] grid) {

        if (posX == 0 || posX == grid.length - 1) return true;
        else if (posY == 0 || posY == grid[0].length - 1) return true;
        else return false;
    }

    public void findPath(Sector[][] grid, Algorithm algorithm) {

        long d1 = System.nanoTime();
        Node[][] map = createGrid(grid);
        if (algorithm == Algorithm.BFS) bfs(map);
        else {

            Node[][] tempGrid = calculateHeuristic(map, posX, posY);

            List<Node> endNodes = new ArrayList<Node>();

            for (int i = 0; i < tempGrid.length; i++) {
                endNodes.add(tempGrid[i][0]);
                endNodes.add(tempGrid[i][tempGrid.length - 1]);
            }

            for (int i = 1; i < tempGrid.length - 1; i++) {
                endNodes.add(tempGrid[0][i]);
                endNodes.add(tempGrid[tempGrid.length - 1][i]);
            }

            Collections.sort(endNodes, new Comparator<Node>() {
                @Override
                public int compare(Node n1, Node n2) {
                    return n1.getWeight() - n2.getWeight();
                }
            });

            int min = endNodes.get(0).getWeight();
            ArrayList<Node> minPath = null;
            ArrayList<Node> minVisited = new ArrayList<Node>();
            for (Node n : endNodes) {
                if (n.getWeight() > min) {
                    min = n.getWeight();
                    if (minPath != null) {
                        if (minPath.size() <= min + 1) {
                            this.path = minPath;
                            this.visited = minVisited;
                            break;
                        }
                    }
                }

                Node[][] tempMap = createGrid(grid);
                calculateHeuristic(tempMap, n.getX(), n.getY());
                ArrayList<Node> currentPath = astar(tempMap, tempMap[n.getX()][n.getY()]);

                if (currentPath == null) {
                    this.open.clear();
                    this.visited.clear();
                    this.path = null;
                    continue;
                }
                if (minPath == null) {
                    minPath = currentPath;
                    minVisited.addAll(this.visited);
                } else if (currentPath.size() < minPath.size()) {
                    minPath = currentPath;
                    minVisited.addAll(this.visited);
                }

                if (minPath.size() <= min + 1) {
                    this.path = minPath;
                    this.visited = minVisited;
                    break;
                } else {
                    this.open.clear();
                    this.visited.clear();
                }
            }
            if (this.path == null) {
                this.visited = minVisited;
                if (minPath != null) {
                    this.path = minPath;
                }
            }

        }

        long d2 = System.nanoTime();
        long diff = d2 - d1;

        // long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);

        System.out.println(algorithm.toString() + ": " + diff);
    }

    private Node[][] createGrid(Sector[][] map) {

        Node[][] grid = new Node[map.length][map[0].length];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                grid[i][j] = new Node();
                grid[i][j].setOpen(map[i][j].open);
                grid[i][j].setX(i);
                grid[i][j].setY(j);
            }
        }
        return grid;
    }

    private Node[][] calculateHeuristic(Node[][] grid, int endX, int endY) {

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {

                int odd = 0;
                if ((j % 2 == 0 && endY % 2 == 1 && i < endX) || (endY % 2 == 0 && j % 2 == 1 && endX < i))
                    odd = 1;
                int dx = Math.abs(endX - i);
                int dy = Math.abs(endY - j);

                grid[i][j].setWeight(Math.max(dy, dx + (int) Math.floor(dy / 2) + odd));
            }
        }
        return grid;
    }

    private void bfs(Node[][] grid) {
        Node currentNode = grid[posX][posY];
        ArrayList<Node> route = new ArrayList<Node>();

        int[] yDirection = {0, -1, -1, 0, 1, 1};
        int[] xDirectionEven = {-1, -1, 0, 1, -1, 0};
        int[] xDirectionNotEven = {-1, 0, 1, 1, 0, 1};
        int[] xDirection;

        while (true) {
            visited.add(currentNode);
            int x = currentNode.getX();
            int y = currentNode.getY();

            if (y % 2 == 0) xDirection = xDirectionEven;
            else xDirection = xDirectionNotEven;
            for (int k = 0; k < xDirection.length; k++) {
                int j = yDirection[k];
                int i = xDirection[k];

                if (x + i < grid.length && y + j < grid.length &&
                        x + i > -1 && y + j > -1) {
                    if (grid[x + i][y + j].isOpen() && !visited.contains(grid[x + i][y + j])) {
                        if (!open.contains(grid[x + i][y + j])) {
                            grid[x + i][y + j].setParent(currentNode);
                            open.add(grid[x + i][y + j]);
                        }
                    }
                }
            }

            if (open.size() == 0) {
                this.path = null;
                return;
            }

            Node nextNode = open.get(0);
            open.remove(nextNode);
            currentNode = nextNode;


            if (currentNode.getX() == 0 || currentNode.getX() == grid.length - 1 ||
                    currentNode.getY() == 0 || currentNode.getY() == grid.length - 1) {
                Node lastPathNode = currentNode;
                visited.add(lastPathNode);
                route.add(lastPathNode);
                while (true) {
                    if (lastPathNode.getParent() == null) break;
                    lastPathNode = lastPathNode.getParent();
                    route.add(lastPathNode);

                }
                this.path = route;
                return;


            }

        }
    }

    private ArrayList<Node> astar(Node[][] grid, Node end) {
        Node currentNode = grid[posX][posY];
        ArrayList<Node> route = new ArrayList<Node>();

        int[] yDirection = {0, -1, -1, 0, 1, 1};
        int[] xDirectionEven = {-1, -1, 0, 1, -1, 0};
        int[] xDirectionNotEven = {-1, 0, 1, 1, 0, 1};
        int[] xDirection;

        while (true) {
            visited.add(currentNode);
            int x = currentNode.getX();
            int y = currentNode.getY();

            if (y % 2 == 0) xDirection = xDirectionEven;
            else xDirection = xDirectionNotEven;
            for (int k = 0; k < xDirection.length; k++) {
                int j = yDirection[k];
                int i = xDirection[k];

                if (x + i < grid.length && y + j < grid.length &&
                        x + i > -1 && y + j > -1) {
                    if (grid[x + i][y + j].isOpen() && !visited.contains(grid[x + i][y + j])) {
                        if (open.contains(grid[x + i][y + j])) {
                            if (grid[x + i][y + j].getWeight() < grid[x][y].getWeight()) {
                                grid[x + i][y + j].setParent(currentNode);
                            }
                        } else {
                            grid[x + i][y + j].setParent(currentNode);
                            open.add(grid[x + i][y + j]);
                        }
                    }
                }
            }

            if (open.size() == 0) {
                return null;
            }

            Node nextNode = open.get(0);
            for (Node n : open) {

                if (n.getWeight() < nextNode.getWeight()) nextNode = n;

            }
            open.remove(nextNode);
            currentNode = nextNode;


            if (currentNode == end) {
                Node lastPathNode = currentNode;
                visited.add(lastPathNode);
                route.add(lastPathNode);
                while (true) {
                    if (lastPathNode.getParent() == null) break;
                    lastPathNode = lastPathNode.getParent();
                    route.add(lastPathNode);

                }
                return route;
            }

        }
    }
}
