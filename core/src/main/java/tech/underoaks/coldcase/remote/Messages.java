package tech.underoaks.coldcase.remote;

import tech.underoaks.coldcase.game.Interaction;

import java.util.Queue;

/**
 * Contains nested classes that represent various types of messages used in the remote game controller system.
 */
public class Messages {

    /**
     * Message used to append an interaction to a remote interaction chain.
     * Contains an {@link Interaction} and a flag to suppress transcendent follow-up interactions.
     */
    public static class AppendRemoteInteractionMessage extends Message {

        private Interaction interaction;

        /**
         * Returns whether transcendent follow-up interactions should be suppressed.
         *
         * @return {@code true} if transcendent follow-up should be suppressed, {@code false} otherwise
         */
        public boolean getSuppressTranscendentFollowUp() {
            return suppressTranscendentFollowUp;
        }

        /**
         * Sets the flag indicating whether transcendent follow-up interactions should be suppressed.
         *
         * @param suppressTranscendentFollowUp {@code true} to suppress follow-up interactions, {@code false} otherwise
         */
        public void setSuppressTranscendentFollowUp(boolean suppressTranscendentFollowUp) {
            this.suppressTranscendentFollowUp = suppressTranscendentFollowUp;
        }

        private boolean suppressTranscendentFollowUp;

        /**
         * Constructs an empty {@code AppendRemoteInteractionMessage}.
         */
        public AppendRemoteInteractionMessage() {
        }

        /**
         * Constructs an {@code AppendRemoteInteractionMessage} with the specified remote game controller instance identifier,
         * interaction, and flag indicating whether to suppress transcendent follow-up.
         *
         * @param remoteGameControllerInstanceId the identifier of the remote game controller instance
         * @param interaction                    the {@link Interaction} to be appended
         * @param suppressTranscendentFollowUp   flag indicating whether to suppress transcendent follow-up interactions
         */
        public AppendRemoteInteractionMessage(String remoteGameControllerInstanceId, Interaction interaction, boolean suppressTranscendentFollowUp) {
            this.setRemoteGameControllerInstanceId(remoteGameControllerInstanceId);
            this.interaction = interaction;
            this.suppressTranscendentFollowUp = suppressTranscendentFollowUp;
        }

        /**
         * Returns the {@link Interaction} associated with this message.
         *
         * @return the {@link Interaction} to be appended remotely
         */
        public Interaction getInteraction() {
            return interaction;
        }

        /**
         * Sets the {@link Interaction} for this message.
         *
         * @param interaction the {@link Interaction} to be appended
         */
        public void setInteraction(Interaction interaction) {
            this.interaction = interaction;
        }
    }

    /**
     * Response message for an append remote interaction request.
     * Contains a queue of {@link Interaction} objects resulting from the append operation.
     */
    public static class AppendRemoteInteractionResponseMessage extends Message {
        private Queue<Interaction> interactions;

        /**
         * Constructs an empty {@code AppendRemoteInteractionResponseMessage}.
         */
        public AppendRemoteInteractionResponseMessage() {
        }

        /**
         * Constructs an {@code AppendRemoteInteractionResponseMessage} with the specified remote game controller instance identifier and interactions.
         *
         * @param remoteGameControllerInstanceId the identifier of the remote game controller instance
         * @param interactions                   the queue of {@link Interaction} objects returned as a response
         */
        public AppendRemoteInteractionResponseMessage(String remoteGameControllerInstanceId, Queue<Interaction> interactions) {
            this.setRemoteGameControllerInstanceId(remoteGameControllerInstanceId);
            this.interactions = interactions;
        }

        /**
         * Returns the queue of {@link Interaction} objects associated with this response.
         *
         * @return the queue of interactions
         */
        public Queue<Interaction> getInteractions() {
            return interactions;
        }

        /**
         * Sets the queue of {@link Interaction} objects for this response.
         *
         * @param interactions the queue of interactions to set
         */
        public void setInteractions(Queue<Interaction> interactions) {
            this.interactions = interactions;
        }
    }

    /**
     * Message to request the creation of a new remote interaction chain.
     * Sent to initialize a remote simulation of interactions.
     */
    public static class CreateRemoteInteractionChainMessage extends Message {

        /**
         * Constructs an empty {@code CreateRemoteInteractionChainMessage}.
         */
        public CreateRemoteInteractionChainMessage() {
        }

        /**
         * Constructs a {@code CreateRemoteInteractionChainMessage} with the specified remote game controller instance identifier.
         *
         * @param remoteGameControllerInstanceId the identifier of the remote game controller instance
         */
        public CreateRemoteInteractionChainMessage(String remoteGameControllerInstanceId) {
            this.setRemoteGameControllerInstanceId(remoteGameControllerInstanceId);
        }
    }

    /**
     * Response message for a create remote interaction chain request.
     * Contains the identifier of the newly created remote interaction chain.
     */
    public static class CreateRemoteInteractionChainResponseMessage extends Message {
        private String remoteInteractionChainId;

