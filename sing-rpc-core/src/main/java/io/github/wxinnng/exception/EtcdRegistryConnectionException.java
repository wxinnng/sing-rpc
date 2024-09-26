package io.github.wxinnng.exception;

public class EtcdRegistryConnectionException extends AbstractConnectionException{
    private static final String MESSAGE = "连接etcd失败";

    public EtcdRegistryConnectionException() {
        super(MESSAGE);
    }

    public EtcdRegistryConnectionException(String message) {
        super(message);
    }

    public EtcdRegistryConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public EtcdRegistryConnectionException(Throwable cause) {
        super(cause);
    }
}
