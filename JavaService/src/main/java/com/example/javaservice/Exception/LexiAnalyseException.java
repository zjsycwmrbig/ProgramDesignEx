package com.example.javaservice.Exception;

public class LexiAnalyseException extends CompileErrorException {
            public LexiAnalyseException() {
            }

            public LexiAnalyseException(String msg, Integer line) {
                super(msg, line, null);
            }
}
