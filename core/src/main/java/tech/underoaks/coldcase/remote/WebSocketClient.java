package tech.underoaks.coldcase.remote;

import com.badlogic.gdx.utils.Json;
import jakarta.websocket.*;
import tech.underoaks.coldcase.game.GameController;
import tech.underoaks.coldcase.state.InteractionChain;


import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


@ClientEndpoint
public class WebSocketClient {
    private static Json json = new Json();
    private static Session session;
    private static WebSocketClient instance = null;
    private InteractionChain chain;

    public CountDownLatch getLatch() {
        return latch;
    }

    // Synchronization mechanism for waiting for responses
    private CountDownLatch latch;
    private boolean responseSuccess;

    public static WebSocketClient getInstance(){
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
                e.printStackTrace();    //TODO Handle error better / correcktly
            }
        }
        return instance;
    }

    @OnOpen
    public void onOpen(Session session) throws IOException, EncodeException {
        //session.getBasicRemote().sendText("HALLO SERVER");
        //session.getBasicRemote().sendObject(new movableBlock());
        System.out.println("Connected to server");
    }

    @OnMessage
    public void onMessage(String message) {
        //System.out.println("incomming:");
        //System.out.println(json.prettyPrint(message));
        Object deserializedObject = json.fromJson(Object.class, message);

        if (deserializedObject instanceof CreateRemoteInteractionChainMessage) {
            //GameController.getInstance().handleCreateRemoteInteractionChain();
        } else if (deserializedObject instanceof AbortRemoteInteractionChainMessage) {
            //GameController.getInstance().handleAbortRemoteInteractionChain();
        } else if (deserializedObject instanceof AppendRemoteInteractionMessage) {

            try {
                AppendRemoteInteractionResponseMessage returnMessage = new AppendRemoteInteractionResponseMessage();
                //boolean test = GameController.getInstance().handleAppendRemoteInteraction();
                //returnMessage.setSuccess(test); //WTF only gets includet if true
                String responseMessage = json.toJson(returnMessage, Object.class);
                session.getBasicRemote().sendText(responseMessage);
            } catch (IOException e) {
                System.err.println("Failed to send message: " + e.getMessage());
            }
        } else if (deserializedObject instanceof AppendRemoteInteractionResponseMessage res) {
            System.out.println("RETURN");
            System.out.println(getLatch());
            responseSuccess = res.getSuccess();

            if (getLatch() != null) {
                System.out.println("Countdown latch before: " + latch.getCount());
                getLatch().countDown();
                System.out.println("Countdown latch after: " + latch.getCount());
            }
        } else if (deserializedObject instanceof ApplyRemoteGSUsMessage) {
            //GameController.getInstance().handleApplyRemoteGSUs();
        } else {
            System.out.println("unown message");
        }
    }


    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("Connection closed: " + closeReason);
    }

    public void createRemoteInteractionChain(){
        try {
            String message = json.toJson(new CreateRemoteInteractionChainMessage(), Object.class);
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            System.err.println("Failed to send message: " + e.getMessage());
        }
    }

    public void abortRemoteInteractionChain(){
        try {
            String message = json.toJson(new AbortRemoteInteractionChainMessage(), Object.class);
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            System.err.println("Failed to send message: " + e.getMessage());
        }
    }

    public boolean appendRemoteInteraction() {
        latch = new CountDownLatch(1); //wait for 1 response
        responseSuccess = false;

        try {
            String message = json.toJson(new AppendRemoteInteractionMessage(), Object.class);
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            System.err.println("Failed to send message: " + e.getMessage());
        }
        System.out.println("waiting for response latch ="+latch);
        try {
            // Wait for the response or timeout after 5 seconds
            if (!latch.await(5, TimeUnit.SECONDS)) {
                System.err.println("Timeout waiting for append response.");
            }
        } catch (InterruptedException e) {
            System.err.println("Interrupted while waiting for append response.");
        }

        return responseSuccess;  // Return the result of the response
    }

    public void applyRemoteGSUs(){
        try {
            String message = json.toJson(new ApplyRemoteGSUsMessage(), Object.class);
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            System.err.println("Failed to send message: " + e.getMessage());
        }
    }


}
