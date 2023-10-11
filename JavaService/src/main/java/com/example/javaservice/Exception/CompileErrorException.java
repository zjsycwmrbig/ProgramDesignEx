package com.example.javaservice.Exception;

import com.example.javaservice.Pojo.Entity.CompileError;
import com.example.javaservice.Pojo.Entity.CompileInfo;
import com.example.javaservice.Pojo.Entity.CompileWarning;

import java.util.List;

public class ErrorException{

    public List<CompileWarning> warnings;
        public String errorMsg;
        public Integer line;

        public ErrorException() {
        }
        public ErrorException(String msg,Integer line,List<CompileWarning> warnings) {
            this.errorMsg = msg;
            this.line = line;
            this.warnings = warnings;
        }

}
