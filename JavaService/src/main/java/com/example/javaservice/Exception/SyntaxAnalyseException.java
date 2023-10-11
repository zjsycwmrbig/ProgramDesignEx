package com.example.javaservice.Exception;

public class SyntaxAnalyseException extends CompileErrorException {
    public SyntaxAnalyseException(String message,Integer line) {
        super(message,line,null);
    }
}
