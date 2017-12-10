
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by va29 on 27/03/17.
 */


public class Player {

    public static final int MAX_DEPTH = 1;
    private Grid grid = new Grid();
    private String playerType = "";
    private AI ai = new AI();
    private Human human = new Human();

    public int playerX;
    public int playerY;
    public Point AIPoint;


    public int[][] AIdirections = {
            {0, -1},     // N
            {1, -1},     //NE
            {1, 0},     //E
            {1, 1},     //SE
            {0, 1},     //S
            {-1, 1},    //SW
            {-1, 0},    //W
            {-1, -1}    //NW
    };


    private States color;


    public Player() {

    }

    //constructor which creates a player according to the player mode
    public Player(Grid grid, String playerType) {

        this.grid = grid;
        this.playerType = playerType;
        color = States.WHITE;

    }


    public Player(Grid grid, String playerType, States color) {

        this.grid = grid;
        this.playerType = playerType;
        this.color = color;

    }

    public States getColor() {
        return color;
    }

/*
    public static States getColorOpposite() {
        if (getColor() == States.BLACK) {
            return States.WHITE;
        } else if (getColor() == States.WHITE) {
            return States.BLACK;
        } else {
            return States.NOTHING;
        }
    }
*/

    public void setColor(States color) {
        System.out.println("afsasfas   " + this.toString());
        this.color = color;
    }

