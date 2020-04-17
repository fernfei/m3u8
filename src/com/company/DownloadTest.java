/**
 * Copyright (C), 2019-2020, XXX有限公司
 * FileName: DownloadTest
 * Author:   Administrator
 * Date:     2020/4/15 2:02
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.company;

import java.io.*;


/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Administrator
 * @create 2020/4/15
 * @since 1.0.0
 */
public class DownloadTest {
    public static void main(String[] args) throws IOException {
        File file = new File("D:\\m3u8\\1624f41898a996558e0b022b3b066612.m3u8");
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream, "GBK"));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("https://")) {
                stringBuilder.append(line).append(",");
            }
        }
        String[] urls = stringBuilder.toString().split(",");
        for (int i = 0; i < urls.length; i++) {
            Thread thread = new Thread(new DownLoadThread(i, urls[i]));
            thread.start();

        }
        reader.close();
        fileInputStream.close();
    }
}