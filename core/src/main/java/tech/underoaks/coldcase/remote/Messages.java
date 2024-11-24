package tech.underoaks.coldcase.remote;

import com.badlogic.gdx.math.Vector2;
import org.glassfish.grizzly.utils.Pair;
import tech.underoaks.coldcase.game.Direction;

import java.util.Queue;

public class Messages {

    // AppendRemoteInteractionMessage
    public static class AppendRemoteInteractionMessage extends Message {
        private Vector2 targetPos;
        private Direction actionDirection;

        // No-args constructor
        public AppendRemoteInteractionMessage() {}

        // All-args constructor
        public AppendRemoteInteractionMessage(String remoteGameControllerInstanceId, Vector2 targetPos, Direction actionDirection) {
            this.setRemoteGameControllerInstanceId(remoteGameControllerInstanceId);
            this.targetPos = targetPos;
            this.actionDirection = actionDirection;
        }

        // Getters and setters
        public Vector2 getTargetPos() {
            return targetPos;
        }

        public void setTargetPos(Vector2 targetPos) {
            this.targetPos = targetPos;
        }

        public Direction getActionDirection() {
            return actionDirection;
        }

        public void setActionDirection(Direction actionDirection) {
            this.actionDirection = actionDirection;
        }
    }

    // AppendRemoteInteractionResponseMessage
    public static class AppendRemoteInteractionResponseMessage extends Message {
        private Queue<Pair<Vector2, Direction>> interactions;

        // No-args constructor
        public AppendRemoteInteractionResponseMessage() {}

        // All-args constructor
        public AppendRemoteInteractionResponseMessage(String remoteGameControllerInstanceId, Queue<Pair<Vector2, Direction>> interactions) {
            this.setRemoteGameControllerInstanceId(remoteGameControllerInstanceId);
            this.interactions = interactions;
        }

        // Getters and setters
        public Queue<Pair<Vector2, Direction>> getInteractions() {
            return interactions;
        }

        public void setInteractions(Queue<Pair<Vector2, Direction>> interactions) {
            this.interactions = interactions;
        }
    }

    // CreateRemoteInteractionChainMessage
    public static class CreateRemoteInteractionChainMessage extends Message {

        // No-args constructor
        public CreateRemoteInteractionChainMessage() {}

        // All-args constructor
        public CreateRemoteInteractionChainMessage(String remoteGameControllerInstanceId) {
            this.setRemoteGameControllerInstanceId(remoteGameControllerInstanceId);
        }
    }

    // CreateRemoteInteractionChainResponseMessage
    public static class CreateRemoteInteractionChainResponseMessage extends Message {
        private String remoteInteractionChainId;

        // No-args constructor
        public CreateRemoteInteractionChainResponseMessage() {}

        // All-args constructor
        public CreateRemoteInteractionChainResponseMessage(String remoteGameControllerInstanceId, String remoteInteractionChainId) {
            this.setRemoteGameControllerInstanceId(remoteGameControllerInstanceId);
            this.remoteInteractionChainId = remoteInteractionChainId;
        }

        // Getters and setters
        public String getRemoteInteractionChainId() {
            return remoteInteractionChainId;
        }

        public void setRemoteInteractionChainId(String remoteInteractionChainId) {
            this.remoteInteractionChainId = remoteInteractionChainId;
        }
    }

    // ApplyRemoteGSUsMessage
    public static class ApplyRemoteGSUsMessage extends Message {

        // No-args constructor
        public ApplyRemoteGSUsMessage() {}

        // All-args constructor
        public ApplyRemoteGSUsMessage(String remoteGameControllerInstanceId) {
            this.setRemoteGameControllerInstanceId(remoteGameControllerInstanceId);
        }
    }

    public static class AbortRemoteGSUsMessage extends Message {

        public AbortRemoteGSUsMessage() {
        }

        public AbortRemoteGSUsMessage(String remoteGameControllerInstanceId) {
            this.setRemoteGameControllerInstanceId(remoteGameControllerInstanceId);
        }
    }
}
