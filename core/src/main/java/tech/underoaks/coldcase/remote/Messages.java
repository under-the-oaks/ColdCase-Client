package tech.underoaks.coldcase.remote;

import tech.underoaks.coldcase.game.Interaction;

import java.util.Queue;

/**
 * Contains nested classes that represent various types of messages used in the remote game controller system.
 */
public class Messages {

    // AppendRemoteInteractionMessage
    public static class AppendRemoteInteractionMessage extends Message {

        private Interaction interaction;

        public boolean getSuppressTranscendentFollowUp() {
            return suppressTranscendentFollowUp;
        }

        public void setSuppressTranscendentFollowUp(boolean suppressTranscendentFollowUp) {
            this.suppressTranscendentFollowUp = suppressTranscendentFollowUp;
        }

        private boolean suppressTranscendentFollowUp;

        // No-args constructor
        public AppendRemoteInteractionMessage() {}

        // All-args constructor
        public AppendRemoteInteractionMessage(String remoteGameControllerInstanceId, Interaction interaction, boolean suppressTranscendentFollowUp) {
            this.setRemoteGameControllerInstanceId(remoteGameControllerInstanceId);
            this.interaction = interaction;
            this.suppressTranscendentFollowUp = suppressTranscendentFollowUp;
        }

        // Getters and setters
        public Interaction getInteraction() {
            return interaction;
        }

        public void setInteraction(Interaction interaction) {
            this.interaction = interaction;
        }
    }

    // AppendRemoteInteractionResponseMessage
    public static class AppendRemoteInteractionResponseMessage extends Message {
        private Queue<Interaction> interactions;

        // No-args constructor
        public AppendRemoteInteractionResponseMessage() {}

        // All-args constructor
        public AppendRemoteInteractionResponseMessage(String remoteGameControllerInstanceId, Queue<Interaction> interactions) {
            this.setRemoteGameControllerInstanceId(remoteGameControllerInstanceId);
            this.interactions = interactions;
        }

        // Getters and setters
        public Queue<Interaction> getInteractions() {
            return interactions;
        }

        public void setInteractions(Queue<Interaction> interactions) {
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

    public static class lobbyIdMessage{
        String lobbyId;

        public lobbyIdMessage(String lobbyId) {
            this.lobbyId = lobbyId;
        }
        public lobbyIdMessage(){}

        public String getLobbyId() {
            return lobbyId;
        }

        public void setLobbyId(String lobbyId) {
            this.lobbyId = lobbyId;
        }
    }

    public static class startGameMessage{
        int levelIndex;
        public startGameMessage(int levelIndex) {
            this.levelIndex = levelIndex;
        }
        public startGameMessage(){}

        public int getLevelIndex() {
            return levelIndex;
        }

        public void setLevelIndex(int levelIndex) {
            this.levelIndex = levelIndex;
        }
    }

    public static class exitToMainMenuMessage{
        public exitToMainMenuMessage(){}
    }
}
