package com.networkcourse.httpclient.history;

import com.networkcourse.httpclient.utils.TimeUtil;

/**
 * @author fguohao
 * @date 2021/06/25
 */
public class MessageHistoryInfo extends HistoryInfo{
    String msg;

    public MessageHistoryInfo(int level,String msg) {
        this.logLevel = level;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return msg;
    }
}
