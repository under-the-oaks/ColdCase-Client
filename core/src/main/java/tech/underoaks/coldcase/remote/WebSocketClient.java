package tech.underoaks.coldcase.remote;

import com.badlogic.gdx.utils.Json;
import jakarta.websocket.*;
import java.io.IOException;
import java.net.URI;

/**
 * A WebSocket client for communicating with the remote game server.
 * <p>
 * This singleton class establishes and manages a WebSocket connection to the server,
 * providing methods for sending and receiving messages.
 * </p>
 */
@ClientEndpoint
public class WebSocketClient {
    private static final Json json = new Json();
    private static Session session;
    private static WebSocketClient instance = null;
    private static String lobbyID = "";

    public static WebSocketClient create(String websocket_url, String session_id) {
        if(instance != null) {
            throw new IllegalStateException("WebSocketClient already created");
        }
        instance = new WebSocketClient();

        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            URI uri = new URI("ws://" + websocket_url + "/?session=" + session_id);
            System.out.println("Connecting to Server " + uri);
            session = container.connectToServer(WebSocketClient.class, uri); // Initialize the session
            lobbyID = session_id;
            System.out.println("Connected to WebSocket server on:"+websocket_url);
        } catch (Exception e) {
            System.err.println("Error during WebSocket connection: " + e.getMessage());
            e.printStackTrace();    //TODO Handle error better / correctly
        }

        return instance;
    }

    public static WebSocketClient create(String websocket_url) {
        if(instance != null) {
            throw new IllegalStateException("WebSocketClient already created");
        }
        instance = new WebSocketClient();

        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            URI uri = new URI("ws://" + websocket_url + "/?session=" + "new");
            System.out.println("Connecting to Server " + uri);
            session = container.connectToServer(WebSocketClient.class, uri); // Initialize the session

            System.out.println("Connected to WebSocket server.");
        } catch (Exception e) {
            System.err.println("Error during WebSocket connection: " + e.getMessage());
            e.printStackTrace();    //TODO Handle error better / correctly
        }

        return instance;
    }



    /**
     * Retrieves the singleton instance of the WebSocket client.
     * <p>
     * If the instance is not yet initialized, this method initializes it and connects to the WebSocket server.
     * </p>
     *
     * @return the singleton instance of the WebSocket client.
     */
    public static WebSocketClient getInstance() {
        if (instance == null) {
            throw new IllegalStateException("WebSocketClient not created");
        }
        return instance;
    }

    public static boolean exists() {
        return instance != null;
    }

    public static String getLobbyID() {
        return lobbyID;
    }

    @OnOpen
    public void onOpen(Session session) {
        //System.out.println("Connected to server");
    }

    /**
     * this message gets invoked when a message is received from the server.
     * it !it dosen´t has access to the fields of this class
     *
     * @param message the received message as a {@link String}.
     */
    @OnMessage
    public void onMessage(String message) {
        //System.out.println("incoming:");
        //System.out.println(json.prettyPrint(message));
        //Object deserializedObject = json.fromJson(Object.class, message);
        // Example of parsing Lobby ID from server response
        Object deserializedObject = json.fromJson(Object.class, message);
        if (deserializedObject instanceof Messages.lobbyIdMessage) {
            lobbyID = ((Messages.lobbyIdMessage) deserializedObject).getLobbyId();
            System.out.println("Received Lobby ID: " + lobbyID);
        } else {
            WebSocketMessagesManager.handleIncomingMessages(deserializedObject);
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("Connection closed: " + closeReason);
    }

    /**
     * Sends a message to the WebSocket server.
     *
     * @param message the message to send as a {@link String}.
     */
    public void send(String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            System.err.println("Failed to send message: " + e.getMessage());
        }
    }
}
