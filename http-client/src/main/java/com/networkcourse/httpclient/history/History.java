package com.networkcourse.httpclient.history;

import com.networkcourse.httpclient.message.HttpRequest;
import com.networkcourse.httpclient.message.HttpResponse;
import com.networkcourse.httpclient.utils.TimeUtil;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * log类，用于记录对应client的日志信息
 * @author fguohao
 * @date 2021/05/30
 */
public class History {
    public static final int LOG_LEVEL_ERROR =2;
    public static final int LOG_LEVEL_WARNING = 1;
    public static final int LOG_LEVEL_INFO = 0;
    public static final int LOG_LEVEL_DETAIL = -1;

    private List<HistoryInfo> historyInfos = new LinkedList<>();
    private boolean enableLog = true;
    private int logLevel = LOG_LEVEL_DETAIL;
    private boolean printRealTime = true;
    private PrintStream out = System.out;
    private PrintStream err = System.err;
    public void addHistory(HttpRequest httpRequest, HttpResponse httpResponse){
        if(this.enableLog) {
            HttpRequestHistoryInfo historyInfo = new HttpRequestHistoryInfo(httpRequest, httpResponse);
            historyInfos.add(historyInfo);
            if(printRealTime){
                printLog(historyInfo, logLevel);
            }
        }
    }

    public void addLog(String msg, int logLevel){
        if(this.enableLog) {
            MessageHistoryInfo m = new MessageHistoryInfo(logLevel,msg);
            historyInfos.add(m);
            if(printRealTime){
                printLog(m, logLevel);
            }
        }
    }


    public void printHistory(int logLevel){
        for(HistoryInfo historyInfo:historyInfos){
            printLog(historyInfo,logLevel);
        }
    }

    private void printLog(HistoryInfo historyInfo,int logLevel){
        if(historyInfo.logLevel>=logLevel){
            switch (historyInfo.logLevel){
                case LOG_LEVEL_INFO:case LOG_LEVEL_DETAIL:
                    out.println("\u001b[32m"+"[INFO]"+ TimeUtil.toTimeString(historyInfo.timestamp)+"\r\n"+"\u001b[0m"+historyInfo.toString());
                    break;
                case LOG_LEVEL_WARNING:
                    out.println("\u001b[33m"+"[WARNING]"+ TimeUtil.toTimeString(historyInfo.timestamp)+"\r\n"+"\u001b[0m"+historyInfo.toString());
                    break;
                case LOG_LEVEL_ERROR:
                    out.println("\u001b[31m"+"[ERROR]"+ TimeUtil.toTimeString(historyInfo.timestamp)+"\r\n"+"\u001b[0m"+historyInfo.toString());
                    break;
                default:;
            }
        }
    }

    public void setEnableLog(boolean enableLog) {
        this.enableLog = enableLog;
    }

    public boolean getEnableLog(){
        return enableLog;
    }

    public int getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
    }

    public boolean isPrintRealTime() {
        return printRealTime;
    }

    public void setPrintRealTime(boolean printRealTime) {
        this.printRealTime = printRealTime;
    }


    public void setOut(PrintStream out) {
        this.out = out;
    }

    public void setErr(PrintStream err) {
        this.err = err;
    }
}
