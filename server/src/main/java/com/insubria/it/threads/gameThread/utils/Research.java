package com.insubria.it.threads.gameThread.utils;


/**
 * This class has the logic defined to perform a research of a word into the "Il Paroliere" 4x4 matrix
 */
public class Research {
    /**
     * This is the "Il Paroliere" matrxi to look the word in
     */
    private String[][] matrix;

    /**
     * Constructor that set the matrix attribute
     * 
     * @param matrix
     */
    public Research (String[][] matrix) {
        this.matrix = matrix;
    }

    /**
     * This method returns a String that represents the part of the matrix where the (x, y) point is located
     * 
     * @param x - The x coordinate
     * @param y - The y coordinate
     * 
     * @return - The part of the matrix where the (x, y) point is located
     */
    private String getPointPosition (int x, int y) {
        if (x == 0 && y == 0) {
            return "angolo_alto_sinistro";
        } else if (x == 3 && y == 0) {
            return "angolo_basso_sinistro";
        } else if (x == 0 && y == 3) {
            return "angolo_alto_destro";
        } else if (x == 3 && y == 3) {
            return "angolo_basso_destro";
        } else if ((x == 1 && y == 0) || (x == 2 && y == 0)) {
            return "bordo_sinistro";
        } else if ((x == 1 && y == 3) || (x == 2 && y == 3)) {
            return "bordo_destro";
        } else if ((x == 0 && y== 1) || (x == 0 && y == 2)) {
            return "bordo_alto";
        } else if ((x == 3 && y == 1) || (x == 3 && y == 2)) {
            return "bordo_basso";
        } else {
            return "centro";
        }
    }

    /**
     * It checks wheter the (x, y) point of the visitedMatrix has been already visited.
     * 
     * @param x - The x coordinate
     * @param y - The y coordinate
     * @param visitedMatrix - The matrix to check in
     * 
     * @return - true if the point has been already visited (1), false otherwise
     */
    private boolean alreadyVisited (int x, int y, int[][] visitedMatrix) {
        return visitedMatrix[x][y] == 1;
    }

    /**
     * This method searches the toSearch string in the matrix attached to the object starting from the (x, y) position indicated. It leverages the visitedMatrix to understand whether a cell has been already visited.
     * 
     * @param toSearch - The string to search in the matrix
     * @param x - The x coordinate
     * @param y - The y coordinate
     * @param visitedMatrix - The matrix that is leveraged to understand whether a cell has been already visited.
     * 
     * @return - true if the string is in the matrix, false otherwise
     */
    private boolean makeResearch (String toSearch, int x, int y, int[][] visitedMatrix) {
        if (!toSearch.equals("")) {
            if (!this.alreadyVisited(x, y, visitedMatrix)) {
                if (this.matrix[x][y].equals("" + toSearch.charAt(0))) {
                    visitedMatrix[x][y] = 1;
                    StringBuilder sb = new StringBuilder(toSearch);
                    sb.deleteCharAt(0);

                    switch (this.getPointPosition(x, y)) {
                        case "angolo_alto_sinistro": {
                            return this.makeResearch(sb.toString(), x + 1, y, visitedMatrix) ||
                                   this.makeResearch(sb.toString(), x + 1, y + 1, visitedMatrix) ||
                                   this.makeResearch(sb.toString(), x, y + 1, visitedMatrix);
                        }
                        case "angolo_basso_sinistro": {
                            return this.makeResearch(sb.toString(), x - 1, y, visitedMatrix) ||
                                   this.makeResearch(sb.toString(), x - 1, y + 1, visitedMatrix) ||
                                   this.makeResearch(sb.toString(), x, y + 1, visitedMatrix);
                        }
                        case "angolo_alto_destro": {
                            return this.makeResearch(sb.toString(), x, y - 1, visitedMatrix) ||
                                   this.makeResearch(sb.toString(), x + 1, y - 1, visitedMatrix) ||
                                   this.makeResearch(sb.toString(), x + 1, y, visitedMatrix);
                        }
                        case "angolo_basso_destro": {
                            return this.makeResearch(sb.toString(), x, y - 1, visitedMatrix) ||
                                   this.makeResearch(sb.toString(), x - 1, y - 1, visitedMatrix) ||
                                   this.makeResearch(sb.toString(), x - 1, y, visitedMatrix);
                        }
                        case "bordo_sinistro": {
                            return this.makeResearch(sb.toString(), x - 1, y, visitedMatrix) ||
                                   this.makeResearch(sb.toString(), x - 1, y + 1, visitedMatrix) ||
                                   this.makeResearch(sb.toString(), x, y + 1, visitedMatrix) ||
                                   this.makeResearch(sb.toString(), x + 1, y + 1, visitedMatrix) ||
                                   this.makeResearch(sb.toString(), x + 1, y, visitedMatrix);
                        }
                        case "bordo_destro": {
                            return this.makeResearch(sb.toString(), x - 1, y, visitedMatrix) ||
                                   this.makeResearch(sb.toString(), x - 1, y - 1, visitedMatrix) ||
                                   this.makeResearch(sb.toString(), x, y - 1, visitedMatrix) ||
                                   this.makeResearch(sb.toString(), x - 1, y - 1, visitedMatrix) ||
                                   this.makeResearch(sb.toString(), x + 1, y, visitedMatrix);
                        }
                        case "bordo_alto": {
                            return this.makeResearch(sb.toString(), x, y - 1, visitedMatrix) ||
                                   this.makeResearch(sb.toString(), x + 1, y - 1, visitedMatrix) ||
                                   this.makeResearch(sb.toString(), x + 1, y, visitedMatrix) ||
                                   this.makeResearch(sb.toString(), x + 1, y + 1, visitedMatrix) ||
                                   this.makeResearch(sb.toString(), x, y + 1, visitedMatrix);
                        }
                        case "bordo_basso": {
                            return this.makeResearch(sb.toString(), x, y - 1, visitedMatrix) ||
                                   this.makeResearch(sb.toString(), x - 1, y - 1, visitedMatrix) ||
                                   this.makeResearch(sb.toString(), x - 1, y, visitedMatrix) ||
                                   this.makeResearch(sb.toString(), x - 1, y + 1, visitedMatrix) ||
                                   this.makeResearch(sb.toString(), x, y + 1, visitedMatrix);
                        }
                        case "centro": {
                            return this.makeResearch(sb.toString(), x, y - 1, visitedMatrix) ||
                                   this.makeResearch(sb.toString(), x, y + 1, visitedMatrix) ||
                                   this.makeResearch(sb.toString(), x - 1, y, visitedMatrix) ||
                                   this.makeResearch(sb.toString(), x + 1, y, visitedMatrix) ||
                                   this.makeResearch(sb.toString(), x - 1, y - 1, visitedMatrix) ||
                                   this.makeResearch(sb.toString(), x - 1, y + 1, visitedMatrix) ||
                                   this.makeResearch(sb.toString(), x + 1, y - 1, visitedMatrix) ||
                                   this.makeResearch(sb.toString(), x + 1, y + 1, visitedMatrix);
                        }
                        default: {
                            return false;
                        }
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * This method il called by the server to start the search
     * 
     * @param toSearch - The string to search in the matrix
     * @param x - The x coordinate
     * @param y - The y coordinate
     * 
     * @return - true if the string is in the matrix, false otherwise
     */
    public boolean startSearchProcess (String toSearch, int x, int y) {
        return this.makeResearch(toSearch, x, y, new int[4][4]);
    }
}