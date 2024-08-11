package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.net.RemotePlayerProxy;
import javafx.application.Application;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;
/**
 * ServerMain
 * extends Application
 * @author OMAR MEZGHANI (311542)
 * @author MOHAMED CHARFI (311171)
 */
public class ServerMain extends Application {
    private static final String DEFAULT_FIRST_PLAYER = "Mohamed";
    private static final String DEFAULT_SECOND_PLAYER = "Omar";
    private static final int ARGUMENT_SIZE = 2;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * starts the game for the client
     * @param primaryStage primaryStage
     * @throws IOException  IOException
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        List<String> arguments = new ArrayList<>(this.getParameters().getRaw());
        if (arguments.size() == 0) {
            arguments.add(DEFAULT_FIRST_PLAYER);
            arguments.add(DEFAULT_SECOND_PLAYER);
        } else if (arguments.size() == ARGUMENT_SIZE - 1) {
            arguments.add(DEFAULT_SECOND_PLAYER);
        }

        Player playerProxy;

        try (ServerSocket serverSocket = new ServerSocket(5108)) {
            System.out.println("Waiting for client to connect");
            Socket socket = serverSocket.accept();
            System.out.println("Client connected");
            playerProxy = new RemotePlayerProxy(socket);
            System.out.println("Proxy created");
        }


        Random rng = new Random();
        SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());
        Map<PlayerId, Player> players = Map.of(PLAYER_1, new GraphicalPlayerAdapter(), PLAYER_2, playerProxy);
        Map<PlayerId, String> playerNames = Map.of(PLAYER_1, arguments.get(0), PLAYER_2, arguments.get(1));

        System.out.println("players: " + players);
        System.out.println("playerNames: " + playerNames);
        System.out.println("tickets: " + tickets);


        new Thread(() -> Game.play(players, playerNames, tickets, rng)).start();


    }
}