        /**
         * Constructs an empty {@code CreateRemoteInteractionChainResponseMessage}.
         */
        public CreateRemoteInteractionChainResponseMessage() {
        }

        /**
         * Constructs a {@code CreateRemoteInteractionChainResponseMessage} with the specified remote game controller instance identifier
         * and remote interaction chain identifier.
         *
         * @param remoteGameControllerInstanceId the identifier of the remote game controller instance
         * @param remoteInteractionChainId       the identifier of the created remote interaction chain
         */
        public CreateRemoteInteractionChainResponseMessage(String remoteGameControllerInstanceId, String remoteInteractionChainId) {
            this.setRemoteGameControllerInstanceId(remoteGameControllerInstanceId);
            this.remoteInteractionChainId = remoteInteractionChainId;
        }

        /**
         * Returns the identifier of the remote interaction chain.
         *
         * @return the remote interaction chain identifier
         */
        public String getRemoteInteractionChainId() {
            return remoteInteractionChainId;
        }

        /**
         * Sets the identifier for the remote interaction chain.
         *
         * @param remoteInteractionChainId the remote interaction chain identifier to set
         */
        public void setRemoteInteractionChainId(String remoteInteractionChainId) {
            this.remoteInteractionChainId = remoteInteractionChainId;
        }
    }

    /**
     * Message used to trigger the application of remote Game State Updates (GSUs).
     * This message instructs the remote game controller to process and apply queued GSUs.
     */
    public static class ApplyRemoteGSUsMessage extends Message {

        /**
         * Constructs an empty {@code ApplyRemoteGSUsMessage}.
         */
        public ApplyRemoteGSUsMessage() {
        }

        /**
         * Constructs an {@code ApplyRemoteGSUsMessage} with the specified remote game controller instance identifier.
         *
         * @param remoteGameControllerInstanceId the identifier of the remote game controller instance
         */
        public ApplyRemoteGSUsMessage(String remoteGameControllerInstanceId) {
            this.setRemoteGameControllerInstanceId(remoteGameControllerInstanceId);
        }
    }

    /**
     * Message indicating that the application should abort any pending remote Game State Updates (GSUs).
     * This message is sent to instruct the remote game controller to cancel any in-progress remote updates.
     */
    public static class AbortRemoteGSUsMessage extends Message {

        /**
         * Constructs an empty {@code AbortRemoteGSUsMessage}.
         */
        public AbortRemoteGSUsMessage() {
        }

        /**
         * Constructs an {@code AbortRemoteGSUsMessage} with the specified remote game controller instance identifier.
         *
         * @param remoteGameControllerInstanceId the identifier of the remote game controller instance
         */
        public AbortRemoteGSUsMessage(String remoteGameControllerInstanceId) {
            this.setRemoteGameControllerInstanceId(remoteGameControllerInstanceId);
        }
    }

    /**
     * Message containing a lobby identifier.
     * Used to associate a user with a specific game lobby.
     */
    public static class lobbyIdMessage {
        String lobbyId;

        /**
         * Constructs a {@code lobbyIdMessage} with the specified lobby identifier.
         *
         * @param lobbyId the identifier of the lobby
         */
        public lobbyIdMessage(String lobbyId) {
            this.lobbyId = lobbyId;
        }

        /**
         * Constructs an empty {@code lobbyIdMessage}.
         */
        public lobbyIdMessage() {
        }

        /**
         * Returns the lobby identifier.
         *
         * @return the lobby identifier as a {@link String}
         */
        public String getLobbyId() {
            return lobbyId;
        }

        /**
         * Sets the lobby identifier.
         *
         * @param lobbyId the lobby identifier to set
         */
        public void setLobbyId(String lobbyId) {
            this.lobbyId = lobbyId;
        }
    }

    /**
     * Message to initiate the start of a game.
     * Contains the level index to be loaded when starting the game.
     */
    public static class startGameMessage {
        int levelIndex;

        /**
         * Constructs a {@code startGameMessage} with the specified level index.
         *
         * @param levelIndex the index of the level to start
         */
        public startGameMessage(int levelIndex) {
            this.levelIndex = levelIndex;
        }

        /**
         * Constructs an empty {@code startGameMessage}.
         */
        public startGameMessage() {
        }

        /**
         * Returns the level index associated with this start game message.
         *
         * @return the level index as an {@code int}
         */
        public int getLevelIndex() {
            return levelIndex;
        }

        /**
         * Sets the level index for this start game message.
         *
         * @param levelIndex the level index to set
         */
        public void setLevelIndex(int levelIndex) {
            this.levelIndex = levelIndex;
        }
    }

    /**
     * Message indicating a request to exit to the main menu.
     * Used to signal the termination of the current game session.
     */
    public static class exitToMainMenuMessage {
        /**
         * Constructs an {@code exitToMainMenuMessage}.
         */
        public exitToMainMenuMessage() {
        }
    }
}
