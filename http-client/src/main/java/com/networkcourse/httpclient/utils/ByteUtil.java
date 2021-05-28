package com.networkcourse.httpclient.utils;

import java.util.ArrayList;

/**
 * @author fguohao
 * @date 2021/05/28
 */
public class ByteUtil {
    public static byte[] mergeBytes(ArrayList<byte[]> bytes) {
        int length = 0;
        int index = 0;
        for(int i=0;i<bytes.size();i++){
            length += bytes.get(i).length;
        }
        byte[] result = new byte[length];
        for(int i=0;i<bytes.size();i++){
            System.arraycopy(bytes.get(i), 0, result, index, bytes.get(i).length);
            index += bytes.get(i).length;
        }
        return result;
    }

}
