package io.github.wxinnng.exception;

public class RequestRejectException extends RuntimeException{
    public RequestRejectException(){}
    public RequestRejectException(String msg){
        super(msg);
    }
}
