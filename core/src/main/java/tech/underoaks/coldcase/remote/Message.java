package tech.underoaks.coldcase.remote;

/**
 * Abstract base class representing a generic message in the remote game controller system.
 * <p>
 * This class provides a common structure for all messages by including a unique identifier
 * for the associated remote game controller instance.
 * </p>
 */
public abstract class Message {
    private String remoteGameControllerInstanceId;

    /**
     * Gets the current identifier of the {@link RemoteGameController}
     * @return the id of the associated remote game controller instance
     */
    public String getRemoteGameControllerInstanceId() {
        return remoteGameControllerInstanceId;
    }

    /**
     * Sets the current identifier of the {@link RemoteGameController}
     * @param remoteGameControllerInstanceId The id of the associated remote game controller instance
     */
    public void setRemoteGameControllerInstanceId(String remoteGameControllerInstanceId) {
        this.remoteGameControllerInstanceId = remoteGameControllerInstanceId;
    }
}
