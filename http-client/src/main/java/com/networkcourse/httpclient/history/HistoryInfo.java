package com.networkcourse.httpclient.history;

/**
 * @author fguohao
 * @date 2021/06/25
 */
public class HistoryInfo {
    int logLevel = History.LOG_LEVEL_DETAIL;
    Long timestamp;

    public HistoryInfo() {
        this.timestamp = this.timestamp = System.currentTimeMillis();
    }
}
