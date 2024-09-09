package com.xing.exception;

public class RedisRegistryConnectionException extends AbstractConnectionException{
    public RedisRegistryConnectionException(String message) {
        super(message);
    }

    public RedisRegistryConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public RedisRegistryConnectionException(Throwable cause) {
        super(cause);
    }

    protected RedisRegistryConnectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