    //getter and setter of the Grid object
    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }


    //makes a move according to the player mode
    public Grid makeMove(Grid gameGrid, int moveNumber) {


        if (playerType.equals("human")) {
            //do stuff
            BufferedReader get = new BufferedReader(new InputStreamReader(System.in));
            int x;
            int y;
            String command = "";

            if (gameGrid.checkAdjacentCells(getColor())) {

                do {

                    System.out.print("Enter command: ");
                    try {
                        command = get.readLine();
                        this.translateStringToNumber(command);
                    } catch (IOException e) {
                    }

                    //System.out.println("x -> " + playerX + "     y -> " + playerY);
                    //System.out.println("---------------------------------\n");


                } while (!checkMoveTrue(playerX, playerY, gameGrid));

                gameGrid = updateGrid(playerX, playerY, gameGrid);
                gameGrid.grid[playerX][playerY] = getColor();

            } else {
                playerX = -1;
                playerY = -1;
            }



            } else if (playerType.equals("ai")) {


            if (gameGrid.checkAdjacentCells(getColor())) {
                minimax(gameGrid.returnGrid(), 7, getColor(), true, moveNumber);


                //System.out.println("AI chooses: x-> " + AIPoint.x + " y-> " + AIPoint.y);
                gameGrid = updateGrid(AIPoint.x, AIPoint.y, gameGrid);

                gameGrid.grid[AIPoint.x][AIPoint.y] = getColor();
                //delete
                //   System.out.println(gameGrid.grid[AIPoint.x][AIPoint.y].toString());
                playerX = AIPoint.x;
                playerY = AIPoint.y;

            } else {
                playerX = -1;
                playerY = -1;
            }

        } else if (playerType.equals("client")) {
            //client side
            if (gameGrid.checkAdjacentCells(getColor())) {


            }
        } else if (playerType.equals("server")) {
            //server side
            if (gameGrid.checkAdjacentCells(getColor())) {


            }
        }

        return gameGrid;
    }

    //checks if the move is possible
    public boolean checkMoveTrue(int x, int y, Grid grid) {

        if ((x < 8) && (y < 8) && (x >= 0) && (y >= 0)) {

            if (grid.checkNeighbours(x, y, getColor())) {
                return true;

            } else {
                //prints a suitable message if the move is not possible
                System.out.println("move not possible, please select correct coordinates");

                return false;

            }

        }

        return false;

    }


    public boolean checkPossibleMove(int x, int y, Grid grid) {

        //checks if on the board
        if ((x >= 0) && (x <= 7) && (y >= 0) && (y <= 7)) {

            //checks the north point
            //checks if the point is the opposite color
            if ((grid.getGrid(x, y - 1) != getColor()) && (grid.getGrid(x, y - 1) != States.NOTHING)) {


                int dy = y - 2;

                while (dy >= 0 && grid.getGrid(x, dy) == grid.getGrid(x, y - 1)) {

                    dy--;

                }


                if (dy >= 0 && grid.getGrid(x, dy) == grid.getGrid(x, y)) {
                    return true;

                }


            }


        }

        return false;

    }


    //a move is made if c and y coordinates saticfy the required range
    public Grid updateGrid(int x, int y, Grid grid) {


        //checks if on the board
        if ((x >= 0) && (x <= 7) && (y >= 0) && (y <= 7)) {

            /**
             *
             * CHANGE ZX AND ZY TO
             * MAKE EVERYTHING WORK AT ONCE
             *
             */

            grid.solveAll(x, y, getColor());

        }

        return grid;

    }

    //methods needed to check the borders of the grid
    public boolean compare(int a, int aEnd, int aCondition) {

        if ((aCondition > 0 && a < aEnd) || (aCondition < 0 && a > aEnd) || aCondition == 0) {
            return true;
        } else {
            return false;
        }

    }

    public boolean compare(int a, int aCondition) {

        if ((aCondition > 0 && a < 8) || (aCondition < 0 && a >= 0) || aCondition == 0) {
            return true;
        } else {
            return false;
        }

    }


    public boolean checkNeighbours(int gridx, int gridy, States[][] gameBoard, States color) {

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

            if ((gameBoard[gridx][gridy] == States.NOTHING) && (compare(y, zy) && compare(x, zx)) && ((gameBoard[x][y] != color) && (gameBoard[x][y] != States.NOTHING))) {

                int dx = x + zx;
                int dy = y + zy;

                while (compare(dx, zx) && compare(dy, zy) && (gameBoard[dx][dy] == gameBoard[x][y])) {
                    dy += zy;
                    dx += zx;
                }

                if (compare(dx, zx) && compare(dy, zy) && (gameBoard[dx][dy] == color)) {

                    return true;

                }

            }
        }

        return false;
    }


    //method to create a list of possible moves.
    public LinkedList<Point> moves_list(States[][] gameBoard, States color) {

        LinkedList<Point> points = new LinkedList<>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                if (checkNeighbours(i, j,gameBoard, color)) {

                    //every element of the list is a point object which saves x and y coordinates
                    points.add(new Point(i, j));

                }

            }
        }

        return points;
    }

    //created for checking all 8 possible directions of a point
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

    /**
     * AI
     * <p>
     * AI
     * AI
     * <p>
     * <p>
     * <p>
     * AI
     * <p>
     * AI
     * <p>
     * <p>
     * AI
     * AI
     */


    Point bestPoint = null;

    //changes the color. hence, the other player continues
    public States switchState(States color) {

        if (color == States.BLACK) {
            return States.WHITE;
        } else if (color == States.WHITE) {
            return States.BLACK;
        }

        return States.NOTHING;
    }


    public byte[] minimax2(States[][] gameBoard, States maximizingPlayerColor, boolean maximizingPlayer) {

        byte[] coordinates = new byte[2];
        States[][] oldBoard = gameBoard;


        LinkedList<Point> moves = moves_list(gameBoard, maximizingPlayerColor);

        for (int i = 0; i < moves.size(); i++) {

            gameBoard = simulateMove(moves.get(i).x, moves.get(i).y, maximizingPlayerColor, gameBoard);
            LinkedList<Point> moves2 = moves_list(gameBoard, switchState(maximizingPlayerColor));

            for (int k = 0; k < moves2.size(); i++) {


            }

        }
        return coordinates;

    }



    //minimax algorithm
    public double minimax(States[][] gameBoard, int depth, States maximizingPlayerColor, boolean maximizingPlayer, int moveNumber) {

        int depth2 = 7;

        if(moveNumber>=55){

            depth2=61-moveNumber;
            depth = 61-moveNumber;

        }

        double evaluate=0;
        int value = 0;

        //the deepest node
        if (depth == 0) {
            //    System.out.println("FINAL DEPTH: " + depth + ", evaluation: " + evaluateBoard(gameBoard, maximizingPlayerColor));
            // System.out.println("x: " + AIPoint.x + ", y: "+AIPoint.y);
            return evaluateBoard(gameBoard, maximizingPlayerColor);
        }


        if (maximizingPlayer) {

            double bestValue = -1000;

            LinkedList<Point> moves = new LinkedList<Point>();
            moves = moves_list(gameBoard, maximizingPlayerColor);


            for (Point move : moves) {
                States[][] newBoard = returnGrid(gameBoard);

                //  System.out.println(evaluate);
                //  evaluate+=evaluateBoard(newBoard, maximizingPlayerColor);
                //  System.out.println("pries ejima max :   "+evaluate+"   move x: "+move.x+"   move y: "+move.y+"   depth: "+depth);

                evaluate += checkNeighboursCount(move.x, move.y, maximizingPlayerColor, newBoard);

                if(depth==7) {
                    // System.out.println("EVALUATE pries: "+evaluate);

                }


                if(depth==5)


                    newBoard = simulateMove(move.x, move.y, maximizingPlayerColor, newBoard);


                //   System.out.println("po pirmo ejimo evaluate:   "+evaluate+"   move x: "+move.x+"   move y: "+move.y);


                //  evaluate += -minimax(newBoard, depth - 1, switchState(maximizingPlayerColor), false);
                //        System.out.println(maximizingPlayerColor.toString() + " MAX: depth: " + depth + ", score: " + v + ", move->   x:" + move.x + " y:" + move.y);

                double v = minimax(newBoard, depth - 1, switchState(maximizingPlayerColor), false, moveNumber);
                //     System.out.println("v: "+v+"  bestValue: "+bestValue);


                evaluate=0;
           /*     if(depth==7){
                    //   System.out.println("eina eina");

                    System.out.println("moves:   x: "+move.x+"   y: "+move.y);
                }*/

                if (v > bestValue) {

                    // bestPoint.x = move.x;
                    // bestPoint.y = move.y;
                    bestValue = v;
                    //  System.out.println("eina");
                    //           System.out.println("NEW MAX VALUE: "+ move.x + " --- " + move.y);
                    //      AIPoint = new Point(move.x, move.y);

                    if(depth==depth2){

                        //   System.out.println("eina eina");

                        //creates a point object which saves the coordinates of the move
                        AIPoint = new Point(move.x, move.y);

                    }

                }
            }

            //   AIPoint = bestPoint;
            //AIPoint = bestPoint;
            return bestValue;

        } else {

            double bestValue = 1000;
            //LinkedLiast of possible moves is made
            LinkedList<Point> moves = moves_list(gameBoard, maximizingPlayerColor);

            //goes through each move and calls a minimax method again till the depth = 0
            for (Point move : moves) {

                States[][] newBoard = returnGrid(gameBoard);
               /* for(int a=0; a<8; a++){
                    for(int b=0; b<8; b++){
                        System.out.print(newBoard[b][a]+" ");
                    }
                    System.out.println();
                }*/

                // System.out.println("pries ejima min :   "+evaluate+"   move x: "+move.x+"   move y: "+move.y+"   depth: "+depth);

                evaluate+=checkNeighboursCount(move.x, move.y, maximizingPlayerColor, newBoard);

                if(depth==6) {
                    // System.out.println("EVALUATE pries: "+evaluate);

                }



                // System.out.println("evaluate: "+evaluate+"   depth: "+depth+"   move x: "+move.x+"   move y: "+move.y);

                newBoard = simulateMove(move.x, move.y, maximizingPlayerColor, newBoard);
               /* for(int a=0; a<8; a++){
                    for(int b=0; b<8; b++){
                        System.out.print(newBoard[b][a]+" ");
                    }
                    System.out.println();*/
                // }
                //  System.out.println("po  ejimo min evaluate:   "+evaluate+"   move x: "+move.x+"   move y: "+move.y);


                double v= -minimax(newBoard, depth - 1, switchState(maximizingPlayerColor), true, moveNumber);

                evaluate = 0;
                // evaluate-= -minimax(newBoard, depth - 1, switchState(maximizingPlayerColor), true);
                //      System.out.println(maximizingPlayerColor.toString() + " MIN: depth: " + depth + ", score: " + v + ", move->   x:" + move.x + " y:" + move.y);
                if (v < bestValue) {
                    //  bestPoint.x = move.x;
                    //  bestPoint.y = move.y;

                    bestValue = v;
                    //            System.out.println("NEW MIN VALUE: "+ move.x + " --- " + move.y);
                    //  AIPoint = new Point(move.x, move.y);
                }
            }

            //AIPoint = bestPoint;
            return bestValue;

        }

    }



