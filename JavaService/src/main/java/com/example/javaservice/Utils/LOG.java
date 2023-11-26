package com.example.javaservice.Utils;

import com.example.javaservice.Config.LogConfig;
import com.example.javaservice.Constant.LOGConstant;
import com.example.javaservice.Constant.SystemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LOG {

    private static FileOutputStream fileOutputStream;
    private static void init(){
        try {
            fileOutputStream = new FileOutputStream(SystemConstant.LOG_PATH);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void INFO(String message)  {
        init();
        if(LOGConstant.DEBUG_LEVEL <= LOGConstant.INFO){
            // 时钟打印
            System.out.println(TIME() +" <" + getCallerMethodName() + ">" + " [INFO] " + message);
            try {
                fileOutputStream.write((TIME() +" <" + getCallerMethodName() + ">" + " [INFO] " + message + "\n").getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void DEBUG(String message) {
        init();
        if(LOGConstant.DEBUG_LEVEL <= LOGConstant.DEBUG){
            // 时钟打印
            System.out.println(TIME() + " <" + getCallerMethodName() + ">" + " [DEBUG] " + message);
            try {
                fileOutputStream.write((TIME() +" <" + getCallerMethodName() + ">" + " [DEBUG] " + message + "\n").getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void WARNING(String message)  {
        init();
        if(LOGConstant.DEBUG_LEVEL <= LOGConstant.WARNING){
            // 时钟打印
            System.out.println(TIME() + " <" + getCallerMethodName() + ">" + " [WARNING] " + message);
            try {
                fileOutputStream.write((TIME() +" <" + getCallerMethodName() + ">" + " [WARNING] " + message + "\n").getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void ERROR(String message) {
        init();
        if(LOGConstant.DEBUG_LEVEL <= LOGConstant.ERROR){
            // 时钟打印
            System.out.println(TIME() + " <" + getCallerMethodName() + ">" + " [ERROR] " + message);
            try {
                fileOutputStream.write((TIME() +" <" + getCallerMethodName() + ">" + " [ERROR] " + message + "\n").getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    private static String TIME(){
        // 创建日期对象
        Date currentDate = new Date();
        // 创建日期格式化对象
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 格式化日期为字符串
        return dateFormat.format(currentDate);
    }


    private static String getCallerMethodName() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        // 堆栈跟踪数组的第0个元素是getStackTrace方法本身，第1个元素是getCallerMethodName方法，
        // 第2个元素是调用getCallerMethodName方法的方法（即调用者）。

        if (stackTrace.length >= 3) {
            StackTraceElement callerStackTrace = stackTrace[3];
            return callerStackTrace.getMethodName();
        }
        return "Unknown";
    }
}
