package com.networkcourse.httpclient.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;

/**
 * @author fguohao
 * @date 2021/05/28
 */
public class ChunkReader {
    public static byte[] readChunk(BufferedReader bufferedReader) throws IOException {
        ArrayList<Byte> buffer = new ArrayList<>();
        int length;
        String temp = bufferedReader.readLine();
        length = Integer.parseInt(temp,16);
        while(length!=0){
            for(int i=0;i<length;i++){
                buffer.add((byte) bufferedReader.read());
            }
            temp = bufferedReader.readLine();
            length = Integer.parseInt(temp,16);
        }
        return toByteArray(buffer);
    }

    public static byte[] readChunk(InputStream inputStream) throws IOException {
        ArrayList<byte[]> buffer = new ArrayList<>();
        int length;
        String temp = InputStreamReaderHelper.readLine(inputStream);
        length = Integer.parseInt(temp,16);
        while(length!=0){
            byte[] tempBuffer = new byte[length];
            int count=0;
            while (count!=length){
                count+=inputStream.read(tempBuffer,count,length-count);
            }

            buffer.add(tempBuffer);
            inputStream.read();//CR
            inputStream.read();//LF
            temp = InputStreamReaderHelper.readLine(inputStream);

            length = Integer.parseInt(temp,16);

        }
        inputStream.read();//CR
        inputStream.read();//LF
        return ByteUtil.mergeBytes(buffer);
    }

    public static byte[] toByteArray(ArrayList<Byte> byteArrayList){
        byte[] bytes = new byte[byteArrayList.size()];
        for(int i=0;i<byteArrayList.size();i++){
            bytes[i] = byteArrayList.get(i);
        }
        return bytes;
    }
}