/*
    public int alphabeta(States[][] gameBoard, int depth, int alfa, int beta, boolean maximizingPlayer){

        if(depth ==0){
            return v;
        }



    }*/


    //returns the difference between maximizing player points and minimizing player
    public int evaluateBoard(States[][] gameBoard, States maximizingPlayerColor) {

        int blackPieces = 0;
        int whitePieces = 0;

        for (States[] array : gameBoard) {
            for (States button : array) {
                if (button == (States.BLACK)) {
                    blackPieces++;
                } else if (button == (States.WHITE)) {
                    whitePieces++;
                }
            }
        }
        //       System.out.println("white: " + whitePiecess + " " + "black: " + blackPieces);

        if (maximizingPlayerColor == States.WHITE) {
            return whitePieces - blackPieces;
        } else {
            return blackPieces - whitePieces;
        }
    }


    //does a move and changes the board
    public States[][] simulateMove(int x, int y, States color, States[][] grid) {

        //shoves directions
        int zx;
        int zy;

        //x and y values entered by a user
        int xOld = x;
        int yOld = y;


        for (int i = 0; i < 8; i++) {

            x = xOld;
            y = yOld;

            zx = AIdirections[i][0];
            zy = AIdirections[i][1];

          /*  y += zy;
            x += zx;*/

            if ((compare(y, zy) && compare(x, zx)) && ((grid[x][y] != color) && (grid[x][y] != States.NOTHING))) {

                int dx = x + zx;
                int dy = y + zy;

                while (compare(dx, zx) && compare(dy, zy) && (grid[dx][dy] == grid[x][y])) {
                    dy += zy;
                    dx += zx;
                }

                if (compare(dx, zx) && compare(dy, zy) && (grid[dx][dy] == color)) {

                    while (compare(x, dx, zx) && compare(y, dy, zy)) {

                        grid[x][y] = color;

                        x += zx;
                        y += zy;

                    }


                }

            }
        }

        grid[xOld][yOld] = color;

        return grid;
    }

    //counts how many squares of an opponent's color are painted after making a move
    public double checkNeighboursCount(int x, int y, States color, States[][] newBoard){

/*        //shoves directions
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

            if ((newBoard[gridx][gridy] == States.NOTHING) && (compare(y, zy) && compare(x, zx)) && (newBoard[x][y] != color) && (newBoard[x][y] != States.NOTHING)) {

                int dx = x + zx;
                int dy = y + zy;

                while (compare(dx, zx) && compare(dy, zy) && (newBoard[dx][dy]== newBoard[x][y])) {
                    dy += zy;
                    dx += zx;
                    count++;
                }

            }
        }

        System.out.println("count: "+count);*/


        //shoves directions
        int zx;
        int zy;
        int count=0;

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

            if ((compare(y, zy) && compare(x, zx)) && ((newBoard[x][y] != color) && (newBoard[x][y] != States.NOTHING))) {

                int dx = x + zx;
                int dy = y + zy;

                while (compare(dx, zx) && compare(dy, zy) && (newBoard[dx][dy] == newBoard[x][y])) {
                    dy += zy;
                    dx += zx;
                }

                if (compare(dx, zx) && compare(dy, zy) && (newBoard[dx][dy] == color)) {

                    while (compare(x, dx, zx) && compare(y, dy, zy)) {

                        newBoard[x][y] = color;
                        count+=1;

                        x += zx;
                        y += zy;

                    }


                }

            }
        }
        //  System.out.println("count: "+count);
        return count;
    }

    //returns a grid
    public States[][] returnGrid(States[][] sampleGrid) {

        States[][] myInt = new States[8][];

        for (int i = 0; i < 8; i++) {
            States[] aMatrix = sampleGrid[i];
            myInt[i] = new States[8];
            System.arraycopy(aMatrix, 0, myInt[i], 0, 8);
        }

        return myInt;
    }

    public int max(int x, int y, Point move) {

        if (x > y) {
            this.AIPoint = new Point(move.x, move.y);

            return x;
        } else return y;
    }

    public int min(int x, int y) {

        if (x < y) {
            return x;
        } else return y;
    }

    public String translateNumberToString() {

        String answer = "move ";
        if (playerX == -1 && playerY == -1) {
            answer += "pass";
        } else {
            if (playerX == 0) {
                answer += "a";
            } else if (playerX == 1) {
                answer += "b";
            } else if (playerX == 2) {
                answer += "c";
            } else if (playerX == 3) {
                answer += "d";
            } else if (playerX == 4) {
                answer += "e";
            } else if (playerX == 5) {
                answer += "f";
            } else if (playerX == 6) {
                answer += "g";
            } else if (playerX == 7) {
                answer += "h";
            }

            answer += (playerY + 1);
        }
        //prints out the translated code
        System.out.println(answer);

        return answer;
    }

    public void translateStringToNumber(String key) {

        if (key.charAt(5) == 'a') {
            playerX = 0;
        } else if (key.charAt(5) == 'b') {
            playerX = 1;
        } else if (key.charAt(5) == 'c') {
            playerX = 2;
        } else if (key.charAt(5) == 'd') {
            playerX = 3;
        } else if (key.charAt(5) == 'e') {
            playerX = 4;
        } else if (key.charAt(5) == 'f') {
            playerX = 5;
        } else if (key.charAt(5) == 'g') {
            playerX = 6;
        } else if (key.charAt(5) == 'h') {
            playerX = 7;
        }

        playerY = Character.getNumericValue(key.charAt(6)) - 1;

        //prints out the translated code
        //System.out.println(playerX + " " + playerY);

    }
    public Grid makeMoveOnline(Grid gameGrid, String command) {

        this.translateStringToNumber(command);

        if (playerType.equals("human")) {

            if (gameGrid.checkAdjacentCells(getColor())) {

                if (checkMoveTrue(playerX, playerY, gameGrid)) {

                }

                gameGrid = updateGrid(playerX, playerY, gameGrid);
                gameGrid.grid[playerX][playerY] = getColor();

            }

        }

        return gameGrid;
    }



    public static boolean checkInput(String string) {

        if (string.equals("bye") ) {

            return true;

        } else if (
                string.length() == 7 &&
                        string.substring(0, 5).equals("move ") &&
                        string.charAt(5) >= 'a' &&
                        string.charAt(5) <= 'h' &&
                        string.charAt(6) >= '1' &&
                        string.charAt(6) <= '8') {


            return true;

        } else return false;

    }



}
/*
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

*/
/**
 * Created by va29 on 27/03/17.
 *//*



public class Player {

    public static final int MAX_DEPTH = 1;
    private Grid grid = new Grid();
    private String playerType = "";
    private AI ai = new AI();
    private Human human = new Human();
    public int playerTotalMoveNumber=0;

    public Point AIPoint;

    public int playerX;
    public int playerY;


    public int[][] AIdirections = {
            {0, -1},     // N
            {1, -1},     //NE
            {1, 0},     //E
            {1, 1},     //SE
            {0, 1},     //S
            {-1, 1},    //SW
            {-1, 0},    //W
            {-1, -1}    //NW
    };


    private States color;


    public Player() {

    }


    public Player(Grid grid, String playerType) {

        this.grid = grid;
        this.playerType = playerType;
        color = States.WHITE;

    }


    public Player(Grid grid, String playerType, States color) {

        this.grid = grid;
        this.playerType = playerType;
        this.color = color;

    }

    public States getColor() {
        return color;
    }

*/
/*
    public static States getColorOpposite() {
        if (getColor() == States.BLACK) {
            return States.WHITE;
        } else if (getColor() == States.WHITE) {
            return States.BLACK;
        } else {
            return States.NOTHING;
        }
    }
*//*


    public void setColor(States color) {
        //System.out.println("afsasfas   " + this.toString());
        this.color = color;
    }


    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }


    public Grid makeMove(Grid gameGrid, int moveNumber) {


        if (playerType.equals("human")) {
            //do stuff
            BufferedReader get = new BufferedReader(new InputStreamReader(System.in));
            int x;
            int y;
            String command = "";

            if (gameGrid.checkAdjacentCells(getColor())) {

                do {

                    System.out.print("Enter command: ");
                    try {
                        command = get.readLine();
                        this.translateStringToNumber(command);
                    } catch (IOException e) {
                    }

                    //System.out.println("x -> " + playerX + "     y -> " + playerY);
                    //System.out.println("---------------------------------\n");


                } while (!checkMoveTrue(playerX, playerY, gameGrid));

                gameGrid = updateGrid(playerX, playerY, gameGrid);
                gameGrid.grid[playerX][playerY] = getColor();
                playerTotalMoveNumber++;

            } else {
                playerX = -1;
                playerY = -1;
            }


        } else if (playerType.equals("ai")) {

            if (gameGrid.checkAdjacentCells(getColor())) {
                minimax(gameGrid.returnGrid(), 7, getColor(), true, moveNumber);


                //System.out.println("AI chooses: x-> " + AIPoint.x + " y-> " + AIPoint.y);
                gameGrid = updateGrid(AIPoint.x, AIPoint.y, gameGrid);

                gameGrid.grid[AIPoint.x][AIPoint.y] = getColor();
                //delete
                //   System.out.println(gameGrid.grid[AIPoint.x][AIPoint.y].toString());
                playerX = AIPoint.x;
                playerY = AIPoint.y;
                playerTotalMoveNumber++;

            } else {
                playerX = -1;
                playerY = -1;
            }

        } else if (playerType.equals("client")) {
            //client side
            if (gameGrid.checkAdjacentCells(getColor())) {


            }
        } else if (playerType.equals("server")) {
            //server side
            if (gameGrid.checkAdjacentCells(getColor())) {


            }
        }

        return gameGrid;
    }


    public Grid makeMoveOnline(Grid gameGrid, String command) {

        this.translateStringToNumber(command);

        if (playerType.equals("human")) {

            if (gameGrid.checkAdjacentCells(getColor())) {

                if (checkMoveTrue(playerX, playerY, gameGrid)) {

                }

                gameGrid = updateGrid(playerX, playerY, gameGrid);
                gameGrid.grid[playerX][playerY] = getColor();

            }

        }

        return gameGrid;
    }

    public boolean checkMoveTrue(int x, int y, Grid grid) {

        if ((x < 8) && (y < 8) && (x >= 0) && (y >= 0)) {

            if (grid.checkNeighbours(x, y, getColor())) {
                return true;

            } else {
                System.out.println("move not possible, please select correct coordinates");

                return false;

            }

        }

        return false;

    }


    public boolean checkPossibleMove(int x, int y, Grid grid) {

        //checks if on the board
        if ((x >= 0) && (x <= 7) && (y >= 0) && (y <= 7)) {

            //checks the north point
            //checks if the point is the opposite color
            if ((grid.getGrid(x, y - 1) != getColor()) && (grid.getGrid(x, y - 1) != States.NOTHING)) {


                int dy = y - 2;

                while (dy >= 0 && grid.getGrid(x, dy) == grid.getGrid(x, y - 1)) {

                    dy--;

                }


                if (dy >= 0 && grid.getGrid(x, dy) == grid.getGrid(x, y)) {
                    return true;

                }


            }


        }

        return false;

    }


    public Grid updateGrid(int x, int y, Grid grid) {


        //checks if on the board
        if ((x >= 0) && (x <= 7) && (y >= 0) && (y <= 7)) {

            */
