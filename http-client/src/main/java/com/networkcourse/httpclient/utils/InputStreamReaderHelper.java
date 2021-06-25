package com.networkcourse.httpclient.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * @author fguohao
 * @date 2021/05/28
 */
public class InputStreamReaderHelper {
    public static String readLine(InputStream inputStream) throws IOException {
        ArrayList<Byte> byteArrayList = new ArrayList<>();
        int temp;
        while (true){
            temp = inputStream.read();
            if((char)temp=='\n'){
                return new String(toByteArray(byteArrayList));
            }else if((char)temp=='\r'){
                temp = inputStream.read();
                if((char)temp=='\n'){
                    return new String(toByteArray(byteArrayList));
                }else{
                    byteArrayList.add((byte)('\r'&0xff));
                    byteArrayList.add((byte)(temp&0xff));
                    continue;
                }
            }else if(temp==-1){continue;}
            byteArrayList.add((byte)(temp&0xff));
        }

    }

    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    public static byte[] toByteArray(ArrayList<Byte> byteArrayList){
        byte[] bytes = new byte[byteArrayList.size()];
        for(int i=0;i<byteArrayList.size();i++){
            bytes[i] = byteArrayList.get(i);
        }
        return bytes;
    }


}
