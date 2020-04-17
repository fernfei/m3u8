package com.company;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws IOException {
        File file = new File("D:\\m3u8\\videos");
        File[] files = file.listFiles();
        int[] fileNames = new int[files.length];
        for (int i = 0; i < files.length; i++) {
            fileNames[i] = Integer.parseInt(files[i].getName().replace(".ts", ""));
        }
        Arrays.sort(fileNames);
        File f = new File("D:\\m3u8\\m3u8.txt");
        f.delete();
        f.createNewFile();
        for (int i = 0; i < files.length; i++) {
            String fileName = files[i].getName();
            //System.out.println(fileName);
            String fullPathName=file.getPath()+"\\"+ fileNames[i]+".ts";
            System.out.println(fullPathName);
            FileWriter fw = new FileWriter(f.getPath(),true);
            fw.write(  "file\t"+"'"+fullPathName+"'");
            //linux系统换行\n windows\r\n
            fw.write("\n");
            fw.close();
        }

        // Java调用 dos命令
        String CMD = "D:\\SofrWare\\ffmpeg-20200403-52523b6-win64-static\\bin\\ffmpeg.exe -f concat -safe 0 -i D:\\m3u8\\m3u8.txt -c copy D:\\m3u8\\output.mp4";
        try {
            Process process = Runtime.getRuntime().exec(CMD);
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, Charset.forName("GBK"));
            BufferedReader br = new BufferedReader(isr);
            String content = br.readLine();
            while (content != null) {
                System.out.println(content);
                content = br.readLine();
            }
            is.close();
            br.close();
        } catch (IOException  e) {
            e.printStackTrace();
        }
    }
}