/**
             *
             * CHANGE ZX AND ZY TO
             * MAKE EVERYTHING WORK AT ONCE
             *
             *//*


            grid.solveAll(x, y, getColor());

        }

        return grid;

    }


    public boolean compare(int a, int aEnd, int aCondition) {

        if ((aCondition > 0 && a < aEnd) || (aCondition < 0 && a > aEnd) || aCondition == 0) {
            return true;
        } else {
            return false;
        }

    }

    public boolean compare(int a, int aCondition) {

        if ((aCondition > 0 && a < 8) || (aCondition < 0 && a >= 0) || aCondition == 0) {
            return true;
        } else {
            return false;
        }

    }


    public LinkedList<Point> moves_list(States[][] colors, States color) {

        LinkedList<Point> points = new LinkedList<>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                if (grid.checkNeighbours(i, j, color)) {

                    points.add(new Point(i, j));

                }

            }
        }

        return points;
    }

    */
/**
     * AI
     * <p>
     * AI
     * AI
     * <p>
     * <p>
     * <p>
     * AI
     * <p>
     * AI
     * <p>
     * <p>
     * AI
     * AI
     *//*



    Point bestPoint = null;

    public States switchState(States color) {

        if (color == States.BLACK) {
            return States.WHITE;
        } else if (color == States.WHITE) {
            return States.BLACK;
        }

        return States.NOTHING;
    }


    public byte[] minimax2(States[][] gameBoard, States maximizingPlayerColor, boolean maximizingPlayer) {

        byte[] coordinates = new byte[2];
        States[][] oldBoard = gameBoard;


        LinkedList<Point> moves = moves_list(gameBoard, maximizingPlayerColor);

        for (int i = 0; i < moves.size(); i++) {

            gameBoard = simulateMove(moves.get(i).x, moves.get(i).y, maximizingPlayerColor, gameBoard);
            LinkedList<Point> moves2 = moves_list(gameBoard, switchState(maximizingPlayerColor));

            for (int k = 0; k < moves2.size(); i++) {


            }

        }
        return coordinates;

    }


    //MINIMAX BELOW


    //MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX
    //MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX
    //MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX
    //MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX
    //MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX

    */
