package tech.underoaks.coldcase.remote;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import jakarta.websocket.*;
import org.glassfish.grizzly.utils.Pair;
import tech.underoaks.coldcase.game.Direction;
import tech.underoaks.coldcase.game.GameController;


import java.io.IOException;
import java.net.URI;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@ClientEndpoint
public class WebSocketClient {
    private static final Json json = new Json();
    private static Session session;
    private static WebSocketClient instance = null;

    public static WebSocketClient getInstance() {
        if (instance == null) {
            instance = new WebSocketClient();


            try {
                WebSocketContainer container = ContainerProvider.getWebSocketContainer();
                URI uri = new URI("ws://localhost:8080/?session=12345");  // TODO handle session UID for now static
                System.out.println("Connecting to WebSocket server...");
                session = container.connectToServer(WebSocketClient.class, uri); // Initialize the session
                System.out.println("Connected to WebSocket server.");
            } catch (Exception e) {
                System.err.println("Error during WebSocket connection: " + e.getMessage());
                e.printStackTrace();    //TODO Handle error better / correctly
            }
        }
        return instance;
    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Connected to server");
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("incoming:");
        System.out.println(json.prettyPrint(message));
        //Object deserializedObject = json.fromJson(Object.class, message);
        WebSocketMessagesManager.hanndleIncommingMessages(message);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("Connection closed: " + closeReason);
    }

    public void send(String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            System.err.println("Failed to send message: " + e.getMessage());
        }
    }
}
