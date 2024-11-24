package tech.underoaks.coldcase.remote;

// Abstract base class for all messages
public abstract class Message {
    private String remoteGameControllerInstanceId;

    public String getRemoteGameControllerInstanceId() {
        return remoteGameControllerInstanceId;
    }

    public void setRemoteGameControllerInstanceId(String remoteGameControllerInstanceId) {
        this.remoteGameControllerInstanceId = remoteGameControllerInstanceId;
    }
}