/**
     * MINIMAX WORKING V1.0
     *
     * @param gameBoard
     * @param depth
     * @param maximizingPlayerColor
     * @param maximizingPlayer
     * @return
     *//*


    public double minimax(States[][] gameBoard, int depth, States maximizingPlayerColor, boolean maximizingPlayer, int moveNumber) {

        int depth2 = 7;

        if(moveNumber>=57){

            depth2=62-moveNumber;
            depth = 62-moveNumber;

        }

        double evaluate=0;
        int value = 0;

        if (depth == 0) {
            //    System.out.println("FINAL DEPTH: " + depth + ", evaluation: " + evaluateBoard(gameBoard, maximizingPlayerColor));
            // System.out.println("x: " + AIPoint.x + ", y: "+AIPoint.y);
            return evaluateBoard(gameBoard, maximizingPlayerColor);
        }

        if (maximizingPlayer) {

            double bestValue = -1000;

            LinkedList<Point> moves = new LinkedList<Point>();
            moves = moves_list(gameBoard, maximizingPlayerColor);


            for (Point move : moves) {
                States[][] newBoard = returnGrid(gameBoard);

                //  System.out.println(evaluate);
                //  evaluate+=evaluateBoard(newBoard, maximizingPlayerColor);
                //  System.out.println("pries ejima max :   "+evaluate+"   move x: "+move.x+"   move y: "+move.y+"   depth: "+depth);


                if(depth==7) {
                    // System.out.println("EVALUATE pries: "+evaluate);

                }


                if(depth==5)


                    newBoard = simulateMove(move.x, move.y, maximizingPlayerColor, newBoard);


                //   System.out.println("po pirmo ejimo evaluate:   "+evaluate+"   move x: "+move.x+"   move y: "+move.y);


                //  evaluate += -minimax(newBoard, depth - 1, switchState(maximizingPlayerColor), false);
                //        System.out.println(maximizingPlayerColor.toString() + " MAX: depth: " + depth + ", score: " + v + ", move->   x:" + move.x + " y:" + move.y);

                double v = minimax(newBoard, depth - 1, switchState(maximizingPlayerColor), false, moveNumber);
                //     System.out.println("v: "+v+"  bestValue: "+bestValue);


                evaluate=0;
           */
