package com.Regex;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test1 {
    public static void main(String[] args) {
        // 输入目标目录
        System.out.println("请输入项目文件目录：");
        Scanner sc = new Scanner(System.in);
        String FilePath = sc.nextLine();
        sc.close();

        try (BufferedReader br = new BufferedReader(
                new FileReader(FilePath)); // 输入流
        ) {
            char[] buffer = new char[1024];
            int len;
            StringBuilder strBuilder = new StringBuilder(); // 用于存储全部读取文本的字符串
            while ((len = br.read(buffer)) != -1) {
                String str = new String(buffer, 0, len);
                strBuilder.append(str);
            }

            // 生成HTML文件头
            try (BufferedWriter bw = new BufferedWriter(
                    new FileWriter("D:\\Coding\\Java practise\\Homework2_Regex\\src\\com\\Tests\\taeri.html"))) {
                bw.write("<html><body>\n");
            }

            // 定义正则表达式
            String phoneRegex = "\\b([0]\\d{2,3}[- ]?\\d{7,8})|([1][3-9]\\d{9})\\b";
            String URLRegex = "(\\b(https?|ftp)://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?\\b)|(\\b([^@\\s]+\\.)+[^@\\s]+(/[\\w-./?%&=]*)?\\b)";
            String IPV4Regex = "\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b";
            String EmailRegex = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b";

            // 1. 先处理邮箱并生成HTML，同时将邮箱从文本中移除
            strBuilder = processAndRemoveMatches(strBuilder, EmailRegex, "mailto:");

            // 2. 处理URL，避免将邮箱域名识别为URL
            strBuilder = processAndRemoveMatches(strBuilder, URLRegex, "");

            // 3. 处理其他项，如电话和IP
            processAndRemoveMatches(strBuilder, phoneRegex, "tel:");
            processAndRemoveMatches(strBuilder, IPV4Regex, "http://");

            // 生成HTML文件尾
            try (BufferedWriter bw = new BufferedWriter(
                    new FileWriter("D:\\Coding\\Java practise\\Homework2_Regex\\src\\com\\Tests\\taeri.html",
                            true))) {
                bw.write("</body></html>\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 匹配正则表达式并将其结果替换为HTML链接，同时从文本中移除匹配项
     * @param str 输入文本
     * @param regex 正则表达式
     * @param protocol URL或邮箱前缀（如mailto:或tel:）
     * @return 移除匹配内容后的文本
     */
    public static StringBuilder processAndRemoveMatches(StringBuilder str, String regex, String protocol) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        StringBuilder updatedStr = new StringBuilder(str); // 复制文本，用于移除已匹配的部分

        // 存储调整后的偏移量（因为移除操作会影响索引）
        int offset = 0;

        while (matcher.find()) {
            String match = matcher.group();
            String link;

            // 根据是否需要协议生成链接
            if (protocol.isEmpty()) {
                link = "<a href=\"" + match + "\">" + match + "</a>";
            } else {
                link = "<a href=\"" + protocol + match + "\">" + match + "</a>";
            }

            // 将链接写入HTML文件
            try (BufferedWriter bw = new BufferedWriter(
                    new FileWriter("D:\\Coding\\Java practise\\Homework2_Regex\\src\\com\\Tests\\taeri.html",
                            true))) {
                bw.write(link);
                bw.write("<br/>\n"); // 每个匹配结果后换行
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 从文本中移除已匹配的部分
            int start = matcher.start() - offset; // 偏移调整后的开始索引
            int end = matcher.end() - offset;     // 偏移调整后的结束索引
            updatedStr.replace(start, end, "");   // 替换为“空”，即删除匹配的部分

            // 记录偏移量
            offset += (end - start);
        }

        return updatedStr;
    }
}
