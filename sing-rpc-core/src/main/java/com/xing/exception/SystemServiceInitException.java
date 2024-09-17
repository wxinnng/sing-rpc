package com.xing.exception;

public class SystemServiceInitException extends RuntimeException{
    public SystemServiceInitException(){}
    public SystemServiceInitException(String msg) {
        super(msg);
    }
}