/*     if(depth==7){
                    //   System.out.println("eina eina");

                    System.out.println("moves:   x: "+move.x+"   y: "+move.y);
                }*//*


                if (v > bestValue) {

                    // bestPoint.x = move.x;
                    // bestPoint.y = move.y;
                    bestValue = v;
                    //  System.out.println("eina");
                    //           System.out.println("NEW MAX VALUE: "+ move.x + " --- " + move.y);
                    //      AIPoint = new Point(move.x, move.y);

                    if(depth==depth2){

                        //   System.out.println("eina eina");

                        AIPoint = new Point(move.x, move.y);

                    }

                }
            }

            //   AIPoint = bestPoint;
            //AIPoint = bestPoint;
            return bestValue;

        } else {

            double bestValue = 1000;
            LinkedList<Point> moves = moves_list(gameBoard, maximizingPlayerColor);

            for (Point move : moves) {

                States[][] newBoard = returnGrid(gameBoard);
               */
/* for(int a=0; a<8; a++){
                    for(int b=0; b<8; b++){
                        System.out.print(newBoard[b][a]+" ");
                    }
                    System.out.println();
                }*//*


                // System.out.println("pries ejima min :   "+evaluate+"   move x: "+move.x+"   move y: "+move.y+"   depth: "+depth);



                if(depth==6) {
                    // System.out.println("EVALUATE pries: "+evaluate);

                }



                // System.out.println("evaluate: "+evaluate+"   depth: "+depth+"   move x: "+move.x+"   move y: "+move.y);

                newBoard = simulateMove(move.x, move.y, maximizingPlayerColor, newBoard);
               */
/* for(int a=0; a<8; a++){
                    for(int b=0; b<8; b++){
                        System.out.print(newBoard[b][a]+" ");
                    }
                    System.out.println();*//*

                // }
                //  System.out.println("po  ejimo min evaluate:   "+evaluate+"   move x: "+move.x+"   move y: "+move.y);


                double v= -minimax(newBoard, depth - 1, switchState(maximizingPlayerColor), true, moveNumber);

                evaluate = 0;
                // evaluate-= -minimax(newBoard, depth - 1, switchState(maximizingPlayerColor), true);
                //      System.out.println(maximizingPlayerColor.toString() + " MIN: depth: " + depth + ", score: " + v + ", move->   x:" + move.x + " y:" + move.y);
                if (v < bestValue) {
                    //  bestPoint.x = move.x;
                    //  bestPoint.y = move.y;

                    bestValue = v;
                    //            System.out.println("NEW MIN VALUE: "+ move.x + " --- " + move.y);
                    //  AIPoint = new Point(move.x, move.y);
                }
            }

            //AIPoint = bestPoint;
            return bestValue;

        }

    }


  */
