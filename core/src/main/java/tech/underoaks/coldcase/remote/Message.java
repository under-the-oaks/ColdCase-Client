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

    public String getRemoteGameControllerInstanceId() {
        return remoteGameControllerInstanceId;
    }

    public void setRemoteGameControllerInstanceId(String remoteGameControllerInstanceId) {
        this.remoteGameControllerInstanceId = remoteGameControllerInstanceId;
    }
}
