
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Scanner;

public class GameClient {


    private static Grid grid = new Grid();

    private static Player player1 = new Player();

    private static Player player2 = new Player();

    private static boolean player1move = true;

    private static String connectionType = "";

    public static void terminate(String string) {
        if (string.equals("bye")) {
            System.exit(0);
        }
    }


    public static void gamePlay() {

        String line;
        Scanner scan = new Scanner(System.in);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {


            System.out.print("Type in the host name: ");

            Scanner host = new Scanner(System.in);
            String hostname = host.next();

            Socket server = new Socket(hostname, 10006);
            server.setSoTimeout(700000); //Socket Timeout
            System.out.println("Server accepted connection");


            //reading from server
            BufferedReader serverInput = new BufferedReader(new InputStreamReader(server.getInputStream()));
            // Sending to server
            PrintWriter serverOutput = new PrintWriter(server.getOutputStream(), true);

            //sets hello line
            line = "hello";

            //sends the hello message
            serverOutput.println(line);
            serverOutput.flush();


            do {
                line = serverInput.readLine();
            } while (!line.equals("hello"));

            //  if (line.equals("hello")) {

            System.out.print("Send \"new game\" to start a new game: ");

            do {
                line = reader.readLine();
            } while (!line.equals("new game") && !line.equals("reject"));

            serverOutput.println(line);


            String reply = serverInput.readLine();

            System.out.println(reply);

            if (reply.equals("accept")) {
                System.out.println("Server has accepted to start a new game.");
                //CLIENT IS BLACK
                player1.setColor(States.BLACK);
                System.out.println("---------------------------------");
                System.out.println("--  Player 1 starts the game   --");

                //first print
                grid.printGrid();

                int moveNumber = 0;
                while (!gameEnd(grid)) {

                    if (player1move) {
                        //CLIENT

                        System.out.println("Player 1 chooses move, Color: " + player1.getColor().toString());
                        grid = player1.makeMove(grid, moveNumber);

                        System.out.println(player1.translateNumberToString());
                        serverOutput.println(player1.translateNumberToString());


                        player1move = false;

                    } else {
                        //SERVER


                        System.out.println("Player 2 chooses move, Color: " + player2.getColor().toString());
                        reply = serverInput.readLine();

                        player2.translateStringToNumber(reply);

                        grid = player2.makeMoveOnline(grid, reply);


                        player1move = true;
                    }

                    moveNumber++;
                    System.out.println("Move number: " + moveNumber);
                    grid.printGrid();

                }
                //grid.printGrid();

                System.out.println("Player 1 score: " +
                        playerScore(grid, player1.getColor()) +

                        "\nPlayer 2 score: " +
                        playerScore(grid, player2.getColor())

                );
            }
            // }
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

    public static void setPlayerMode() {


        System.out.println("\nChoose game type below:\n" +
                "\nHuman vs Network:   1" +
                "\nAI vs Network:      2");
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

        }


    }


    public static boolean  checkInput(String string) {

        if (string.equals("bye")) {

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

    public static void game() {

        //sets player mode
        setPlayerMode();

        //starts the game
        gamePlay();
    }


}