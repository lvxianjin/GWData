package com.iscas.data.tool;

import java.io.*;
import java.util.ArrayList;

/**
 * @author : lvxianjin
 * @Date: 2019/10/25 13:55
 * @Description:
 */
public class FileClient {
    public ArrayList<String> LoadFile(String path){
        ArrayList<String> list = new ArrayList<>();
        File file = new File(path);
        file.setReadable(true);
        file.setWritable(true);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String line = "";
        String everyline = "";
        try {
            while ((line = reader.readLine())!= null){
                everyline = line;
                list.add(everyline);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
