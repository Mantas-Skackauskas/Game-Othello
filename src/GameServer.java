
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Scanner;

public class GameServer {


    private static Grid grid = new Grid();

    private static Player player1 = new Player();

    private static Player player2 = new Player();

    private static boolean player1move = true;

    private static String connectionType = "";

    public static void gamePlay() {

        ServerSocket server;
        Socket client;
        BufferedReader clientInput;
        PrintWriter clientOutput;
        String reply = "";
        String gameRequest;

        try {

            do {
                //Waits of incoming connection from clients
                server = new ServerSocket(10006);
                System.out.println("Waiting for client connection");


                //Once a client tries to establish a connection, it accepts the connection
                client = server.accept();
                client.setSoTimeout(1000000); // Socket timeout in milliseconds
                System.out.println("Client connection accepted");
                server.close(); // Don't accept any more connections


                //reading from client
                clientInput = new BufferedReader(new InputStreamReader(client.getInputStream()));
                // Sending to client
                clientOutput = new PrintWriter(client.getOutputStream(), true);

                do {
                    gameRequest = clientInput.readLine();
                } while (gameRequest != null && !gameRequest.equals("hello"));
                //reads the hello message

                //sends hello back
                reply = "hello";
                clientOutput.println(reply);
                clientOutput.flush(); //flush everything just in case


                do {
                    reply = clientInput.readLine();
                    System.out.println(reply);
                } while (!reply.equals("new game") && reply.equals("reject"));


                if (reply.equals("new game")) {
                    System.out.println("Client wants to start a new game. Accept?");

                    reply = "accept";

                    clientOutput.println(reply);
                    //SERVER IS WHITE
                    player1.setColor(States.BLACK);

                    System.out.println("---------------------------------");
                    System.out.println("--  Player 1 starts the game   --");

                    String answer="";
                    reply="";

                    //first print
                    grid.printGrid();

                    int moveNumber = 0;
                    while (!gameEnd(grid) && !answer.equals("bye") & !reply.equals("bye")) {

                        if (player1move) {
                            //CLIENT

                            System.out.println("Player1 chooses move, Color: " + player1.getColor().toString());
                            //reply = clientInput.readLine();


                            do {
                                answer = clientInput.readLine();
                            } while (!checkInput(answer));

                            if (!answer.equals("bye") && !answer.equals("move pass")) {
                                grid = player1.makeMoveOnline(grid, answer);
                            } else if (answer.equals("move pass")) {
                                System.out.println("Player 1 passes the move");
                            } else if (answer.equals("bye")){
                                System.out.println("Terminating connection");
                            }

                            player1move = false;

                        } else {
                            //SERVER

                            System.out.println("Player 2 chooses move, Color: " + player2.getColor().toString());
                            grid = player2.makeMove(grid, moveNumber);
                            reply = player2.translateNumberToString();
                            clientOutput.println(reply);

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

            } while (!reply.equals("new game") && reply.equals("reject") && !reply.equals("bye"));

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
            System.out.println("Wrong input");
            System.exit(0);
        } catch (NullPointerException e) {
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


    }

    public static boolean checkInput(String string) {

        if (string.equals("bye") || string.equals("move pass")) {

            return true;

        } else if (
                string.length() == 7 &&
                        string.substring(0, 5).equals("move ") &&
                        string.charAt(5) >= 'a' &&
                        string.charAt(5) <= 'h' &&
                        string.charAt(6) >= '1' &&
                        string.charAt(6) <= '8'
                ) {


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