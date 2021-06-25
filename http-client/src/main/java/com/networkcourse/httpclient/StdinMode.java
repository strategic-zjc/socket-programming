package com.networkcourse.httpclient;

import com.networkcourse.httpclient.client.Client;

/**
 * @author fguohao
 * @date 2021/06/25
 */
public class StdinMode {
    public static void main(String[] args) {
        Client client = new Client();
        try {
            client.stdMode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