/*  public double minimax(States[][] gameBoard, int depth, States maximizingPlayerColor, boolean maximizingPlayer, int moveNumber) {

        int depth2 = 7;

        if(moveNumber>=56){

            depth2=62-moveNumber;
            depth = 62-moveNumber;

        }


        if (depth == 0) {
            //    System.out.println("FINAL DEPTH: " + depth + ", evaluation: " + evaluateBoard(gameBoard, maximizingPlayerColor));
            // System.out.println("x: " + AIPoint.x + ", y: "+AIPoint.y);
            return evaluateBoard(gameBoard, maximizingPlayerColor);
        }

        if (maximizingPlayer) {

            double bestValue = -1000;

            LinkedList<Point> moves = new LinkedList<Point>();
            moves = moves_list(gameBoard, maximizingPlayerColor);


            for (Point move : moves) {
                States[][] newBoard = returnGrid(gameBoard);

                //  System.out.println(evaluate);
                //  evaluate+=evaluateBoard(newBoard, maximizingPlayerColor);
                //  System.out.println("pries ejima max :   "+evaluate+"   move x: "+move.x+"   move y: "+move.y+"   depth: "+depth);




                    newBoard = simulateMove(move.x, move.y, maximizingPlayerColor, newBoard);


                //   System.out.println("po pirmo ejimo evaluate:   "+evaluate+"   move x: "+move.x+"   move y: "+move.y);


                //  evaluate += -minimax(newBoard, depth - 1, switchState(maximizingPlayerColor), false);
                //        System.out.println(maximizingPlayerColor.toString() + " MAX: depth: " + depth + ", score: " + v + ", move->   x:" + move.x + " y:" + move.y);

                double v = minimax(newBoard, depth - 1, switchState(maximizingPlayerColor), false, moveNumber);
                //     System.out.println("v: "+v+"  bestValue: "+bestValue);



           *//*
*/
/*     if(depth==7){
                    //   System.out.println("eina eina");

                    System.out.println("moves:   x: "+move.x+"   y: "+move.y);
                }*//*
*/
/*

                if (v > bestValue) {

                    // bestPoint.x = move.x;
                    // bestPoint.y = move.y;
                    bestValue = v;
                    //  System.out.println("eina");
                    //           System.out.println("NEW MAX VALUE: "+ move.x + " --- " + move.y);
                    //      AIPoint = new Point(move.x, move.y);

                    if(depth==depth2){

                        //   System.out.println("eina eina");

                        AIPoint = new Point(move.x, move.y);

                    }

                }
            }

            //   AIPoint = bestPoint;
            //AIPoint = bestPoint;
            return bestValue;

        } else {

            double bestValue = 1000;
            LinkedList<Point> moves = moves_list(gameBoard, maximizingPlayerColor);

            for (Point move : moves) {

                States[][] newBoard = returnGrid(gameBoard);
               *//*
*/
/* for(int a=0; a<8; a++){
                    for(int b=0; b<8; b++){
                        System.out.print(newBoard[b][a]+" ");
                    }
                    System.out.println();
                }*//*
*/
/*

                // System.out.println("pries ejima min :   "+evaluate+"   move x: "+move.x+"   move y: "+move.y+"   depth: "+depth);


                if(depth==6) {
                    // System.out.println("EVALUATE pries: "+evaluate);

                }



                // System.out.println("evaluate: "+evaluate+"   depth: "+depth+"   move x: "+move.x+"   move y: "+move.y);

                newBoard = simulateMove(move.x, move.y, maximizingPlayerColor, newBoard);
               *//*
*/
/* for(int a=0; a<8; a++){
                    for(int b=0; b<8; b++){
                        System.out.print(newBoard[b][a]+" ");
                    }
                    System.out.println();*//*
*/
/*
                // }
                //  System.out.println("po  ejimo min evaluate:   "+evaluate+"   move x: "+move.x+"   move y: "+move.y);


                double v= -minimax(newBoard, depth - 1, switchState(maximizingPlayerColor), true, moveNumber);

                // evaluate-= -minimax(newBoard, depth - 1, switchState(maximizingPlayerColor), true);
                //      System.out.println(maximizingPlayerColor.toString() + " MIN: depth: " + depth + ", score: " + v + ", move->   x:" + move.x + " y:" + move.y);
                if (v < bestValue) {
                    //  bestPoint.x = move.x;
                    //  bestPoint.y = move.y;

                    bestValue = v;
                    //            System.out.println("NEW MIN VALUE: "+ move.x + " --- " + move.y);
                    //  AIPoint = new Point(move.x, move.y);
                }
            }

            //AIPoint = bestPoint;
            return bestValue;

        }

    }*//*

    //MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX
    //MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX
    //MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX
    //MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX
    //MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX MINIMAX


    //ALPHA-BETA MINIMAX BELOW


    //ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA
    //ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA
    //ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA
    //ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA
    //ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA

    */
