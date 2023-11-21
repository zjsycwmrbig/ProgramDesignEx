package com.example.javaservice.Core.impl;

import com.example.javaservice.Constant.*;
import com.example.javaservice.Exception.LexiAnalyseException;
import com.example.javaservice.Pojo.Entity.Token;
import com.example.javaservice.Pojo.Entity.Tokens;
import com.example.javaservice.Result.Result;
import com.example.javaservice.Utils.LOG;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class LexicalAnalyzerImpl implements com.example.javaservice.Core.LexicalAnalyzer{
    private Map<Character,Integer> stateMap;
    private Integer lineIndex;

    public LexicalAnalyzerImpl() {
        lineIndex = 1;
        stateMap = new HashMap<>();
        for (int i = 0; i < 256; i++) {
            stateMap.put((char)i, TokensConstant.ERROR);
        }
        // keyword and identifier
        for(int i = 'a'; i <= 'z'; i++){
            stateMap.put((char)i,TokensConstant.IDENTIFIER);
        }
        for(int i = 'A'; i <= 'Z'; i++){
            stateMap.put((char)i,TokensConstant.IDENTIFIER);
        }
        stateMap.put('_',TokensConstant.IDENTIFIER);
        // number
        for(int i = '0'; i <= '9'; i++){
            stateMap.put((char)i,TokensConstant.NUMBER);
        }
        // string
        stateMap.put('\"',TokensConstant.STRING);
        // operator
        stateMap.put('+',TokensConstant.OPERATOR);
        stateMap.put('=',TokensConstant.OPERATOR);
        // delimiter
        stateMap.put('{',TokensConstant.DELIMITER);
        stateMap.put('}',TokensConstant.DELIMITER);
        stateMap.put('(',TokensConstant.DELIMITER);
        stateMap.put(')',TokensConstant.DELIMITER);
        stateMap.put(':',TokensConstant.DELIMITER);
        stateMap.put(';',TokensConstant.DELIMITER);
        stateMap.put(',',TokensConstant.DELIMITER);
        stateMap.put('.',TokensConstant.DELIMITER);
        // matchmaker
        stateMap.put('\'',TokensConstant.MATCHMAKER);
        stateMap.put('<',TokensConstant.MATCHMAKER);
        stateMap.put('>',TokensConstant.MATCHMAKER);
        stateMap.put('`',TokensConstant.MATCHMAKER);
        // input
        stateMap.put('[',TokensConstant.INPUT_FLAG);
        stateMap.put(']',TokensConstant.INPUT_FLAG);
        // default
        stateMap.put(' ',TokensConstant.DEFAULT);
        stateMap.put('\t',TokensConstant.DEFAULT);
        stateMap.put('\r',TokensConstant.DEFAULT);

        stateMap.put('\n',TokensConstant.DEFAULT);
        // 参数
        stateMap.put('$',TokensConstant.PARAMETER);

        // 注释
        stateMap.put('/',TokensConstant.COMMENT);
    }

    @Override
    public Tokens analyze(String code) {
        // 对code修饰
        if(code == null || code.length() == 0){
            throw new LexiAnalyseException("代码文件为空", lineIndex);
        }else{
            if(code.charAt(code.length() - 1) == '\0'){
                code = code.substring(0,code.length() - 1);
            }
            // 代码最后加上换行符，方便词法分析
            if(code.charAt(code.length() - 1) != '\n'){
                code += '\n';
            }
        }

        // 拆解成字节流
        int codeLength = code.length();
        int pointer = 0;

        // 词法分析结果
        List<Token> stream = new ArrayList<>();
        Map<Integer,List<String>> map = new HashMap<>();
        map.put(TokensConstant.KEYWORD,new ArrayList<>());
        map.put(TokensConstant.IDENTIFIER,new ArrayList<>());
        map.put(TokensConstant.STRING,new ArrayList<>());
        map.put(TokensConstant.NUMBER,new ArrayList<>());
        map.put(TokensConstant.OPERATOR,new ArrayList<>());
        map.put(TokensConstant.DELIMITER,new ArrayList<>());
        map.put(TokensConstant.MATCHMAKER,new ArrayList<>());
        map.put(TokensConstant.FUNCTION,new ArrayList<>());
        map.put(TokensConstant.PARAMETER,new ArrayList<>());

        Result res = null;

        while(pointer < codeLength) {
            // 识别token
            if(stateMap.get(code.charAt(pointer)) == null){
                System.out.println("illegal char: " + code.charAt(pointer) + " " + pointer);
                throw new LexiAnalyseException(CompileErrorConstant.ILLEGAL_CHAR, lineIndex);
            }

            switch (stateMap.get(code.charAt(pointer))) {
                case TokensConstant.IDENTIFIER:
                    // identifier
                    res = identifierMach(code.substring(pointer));
                    if(res.getState() == ResultConstant.ERROR){
                        throw new LexiAnalyseException(CompileErrorConstant.ILLEGAL_IDENTIFIER, lineIndex);
                    }else if (res.getState() == TokensConstant.KEYWORD){
                        // keyword
                        stream.add(new Token(TokensConstant.KEYWORD,(String)res.getData(),lineIndex));
                        map.get(TokensConstant.KEYWORD).add((String)res.getData());
                        pointer += ((String)res.getData()).length();
                    }else if(res.getState() == TokensConstant.FUNCTION){
                        // function
                        stream.add(new Token(TokensConstant.FUNCTION,(String)res.getData(),lineIndex));
                        map.get(TokensConstant.FUNCTION).add((String)res.getData());
                        pointer += ((String)res.getData()).length();
                    }else{
                        // identifier
                        stream.add(new Token(TokensConstant.IDENTIFIER,(String)res.getData(),lineIndex));
                        map.get(TokensConstant.IDENTIFIER).add((String)res.getData());
                        pointer += ((String)res.getData()).length();
                    }
                    break;
                case TokensConstant.NUMBER:
                    // number
                    res = numberMach(code.substring(pointer));
                    if(res.getState() == ResultConstant.ERROR){
                        throw new LexiAnalyseException(CompileErrorConstant.ILLEGAL_NUMBER,lineIndex);
                    }else{
                        stream.add(new Token(TokensConstant.NUMBER,(String)res.getData(),lineIndex));
                        map.get(TokensConstant.NUMBER).add((String)res.getData());
                        pointer += ((String)res.getData()).length();
                    }
                    break;
                case TokensConstant.STRING:
                    // string
                    res = stringMach(code.substring(pointer));
                    if(res.getState() == ResultConstant.ERROR){
                        throw new LexiAnalyseException(CompileErrorConstant.ILLEGAL_STRING,lineIndex);
                    }else{
                        stream.add(new Token(TokensConstant.STRING,(String)res.getData(),lineIndex));
                        map.get(TokensConstant.STRING).add((String)res.getData());
                        pointer += ((String)res.getData()).length();
                    }
                    break;
                case TokensConstant.OPERATOR:
                    // operator
                    stream.add(new Token(TokensConstant.OPERATOR,String.valueOf(code.charAt(pointer)),lineIndex));
                    map.get(TokensConstant.OPERATOR).add(String.valueOf(code.charAt(pointer)));
                    pointer++;
                    break;
                case TokensConstant.DELIMITER:
                    // delimiter
                    stream.add(new Token(TokensConstant.DELIMITER,String.valueOf(code.charAt(pointer)),lineIndex));
                    map.get(TokensConstant.DELIMITER).add(String.valueOf(code.charAt(pointer)));
                    pointer++;
                    break;
                case TokensConstant.MATCHMAKER:
                    // matchmaker
                    res = matchmakerMach(code.substring(pointer));
                    if(res.getState() == ResultConstant.ERROR){
                        throw new LexiAnalyseException(CompileErrorConstant.ILLEGAL_MATCHER,lineIndex);
                    }else{
                        stream.add(new Token(TokensConstant.MATCHMAKER,(String)res.getData(),lineIndex));
                        map.get(TokensConstant.MATCHMAKER).add((String)res.getData());
                        pointer += ((String)res.getData()).length();
                    }
                    break;
                case TokensConstant.PARAMETER:
                    // parameter
                    stream.add(new Token(TokensConstant.PARAMETER,String.valueOf(code.charAt(pointer)),lineIndex));
                    map.get(TokensConstant.PARAMETER).add(String.valueOf(code.charAt(pointer)));
                    pointer++;
                    break;
                case TokensConstant.INPUT_FLAG:
                    stream.add(new Token(TokensConstant.INPUT_FLAG,String.valueOf(code.charAt(pointer)),lineIndex));
                    pointer++;
                    break;
                case TokensConstant.COMMENT:
                    // comment 识别到换行符
                    pointer++;
                    while (code.charAt(pointer) != '\n'){
                        pointer++;
                    }
                    break;
                case TokensConstant.DEFAULT:
                    if(code.charAt(pointer) == '\n'){
                        lineIndex++;
                    }
                    pointer++;
                    break;
                case TokensConstant.ERROR:
                    throw new LexiAnalyseException(CompileErrorConstant.ILLEGAL_CHAR,lineIndex);
                default:
                    throw new LexiAnalyseException(CompileErrorConstant.ILLEGAL_CODE,lineIndex);
            }
        }

        // 打印log
        logPrint(new Tokens(stream,map));
        return new Tokens(stream,map);
    }

    @Override
    public Result identifierMach(String code) {
        // 状态机
        int state = 0;
        int pointer = 0; // 指针
        while (true){
            if(pointer >= code.length()){
                return Result.error();
            }
            char c = code.charAt(pointer);
            switch (state) {
                case 0:
                    if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c == '_'){
                        state = 1;
                    }else {
                        return Result.error();
                    }
                    break;
                case 1:
                    if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c == '_' || c >= '0' && c <= '9'){
                        state = 1;
                    }else {
                        if(KeywordConstant.KEYWORD.contains(code.substring(0,pointer))){
                            return new Result(TokensConstant.KEYWORD,code.substring(0,pointer),null);
                        } else if(KeywordConstant.FUNCTION.contains(code.substring(0,pointer))){
                            return new Result(TokensConstant.FUNCTION,code.substring(0,pointer),null);
                        } else{
                            return new Result(TokensConstant.IDENTIFIER,code.substring(0,pointer),null);
                        }
                    }
                    break;
                default:
                    return Result.error();
            }
            pointer++;
        }
    }

    @Override
    public Result numberMach(String code) {
        int state = 0;
        int pointer = 0; // 指针
        while (true){
            if(pointer >= code.length()){
                return Result.error();
            }
            char c = code.charAt(pointer);
            switch (state) {
                case 0:
                    if (c >= '0' && c <= '9'){
                        state = 1;
                    }else {
                        return Result.error();
                    }
                    break;
                case 1:
                    if (c >= '0' && c <= '9'){
                        state = 1;
                    }else if(stateMap.get(c) != TokensConstant.IDENTIFIER){
                        return new Result(TokensConstant.NUMBER,code.substring(0,pointer),null);
                    }else{
                        return Result.error();
                    }
                    break;
                default:
                    return Result.error();
            }
            pointer++;
        }

    }

    @Override
    public Result stringMach(String code) {
        int state = 0;
        int pointer = 0; // 指针
        while (true){
            if(pointer >= code.length()){
                return Result.error();
            }
            char c = code.charAt(pointer);
            switch (state) {
                case 0:
                    if (c == '"'){
                        state = 1;
                    }else {
                        return Result.error();
                    }
                    break;
                case 1:
                    if (c == '"'){
                        state = 2;
                    }else {
                        state = 1;
                    }
                    break;
                case 2:
                    return new Result(TokensConstant.STRING,code.substring(0,pointer),null);
                default:
                    return Result.error();
            }
            pointer++;
        }

    }

    @Override
    public Result operatorMach(String code) {
        return null;
    }

    @Override
    public Result matchmakerMach(String code) {
        int state = 0;
        int pointer = 0; // 指针
        char endChar = code.charAt(0);
        while (true){
            if(pointer >= code.length()){
                return Result.error();
            }
            char c = code.charAt(pointer);
            switch (state) {
                case 0:
                    if(c == MatchConstant.LEFT_EXACT)
                        endChar = MatchConstant.RIGHT_EXACT;
                    else if(c == MatchConstant.LEFT_CONTAIN){
                        endChar = MatchConstant.RIGHT_CONTAIN;
                    }else if(c == MatchConstant.LEFT_INPUT){
                        endChar = MatchConstant.RIGHT_INPUT;
                    }else if(c == MatchConstant.LEFT_REGEX){
                        endChar = MatchConstant.RIGHT_REGEX;
                    }else {
                        return Result.error();
                    }
                    state = 1;
                    break;
                case 1:
                    if(c == endChar) {
                        state = 2;
                    }else {
                        state = 1;
                    }
                    break;
                case 2:
                    return new Result(TokensConstant.MATCHMAKER,code.substring(0,pointer),null);
                default:
                    return Result.error();
            }
            pointer++;
        }
    }


    @Override
    public void logPrint(Tokens tokens) {
        // 把Tokens输出到文件
        try {
            File file = new File(SystemConstant.LEXICAL_ANALYSIS_PATH);
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            for(Token token : tokens.getStream()){
                bufferedWriter.write(token.toString());
                bufferedWriter.newLine();
            }

            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
