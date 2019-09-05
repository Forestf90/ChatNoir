package com.chatnoir;

public enum Algorithm {
    Dijkstra,
    AStar{
        public String toString() {
            return "A*";
        }
    },
    DFS

}
