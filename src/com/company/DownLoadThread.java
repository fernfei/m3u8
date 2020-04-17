/**
 * Copyright (C), 2019-2020, XXX有限公司
 * FileName: DownLoadThread
 * Author:   Administrator
 * Date:     2020/4/14 19:51
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.company;
import java.io.*;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Administrator
 * @create 2020/4/14
 * @since 1.0.0
 */
public class DownLoadThread implements Runnable {
    private int threadId;
    private int startIndex;
    private int endIndex;
    private String downloadURL;
    //存储着文件类型对应的十六进制
    private static final Map<String, String> FILE_TYPE_MAP = new HashMap<>();
    static{
        FILE_TYPE_MAP.put("ffd8ffe000104a464946", "jpg"); // JPEG (jpg)
        FILE_TYPE_MAP.put("89504e470d0a1a0a0000", "png"); // PNG (png)
        FILE_TYPE_MAP.put("47494638396126026f01", "gif"); // GIF (gif)
        FILE_TYPE_MAP.put("49492a00227105008037", "tif"); // TIFF (tif)
        FILE_TYPE_MAP.put("424d228c010000000000", "bmp"); // 16色位图(bmp)
        FILE_TYPE_MAP.put("424d8240090000000000", "bmp"); // 24位位图(bmp)
        FILE_TYPE_MAP.put("424d8e1b030000000000", "bmp"); // 256色位图(bmp)
        FILE_TYPE_MAP.put("41433130313500000000", "dwg"); // CAD (dwg)
        FILE_TYPE_MAP.put("3c21444f435459504520", "html"); // HTML (html)
        FILE_TYPE_MAP.put("3c21646f637479706520", "htm"); // HTM (htm)
        FILE_TYPE_MAP.put("48544d4c207b0d0a0942", "css"); // css
        FILE_TYPE_MAP.put("696b2e71623d696b2e71", "js"); // js
        FILE_TYPE_MAP.put("7b5c727466315c616e73", "rtf"); // Rich Text Format (rtf)
        FILE_TYPE_MAP.put("38425053000100000000", "psd"); // Photoshop (psd)
        FILE_TYPE_MAP.put("46726f6d3a203d3f6762", "eml"); // Email [Outlook Express 6] (eml)
        FILE_TYPE_MAP.put("d0cf11e0a1b11ae10000", "doc"); // MS Excel 注意：word、msi 和 excel的文件头一样
        FILE_TYPE_MAP.put("d0cf11e0a1b11ae10000", "vsd"); // Visio 绘图
        FILE_TYPE_MAP.put("5374616E64617264204A", "mdb"); // MS Access (mdb)
        FILE_TYPE_MAP.put("252150532D41646F6265", "ps");
        FILE_TYPE_MAP.put("255044462d312e350d0a", "pdf"); // Adobe Acrobat (pdf)
        FILE_TYPE_MAP.put("2e524d46000000120001", "rmvb"); // rmvb/rm相同
        FILE_TYPE_MAP.put("464c5601050000000900", "flv"); // flv与f4v相同
        FILE_TYPE_MAP.put("00000020667479706d70", "mp4");
        FILE_TYPE_MAP.put("49443303000000002176", "mp3");
        FILE_TYPE_MAP.put("000001ba210001000180", "mpg"); //
        FILE_TYPE_MAP.put("3026b2758e66cf11a6d9", "wmv"); // wmv与asf相同
        FILE_TYPE_MAP.put("52494646e27807005741", "wav"); // Wave (wav)
        FILE_TYPE_MAP.put("52494646d07d60074156", "avi");
        FILE_TYPE_MAP.put("4d546864000000060001", "mid"); // MIDI (mid)
        FILE_TYPE_MAP.put("504b0304140000000800", "zip");
        FILE_TYPE_MAP.put("526172211a0700cf9073", "rar");
        FILE_TYPE_MAP.put("235468697320636f6e66", "ini");
        FILE_TYPE_MAP.put("504b03040a0000000000", "jar");
        FILE_TYPE_MAP.put("4d5a9000030000000400", "exe");// 可执行文件
        FILE_TYPE_MAP.put("3c25402070616765206c", "jsp");// jsp文件
        FILE_TYPE_MAP.put("4d616e69666573742d56", "mf");// MF文件
        FILE_TYPE_MAP.put("3c3f786d6c2076657273", "xml");// xml文件
        FILE_TYPE_MAP.put("494e5345525420494e54", "sql");// xml文件
        FILE_TYPE_MAP.put("7061636b616765207765", "java");// java文件
        FILE_TYPE_MAP.put("406563686f206f66660d", "bat");// bat文件
        FILE_TYPE_MAP.put("1f8b0800000000000000", "gz");// gz文件
        FILE_TYPE_MAP.put("6c6f67346a2e726f6f74", "properties");// bat文件
        FILE_TYPE_MAP.put("cafebabe0000002e0041", "class");// bat文件
        FILE_TYPE_MAP.put("49545346030000006000", "chm");// bat文件
        FILE_TYPE_MAP.put("04000000010000001300", "mxp");// bat文件
        FILE_TYPE_MAP.put("504b0304140006000800", "docx");// docx文件
        FILE_TYPE_MAP.put("d0cf11e0a1b11ae10000", "wps");// WPS文字wps、表格et、演示dps都是一样的
        FILE_TYPE_MAP.put("6431303a637265617465", "torrent");

        FILE_TYPE_MAP.put("6D6F6F76", "mov"); // Quicktime (mov)
        FILE_TYPE_MAP.put("FF575043", "wpd"); // WordPerfect (wpd)
        FILE_TYPE_MAP.put("CFAD12FEC5FD746F", "dbx"); // Outlook Express (dbx)
        FILE_TYPE_MAP.put("2142444E", "pst"); // Outlook (pst)
        FILE_TYPE_MAP.put("AC9EBD8F", "qdf"); // Quicken (qdf)
        FILE_TYPE_MAP.put("E3828596", "pwl"); // Windows Password (pwl)
        FILE_TYPE_MAP.put("2E7261FD", "ram"); // Real Audio (ram)
        FILE_TYPE_MAP.put("null", null); // null
    }
    public DownLoadThread() {
    }

