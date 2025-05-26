package com.lazar.exception;

public class InvalidCommandException extends Exception{
    public InvalidCommandException(String command){
        super(String.format("Invalid command: %s", command));
    }
}
