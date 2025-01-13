package tech.underoaks.coldcase.remote;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import jakarta.websocket.*;
import org.glassfish.grizzly.http.server.SessionManager;
import tech.underoaks.coldcase.stages.AbstractStage;
import tech.underoaks.coldcase.stages.StageManager;

import java.io.IOException;
import java.net.URI;
import java.util.Objects;

/**
 * A WebSocket client for communicating with the remote game server.
 * <p>
 * This singleton class establishes and manages a WebSocket connection to the server,
 * providing methods for sending and receiving messages.
 * autrh
 * </p>
 */
@ClientEndpoint
public class WebSocketClient {
    private static final Json json = new Json();
    private static Session session;
    private static WebSocketClient instance = null;
    private static String lobbyID = null;


    //TODO javadoc
    public void connect(String websocket_url, String session_id) {
        if(this.isConnectionOpen() || session_id.isEmpty()){
            return;
        }

        if(!Objects.equals(session_id, "new")){
            lobbyID = session_id;
        }


        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            URI uri = new URI("ws://" + websocket_url + "/?session=" + session_id);
            System.out.println("Connecting to Server " + uri);
            session = container.connectToServer(WebSocketClient.class, uri); // Initialize the session
            System.out.println("Connected to WebSocket server on:"+websocket_url);
        } catch (Exception e) {
            System.err.println("Error during WebSocket connection: " + e.getMessage());
            e.printStackTrace();
            return;
        }
    }
    //TODO javadoc
    public void connect(String websocket_url) {
        connect(websocket_url,"new");
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
            instance = new WebSocketClient();
        }
        return instance;
    }

    public static boolean exists() {
        return instance != null;
    }

    public static String getLobbyID() {
        if( lobbyID == "new"){
            return null;
        }
        return lobbyID;
    }

    @OnOpen
    public void onOpen(Session session) {
        //System.out.println("Connected to server");
        AbstractStage stage = StageManager.getInstance().getCurrentStage();
        stage.onConnected();
    }

    /**
     * this message gets invoked when a message is received from the server.
     * it !it doesn´t has access to the fields of this class
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
        StageManager.getInstance().getCurrentStage().onDisconnected();
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

    /**
     * Closes the current WebSocket session gracefully.
     * <p>
     * This method checks if the WebSocket session is open and attempts to close it.
     * If the session is already closed or not initialized, it simply returns true.
     * In case of an IOException during the closing process, it logs the error and returns false.
     * </p>
     *
     * @return {@code true} if the session was closed successfully or was already closed,
     *         {@code false} if an error occurred while attempting to close the session.
     */
    public boolean closeSession(){
        if (session != null && session.isOpen()) {
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE,""));
                lobbyID = null;
                System.out.println("WebSocket session closed successfully.");
            } catch (IOException e) {
                System.err.println("Error while closing WebSocket session: " + e.getMessage());
                return false;
            }
        }
        return true;
    }

    public boolean isConnectionOpen() {
        if(session == null)return false;
        return session.isOpen();
    }
}