    public DownLoadThread(int threadId,String downloadURL) {
        this.threadId = threadId;
        this.downloadURL = downloadURL;
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        if (bytes == null || bytes.length < 0) {
            throw new NullPointerException("文件流数组为空");
        }
        for (int i = 0; i < bytes.length; i++) {
            int hex = bytes[i] & 0xFF;
            String hexString = Integer.toHexString(hex);
            if (hexString.length() == 1) {
                sb.append(0);
            }
            sb.append(hexString);
        }
        return sb.toString();
    }

    public static String getFileType(String hexString) {
            Iterator<String> iterator = FILE_TYPE_MAP.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                if (key.toLowerCase().startsWith(hexString.toLowerCase().substring(0, 5)) || hexString.toLowerCase().substring(0, 5).startsWith(key.toLowerCase())) {
                    return FILE_TYPE_MAP.get(key);
                }
            }
        return null;
    }

    public static byte[] readInputStream(InputStream inputStream,double contentLength) throws IOException {
        byte[] bytes = new byte[1024];
        int len = 0;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while ((len = inputStream.read(bytes)) != -1) {
            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            byteArrayOutputStream.write(bytes, 0, len);
            double currentProgress = len / contentLength;
            decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
            System.out.println("每次读取的数据大小：" + len);
            System.out.println("文件总大小：" + contentLength);
            System.out.println("当前进度：" + currentProgress);
        }
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }
    @Override
    public void run() {
        FileOutputStream fileOutputStream = null;
        try {
            URL url = new URL(downloadURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //设置连接超时时间
            connection.setConnectTimeout(3 * 1000);

            // 防止屏蔽程序抓取而返回403错误
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            //获取文件大小
            int contentLength = connection.getContentLength();
            System.out.println("当前文件大小：" + contentLength);

            InputStream is = connection.getInputStream();
            byte[] bytes = readInputStream(is,Double.valueOf(contentLength));

            String hexString = bytesToHexString(bytes);
            String fileType = getFileType(hexString);
            File directory = new File("D:\\m3u8\\videos");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File file = new File(directory + File.separator + threadId +".ts");
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

    }
}