/**
     * ALPHA-BETA MINIMAX
     *
     * @param gameBoard
     * @param depth
     * @param maximizingPlayerColor
     * @param maximizingPlayer
     * @return
     *//*


    public int alphabeta(int alpha, int beta, States[][] gameBoard, int depth, States maximizingPlayerColor, boolean maximizingPlayer) {
        // System.out.println(" starting depth : " + depth);

        if (depth == 0) {
            //    System.out.println("FINAL DEPTH: " + depth + ", evaluation: " + evaluateBoard(gameBoard, maximizingPlayerColor));
            // System.out.println("x: " + AIPoint.x + ", y: "+AIPoint.y);
            return evaluateBoard(gameBoard, getColor());
        }

        if (maximizingPlayer) {
            int v = Integer.MIN_VALUE;
            LinkedList<Point> moves = moves_list(gameBoard, maximizingPlayerColor);
            for (Point move : moves) {
                States[][] newBoard = returnGrid(gameBoard);
                newBoard = simulateMove(move.x, move.y, maximizingPlayerColor, newBoard);
                v = max(v, -alphabeta(alpha, beta, newBoard, depth - 1, switchState(maximizingPlayerColor), false), move);

                alpha = max(alpha, v, move);
                if (beta <= alpha) {
                    break;
                }
            }
            return v;
        } else {
            int v = Integer.MAX_VALUE;
            LinkedList<Point> moves = moves_list(gameBoard, maximizingPlayerColor);
            for (Point move : moves) {
                States[][] newBoard = returnGrid(gameBoard);
                newBoard = simulateMove(move.x, move.y, maximizingPlayerColor, newBoard);
                v = min(v, -alphabeta(alpha, beta, newBoard, depth - 1, switchState(maximizingPlayerColor), false));

                beta = min(beta, v);
                if (beta <= alpha) {
                    break;
                }
            }
            return v;
        }
    }
    //ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA
    //ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA
    //ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA
    //ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA
    //ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA ALPHA-BETA


    public int evaluateBoard(States[][] gameBoard, States maximizingPlayerColor) {

        int blackPieces = 0;
        int whitePiecess = 0;

        for (States[] array : gameBoard) {
            for (States button : array) {
                if (button == (States.BLACK)) {
                    blackPieces++;
                } else if (button == (States.WHITE)) {
                    whitePiecess++;
                }
            }
        }
        //       System.out.println("white: " + whitePiecess + " " + "black: " + blackPieces);


        if (maximizingPlayerColor == States.WHITE) {
            return blackPieces - whitePiecess;
        } else {
            return whitePiecess - blackPieces;
        }
    }

    public States[][] simulateMove(int x, int y, States color, States[][] grid) {

        //shoves directions
        int zx;
        int zy;

        //x and y values entered by a user
        int xOld = x;
        int yOld = y;


        for (int i = 0; i < 8; i++) {

            x = xOld;
            y = yOld;

            zx = AIdirections[i][0];
            zy = AIdirections[i][1];

            y += zy;
            x += zx;

            if ((compare(y, zy) && compare(x, zx)) && ((grid[x][y] != color) && (grid[x][y] != States.NOTHING))) {

                int dx = x + zx;
                int dy = y + zy;

                while (compare(dx, zx) && compare(dy, zy) && (grid[dx][dy] == grid[x][y])) {
                    dy += zy;
                    dx += zx;
                }

                if (compare(dx, zx) && compare(dy, zy) && (grid[dx][dy] == color)) {

                    while (compare(x, dx, zx) && compare(y, dy, zy)) {

                        grid[x][y] = color;

                        x += zx;
                        y += zy;

                    }


                }

            }
        }

        grid[xOld][yOld] = color;

        return grid;
    }


    public States[][] returnGrid(States[][] sampleGrid) {

        States[][] myInt = new States[8][];

        for (int i = 0; i < 8; i++) {
            States[] aMatrix = sampleGrid[i];
            myInt[i] = new States[8];
            System.arraycopy(aMatrix, 0, myInt[i], 0, 8);
        }

        return myInt;
    }


    public int max(int x, int y, Point move) {

        if (x > y) {
            this.AIPoint = new Point(move.x, move.y);

            return x;
        } else return y;
    }

    public int min(int x, int y) {

        if (x < y) {
            return x;
        } else return y;
    }

    public String translateNumberToString() {

        String answer = "move ";
        if (playerX == -1 && playerY == -1) {
            answer += "pass";
        } else {
            if (playerX == 0) {
                answer += "a";
            } else if (playerX == 1) {
                answer += "b";
            } else if (playerX == 2) {
                answer += "c";
            } else if (playerX == 3) {
                answer += "d";
            } else if (playerX == 4) {
                answer += "e";
            } else if (playerX == 5) {
                answer += "f";
            } else if (playerX == 6) {
                answer += "g";
            } else if (playerX == 7) {
                answer += "h";
            }

            answer += (playerY + 1);
        }
        //prints out the translated code
        System.out.println(answer);

        return answer;
    }

    public void translateStringToNumber(String key) {

        if (key.charAt(5) == 'a') {
            playerX = 0;
        } else if (key.charAt(5) == 'b') {
            playerX = 1;
        } else if (key.charAt(5) == 'c') {
            playerX = 2;
        } else if (key.charAt(5) == 'd') {
            playerX = 3;
        } else if (key.charAt(5) == 'e') {
            playerX = 4;
        } else if (key.charAt(5) == 'f') {
            playerX = 5;
        } else if (key.charAt(5) == 'g') {
            playerX = 6;
        } else if (key.charAt(5) == 'h') {
            playerX = 7;
        }

        playerY = Character.getNumericValue(key.charAt(6)) - 1;

        //prints out the translated code
        //System.out.println(playerX + " " + playerY);

    }


    public static boolean checkInput(String string) {

        if (string.equals("bye") ) {

            return true;

        } else if (
                string.length() == 7 &&
                        string.substring(0, 5).equals("move ") &&
                        string.charAt(5) >= 'a' &&
                        string.charAt(5) <= 'h' &&
                        string.charAt(6) >= '1' &&
                        string.charAt(6) <= '8') {


            return true;

        } else return false;

    }


}*/
