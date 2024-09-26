package io.github.wxinnng.exception;

public abstract class AbstractConnectionException extends RuntimeException{
    public AbstractConnectionException(String message){
        super(message);
    }
    public AbstractConnectionException(String message, Throwable cause){
        super(message, cause);
    }
    public AbstractConnectionException(Throwable cause){
        super(cause);
    }
    protected AbstractConnectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace){
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
