package com.chatnoir;

public enum Algorithm {
    BFS,
    AStar{
        public String toString() {
            return "A*";
        }
    }

}
