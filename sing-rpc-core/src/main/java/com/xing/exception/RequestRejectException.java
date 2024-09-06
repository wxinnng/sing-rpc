package com.xing.exception;

public class RequestRejectException extends Exception{
    public RequestRejectException(){}
    public RequestRejectException(String msg){
        super(msg);
    }
}
