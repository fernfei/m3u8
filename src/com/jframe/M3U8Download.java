/**
 * Copyright (C), 2019-2020, XXX有限公司
 * FileName: M3U8Download
 * Author:   Administrator
 * Date:     2020/4/15 4:08
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.jframe;

import com.company.DownLoadThread;
import javafx.geometry.Pos;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Administrator
 * @create 2020/4/15
 * @since 1.0.0
 */
public class M3U8Download {


    public static void main(String[] args) {
        // 1. 创建一个顶层容器（窗口）
        JFrame jf = new JFrame("m3u8下载合并器");          // 创建窗口
        jf.setSize(800, 700);                       // 设置窗口大小
        jf.setLocationRelativeTo(null);             // 把窗口位置设置到屏幕中心
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // 当点击窗口的关闭按钮时退出程序（没有这一句，程序不会退出）

        // 2. 创建中间容器（面板容器）
        JPanel panel = new JPanel();                // 创建面板容器，使用默认的布局管理器

        // 3. 文本区域，用于显示相关信息
        JLabel jLabel = new JLabel();

        jLabel(panel, jLabel);
        jButton(jf, panel, jLabel);
        //jProgressBar(panel);


        // 4. 把 面板容器 作为窗口的内容面板 设置到 窗口
        jf.setContentPane(panel);

        // 5. 显示窗口，前面创建的信息都在内存中，通过 jf.setVisible(true) 把内存中的窗口显示在屏幕上。
        jf.setVisible(true);

    }

    /***
     * 按钮
     * @param jf JFrame
     * @param panel JPanel
     * @param jLabel JLabel
     */
    private static void jButton(JFrame jf, JPanel panel, JLabel jLabel) {
        //按钮
        JButton jButton = new JButton("选择文件");
        //按钮被点击事件
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filePath = showFileOpenDialog(jf, jLabel);

                if (filePath == null || "".equals(filePath)) {

                }else {
                    //下载视频
                    downloadVideos(filePath);
                }
            }
        });


        panel.add(jButton);
    }

    /***
     * 标签
     * @param panel
     * @param jLabel
     */
    private static void jLabel(JPanel panel, JLabel jLabel) {
        panel.add(jLabel);
    }

    /***
     * 进度条
     * @param jPanel
     */
    private static void jProgressBar(JPanel jPanel) {
        final int MIN_PROGRESS = 0;
        final int MAX_PROGRESS = 100;
        int currentProgress = MIN_PROGRESS;
        // 创建一个进度条
        JProgressBar jProgressBar = new JProgressBar();
        // 设置进度的 最小值 和 最大值
        jProgressBar.setMinimum(MIN_PROGRESS);
        jProgressBar.setMaximum(MAX_PROGRESS);
        // 设置当前进度
        jProgressBar.setValue(currentProgress);
        // 百分比
        jProgressBar.setStringPainted(true);
        jProgressBar.addChangeListener(e -> System.out.println("当前进度值: " + jProgressBar.getValue() + "; " +
                "进度百分比: " + jProgressBar.getPercentComplete()));
        jPanel.add(jProgressBar);
        jProgressBar.setValue(currentProgress);
    }

    /***
     * 选择文件
     * @param parent
     * @param jLabel
     */
    private static String showFileOpenDialog(Component parent, JLabel jLabel) {
        String filePath = null;
        //创建一个默认的文件选择器
        JFileChooser jFileChooser = new JFileChooser();
        //设置默认显示的文件夹为D盘
        jFileChooser.setCurrentDirectory(new File("D:\\"));
        //设置文件选择的模式（只选文件、只选文件夹、文件和文件夹可选）
        jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);//只选文件
        //不设置多选
        jFileChooser.setMultiSelectionEnabled(false);

        // 打开文件选择框（线程将被阻塞, 直到选择框被关闭）
        int result = jFileChooser.showSaveDialog(parent);
        //点击了保存
        if (result == jFileChooser.APPROVE_OPTION) {
            File selectedFile = jFileChooser.getSelectedFile();
            jLabel.setText("选择的文件：" + selectedFile.getPath());
            filePath = selectedFile.getPath();
            return filePath;
        }
        return null;
    }

    public static void downloadVideos(String filePath) {
        File file = new File(filePath);
        FileInputStream fileInputStream = null;
        BufferedReader reader = null;
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            fileInputStream = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(fileInputStream, "GBK"));
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}