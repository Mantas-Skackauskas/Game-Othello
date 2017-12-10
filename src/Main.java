/**
 * Created by va29 on 27/03/17.
 */
public class Main {

    public static void main(String[] args) {

        //checks if the right input was entered
        //creates a Game object according of the game mode
        if (args.length < 1) {
            System.out.println("Usage: java Main <Game_GameClient_GameServer>");
            System.exit(0);
        } else if (args[0].equals("Game")) {
            Game othello = new Game();
            othello.game();

        } else if (args[0].equals("GameClient")) {

            GameClient othello = new GameClient();
            othello.game();

        } else if (args[0].equals("GameServer")) {

            GameServer othello = new GameServer();
            othello.game();

        }

    }

}
