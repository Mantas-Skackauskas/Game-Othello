
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Scanner;

public class Game {


    private static Grid grid = new Grid();

    private static Player player1 = new Player();

    private static Player player2 = new Player();

    private static boolean player1move = true;

    private static String connectionType = "";

    public static void gamePlay() {

        boolean end = false;

        //randomly chooses which player starts the game
        double a = Math.random();

        if (a < 0.5) {

            player1move = false;
            player2.setColor(States.BLACK);

            System.out.println("---------------------------------");
            System.out.println("--   Player2 starts the game   --");

        } else {

            player1.setColor(States.BLACK);

            System.out.println("---------------------------------");
            System.out.println("--   Player1 starts the game   --");
        }

        //first print
        grid.printGrid();

        int moveNumber = 0;
        while (!gameEnd(grid)) {


            if (player1move) {
                System.out.println("Player1 chooses move, Color: " + player1.getColor().toString());
              //  player1.playerTotalMoveNumber=moveNumber;
                grid = player1.makeMove(grid, moveNumber);
              //  moveNumber=player1.playerTotalMoveNumber;

                player1move = false;
            } else {
                System.out.println("Player2 chooses move, Color: " + player2.getColor().toString());
             //   player2.playerTotalMoveNumber=moveNumber;
                grid = player2.makeMove(grid, moveNumber);
               // moveNumber=player2.playerTotalMoveNumber;

                player1move = true;
            }
            //number of moves is increased and printed
            moveNumber++;
            System.out.println("Move number: " + moveNumber);
            grid.printGrid();

        }
        //grid.printGrid();

        System.out.println("player1 score: " +
                playerScore(grid, player1.getColor()) +

                "\nplayer2 score: " +
                playerScore(grid, player2.getColor())

        );

    }

    //counts how many squares are painted of the player color
    public static int playerScore(Grid grid, States state) {

        int count = 0;

        for (int i = 0; i < 8; i++) {
            for (int k = 0; k < 8; k++) {
                if (grid.getGrid(i, k) == state) {
                    count++;
                }
            }
        }
        return count;
    }

    //goes after each move and checks if the game finishes (when all the squares are taken) or if there are no moves
    public static boolean gameEnd(Grid grid) {

        int whites = 0;
        int blacks = 0;

        for (int i = 0; i < 8; i++) {
            for (int k = 0; k < 8; k++) {
                if (grid.getGrid(i, k) == States.WHITE) {
                    whites++;
                } else if (grid.getGrid(i, k) == States.BLACK) {
                    blacks++;
                }
            }
        }

        System.out.println("Total WHITE: " + whites + ", Total BLACK: " + blacks);

        if (blacks == 0 || whites == 0) {

            System.out.println("GAME END. STALL");
            return true;

        } else if (blacks + whites == 64) {

            System.out.println("GAME END. GAME FINISHED");
            return true;

        } else {

            return false;
        }
    }

    //sets player mode according to the user's input
    public static void setPlayerMode() {


        System.out.println(
                "\nChoose game type below:\n" +
                        "\nHuman vs Human:   1" +
                        "\nHuman vs AI:      2" +
                        "\nAI vs AI:         3"
        );

        Scanner get = new Scanner(System.in);

        String playerMode = get.next();

        //human vs human
        if (playerMode.equals("1")) {

            player1 = new Player(grid, "human");
            player2 = new Player(grid, "human");

        }
        //human vs AI
        else if (playerMode.equals("2")) {

            player1 = new Player(grid, "human");
            player2 = new Player(grid, "ai");

        }
        //AI vs AI
        else if (playerMode.equals("3")) {

            player1 = new Player(grid, "ai");
            player2 = new Player(grid, "ai");

        }
        //AI vs other pc
        else if (playerMode.equals("4")) {

            player1 = new Player(grid, "ai");
            player2 = new Player(grid, "network");

        }
        //human vs other pc
        else if (playerMode.equals("5")) {

            player1 = new Player(grid, "human");

            Scanner scan = new Scanner(System.in);
            System.out.println("client or server?");
            String line;


            //CLIENT
            line = scan.next();

            if ((line).equals("client")) {
                connectionType = line;
                // Connects to a server
                System.out.println("Enter hostname");
                String host = scan.next();
                try {
                    Socket server = new Socket(host, 10006);

                    server.setSoTimeout(7000); //Socket Timeout


                    System.out.println("Server accepted connection");

                    //reading from server

                    BufferedReader serverInput = new BufferedReader
                            (new InputStreamReader
                                    (server.getInputStream()));

                    // Sending to server

                    PrintWriter serverOutput = new PrintWriter
                            (server.getOutputStream(), true);

                    System.out.print("Send game request to server: ");
                    line = scan.next();
                    serverOutput.println(line);
                    serverOutput.flush();

                    String reply = serverInput.readLine();


                    player2 = new Player(grid, line);

                } catch (SocketTimeoutException e) {
                    System.out.println("Timeout: Host is not responding");
                    System.exit(0);
                } catch (UnknownHostException e) {
                    System.out.println("Unknown host");
                    System.exit(0);
                } catch (ConnectException e) {
                    System.out.println("Connect Exception");
                    System.exit(0);
                } catch (SocketException e) {
                    System.out.println("Socket Exception");
                    System.exit(0);
                } catch (IOException e) {
                    System.out.println("Ai kazkas neveikia");
                    System.exit(0);
                }

                //SERVER
            } else if ((line).equals("server")) {

                try {

                    //Waits of incoming connection from clients
                    ServerSocket server = new ServerSocket(10006);
                    System.out.println("Waiting for client connection");

                    //Once a client tries to establish a connection, it accepts the connection
                    Socket client = server.accept();
                    client.setSoTimeout(10000); // Socket timeout in milliseconds
                    System.out.println("Client connection accepted");

                    server.close(); // Don't accept any more connections

                    //reading from client

                    BufferedReader clientInput = new BufferedReader
                            (new InputStreamReader
                                    (client.getInputStream()));

                    // Sending to client

                    PrintWriter clientOutput = new PrintWriter(client.getOutputStream(), true);

                    String gameRequest = clientInput.readLine();

                    String reply = "";

                    if (gameRequest.equals("new game")) {
                        System.out.println("Game request from " + client.getInetAddress().getHostName());
                        System.out.print("yes/no: ");

                        clientOutput.println(reply);
                        clientOutput.flush(); //flush everything just in case

                    }

                    player2 = new Player(grid, line);

                } catch (SocketTimeoutException e) {
                    System.out.println("Timeout: Host is not responding");
                    System.exit(0);
                } catch (UnknownHostException e) {
                    System.out.println("Unknown host");
                    System.exit(0);
                } catch (ConnectException e) {
                    System.out.println("Connect Exception");
                    System.exit(0);
                } catch (SocketException e) {
                    System.out.println("Socket Exception");
                    System.exit(0);
                } catch (IOException e) {
                    System.out.println("Ai kazkas neveikia");
                    System.exit(0);
                }


            }

        }


    }

    public static void game() {

        //sets player mode
        setPlayerMode();

        //starts the game
        gamePlay();
    }


}