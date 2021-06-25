package com.networkcourse.httpclient.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author fguohao
 * @date 2021/06/25
 */
public class FileUtil {
    public static void save(byte[] content, String path) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(content);
        fos.close();
    }

    public static byte[] read(String path) throws IOException {
        FileInputStream in=new FileInputStream(path);
        ByteArrayOutputStream out=new ByteArrayOutputStream(1024);
        byte[] temp=new byte[1024];
        int size=0;
        while((size=in.read(temp))!=-1)
        {
            out.write(temp,0,size);
        }
        in.close();
        byte[] bytes=out.toByteArray();
        return bytes;
    }
}
