package tech.underoaks.coldcase.remote;

import tech.underoaks.coldcase.game.GameController;
import tech.underoaks.coldcase.game.Interaction;

import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import static com.badlogic.gdx.net.HttpRequestBuilder.json;

public class WebSocketMessagesManager {

    /**
     * Singleton instance of the WebSocketMessagesManager.
     */
    private static WebSocketMessagesManager instance;

    /**
     * A thread-safe map to store pending callbacks for asynchronous message responses.
     */
    private final ConcurrentHashMap<String, CompletableFuture<Object>> pendingCallbacks = new ConcurrentHashMap<>();

    /**
     * Retrieves the singleton instance of the WebSocketMessagesManager.
     * <p>
     * If the instance is not yet initialized, it creates a new one.
     * </p>
     *
     * @return the singleton instance of the WebSocketMessagesManager.
     */
    public static WebSocketMessagesManager getInstance() {
        if (instance == null) {
            instance = new WebSocketMessagesManager();
        }
        return instance;
    }

    /**
     * Sends a request to create a remote interaction chain and registers a callback to handle the response.
     *
     * @param remoteGameControllerInstanceId the ID of the remote game controller instance.
     * @param future                         the {@link CompletableFuture} to complete when the response is received.
     */
    public void createRemoteInteractionChain(String remoteGameControllerInstanceId, CompletableFuture<Object> future) {
        pendingCallbacks.put(remoteGameControllerInstanceId, future); //register callback
        String message = json.toJson(new Messages.CreateRemoteInteractionChainMessage(remoteGameControllerInstanceId), Object.class);
        WebSocketClient.getInstance().send(message);
    }

    /**
     * Sends a request to append a remote interaction and registers a callback to handle the response.
     *
     * @param remoteGameControllerInstanceId the ID of the remote game controller instance.
     * @param future                         the {@link CompletableFuture} to complete when the response is received.
     * @param interaction                    The interaction to trigger.
     * @param suppressTranscendentFollowUp   whether to suppress follow-up interactions.
     */
    public void appendRemoteInteraction(String remoteGameControllerInstanceId, CompletableFuture<Object> future, Interaction interaction, boolean suppressTranscendentFollowUp) {

        pendingCallbacks.put(remoteGameControllerInstanceId, future); //register callback
        String message = json.toJson(new Messages.AppendRemoteInteractionMessage(remoteGameControllerInstanceId, interaction, suppressTranscendentFollowUp), Object.class);

        WebSocketClient.getInstance().send(message);
    }

    /**
     * Sends a request to apply remote game state updates (GSUs).
     *
     * @param remoteGameControllerInstanceId the ID of the remote game controller instance.
     */
    public void applyRemoteGSUs(String remoteGameControllerInstanceId) {
        WebSocketClient.getInstance().send(json.toJson(new Messages.ApplyRemoteGSUsMessage(remoteGameControllerInstanceId), Object.class));
    }

    /**
     * Sends a request to abort remote game state updates (GSUs).
     *
     * @param remoteGameControllerInstanceId the ID of the remote game controller instance.
     */
    public void abortRemoteGSU(String remoteGameControllerInstanceId) {
        WebSocketClient.getInstance().send(json.toJson(new Messages.ApplyRemoteGSUsMessage(remoteGameControllerInstanceId), Object.class));
    }

    /**
     * Completes the future associated with a given message.
     *
     * @param messageObj the {@link Message} object to use for completing the future.
     */
    public void callback(Message messageObj) {
        CompletableFuture<Object> future = pendingCallbacks.get(messageObj.getRemoteGameControllerInstanceId());
        if (future == null) {
            System.out.println("no completable future pending for this" + messageObj.getClass());
            return;
        }
        future.complete(messageObj);
    }

    /**
     * Handles incoming messages from the WebSocket connection.
     * <p>
     * This method runs asynchronously and dispatches messages based on their type.
     * </p>
     *
     * @param deserializedObject the deserialized message.
     */
    public static void handleIncomingMessages(Object deserializedObject) {
        // Run asynchronously with CompletableFuture
        CompletableFuture.runAsync(() -> {
            try {
                //Object deserializedObject = json.fromJson(Object.class, message);

                switch (deserializedObject) {
                    case Messages.CreateRemoteInteractionChainMessage messageObj -> {
                        String remoteInteractionChainId = "TEST" + UUID.randomUUID().toString(); // not needed for now
                        GameController.getInstance().handleCreateRemoteInteractionChain();
                        WebSocketClient.getInstance().send(json.toJson(new Messages.CreateRemoteInteractionChainResponseMessage(messageObj.getRemoteGameControllerInstanceId(), remoteInteractionChainId), Object.class));
                    }
                    case Messages.AppendRemoteInteractionMessage messageObj -> {
                        Queue<Interaction> interactions = GameController.getInstance().handleTriggerRemoteInteraction(messageObj.getInteraction(), messageObj.getSuppressTranscendentFollowUp());

                        WebSocketClient.getInstance().send(json.toJson(new Messages.AppendRemoteInteractionResponseMessage(messageObj.getRemoteGameControllerInstanceId(), interactions), Object.class));
                    }
                    case Messages.ApplyRemoteGSUsMessage messageObj -> {
                        GameController.getInstance().handleApplyRemoteGSUsMessage();
                        //System.out.println("ApplyRemoteGSUs called but function is missing for now");
                    }
                    case Messages.AbortRemoteGSUsMessage messageObj -> {
                        GameController.getInstance().handleAbortRemoteGSUsMessage();
                        //System.out.println("AbortRemoteGSUsMessage called but function is missing for now");
                    }
                    case Messages.AppendRemoteInteractionResponseMessage messageObj -> {
                        WebSocketMessagesManager.getInstance().callback(messageObj);
                    }
                    case Messages.CreateRemoteInteractionChainResponseMessage messageObj -> {   //not needed for now
                        WebSocketMessagesManager.getInstance().callback(messageObj);
                    }
                    case null, default -> System.out.println("unknown message");
                }
            } catch (Exception e) {
                System.err.println("Error processing message: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

}
