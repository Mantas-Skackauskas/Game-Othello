import java.util.ArrayList;

/**
 * Created by va29 on 27/03/17.
 */
public class Grid {

    static final byte HEIGHT = 8;
    static final byte WIDTH = 8;

    public static final String GREEN = "\u001B[42m";
    public static final String ANSI_RESET = "\u001B[0m";

    public States[][] grid = new States[HEIGHT][WIDTH];

    //created for checking all the 8 squares around the square
    public int[][] directions = {
            {0, -1},     // N
            {1, -1},     //NE
            {1, 0},     //E
            {1, 1},     //SE
            {0, 1},     //S
            {-1, 1},    //SW
            {-1, 0},    //W
            {-1, -1}    //NW
    };


    public Grid() {

        createGrid();

    }


    public States getGrid(int x, int y) {
        return grid[x][y];
    }


    //method to change the color of the grid point
    public void setGridColor(int x, int y, States color) {

        grid[x][y] = color;

    }


    //grid is created
    public void createGrid() {

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                grid[i][j] = States.NOTHING;
            }
        }

        //initial points are added to the initial grid
        grid[3][3] = States.WHITE;
        grid[4][4] = States.WHITE;
        grid[3][4] = States.BLACK;
        grid[4][3] = States.BLACK;

    }


    //prints the grid with suitable colours
    public void printGrid() {
        System.out.println("          a b c d e f g h");
        for (int i = 0; i < 8; i++) {
            System.out.print("       " + (i+1) + "  ");

            for (int j = 0; j < 8; j++) {
                if (getGrid(j, i) == States.WHITE) {
                    System.out.print(GREEN + "■ " + ANSI_RESET);
                } else if (getGrid(j, i) == States.BLACK) {
                    System.out.print(GREEN + "▢ " + ANSI_RESET);
                } else {
                    System.out.print(GREEN + "- " + ANSI_RESET);

                }
            }
            System.out.println();

        }
        System.out.println("\n---------------------------------");

    }


    //prints out the score of players and the final score if the game has reached the end
    public void getScore() {

        int blackScore = 0;
        int whiteScore = 0;

        int gameScore;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                if (getGrid(i, j) == States.BLACK) {
                    blackScore++;
                } else if (getGrid(i, j) == States.WHITE) {
                    whiteScore++;
                }

            }
        }
        gameScore = whiteScore + blackScore;


        System.out.println("White score: " + whiteScore);
        System.out.println("Black score: " + blackScore);

        //prints the result of the game
        if (gameScore == 64) {
            if (whiteScore == blackScore) {
                System.out.println("Ties");
            } else {
                System.out.println("End game");
            }
        }
    }


    //method for checking if at least one cell contains required neighbours
    public boolean checkAdjacentCells(States color) {

        for (int gridx = 0; gridx < 8; gridx++) {
            for (int gridy = 0; gridy < 8; gridy++) {
                if (checkNeighbours(gridx, gridy, color)) {
                    return true;
                }
            }
        }

        //goes here if the player has no moves
        System.out.println("Skipping move");
        return false;

    }

    //returns 'false' if there are no possible moves
    public boolean checkNeighbours(int gridx, int gridy, States color) {

        //shows directions
        int zx;
        int zy;

        //x and y values entered by the user
        int x, y;

        for (int i = 0; i < 8; i++) {

            x = gridx;
            y = gridy;

            zx = directions[i][0];
            zy = directions[i][1];

            y += zy;
            x += zx;

            if ((getGrid(gridx, gridy) == States.NOTHING) && (compare(y, zy) && compare(x, zx)) && ((getGrid(x, y) != color) && (getGrid(x, y) != States.NOTHING))) {

                int dx = x + zx;
                int dy = y + zy;

                while (compare(dx, zx) && compare(dy, zy) && (getGrid(dx, dy) == getGrid(x, y))) {
                    dy += zy;
                    dx += zx;
                }

                if (compare(dx, zx) && compare(dy, zy) && (getGrid(dx, dy) == color)) {

                    return true;

                }

            }
        }

        return false;
    }


    //method for changing the grid
    public void solveAll(int x, int y, States color) {

        //shoves directions
        int zx;
        int zy;

        //x and y values entered by a user
        int xOld = x;
        int yOld = y;


        for (int i = 0; i < 8; i++) {

            x = xOld;
            y = yOld;

            zx = directions[i][0];
            zy = directions[i][1];

            y += zy;
            x += zx;

            if ((compare(y, zy) && compare(x, zx)) && ((getGrid(x, y) != color) && (getGrid(x, y) != States.NOTHING))) {

                int dx = x + zx;
                int dy = y + zy;

                while (compare(dx, zx) && compare(dy, zy) && (getGrid(dx, dy) == getGrid(x, y))) {
                    dy += zy;
                    dx += zx;
                }

                if (compare(dx, zx) && compare(dy, zy) && (getGrid(dx, dy) == color)) {

                    while (compare(x, dx, zx) && compare(y, dy, zy)) {

                        setGridColor(x, y, color);
                        //System.out.println(x + " " + y);

                        x += zx;
                        y += zy;

                    }


                }

            }
        }
    }


    //methods needed for checking the borders
    public boolean compare(int a, int aEnd, int aCondition) {
        return ((aCondition > 0 && a < aEnd) || (aCondition < 0 && a > aEnd) || aCondition == 0);
    }

    public boolean compare(int a, int aCondition) {
        return ((aCondition > 0 && a < 8) || (aCondition < 0 && a >= 0) || aCondition == 0);
    }


    public int checkNeighboursCount(int gridx, int gridy, States color) {

        //shoves directions
        int zx;
        int zy;

        int count = 0;
        //x and y values entered by a user
        int x, y;

        for (int i = 0; i < 8; i++) {

            x = gridx;
            y = gridy;

            zx = directions[i][0];
            zy = directions[i][1];

            y += zy;
            x += zx;

            if ((getGrid(gridx, gridy) == States.NOTHING) && (compare(y, zy) && compare(x, zx)) && ((getGrid(x, y) != color) && (getGrid(x, y) != States.NOTHING))) {

                int dx = x + zx;
                int dy = y + zy;

                while (compare(dx, zx) && compare(dy, zy) && (getGrid(dx, dy) == getGrid(x, y))) {
                    dy += zy;
                    dx += zx;
                    count++;
                }

            }
        }

        return count;
    }

    public States[][] returnGrid() {

        States[][] myInt = new States[8][];
        for (int i = 0; i < 8; i++) {
            States[] aMatrix = grid[i];
            myInt[i] = new States[8];
            System.arraycopy(aMatrix, 0, myInt[i], 0, 8);
        }
        return myInt;
    }


}