package com.networkcourse.httpclient.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author fguohao
 * @date 2021/05/28
 */
public class ByteReader {
    public static byte[] read(BufferedReader bufferedReader, int length) throws IOException {
        byte[] b = new byte[length];
        for(int i=0;i<length/2;i++){
            int temp = bufferedReader.read();
            b[i*2] = (byte)((temp>>4)&0xff);
            b[i*2+1] = (byte)((temp)&0xff);
        }
        if(length%2==1){
            int temp = bufferedReader.read();
            b[length-1]=(byte)((temp)&0xff);
        }
        return b;
    }
    public static byte[] readByte(InputStream inputStreamReader, int length) throws IOException {
        byte[] b = new byte[length];

        int count=0;
        while (count!=length){
            count+=inputStreamReader.read(b,count,length-count);
        }
        return b;
    }
}
