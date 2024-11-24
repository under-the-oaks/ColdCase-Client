package tech.underoaks.coldcase.remote;

import com.badlogic.gdx.math.Vector2;
import org.glassfish.grizzly.utils.Pair;
import tech.underoaks.coldcase.game.Direction;
import tech.underoaks.coldcase.game.GameController;

import java.util.HashMap;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.badlogic.gdx.net.HttpRequestBuilder.json;

public class WebSocketMessagesManager {

    private static WebSocketMessagesManager instance;
    private final HashMap<String, CompletableFuture<Object>> pendingCallbacks = new HashMap<>();

    public static WebSocketMessagesManager getInstace(){
        if (instance == null) {
            instance = new WebSocketMessagesManager();
        }
        return instance;
    }


    public void createRemoteInteractionChain(String remoteGameControllerInstanceId, CompletableFuture<Object> future) {
        pendingCallbacks.put(remoteGameControllerInstanceId, future); //register callback
        String message = json.toJson(new Messages.CreateRemoteInteractionChainMessage(remoteGameControllerInstanceId), Object.class);
        WebSocketClient.getInstance().send(message);
    }

    public void appendRemoteInteraction(String remoteGameControllerInstanceId, CompletableFuture<Object> future, Vector2 targetPos, Direction actionDirection) {
        pendingCallbacks.put(remoteGameControllerInstanceId, future); //register callback
        String message = json.toJson(new Messages.AppendRemoteInteractionMessage(remoteGameControllerInstanceId, targetPos, actionDirection), Object.class);

        WebSocketClient.getInstance().send(message);
    }

    public void applyRemoteGSUs(String remoteGameControllerInstanceId) {
        WebSocketClient.getInstance().send(json.toJson(new Messages.ApplyRemoteGSUsMessage(remoteGameControllerInstanceId), Object.class));
    }

    public void abortRemoteGSU(String remoteGameControllerInstanceId) {
        WebSocketClient.getInstance().send(json.toJson(new Messages.ApplyRemoteGSUsMessage(remoteGameControllerInstanceId), Object.class));
    }

    public void callback(Message messageObj){
        CompletableFuture<Object> future = pendingCallbacks.get(messageObj.getRemoteGameControllerInstanceId());
        if(future == null){
            System.out.println("no completable future pending for this" + messageObj.getClass());
            return;
        }
        future.complete(messageObj);
    }

    public static void hanndleIncommingMessages(String message){
        // Run asynchronously with CompletableFuture
        CompletableFuture.runAsync(() -> {
            try {
                Object deserializedObject = json.fromJson(Object.class, message);

                switch (deserializedObject) {
                    case Messages.CreateRemoteInteractionChainMessage messageObj -> {
                        String remoteInteractionChainId = "TEST" + UUID.randomUUID().toString(); // Handle logic here
                        System.out.println("CreateRemoteInteractionChainMessage called but function is missing for now");
                        WebSocketClient.getInstance().send(json.toJson(new Messages.CreateRemoteInteractionChainResponseMessage(messageObj.getRemoteGameControllerInstanceId(), remoteInteractionChainId), Object.class));
                    }
                    case Messages.AppendRemoteInteractionMessage messageObj -> {
                        Queue<Pair<Vector2, Direction>> interactions = GameController.getInstance().handleTriggerRemoteInteraction(messageObj.getTargetPos(), messageObj.getActionDirection());
                        WebSocketClient.getInstance().send(json.toJson(new Messages.AppendRemoteInteractionResponseMessage(messageObj.getRemoteGameControllerInstanceId(), interactions), Object.class));
                    }
                    case Messages.ApplyRemoteGSUsMessage messageObj -> {
                        //GameController.getInstance().handleApplyRemoteGSUsMessage();
                        System.out.println("ApplyRemoteGSUs called but function is missing for now");
                    }
                    case Messages.AbortRemoteGSUsMessage messageObj -> {
                        //GameController.getInstance().handleAbortRemoteGSUsMessage();
                        System.out.println("AbortRemoteGSUsMessage called but function is missing for now");
                    }
                    case Messages.AppendRemoteInteractionResponseMessage messageObj -> {
                        WebSocketMessagesManager.getInstace().callback(messageObj);
                    }
                    case Messages.CreateRemoteInteractionChainResponseMessage messageObj -> {
                        WebSocketMessagesManager.getInstace().callback(messageObj);
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
